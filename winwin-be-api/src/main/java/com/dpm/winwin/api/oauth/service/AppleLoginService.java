package com.dpm.winwin.api.oauth.service;

import static com.dpm.winwin.api.common.error.enums.ErrorMessage.APPLE_TOKEN_GENERATE_FAIL;
import static com.dpm.winwin.api.common.error.enums.ErrorMessage.DOES_NOT_MATCH_NONCE;
import static com.dpm.winwin.api.common.error.enums.ErrorMessage.INTERVAL_SERVER_ERROR;
import static com.dpm.winwin.api.common.error.enums.ErrorMessage.INVALID_CLIENT_ID;
import static com.dpm.winwin.api.common.error.enums.ErrorMessage.MEMBER_NOT_FOUND;

import com.dpm.winwin.api.common.error.exception.custom.AppleTokenGenerateException;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.common.error.exception.custom.InvalidIdTokenException;
import com.dpm.winwin.api.common.file.service.FileService;
import com.dpm.winwin.api.configuration.NicknameGenerator;
import com.dpm.winwin.api.jwt.TokenProvider;
import com.dpm.winwin.api.jwt.TokenResponse;
import com.dpm.winwin.api.oauth.dto.ApplePublicKeys;
import com.dpm.winwin.api.oauth.dto.AppleToken;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.member.enums.ProviderType;
import com.dpm.winwin.domain.entity.member.enums.Ranks;
import com.dpm.winwin.domain.entity.oauth.OauthToken;
import com.dpm.winwin.domain.entity.token.RefreshTokenEntity;
import com.dpm.winwin.domain.repository.member.MemberRepository;
import com.dpm.winwin.domain.repository.oauth.OauthRepository;
import com.dpm.winwin.domain.repository.token.JwtTokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AppleLoginService {

    private final MemberRepository memberRepository;
    private final JwtTokenRepository jwtTokenRepository;
    private final TokenProvider tokenProvider;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final RestTemplate restTemplate;
    private final OauthRepository oauthRepository;
    private final NicknameGenerator nicknameGenerator;
    private final FileService fileService;
    private static final String APPLE_BASE_URL = "https://appleid.apple.com";
    private static final String APPLE_TOKEN_REQUEST_URL = APPLE_BASE_URL + "/auth/token";

    public Claims appleIdTokenParser(String idToken) throws ParseException, InvalidKeySpecException, NoSuchAlgorithmException {

        ApplePublicKeys.KeyInfo keyInfo = getKeyInfo(idToken);

        Claims body = getClaims(idToken, keyInfo);
        log.info("body = {}", body);
        return body;
    }

    public TokenResponse signUpMember(String memberInfo, String code) throws ParseException, NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        AppleToken appleToken = generatedToken(code);
        String idToken = appleToken.idToken();
        Claims claims = appleIdTokenParser(idToken);

        String socialId = (String) claims.get("sub");
        ProviderType provider = ProviderType.APPLE;

        log.info("socialId :: {}, provider :: {}", socialId, provider);

        Member member = saveMember();
        OauthToken oauthToken = new OauthToken(member, socialId, provider, appleToken.accessToken(), appleToken.refreshToken());
        oauthRepository.save(oauthToken);

        return getTokenResponse(member);
    }

    private Member saveMember(){
        Member newMember = new Member(fileService.getDefaultRandomProfileImageUrl(), Ranks.ROOKIE);
        return memberRepository.save(newMember);
    }

    private TokenResponse getTokenResponse(Member member) {
        String accessToken = tokenProvider.createToken(member.getId(), member.getNickname(), 1);
        String refreshToken = tokenProvider.createToken(member.getId(), member.getNickname(), 30);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(member.getId(), refreshToken);
        jwtTokenRepository.save(refreshTokenEntity);

        String nickname = member.getNickname();
        boolean isExistNickname = StringUtils.hasText(nickname);

        return new TokenResponse(member.getId(), accessToken, refreshToken, isExistNickname);
    }

    public TokenResponse signInMember(String code) throws ParseException, InvalidKeySpecException, NoSuchAlgorithmException {
        AppleToken appleToken = generatedToken(code);
        String idToken = appleToken.idToken();
        Claims claims = appleIdTokenParser(idToken);

        String socialId = (String) claims.get("sub");
        ProviderType provider = ProviderType.APPLE;

        log.info("socialId :: {}, provider :: {}", socialId, provider);
        Optional<Member> findMember = memberRepository.findByMemberByOauthProviderAndSocialId(provider, socialId);
        if (findMember.isPresent()){
            Member member = findMember.get();
            OauthToken oauthToken = member.getOauthToken();
            oauthToken.changeToken(appleToken.accessToken(), appleToken.refreshToken());
            return getTokenResponse(member);
        }

        Member member = saveMember();
        OauthToken oauthToken = new OauthToken(member, socialId, provider, appleToken.accessToken(), appleToken.refreshToken());
        oauthRepository.save(oauthToken);

        return getTokenResponse(member);
    }


    private ApplePublicKeys.KeyInfo getKeyInfo(String idToken) throws ParseException {
        JWSHeader idTokenHeader = getJwsHeader(idToken);
        String algorithm = idTokenHeader.getAlgorithm().getName();
        String keyID = idTokenHeader.getKeyID();

        List<ApplePublicKeys.KeyInfo> keyInfos = getApplePublicKeys();

        ApplePublicKeys.KeyInfo keyInfo = keyInfos.stream()
                .filter(publicKey -> publicKey.alg().equals(algorithm) && publicKey.kid().equals(keyID))
                .findFirst().orElseThrow();
        return keyInfo;
    }

    private List<ApplePublicKeys.KeyInfo> getApplePublicKeys() {
        ApplePublicKeys applePublicKeys = restTemplate.getForObject("https://appleid.apple.com/auth/keys", ApplePublicKeys.class, (Object) null);
        log.info("keyInfos : {}", applePublicKeys);
        List<ApplePublicKeys.KeyInfo> keyInfos = applePublicKeys.keys();
        return keyInfos;
    }

    private Claims getClaims(String idToken, ApplePublicKeys.KeyInfo keyInfo) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] nBytes = Base64.getUrlDecoder().decode(keyInfo.n());
        byte[] eBytes = Base64.getUrlDecoder().decode(keyInfo.e());

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
        KeyFactory keyFactory = KeyFactory.getInstance(keyInfo.kty());
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        Claims body = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(idToken).getBody();
        return body;
    }

    private JWSHeader getJwsHeader(String idToken) throws ParseException {
        SignedJWT idTokenJwt = SignedJWT.parse(idToken);
        return idTokenJwt.getHeader();
    }

    private AppleToken generatedToken(String code) {
        ClientRegistration apple = clientRegistrationRepository.findByRegistrationId("apple");
        log.info("apple :: {} ", apple);
        log.info("apple clientId:: {} ", apple.getClientId());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", apple.getClientId());
        params.add("client_secret", apple.getClientSecret());
        params.add("grant_type", apple.getAuthorizationGrantType().getValue());
        params.add("redirect_uri", apple.getRedirectUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        log.info("request.getBody() :: {} ", request.getBody());

        ResponseEntity<AppleToken> appleTokenResponseEntity = restTemplate.postForEntity(APPLE_TOKEN_REQUEST_URL, request, AppleToken.class);
        HttpStatus tokenRequestStatus = appleTokenResponseEntity.getStatusCode();
        if (tokenRequestStatus.equals(HttpStatus.OK)) {
            AppleToken appleToken = appleTokenResponseEntity.getBody();
            log.info("appleToken :: {}", appleToken);
            return appleToken;
        }

        if (tokenRequestStatus.equals(HttpStatus.BAD_REQUEST)) {
            AppleToken response = appleTokenResponseEntity.getBody();
            if (response.error().equals("invalid_client")) {
                throw new AppleTokenGenerateException(APPLE_TOKEN_GENERATE_FAIL, "client 정보가 잘못되었습니다.");
            }
            throw new AppleTokenGenerateException(APPLE_TOKEN_GENERATE_FAIL, "[ " + code + " ] 코드가 잘못되었습니다.");
        }

        throw new BusinessException(INTERVAL_SERVER_ERROR);
    }

    private void verifyIdtokenExpire(Claims body) {
        Date expiration = body.getExpiration();
        Date now = new Date();
        if (expiration.before(now)) {
            /**
             * id 토큰 만료되었을때 처리
             */
        }
    }

    /**
     * aud 값이 지정한 service Id 값이 아닌경우
     */
    private void verifyClientId(Claims body) {
        String clientId = body.getAudience();
        if (!clientId.equals("com.dpm.pingpong-login")) {
            throw new InvalidIdTokenException(INVALID_CLIENT_ID);
        }
    }

    /**
     * issuer값에 애플이 포함되었는지
     */
    private void verifyIssuer(Claims body) {
        String issuer = body.getIssuer();
        if (!issuer.contains(APPLE_BASE_URL)) {
            throw new InvalidIdTokenException(INTERVAL_SERVER_ERROR);
        }
    }

    /**
     * nonce값이 일치하는지
     */
    private void verifyNonce(Claims body) {
        String nonce = (String) body.get("nonce");
        if (!nonce.equals("nonce value")) {
            throw new InvalidIdTokenException(DOES_NOT_MATCH_NONCE);
        }
    }

}

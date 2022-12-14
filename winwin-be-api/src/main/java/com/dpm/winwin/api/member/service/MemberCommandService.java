package com.dpm.winwin.api.member.service;

import static com.dpm.winwin.api.common.constant.ImageType.PROFILE_IMAGE;
import static com.dpm.winwin.api.common.error.enums.ErrorMessage.APPLE_TOKEN_REVOKE_FAIL;
import static com.dpm.winwin.api.common.error.enums.ErrorMessage.MEMBER_NOT_FOUND;
import static com.dpm.winwin.domain.entity.member.enums.TalentType.GIVE;
import static com.dpm.winwin.domain.entity.member.enums.TalentType.TAKE;

import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import com.dpm.winwin.api.common.file.service.FileService;
import com.dpm.winwin.api.member.dto.request.MemberNicknameRequest;
import com.dpm.winwin.api.member.dto.request.MemberUpdateRequest;
import com.dpm.winwin.api.member.dto.response.MemberDeleteResponse;
import com.dpm.winwin.api.member.dto.response.MemberNicknameResponse;
import com.dpm.winwin.api.member.dto.response.MemberUpdateImageResponse;
import com.dpm.winwin.api.member.dto.response.MemberUpdateResponse;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.oauth.OauthToken;
import com.dpm.winwin.domain.entity.post.Likes;
import com.dpm.winwin.domain.repository.category.SubCategoryRepository;
import com.dpm.winwin.domain.repository.member.MemberRepository;
import com.dpm.winwin.domain.repository.post.LikesRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberCommandService {

    private static final String TOKEN_TYPE_HINT = "refresh_token";
    private final MemberRepository memberRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final RestTemplate restTemplate;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final FileService fileService;

    private final LikesRepository likesRepository;

    public MemberNicknameResponse updateMemberNickname(Long memberId,
                                                       MemberNicknameRequest memberNicknameRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        member.updateNickname(memberNicknameRequest.nickname());
        return new MemberNicknameResponse(member.getNickname());
    }

    public MemberUpdateImageResponse updateProfileImage(Long memberId, MultipartFile multipartFile) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        String profileImageUrl = fileService.uploadFile(multipartFile, memberId, PROFILE_IMAGE);
        member.updateProfileImage(profileImageUrl);
        return new MemberUpdateImageResponse(profileImageUrl);
    }

    public MemberUpdateResponse updateMember(Long memberId,
                                         MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        List<SubCategory> takenTalents = null;
        if (!CollectionUtils.isEmpty(memberUpdateRequest.takenTalents())){
            takenTalents = subCategoryRepository.findAllById(memberUpdateRequest.takenTalents());
        }

        List<SubCategory> givenTalents = null;
        if (!CollectionUtils.isEmpty(memberUpdateRequest.givenTalents())){
            givenTalents = subCategoryRepository.findAllById(memberUpdateRequest.givenTalents());
        }

        member.update(memberUpdateRequest.toDto(), givenTalents, takenTalents);

        return new MemberUpdateResponse(
                member.getNickname(),
                member.getImage(),
                member.getIntroduction(),
                member.getProfileLink(),
                member.getTalents().stream()
                        .filter(memberTalent -> memberTalent.getType().equals(GIVE))
                        .map(memberTalent -> memberTalent.getTalent().getName())
                        .toList(),
                member.getTalents().stream()
                        .filter(memberTalent -> memberTalent.getType().equals(TAKE))
                        .map(memberTalent -> memberTalent.getTalent().getName())
                        .toList()
        );
    }

    public MemberDeleteResponse deleteMember(Long memberId, String content) {

        Member member = memberRepository.findMemberWithToken(memberId)
            .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        List<Likes> likes = likesRepository.findAllByMember(member);

        log.info("member : {}", member);
        ResponseEntity<Object> response = revokeAppleToken(member);

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            likesRepository.deleteAll(likes);
            memberRepository.delete(member);

            MemberDeleteResponse memberDeleteResponse = new MemberDeleteResponse(memberId);
            return memberDeleteResponse;
        }

        throw new BusinessException(APPLE_TOKEN_REVOKE_FAIL);
    }

    @NotNull
    private ResponseEntity<Object> revokeAppleToken(Member member) {
        OauthToken oauthToken = member.getOauthToken();
        String refreshToken = oauthToken.getRefreshToken();

        return appleTokenRevokeRequest(oauthToken.getProviderType().name(), refreshToken);
    }

    private ResponseEntity<Object> appleTokenRevokeRequest(String providerName, String refreshToken) {
        ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(providerName.toLowerCase());
        String clientSecret = registration.getClientSecret();
        String clientId = registration.getClientId();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("token_type_hint", TOKEN_TYPE_HINT);
        params.add("token", refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> revokeRequest = new HttpEntity<>(params, headers);

        return restTemplate.postForEntity("https://appleid.apple.com/auth/revoke", revokeRequest, Object.class);
    }
}

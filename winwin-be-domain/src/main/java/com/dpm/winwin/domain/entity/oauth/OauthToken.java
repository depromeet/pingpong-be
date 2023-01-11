package com.dpm.winwin.domain.entity.oauth;

import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.member.enums.ProviderType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints =
        {@UniqueConstraint(
                name = "oauth_token_infomation",
                columnNames = {"social_id", "provider_type"})}
)
@Entity
public class OauthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "social_id")
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type")
    private ProviderType providerType;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    public OauthToken(Member member, String socialId, ProviderType providerType, String accessToken, String refreshToken) {
        this.member = member;
        this.socialId = socialId;
        this.providerType = providerType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void changeToken(String accessToken, String refreshToken){
        if (StringUtils.hasText(accessToken)) {
            this.accessToken = accessToken;
        }

        if (StringUtils.hasText(accessToken)) {
            this.refreshToken = refreshToken;
        }
    }
}

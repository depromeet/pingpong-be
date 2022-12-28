package com.dpm.winwin.domain.entity.post;

import com.dpm.winwin.domain.entity.BaseEntity;
import com.dpm.winwin.domain.entity.member.Member;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Getter
@Entity
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Likes(Member member, Post post) {
        this.member = member;
        this.post = post;
    }

    public static String changeFormatCountToString(int likeCount) {
        if (likeCount >= 1000) {
            double kCount = likeCount / 1000.0;
            return String.valueOf(Math.floor(kCount * 10) / 10.0)
                .replace(".0", "") + "k";
        }
        return String.valueOf(likeCount);
    }
}

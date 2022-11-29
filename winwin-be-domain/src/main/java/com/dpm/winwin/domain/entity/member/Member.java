package com.dpm.winwin.domain.entity.member;

import com.dpm.winwin.domain.entity.BaseEntity;
import com.dpm.winwin.domain.entity.chat.ChatRoom;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@DynamicInsert
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "host")
    private final List<ChatRoom> chatRooms = new ArrayList<>();

    @Column(nullable = false)
    private String nickname;

    private String image;

    private String introductions;

    private int exchangeCount;

    private String profileLink;

    private String ranks;

    private String provider;

    private String socialId;

    @Builder
    public Member(String nickname, String provider, String socialId) {
        this.nickname = nickname;
        this.provider = provider;
        this.socialId = socialId;
    }
}

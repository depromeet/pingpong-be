package com.dpm.winwin.domain.entity.member;

import com.dpm.winwin.domain.entity.BaseEntity;
import com.dpm.winwin.domain.entity.chat.ChatRoom;
import com.dpm.winwin.domain.entity.post.Post;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "host")
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @Column(nullable = false)
    private String nickname;

    private String image;

    private String introductions;

    private int exchangeCount;

    private String profileLink;
}

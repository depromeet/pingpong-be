package com.dpm.winwin.domain.entity.member;

import com.dpm.winwin.domain.entity.BaseEntity;
import com.dpm.winwin.domain.dto.member.MemberUpdateDto;
import com.dpm.winwin.domain.entity.chat.ChatRoom;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.member.enums.Ranks;
import com.dpm.winwin.domain.entity.post.Post;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dpm.winwin.domain.entity.member.enums.TalentType.GIVE;
import static com.dpm.winwin.domain.entity.member.enums.TalentType.TAKE;

@DynamicInsert
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "host")
    private final List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTalent> talents = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @Column(nullable = false)
    private String nickname;

    private String image;

    private String introduction;

    private String profileLink;

    private int exchangeCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ranks ranks;

    private Integer likeCount;

    private String provider;

    private String socialId;

    @Builder
    public Member(String nickname, String provider, String socialId) {
        this.nickname = nickname;
        this.provider = provider;
        this.socialId = socialId;
        this.likeCount = 0;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    private void setGivenTalents(List<SubCategory> afterGivenTalents) {

        Set<Long> before = this.talents.stream()
                .filter(givenTalent -> givenTalent.getType().equals(GIVE))
                .map(givenTalent -> givenTalent.getTalent().getId())
                .collect(Collectors.toSet());

        Set<Long> after = afterGivenTalents.stream()
                .map(SubCategory::getId)
                .collect(Collectors.toSet());

        this.talents.removeIf(givenTalent -> (givenTalent.getType().equals(GIVE) && !after.contains(givenTalent.getTalent().getId())));

        this.talents.addAll(afterGivenTalents.stream()
                .filter(category -> !before.contains(category.getId()))
                .map(category -> MemberTalent.of(this, category, GIVE))
                .toList());

    }

    private void setTakenTalents(List<SubCategory> afterTakenTalents) {

        Set<Long> before = this.talents.stream()
                .filter(takenTalent -> takenTalent.getType().equals(TAKE))
                .map(takenTalent -> takenTalent.getTalent().getId())
                .collect(Collectors.toSet());

        Set<Long> after = afterTakenTalents.stream()
                .map(SubCategory::getId)
                .collect(Collectors.toSet());

        this.talents.removeIf(takenTalent -> (takenTalent.getType().equals(TAKE) && !after.contains(takenTalent.getTalent().getId())));

        this.talents.addAll(afterTakenTalents.stream()
                .filter(category -> !before.contains(category.getId()))
                .map(category -> MemberTalent.of(this, category, TAKE))
                .toList());

    }

    public void update(MemberUpdateDto memberUpdateDto, List<SubCategory> givenTalents, List<SubCategory> takenTalents){
        this.nickname = memberUpdateDto.nickname();
        this.image = memberUpdateDto.image();
        this.introduction = memberUpdateDto.introduction();
        this.profileLink = memberUpdateDto.profileLink();
        setGivenTalents(givenTalents);
        setTakenTalents(takenTalents);
    }

    public Integer getLikeCount(){
        if (this.likeCount == null){
            this.likeCount = 0;
        }
        return this.likeCount;
    }

    public void updateTotalPostLike(){
        this.likeCount += 1;
        updateRank(this.likeCount);
    }

    private void updateRank(Integer likesCount){

        Ranks rank = Arrays.stream(Ranks.values())
                .filter( value -> likesCount >= value.getLikeCount())
                .findFirst().get();

        updateMemberRank(rank);

    }

    private void updateMemberRank(Ranks rank){
        this.ranks = rank;
    }
}

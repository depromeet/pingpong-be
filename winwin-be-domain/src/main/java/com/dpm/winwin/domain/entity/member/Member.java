package com.dpm.winwin.domain.entity.member;

import static com.dpm.winwin.domain.entity.member.enums.TalentType.GIVE;
import static com.dpm.winwin.domain.entity.member.enums.TalentType.TAKE;

import com.dpm.winwin.domain.dto.member.MemberUpdateDto;
import com.dpm.winwin.domain.entity.BaseEntity;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.chat.ChatRoom;
import com.dpm.winwin.domain.entity.member.enums.Ranks;
import com.dpm.winwin.domain.entity.oauth.OauthToken;
import com.dpm.winwin.domain.entity.post.Post;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

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

    private BigDecimal likeCount;

    // TODO: one to one 연관관계 편의 메서드?
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.REMOVE)
    private OauthToken oauthToken;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.REMOVE)
    private final List<RefreshToken> refreshTokens = new ArrayList<>();

    public Member(String nickname, Ranks ranks) {
        this.nickname = nickname;
        this.ranks = ranks;
        this.likeCount = BigDecimal.valueOf(0);
    }

    public void updateOauthToken(OauthToken oauthToken){
        this.oauthToken = oauthToken;
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

    public BigDecimal getLikeCount(){
        if (this.likeCount == null){
            nullToZero();
        }
        return this.likeCount;
    }

    private void nullToZero(){
        this.likeCount = BigDecimal.valueOf(0);
    }

    public void plusTotalPostLike(){
        if (this.likeCount == null){
            nullToZero();
        }
        this.likeCount.add(BigDecimal.valueOf(1));
        updateRank(this.likeCount);
    }

    public void minusTotalPostLike(){
        this.likeCount.subtract(BigDecimal.valueOf(1));
        updateRank(this.likeCount);
    }

    private void updateRank(BigDecimal likesCount){

        Ranks rank = Arrays.stream(Ranks.values())
                .filter( value -> likesCount.compareTo(value.getLikeCount()) >= 0 )
                .findFirst().get();

        updateMemberRank(rank);

    }

    private void updateMemberRank(Ranks rank){
        this.ranks = rank;
    }
}

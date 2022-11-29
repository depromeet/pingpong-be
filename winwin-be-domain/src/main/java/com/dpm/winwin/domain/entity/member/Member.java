package com.dpm.winwin.domain.entity.member;

import com.dpm.winwin.domain.dto.member.MemberUpdateDto;
import com.dpm.winwin.domain.entity.BaseEntity;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.chat.ChatRoom;
import com.dpm.winwin.domain.entity.talent.MemberTalent;
import com.dpm.winwin.domain.entity.talent.enums.TalentType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "host")
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTalent> givenTalents = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTalent> takenTalents = new ArrayList<>();

    @Column(nullable = false)
    private String nickname;

    private String image;

    private String introductions;

    private int exchangeCount;

    private String profileLink;

    @Builder
    public Member(String nickname,
                  String image,
                  String introduction,
                  int exchangeCount,
                  String profileLink){
        this.nickname = nickname;
        this.image = image;
        this.introductions = introduction;
        this.exchangeCount = exchangeCount;
        this.profileLink = profileLink;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateImage(String image){
        this.image = image;
    }

    public void setGivenTalents(List<SubCategory> afterTalentCategories) {

        Set<Long> before = this.takenTalents.stream()
                .map(takenTalent -> takenTalent.getTalent().getId())
                .collect(Collectors.toSet());

        Set<Long> after = afterTalentCategories.stream()
                .map(SubCategory::getId)
                .collect(Collectors.toSet());

        this.takenTalents.removeIf(takenTalent -> !after.contains(takenTalent.getTalent().getId()));

        this.takenTalents.addAll(afterTalentCategories.stream()
                .filter(category -> !before.contains(category.getId()))
                .map(category -> MemberTalent.of(this, category, TalentType.GIVE))
                .toList());

    }

    public void setTakenTalents(List<SubCategory> afterTalentCategories) {

        Set<Long> before = this.takenTalents.stream()
                .map(takenTalent -> takenTalent.getTalent().getId())
                .collect(Collectors.toSet());

        Set<Long> after = afterTalentCategories.stream()
                .map(SubCategory::getId)
                .collect(Collectors.toSet());

        this.takenTalents.removeIf(takenTalent -> !after.contains(takenTalent.getTalent().getId()));

        this.takenTalents.addAll(afterTalentCategories.stream()
                .filter(category -> !before.contains(category.getId()))
                .map(category -> MemberTalent.of(this, category, TalentType.TAKE))
                .toList());

    }

    public void update(MemberUpdateDto memberUpdateDto,List<SubCategory> givenTalents,List<SubCategory> takenTalents){
        this.nickname = memberUpdateDto.nickname();
        this.image = memberUpdateDto.image();
        this.introductions = memberUpdateDto.introductions();
        this.profileLink = memberUpdateDto.profileLink();
        setGivenTalents(givenTalents);
        setTakenTalents(takenTalents);
    }

}

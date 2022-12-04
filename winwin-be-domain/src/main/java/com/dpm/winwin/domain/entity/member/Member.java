package com.dpm.winwin.domain.entity.member;

import com.dpm.winwin.domain.dto.link.LinkDto;
import com.dpm.winwin.domain.entity.BaseEntity;
import com.dpm.winwin.domain.dto.member.MemberUpdateDto;
import com.dpm.winwin.domain.entity.chat.ChatRoom;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.link.Link;
import com.dpm.winwin.domain.entity.member.enums.Ranks;
import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.entity.member.enums.TalentType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dpm.winwin.domain.entity.member.enums.TalentType.GIVE;
import static com.dpm.winwin.domain.entity.member.enums.TalentType.TAKE;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "host")
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTalent> talents = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<Link> profileLinks = new ArrayList<>();

    @Column(nullable = false)
    private String nickname;

    private String image;

    private String introduction;

    private int exchangeCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ranks ranks;

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void setGivenTalents(List<SubCategory> afterGivenTalents) {

        Set<Long> before = this.talents.stream()
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

    public void setTakenTalents(List<SubCategory> afterTakenTalents) {

        Set<Long> before = this.talents.stream()
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

    public void setProfileLinks(List<LinkDto> afterLinks) {

        if (this.profileLinks.isEmpty()) {
            return;
        }

        Set<Long> before = this.profileLinks.stream()
                .map(Link::getId)
                .collect(Collectors.toSet());

        Set<Long> after = afterLinks.stream()
                .map(LinkDto::id)
                .collect(Collectors.toSet());

        this.profileLinks.removeIf(
                link -> !after.contains(link.getId()));

        this.profileLinks.addAll(afterLinks
                .stream()
                .filter(link -> !before.contains(link.id()))
                .map(LinkDto::toEntity)
                .toList());
    }

    public void update(MemberUpdateDto memberUpdateDto, List<SubCategory> givenTalents, List<SubCategory> takenTalents){
        this.nickname = memberUpdateDto.nickname();
        this.image = memberUpdateDto.image();
        this.introduction = memberUpdateDto.introduction();
        setGivenTalents(givenTalents);
        setTakenTalents(takenTalents);
        setProfileLinks(memberUpdateDto.profileLinks());
    }
}

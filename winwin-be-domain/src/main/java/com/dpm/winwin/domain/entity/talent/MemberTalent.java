package com.dpm.winwin.domain.entity.talent;

import com.dpm.winwin.domain.entity.BaseEntity;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.talent.enums.TalentType;
import lombok.AccessLevel;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTalent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talent_id")
    private SubCategory talent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TalentType type;

    public MemberTalent(Member member,SubCategory talent, TalentType type){
        this.member = member;
        this.talent = talent;
        this.type = type;
    }

    public static MemberTalent of(Member member,SubCategory talent,TalentType type){
        return new MemberTalent(member,talent,type);
    }

    public MemberTalent(SubCategory talent, TalentType type){
        this.talent = talent;
        this.type = type;
    }

    public static MemberTalent of(SubCategory talent,TalentType type){
        return new MemberTalent(talent,type);
    }

}

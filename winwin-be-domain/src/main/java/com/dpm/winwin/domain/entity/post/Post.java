package com.dpm.winwin.domain.entity.post;

import com.dpm.winwin.domain.entity.BaseEntity;
import com.dpm.winwin.domain.entity.category.MainCategory;
import com.dpm.winwin.domain.entity.category.MidCategory;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.link.Link;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid_category_id")
    private MidCategory midCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id")
    private List<Link> links = new ArrayList<>();

    @Column(nullable = false)
    private String chatLink;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTalent> takenTalents = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes;

    @Column(nullable = false)
    private String title;

    private String content;

    @Column(nullable = false)
    private boolean isShare;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExchangeType exchangeType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExchangePeriod exchangePeriod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExchangeTime exchangeTime;

    private String takenContent;

    @Builder
    public Post(Member member, MainCategory mainCategory, MidCategory midCategory, SubCategory subCategory,
                List<Link> links, String title, String content, boolean isShare, ExchangeType exchangeType,
                ExchangePeriod exchangePeriod, ExchangeTime exchangeTime, String takenContent) {
        this.member = member;
        this.mainCategory = mainCategory;
        this.midCategory = midCategory;
        this.subCategory = subCategory;
        this.links = links;
        this.title = title;
        this.content = content;
        this.isShare = isShare;
        this.exchangeType = exchangeType;
        this.exchangePeriod = exchangePeriod;
        this.exchangeTime = exchangeTime;
        this.takenContent = takenContent;
    }

    public void writeBy(Member member) {
        this.member = member;
    }

    public void setAllCategory(MainCategory mainCategory, MidCategory midCategory, SubCategory subCategory) {
        this.mainCategory = mainCategory;
        this.midCategory = midCategory;
        this.subCategory = subCategory;
    }

    public void setLink(List<Link> links) {
        this.links = links;
    }

    public void addTakenTalent(PostTalent postTalent) {
        this.takenTalents.add(postTalent);
    }

    public void setTakenContent(String takenContent) {
        this.takenContent = takenContent;
    }
}

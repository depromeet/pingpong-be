package com.dpm.winwin.domain.entity.post;

import com.dpm.winwin.domain.dto.link.LinkDto;
import com.dpm.winwin.domain.dto.post.PostUpdateDto;
import com.dpm.winwin.domain.entity.BaseEntity;
import com.dpm.winwin.domain.entity.category.MainCategory;
import com.dpm.winwin.domain.entity.category.MidCategory;
import com.dpm.winwin.domain.entity.category.SubCategory;
import com.dpm.winwin.domain.entity.link.Link;
import com.dpm.winwin.domain.entity.member.Member;
import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import java.util.ArrayList;
import java.util.HashSet;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

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
    private Set<Likes> likes = new HashSet<>();

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
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

    @Column(length = 500)
    private String takenContent;

    public Post(String title, String content, boolean isShare, String chatLink,
                ExchangeType exchangeType, ExchangePeriod exchangePeriod, ExchangeTime exchangeTime) {
        this.title = title;
        this.content = content;
        this.isShare = isShare;
        this.chatLink = chatLink;
        this.exchangeType = exchangeType;
        this.exchangePeriod = exchangePeriod;
        this.exchangeTime = exchangeTime;
    }

    public void writeBy(Member member) {
        this.member = member;
    }

    public void setAllCategoriesBySubCategory(SubCategory subCategory) {
        this.mainCategory = subCategory.getMidCategory().getMainCategory();
        this.midCategory = subCategory.getMidCategory();
        this.subCategory = subCategory;
    }

    public void setLink(List<String> links) {
        if (CollectionUtils.isEmpty(links)) {
            return ;
        }
        this.links = links.stream()
            .map(Link::from)
            .toList();
    }

    public void addTakenTalent(PostTalent postTalent) {
        this.takenTalents.add(postTalent);
    }

    public void setTakenContent(String takenContent) {
        this.takenContent = takenContent;
    }

    public void setLinks(List<LinkDto> afterLinks) {
        if (this.links.isEmpty()) {
            return;
        }

        Set<Long> before = this.links.stream()
            .map(Link::getId)
            .collect(Collectors.toSet());

        Set<Long> after = afterLinks.stream()
            .map(LinkDto::id)
            .collect(Collectors.toSet());

        this.links.removeIf(
            link -> !after.contains(link.getId())); // 클라이언트에서 요청한 link ID 가 존재하지 않으면 삭제
        this.links.addAll(afterLinks
            .stream()
            .filter(link -> !before.contains(link.id()))
            .map(LinkDto::toEntity)
            .toList()); // 기존에 있던게 아니면 새로 Entity 를 만들어 넣어준다

    }

    public void setTakenTalents(List<SubCategory> afterTakenTalents) {

        Set<Long> before = this.takenTalents.stream()
            .map(takenTalent -> takenTalent.getTalent().getId())
            .collect(Collectors.toSet());

        Set<Long> after = afterTakenTalents.stream()
            .map(SubCategory::getId)
            .collect(Collectors.toSet());

        this.takenTalents.removeIf(takenTalent -> !after.contains(takenTalent.getTalent().getId()));

        this.takenTalents.addAll(afterTakenTalents.stream()
            .filter(category -> !before.contains(category.getId()))
            .map(postTalent -> PostTalent.of(this, postTalent))
            .toList());

    }

    public void minusLikes(Likes likes) {
        this.likes.remove(likes);
    }

    public void update(PostUpdateDto updateDto, SubCategory subCategory, List<SubCategory> savedTalents) {
        this.title = updateDto.title();
        this.content = updateDto.content();
        this.isShare = updateDto.isShare();
        this.takenContent = updateDto.takenContent();
        this.chatLink = updateDto.chatLink();
        this.exchangeType = updateDto.exchangeType();
        this.exchangePeriod = updateDto.exchangePeriod();
        this.exchangeTime = updateDto.exchangeTime();
        setAllCategoriesBySubCategory(subCategory);
        setTakenTalents(savedTalents);
        setLinks(updateDto.links());
    }
}

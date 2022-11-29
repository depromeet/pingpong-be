package com.dpm.winwin.domain.entity.post;

import com.dpm.winwin.domain.entity.category.SubCategory;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTalent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talent_id")
    private SubCategory talent;

    public PostTalent(Post post, SubCategory talent) {
        this.post = post;
        this.talent = talent;
    }

    public static PostTalent of(Post post, SubCategory talent) {
        return new PostTalent(post, talent);
    }
}

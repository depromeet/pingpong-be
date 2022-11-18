package com.dpm.winwin.domain.entity.category;

import com.dpm.winwin.domain.entity.BaseEntity;
import com.dpm.winwin.domain.entity.post.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MidCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long mainCategoryId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String image;

    @OneToMany(mappedBy = "midCategory")
    private List<Post> posts = new ArrayList<>();
}

package com.dpm.winwin.domain.entity.category;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long midCategoryId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String image;

    @OneToMany(mappedBy = "subCategory")
    private List<Post> posts = new ArrayList<>();
}

package com.dpm.winwin.domain.configuration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.dpm.winwin.domain.repository.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


@DataJpaTest
@Import(QuerydslConfiguration.class)
class JpaConfigurationTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void injectedComponentIsNotNull() {
        assertThat(postRepository).isNotNull();
    }
}

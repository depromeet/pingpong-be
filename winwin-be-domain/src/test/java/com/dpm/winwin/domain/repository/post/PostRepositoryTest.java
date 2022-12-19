package com.dpm.winwin.domain.repository.post;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.dpm.winwin.domain.configuration.TestConfig;
import com.dpm.winwin.domain.entity.post.Post;
import com.dpm.winwin.domain.entity.post.enums.ExchangePeriod;
import com.dpm.winwin.domain.entity.post.enums.ExchangeTime;
import com.dpm.winwin.domain.entity.post.enums.ExchangeType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


@DataJpaTest
@Import(TestConfig.class)
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void injectedComponentIsNotNull() {
        assertThat(postRepository).isNotNull();
    }
}

package com.dpm.winwin.domain.repository.oauth;

import com.dpm.winwin.domain.entity.oauth.OauthToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthRepository extends JpaRepository<OauthToken, Long> {

}

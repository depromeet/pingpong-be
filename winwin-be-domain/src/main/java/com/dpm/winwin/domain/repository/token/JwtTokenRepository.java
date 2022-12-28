package com.dpm.winwin.domain.repository.token;

import com.dpm.winwin.domain.entity.token.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRepository extends CrudRepository<RefreshTokenEntity, Long> {

}

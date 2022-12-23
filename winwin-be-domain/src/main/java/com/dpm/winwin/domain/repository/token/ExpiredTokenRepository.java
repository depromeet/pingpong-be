package com.dpm.winwin.domain.repository.token;

import com.dpm.winwin.domain.entity.token.ExpiredToken;
import org.springframework.data.repository.CrudRepository;

public interface ExpiredTokenRepository extends CrudRepository<ExpiredToken, String> {

}

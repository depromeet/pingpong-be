package com.dpm.winwin.domain.repository.member;

import com.dpm.winwin.domain.entity.member.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findRefreshTokenByMemberId(Long memberId);

}

package com.dpm.winwin.domain.repository.member;

import com.dpm.winwin.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {

    Optional<Member> findByProviderAndSocialId(@Param("provider") String provider, @Param("socialId") String socialId);
}

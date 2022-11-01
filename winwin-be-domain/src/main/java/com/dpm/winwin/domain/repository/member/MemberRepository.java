package com.dpm.winwin.domain.repository.member;

import com.dpm.winwin.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {

}

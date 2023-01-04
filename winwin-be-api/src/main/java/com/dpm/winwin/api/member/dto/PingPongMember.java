package com.dpm.winwin.api.member.dto;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class PingPongMember implements UserDetails {

    private final Long memberId;
    private final List<GrantedAuthority> authorities;

    public PingPongMember(Long memberId, List authorities) {
        this.memberId = memberId;
        this.authorities = authorities;
    }

    public Long getMemberId() {
        return memberId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

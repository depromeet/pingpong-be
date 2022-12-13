package com.dpm.winwin.api.oauth.dto;

public record MemberInfo(Name name, String email) {

    public record Name(String firstName, String lastName) { }
}

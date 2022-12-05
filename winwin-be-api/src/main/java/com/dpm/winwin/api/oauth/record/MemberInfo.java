package com.dpm.winwin.api.oauth.record;

public record MemberInfo(Name name, String email) {

    public record Name(String firstName, String lastName) { }
}

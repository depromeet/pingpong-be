package com.dpm.winwin.api.record;

public record MemberInfo(Name name, String email) {

    public record Name(String firstName, String lastName) { }
}

package com.dpm.winwin.api.apple.record;

public record MemberInfo(Name name, String email) {

    public record Name(String firstName, String lastName) { }
}

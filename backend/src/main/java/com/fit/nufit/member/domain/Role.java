package com.fit.nufit.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    GUEST("ROLE_GUEST"), USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String key;
}

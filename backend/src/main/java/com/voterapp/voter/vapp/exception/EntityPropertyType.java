package com.voterapp.voter.vapp.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EntityPropertyType {

    EMAIL("email"),
    USERNAME("username"),
    POLL("poll");

    @NonNull
    private String value;
}

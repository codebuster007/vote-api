package com.voterapp.voter.vapp.exception;


import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionType {
    ENTITY_NOT_FOUND("not.found"),
    DUPLICATE_ENTITY("duplicate"),
    ENTITY_EXCEPTION("exception"),
    ALREADY_VOTED("already.voted"),
    POLL_TIME_EXPIRED("expired");

    @NonNull
    private String value;

}

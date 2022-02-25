package com.joomak.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    NOT_ENOUGH_STOCK("재고가 충분하지 않습니다."),
    ALREADY_EXISTS_MEMBER("은 이미 존재하는 회원입니다"),
    ;
// @RequiredArgsConstructor 로 아래코드 처리됨
//    ErrorMessage(String ErrorMessage) {
//        this.ErrorMessage =ErrorMessage;
//    }

    private final String errorMessage;
}

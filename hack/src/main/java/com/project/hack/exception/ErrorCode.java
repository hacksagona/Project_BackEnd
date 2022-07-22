package com.project.hack.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400 Bad Request
    COMMON_BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    EMPTY_CONTENT(HttpStatus.BAD_REQUEST,"필수입력값이 없습니다"),
    LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST,"제한수량을 초과했습니다"),
    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "만료되었거나 유효하지 않은 토큰입니다"),
    INVALID_LOGIN_ATTEMPT(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다(소셜로그인 에러)"),
    /* 403 FORBIDDEN : 권한이 없는 사용자 */
    INVALID_AUTHORITY(HttpStatus.FORBIDDEN,"권한이 없는 사용자 입니다"),
    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    CHATMESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅 정보를 찾을 수 없습니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 목표를 찾을 수 없습니다"),
    NICKNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "닉네임은 null이나 빈칸일수 없습니다"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물을 찾을 수 없습니다"),
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅방을 찾을 수 없습니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 코멘트를 찾을 수 없습니다"),
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 답글을 찾을 수 없습니다"),
    AUTH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다"),
    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일이 존재합니다"),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "중복된 닉네임이 존재합니다")
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
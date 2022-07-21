package com.project.hack.validator;

import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component // 선언하지 않으면 사용할 수 없다!!!!!
@RequiredArgsConstructor
public class TokenValidator {

    //userid와 토큰 비교
    public void userIdCompareToken(Long userid, Long tokenuserid) {
        if(!Objects.equals(userid, tokenuserid)){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }
}

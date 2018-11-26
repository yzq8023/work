package com.github.hollykunge.security.common.msg.auth;

import com.github.hollykunge.security.common.constant.RestCodeConstants;
import com.github.hollykunge.security.common.msg.BaseResponse;

/**
 * Created by 协同设计小组 on 2017/8/25.
 */
public class TokenForbiddenResponse  extends BaseResponse {
    public TokenForbiddenResponse(String message) {
        super(RestCodeConstants.TOKEN_FORBIDDEN_CODE, message);
    }
}

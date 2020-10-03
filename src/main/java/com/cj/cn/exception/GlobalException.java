package com.cj.cn.exception;

import com.cj.cn.response.CodeEnum;

public class GlobalException extends RuntimeException {
    private CodeEnum codeEnum;

    public GlobalException(CodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.codeEnum = codeEnum;
    }

    public CodeEnum getCodeEnum() {
        return codeEnum;
    }
}

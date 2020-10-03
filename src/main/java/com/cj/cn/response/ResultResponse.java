package com.cj.cn.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultResponse {
    int code;
    String msg;
    Object data;

    private ResultResponse() {
    }

    private ResultResponse(CodeEnum codeEnum) {
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMsg();
    }

    private ResultResponse(CodeEnum codeEnum, Object data) {
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMsg();
        this.data = data;
    }

    public static ResultResponse ok() {
        return new ResultResponse(CodeEnum.OK);
    }

    public static ResultResponse ok(Object data) {
        return new ResultResponse(CodeEnum.OK, data);
    }

    public static ResultResponse error(CodeEnum codeEnum) {
        return new ResultResponse(codeEnum);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}

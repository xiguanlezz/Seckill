package com.cj.cn.response;

public enum CodeEnum {
    //通用枚举对象
    OK(0, "success"),
    //登录模块(4001XX)
    INCORRECT_MOBILE(400100, "手机号码输入错误"),
    INCORRECT_PWD(400110, "密码输入错误"),
    BIND_ERROR(400120, "参数绑定异常"),
    COOKIE_EXPIRE(400130, "会话已过期, 请重新登录"),
    NO_LOGIN(400140, "用户未登录"),
    //秒杀模块(4002XX)
    REPEATE_MIAOSHA(400200, "不能重复秒杀"),
    MIAOSHA_OVER(400210, "商品已经秒杀完毕"),
    MIAOSHA_FAIL(400220, "商品秒杀失败");

    int code;
    String msg;

    CodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public CodeEnum setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}

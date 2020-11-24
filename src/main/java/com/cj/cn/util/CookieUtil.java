package com.cj.cn.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(ConstUtil.cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 30);  //设置30分钟过期
        response.addCookie(cookie);
    }

    public static String readLoginToken(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(ConstUtil.cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static boolean isExistToken(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(ConstUtil.cookieName)) {
                return true;
            }
        }
        return false;
    }
}

package com.cj.cn.cache;

import com.cj.cn.pojo.User;

/**
 * 没有写remove方法的ThreadLocal类
 */
public class UserContext {
    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }
}

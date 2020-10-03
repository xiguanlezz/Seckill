package com.cj.cn.service;

import com.cj.cn.dto.LoginDTO;

public interface IUserService {
    /**
     * 用户登录
     */
    boolean login(LoginDTO loginDTO);
}

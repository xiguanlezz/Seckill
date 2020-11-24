package com.cj.cn.service;

import com.cj.cn.dto.LoginDTO;
import com.cj.cn.pojo.User;

public interface IUserService {
    /**
     * 用户登录
     */
    User login(LoginDTO loginDTO);
}

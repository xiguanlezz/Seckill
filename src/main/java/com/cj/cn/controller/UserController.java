package com.cj.cn.controller;

import com.cj.cn.pojo.User;
import com.cj.cn.response.ResultResponse;
import com.cj.cn.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping("info")
    public ResultResponse info(User user) {
        return ResultResponse.ok(user);
    }
}

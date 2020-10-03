package com.cj.cn.controller;

import com.cj.cn.dto.LoginDTO;
import com.cj.cn.response.ResultResponse;
import com.cj.cn.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody   //SpringMVC默认用的是JSON序列化的方式, 如果这个类没有get方法会报错(猜测底层是反射)
    public ResultResponse doLogin(@Valid LoginDTO loginDTO) {
        log.info(loginDTO.toString());
        //登录
        iUserService.login(loginDTO);
        return ResultResponse.ok();
    }
}

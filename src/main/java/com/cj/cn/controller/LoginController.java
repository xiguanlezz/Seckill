package com.cj.cn.controller;

import com.cj.cn.dto.LoginDTO;
import com.cj.cn.pojo.User;
import com.cj.cn.response.ResultResponse;
import com.cj.cn.service.IUserService;
import com.cj.cn.util.ConstUtil;
import com.cj.cn.util.CookieUtil;
import com.cj.cn.util.JsonUtil;
import com.cj.cn.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody   //SpringMVC默认用的是JSON序列化的方式, 如果这个类没有get方法会报错(猜测底层是反射)
    public ResultResponse doLogin(@Valid LoginDTO loginDTO, HttpServletResponse response) {
        log.info(loginDTO.toString());
        User user = iUserService.login(loginDTO);
        assert user != null;
        String token = ConstUtil.cookieValuePrefix + user.getId();
        CookieUtil.writeLoginToken(response, token);
        redisUtil.set(ConstUtil.cookieValuePrefix + user.getId(), JsonUtil.objToStr(user), 30, TimeUnit.MINUTES);
        return ResultResponse.ok(token);
    }
}

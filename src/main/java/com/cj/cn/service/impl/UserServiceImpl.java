package com.cj.cn.service.impl;

import com.cj.cn.dto.LoginDTO;
import com.cj.cn.exception.GlobalException;
import com.cj.cn.mapper.UserMapper;
import com.cj.cn.pojo.User;
import com.cj.cn.response.CodeEnum;
import com.cj.cn.service.IUserService;
import com.cj.cn.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean login(LoginDTO loginDTO) {
        User user = userMapper.selectByPrimaryKey(loginDTO.getMobile());
        if (user == null) {
            throw new GlobalException(CodeEnum.INCORRECT_MOBILE);
        }
        String dbPwd = user.getPassword();
        String pwd = MD5Util.formPwdToDBPwd(loginDTO.getPassword(), user.getSalt());
        if (!StringUtils.equals(dbPwd, pwd)) {
            throw new GlobalException(CodeEnum.INCORRECT_PWD);
        }
        return true;
    }
}

package com.cj.cn.dto;

import com.cj.cn.annotation.IsMobile;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class LoginDTO {
    @IsMobile(required = true)
    private String mobile;

    @NotNull(message = "密码不能为空")
    @Length(min = 6, message = "密码长度不对")
    private String password;
}

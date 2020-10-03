package com.cj.cn.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

//第一个泛型表示注解的类, 第二个泛型表示注解作用的成员属性的类型
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) {
            return isMobilePattern(value);
        }
        return false;
    }

    private boolean isMobilePattern(String mobile) {
        String pattern = "1\\d{10}";
        return Pattern.matches(pattern, mobile);
    }

    public static void main(String[] args) {
        IsMobileValidator v = new IsMobileValidator();
        boolean flag = v.isMobilePattern("12345678910");
        System.out.println(flag);
    }
}

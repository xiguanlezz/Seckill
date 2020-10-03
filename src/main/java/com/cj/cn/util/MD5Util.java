package com.cj.cn.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    private static final String salt = "this#is#a#salt";

    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static String inputPwdToFormPwd(String inputPwd) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(salt.charAt(1))
                .append(salt.charAt(3))
                .append(inputPwd)
                .append(salt.charAt(5))
                .append(salt.charAt(7));
        //在表单提交处第一次加盐MD5
        return DigestUtils.md5Hex(buffer.toString());
    }

    public static String formPwdToDBPwd(String formPwd, String dbSalt) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(dbSalt.charAt(1))
                .append(dbSalt.charAt(3))
                .append(formPwd)
                .append(dbSalt.charAt(5))
                .append(dbSalt.charAt(7));
        //入库时使用数据库中的盐再次MD5
        return DigestUtils.md5Hex(buffer.toString());
    }

    public static String inputPwdToDBPwd(String inputPwd, String dbSalt) {
        String formPwd = inputPwdToFormPwd(inputPwd);
        String dbPwd = formPwdToDBPwd(formPwd, dbSalt);
        return dbPwd;
    }

    public static void main(String[] args) {
        String pwd = inputPwdToDBPwd("123456", "qwe123456");
        System.out.println(pwd);
    }
}

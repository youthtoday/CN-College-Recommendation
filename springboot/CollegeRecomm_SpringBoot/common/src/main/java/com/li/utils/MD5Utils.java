package com.li.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//MD5生成器
public class MD5Utils {
    public static String md5(String password){
        //生成一个md5加密器
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            //计算MD5的值
            md.update(password.getBytes());
            //BigInteger将8位的字符串转成16位的字符串，得到的字符串形式是哈希码值
            //BigInteger(参数1，参数2...)参数1是 1为正数 0为0 -1为负数
            return new BigInteger(1,md.digest()).toString(16);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }
}

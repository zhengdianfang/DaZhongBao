package com.zhengdianfang.dazhongbao.models.cache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zheng on 16/6/15.
 */

public class MD5 {

    /**
     * 对输入文本进行MD5加密
     * @param info 被加密文本，如果输入为null则返回null
     * *
     * @return MD5 加密后的内容，如果为null说明输入为null
     */
    public  static String getMD5(String info) {
        return getMD5(info.getBytes());
    }

    public static String getMD5(byte[] info) {
        if (null == info) {
            return null;
        }
        StringBuffer buf = new StringBuffer("");
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        md.update(info);
        byte[] b = md.digest();
        for (int i = 0; i < b.length; i++) {
            i = (int)b[i];
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));


        }
        return buf.toString();
    }
}

package com.crt.common.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * @author: wangyonggui@e6yun.com
 * @date: 2019/2/20
 * @description:
 */
public class MD5Help {

    //    明文(123456)密码MD5后： e10adc3949ba59abbe56e057f20f883e
    //    密码盐：zMsImskFQyaJv5XJ2CFzmg==
    //    密码：PQ05ekpjVNZRiPgxFuPgNw==

    public static void main(String[] args) {
        String sss = EncodePassword("d0748ae7e7d2f0808f0fd4c36e03ec45", "KtlU8RNBYzyU/vqnwBmixw==");
        String password = "iI7aFZyyq7u1WkPDs3uyzQ==";
        if(sss.equals(password)){
            System.out.println(sss);
        }

    }

    /**
     * 兼容.net中的unicode字符编码
     *
     * @param bytesAfterMd5
     * @param salt
     * @return
     */
    public static String EncodePassword(String bytesAfterMd5, String salt) {
        try {
            // 对应.net中的unicode
            return EncodePassword(bytesAfterMd5.getBytes("UTF-16LE"), salt);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return String.valueOf("");
        }
    }

    private static String EncodePassword(byte[] bytesAfterMd5, String salt) {
        byte[] hasSymbolBytePassword = Base64.decodeBase64(salt);
        byte[] notSymbolBytePassword = getBytes(hasSymbolBytePassword);

        // 无符号的
        byte[] md5passBytes = getBytes(bytesAfterMd5);
        int srcLen = notSymbolBytePassword.length;
        byte[] dst = new byte[srcLen + md5passBytes.length + 1];

        System.arraycopy(notSymbolBytePassword, 0, dst, 0, srcLen);
        dst[srcLen] = 0x39;
        System.arraycopy(md5passBytes, 0, dst, srcLen + 1, md5passBytes.length);
        return Base64.encodeBase64String(md5Update(dst));
    }

    private static byte[] getBytes(byte[] bytesAfterMd5) {
        byte[] md5passBytes = new byte[bytesAfterMd5.length];
        byte indexPassItem = 0;
        for (int i = 0; i < bytesAfterMd5.length; i++) {
            indexPassItem = bytesAfterMd5[i];
            if (indexPassItem < 0) {
                indexPassItem += 256;
            }
            md5passBytes[i] = indexPassItem;
        }
        return md5passBytes;
    }

    /**
     * 获取MD5值
     *
     * @param value
     * @return
     */
    private static byte[] md5Update(byte[] value) {
        byte[] digest = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            digest = m.digest(value);
        } catch (Exception e) {
            String err = "";
        }
        return digest;
    }
}
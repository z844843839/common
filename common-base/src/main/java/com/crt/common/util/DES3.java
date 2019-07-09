package com.crt.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @Description DES工具类
 * @Author changyandong@e6yun.com
 * @Created Date: 2018/7/17 9:29
 * @ClassName DES3
 * @Version: 1.0
 */
public class DES3 {
    private static final String PASSWORD_CRYPT_KEY = "e6gisgps";
    private static final String IV = "e656#%&@";

    public DES3()
    {

    }

    /**
     * 加密

     *            密钥，长度必须是8的倍数
     * @return 返回加密后的数据
     * @throws Exception
     */
    public static String encrypt(String message) throws Exception
    {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(PASSWORD_CRYPT_KEY
                .getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] b = cipher.doFinal(message.getBytes("GBK"));

        return Base64.encodeBase64String(b);
    }

    /**
     * 解密
     *
     *            密钥，长度必须是8的倍数
     * @return 返回解密后的原始数据
     * @throws Exception
     */
    public static String decrypt(String message) throws Exception
    {

        //BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytesrc =  Base64.decodeBase64(message.getBytes());
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(PASSWORD_CRYPT_KEY
                .getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));

        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);

    }

    public static void main(String[] args) throws Exception
    {

//        String ss = "YYT,e94b7d0dce43545ec2af4daa00271c84";
//        System.out.println(des.encrypt(ss));
//        System.out.println(des.decrypt(des.encrypt(ss)));
//
//        String a = "i8kl48UWX4VGuK/UOW0yp0jyiOzhTXR+PAWsQvwwl8gogtXrzpcw0yIBk6lz+5SoPF3SYIce344=";
//        System.out.println(des.decrypt(a));
//
//        String b = "2012-06-01 15:36:22,ginfolist,1246976,1378560,bawang,1";
//        System.out.println(des.encrypt(b));

//        String ss = "hygj168,34ca67a05cbc9d9446178fd278d06625";
//        String desStr = des.encrypt(ss);
//        String s = "6Qr46Gh9SqsqiPz0gy/HH+O26/+IlVUo71Yhbt5m7fwtZGWSzGVu5vWxjV/HSF+6";
//        System.out.println(desStr);
//        System.out.println(desStr.equals(s));

        String a = "{'userName':'张三','password':'111','clientTime':'2012-12-17 10:10:10'}";
        System.out.println(a);
        System.out.println(DES3.encrypt(a));
        System.out.println(DES3.decrypt("wWW0nfQZgvkE22olnrx8VdcPgF89UkO/Tun9JnRf9OGViDT5KabxsSOACyJtoDkHM+65TnwSUJ4+zBZsHN3FAEUi9FlursIeKszDSMJLxgU="));
    }

}
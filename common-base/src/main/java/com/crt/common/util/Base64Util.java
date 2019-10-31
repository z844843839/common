package com.crt.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Base64工具类
 * @author malin@e6yun.com@e6yun.com on 2019年10月14日
 */
public class Base64Util {

    private static final Logger logger = LoggerFactory.getLogger(Base64Util.class);

    /**
     * 对字符串进行Base64加密
     * @param source
     * @return encodedText
     */
    public static String base64Encode(String source) {
        String encodedText = "";
        try{
            final Base64.Encoder encoder = Base64.getEncoder();
            final byte[] textByte = source.getBytes("UTF-8");
            encodedText = encoder.encodeToString(textByte);
        }catch (UnsupportedEncodingException e){
            logger.error("base64 encode exception",e);
        }
        return encodedText;
    }

    /**
     * 对字符串进行Base64解密
     * @param source
     * @return decodedText
     */
    public static String base64Decode(String source) {
        String decodedText = "";
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            decodedText = new String(decoder.decode(source), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("base64 encode exception",e);
        }
        return decodedText;
    }
}

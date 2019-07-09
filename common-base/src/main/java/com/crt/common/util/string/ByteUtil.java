package com.crt.common.util.string;

/**
 * Created with IntelliJ IDEA.
 * User: xue-song.e6yun
 * Date: 2014/9/8
 * Time: 0:05
 * To change this template use File | Settings | File Templates.
 */

public class ByteUtil {


    /**
     * 转换short为byte
     *
     * @param src
     * @param s     需要转换的short
     * @param index
     */
    public static void putShort(byte src[], short s, int index) {
        src[index + 1] = (byte) (s >> 8);
        src[index + 0] = (byte) (s >> 0);
    }

    /**
     * 通过byte数组取到short
     *
     * @param src
     * @return
     */
    public static short getShort(byte[] src) {
        return (short) getLong(src, 0, src.length);
    }

    /**
     * 通过byte数组取到short
     *
     * @param src
     * @param fromIndex
     * @param toIndex
     * @return
     */
    public static short getShort(byte[] src, int fromIndex, int toIndex) {
        if (src.length <= 2 || toIndex - fromIndex <= 2) {
            return (short) getLong(src, fromIndex, toIndex);
        }
        throw new RuntimeException("a");
    }

    /**
     * 转换int为byte数组
     *
     * @param src
     * @param x
     * @param index
     */
    public static void putInt(byte[] src, int x, int index) {
        int i = 0;

        if (x > 0xFFFFFF) {
            src[index + i] = (byte) (x >> 24);
            i++;
        }
        if (x > 0xFFFF) {
            src[index + i] = (byte) (x >> 16);
            i++;
        }
        if (x > 0xFF) {
            src[index + i] = (byte) (x >> 8);
            i++;
        }
        src[index + i] = (byte) (x >> 0);
    }


    public static void main(String[] args) {
        byte[] abc = new byte[10];
        putInt(abc, 250, 0);
    }

    /**
     * 通过byte数组取到int
     *
     * @param src
     * @return
     */
    public static int getInt(byte[] src) {
        return (int) getLong(src, 0, src.length);
    }

    /**
     * 通过byte数组取到int
     *
     * @param src
     * @param fromIndex
     * @param toIndex
     * @return
     */
    public static int getInt(byte[] src, int fromIndex, int toIndex) {
        if (src.length <= 4 || toIndex - fromIndex <= 4) {
            return (int) getLong(src, fromIndex, toIndex);
        }
        throw new RuntimeException("a");
    }


    /**
     * 转换long型为byte数组
     *
     * @param src
     * @param x
     * @param index
     */
    public static void putLong(byte[] src, long x, int index) {
        src[index + 7] = (byte) (x >> 56);
        src[index + 6] = (byte) (x >> 48);
        src[index + 5] = (byte) (x >> 40);
        src[index + 4] = (byte) (x >> 32);
        src[index + 3] = (byte) (x >> 24);
        src[index + 2] = (byte) (x >> 16);
        src[index + 1] = (byte) (x >> 8);
        src[index + 0] = (byte) (x >> 0);
    }

    /**
     * 通过byte数组取到long
     *
     * @param src
     * @return
     */
    public static long getLong(byte[] src) {
        return getLong(src, 0, src.length);
    }

    /**
     * 通过byte数组取到long
     *
     * @param src
     * @param fromIndex
     * @param toIndex
     * @return
     */
    public static long getLong(byte[] src, int fromIndex, int toIndex) {
        if (src.length <= 8 || toIndex - fromIndex <= 8) {
            long temp = 0;
            long res = 0;
            for (int i = fromIndex; i < toIndex; i++) {
                res <<= 8;
                temp = src[i] & 0xff;
                res |= temp;
            }
            return res;
        }
        throw new RuntimeException("a");
    }

    /**
     * 字符到字节转换
     *
     * @param ch
     * @return
     */
    public static void putChar(byte[] src, char ch, int index) {
        int temp = (int) ch;
        // byte[] src = new byte[2];
        for (int i = 0; i < 2; i++) {
            src[index + i] = new Integer(temp & 0xff).byteValue(); // 将最高位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
    }

    /**
     * 字节到字符转换
     *
     * @param src
     * @return
     */
    public static char getChar(byte[] src, int index) {
        return (char) (src[index] & 0xFF);
    }

    /**
     * float转换byte
     *
     * @param src
     * @param x
     * @param index
     */
    public static void putFloat(byte[] src, float x, int index) {
        // byte[] src = new byte[4];
        int l = Float.floatToIntBits(x);
        for (int i = 0; i < 4; i++) {
            src[index + i] = new Integer(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * 通过byte数组取得float
     *
     * @param src
     * @param index
     * @return
     */
    public static float getFloat(byte[] src, int index) {
        int l;
        l = src[index + 0];
        l &= 0xff;
        l |= ((long) src[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) src[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) src[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * double转换byte
     *
     * @param src
     * @param x
     * @param index
     */
    public static void putDouble(byte[] src, double x, int index) {
        // byte[] src = new byte[8];
        long l = Double.doubleToLongBits(x);
        for (int i = 0; i < 4; i++) {
            src[index + i] = new Long(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * 通过byte数组取得float
     *
     * @param src
     * @param index
     * @return
     */
    public static double getDouble(byte[] src, int index) {
        long l;
        l = src[0];
        l &= 0xff;
        l |= ((long) src[1] << 8);
        l &= 0xffff;
        l |= ((long) src[2] << 16);
        l &= 0xffffff;
        l |= ((long) src[3] << 24);
        l &= 0xffffffffl;
        l |= ((long) src[4] << 32);
        l &= 0xffffffffffl;
        l |= ((long) src[5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) src[6] << 48);
        l &= 0xffffffffffffffl;
        l |= ((long) src[7] << 56);
        return Double.longBitsToDouble(l);
    }

    /**
     * byte数组转十六进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        return bytesToHexString(src, 0, src.length);
    }

    /**
     * byte数组转十六进制字符串
     *
     * @param src
     * @param fromIndex
     * @param toIndex
     * @return
     */
    public static String bytesToHexString(byte[] src, int fromIndex, int toIndex) {
        StringBuffer stringBuffer = new StringBuffer("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = fromIndex; i < toIndex; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuffer.append(0);
            }
            stringBuffer.append(hv);
        }
        return stringBuffer.toString().toUpperCase();
    }

    /**
     * 十六进制字符串转byte数组
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * byte数组转二进制字符串
     *
     * @param src
     * @param fromIndex
     * @param toIndex
     * @return
     */
    public static String bytesToBitString(byte[] src, int fromIndex, int toIndex) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Long.toBinaryString(getLong(src, fromIndex, toIndex)));
        while ((toIndex - fromIndex) * 8 > stringBuffer.length()) {
            stringBuffer.insert(0, "0");
        }
        return stringBuffer.toString();

    }

    /**
     * Byte[] 转换 byte[]
     *
     * @param src
     * @return
     */
    public static byte[] BytesTobytes(Byte[] src) {
        byte[] bytes = new byte[src.length];
        for (int i = 0; i < src.length; i++) {
            bytes[i] = src[i];
        }
        return bytes;
    }

    /**
     * byte[]合并
     *
     * @param from
     * @param to
     * @param index
     * @return
     */
    public static byte[] bytesFillbytes(byte[] from, byte[] to, int index) {
        for (int i = 0; i < from.length; i++) {
            to[index++] = from[i];
        }
        return to;
    }

    /**
     * 字串首处填充0
     *
     * @param hexTemp
     * @param size
     * @return
     */
    public static StringBuffer fillHexStrZero(StringBuffer hexTemp, int size) {
        while (hexTemp.length() < size) {
            hexTemp.insert(0, "0");
        }
        return hexTemp;
    }
}
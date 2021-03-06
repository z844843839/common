package com.crt.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 验证码
 * @Author changyandong@e6yun.com
 * @Created Date: 2018/11/19 10:28
 * @ClassName VerifyUtil
 * @Version: 1.0
 */
public class VerifyUtil {
	// 验证码字符集
	private static final char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z' };
	// 字符数量
	private static final int SIZE = 4;
	// 干扰线数量
	private static final int LINES = 5;
	// 宽度
	private static final int WIDTH = 80;
	// 高度
	private static final int HEIGHT = 40;
	// 字体大小
	private static final int FONT_SIZE = 30;

	/**
	 * 生成随机验证码及图片 Object[0]：验证码字符串； Object[1]：验证码图片。
	 */
	public static Object[] createImage() {
		StringBuffer sb = new StringBuffer();
		// 1.创建空白图片
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		// 2.获取图片画笔
		Graphics graphic = image.getGraphics();
		// 3.设置画笔颜色
		graphic.setColor(Color.LIGHT_GRAY);
		// 4.绘制矩形背景
		graphic.fillRect(0, 0, WIDTH, HEIGHT);
		// 5.画随机字符
		Random ran = new Random();
		for (int i = 0; i < SIZE; i++) {
			// 取随机字符索引
			int n = ran.nextInt(chars.length);
			// 设置随机颜色
			graphic.setColor(getRandomColor());
			// 设置字体大小
			graphic.setFont(new Font(null, Font.BOLD + Font.ITALIC, FONT_SIZE));
			// 画字符
			graphic.drawString(chars[n] + "", i * WIDTH / SIZE, HEIGHT * 2 / 3);
			// 记录字符
			sb.append(chars[n]);
		}
		// 6.画干扰线
		for (int i = 0; i < LINES; i++) {
			// 设置随机颜色
			graphic.setColor(getRandomColor());
			// 随机画线
			graphic.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT), ran.nextInt(WIDTH), ran.nextInt(HEIGHT));
		}
		// 7.返回验证码和图片
		return new Object[] { sb.toString(), image };
	}

	/**
	 * 随机取色
	 */
	public static Color getRandomColor() {
		Random ran = new Random();
		Color color = new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
		return color;
	}


	static Logger logger = LoggerFactory.getLogger(VerifyUtil.class);
	static String baseCode = "0123456789ABCDEFGHJKLMNPQRTUWXY";
	static char[] baseCodeArray = baseCode.toCharArray();
	static int[] wi = { 1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28 };

	/**
	 *    * 较验社会统一信用代码    *    * @param unifiedCreditCode 统一社会信息代码    * @return 符合:
	 * true    
	 */
	public static boolean isLicense18(String unifiedCreditCode) {
		if ((unifiedCreditCode.equals("")) || unifiedCreditCode.length() != 18) {
			return false;
		}

		Map<Character, Integer> codes = new TreeBidiMap<>();
		
		for (int i = 0; i < baseCode.length(); i++) {
			codes.put(baseCodeArray[i], i);
		}
		
		int parityBit;
		try {
			char[] businessCodeArray = unifiedCreditCode.toCharArray();

			int sum = 0;
			for (int i = 0; i < 17; i++) {
				char key = businessCodeArray[i];
				if (baseCode.indexOf(key) == -1) {
					throw new ValidationException("第" + String.valueOf(i + 1) + "位传入了非法的字符" + key);
				}
				sum += (codes.get(key) * wi[i]);
			}
			int result = 31 - sum % 31;
			parityBit = (result == 31 ? 0 : result);
		} catch (ValidationException e) {
			return false;
		}

		return parityBit == codes.get(unifiedCreditCode.charAt(unifiedCreditCode.length() - 1));
	}

	/**
	 * @Title: isEmail
	 * @Description: 是否是邮箱
	 * @param email
	 * @return
	 * @return: boolean
	 */
	public static boolean isEmail(String email) {
//        if (null==email || "".equals(email)){
//        	return false; 
//        }
		String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p = Pattern.compile(regEx1);
		Matcher m = p.matcher(email);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @Title: isMobile
	 * @Description: 手机号验证
	 * @param str
	 * @return
	 * @return: boolean
	 */
	public static boolean isMobile(final String str) {
		boolean b = false;
		Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		Matcher m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * @Title: isPhone
	 * @Description: 电话号码验证
	 * @param str
	 * @return
	 * @return: boolean
	 */
	public static boolean isPhone(final String str) {
		Pattern p1 = null , p2 = null;
		Matcher m = null;
		boolean b = false;
		  p1 = Pattern.compile("^[0][0-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的,中间有"-"
         p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");  
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	public static void main(String[] args) {
		System.out.println(isPhone("010-87886152"));
	}
	
	public static boolean matchPhone(String number){
        Matcher m1 = null;
        Matcher m2 = null;
        Pattern p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的,中间有"-"
        Pattern p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        m1 = p1.matcher(number);
        m2 = p2.matcher(number);
        return m1.matches()||m2.matches();
	}
	
	/**
	 * @Title: isQq
	 * @Description: 电话号码验证
	 * @param str
	 * @return
	 * @return: boolean
	 */
	public static boolean isQq(String str) {
		boolean b = false;
		Pattern p = Pattern.compile("[1-9][0-9]{4,14}"); // 验证手机号
		Matcher m = p.matcher(str);
		b = m.matches();
		return b;

	}

	/**
	 * @Title: isBankCard
	 * @Description: 校验银行卡卡号
	 * @param bankCard
	 * @return
	 * @return: boolean
	 */
	public static boolean isBankCard(String bankCard) {
		if (bankCard.length() < 15 || bankCard.length() > 19) {
			return false;
		}
		char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
		if (bit == 'N') {
			return false;
		}
		return bankCard.charAt(bankCard.length() - 1) == bit;
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 * 
	 * @param nonCheckCodeBankCard
	 * @return
	 */
	public static char getBankCardCheckCode(String nonCheckCodeBankCard) {
		if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
				|| !nonCheckCodeBankCard.matches("\\d+")) {
			// 如果传的不是数据返回N
			return 'N';
		}
		char[] chs = nonCheckCodeBankCard.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
	}
}

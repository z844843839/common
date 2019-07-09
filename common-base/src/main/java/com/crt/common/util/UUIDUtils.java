package com.crt.common.util;

import java.util.UUID;

/**
 * @Description
 * @Author jinshiqiang@e6yun.com
 * @Created Date: 2019/6/6 
 * @ClassName UUIDUtils
 * @Version: 1.0
 */
public class UUIDUtils {
	
	/**
	 * 生成32为UUID
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}

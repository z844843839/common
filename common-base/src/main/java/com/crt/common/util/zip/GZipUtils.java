package com.crt.common.util.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZip压缩工具类
 * 
 * @author liupengfei@e6yun.com
 */
public class GZipUtils {
	
	public final static String DEFAULT_CHARSET="UTF-8";
	public final static String CHARSET_GB2312="GB2312";
	public final static String CHARSET_UTF8="UTF-8";
	public final static String CHARSET_GBK="GBK";
	/**
	 * 压缩
	 * @param primStr 源字符串
	 * @return 压缩后的字符串
	 */
	public static String gzip(String primStr) {
		return gzip(primStr,DEFAULT_CHARSET);
	}
	public static String gzip(String primStr,String charset) {
		if (primStr == null || primStr.length() == 0) {
			return primStr;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(primStr.getBytes(charset));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (gzip != null) {
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new sun.misc.BASE64Encoder().encode(out.toByteArray());
	}
	
	/**
	 * 解压字符串
	 * @param compressedStr 被压缩的字符串
	 * @return 解压后的字符串
	 */
	public static String gunzip(String compressedStr) {
		return gunzip(compressedStr,DEFAULT_CHARSET);
	}
	

	/**
	 * 解压字符串
	 * @param compressedStr 被压缩的字符串
	 * @param charset 解压期望的字符编码
	 * @return 解压后的字符串
	 */
	public static String gunzip(String compressedStr,String charset) {
		if (compressedStr == null) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		GZIPInputStream ginzip = null;
		byte[] compressed = null;
		String decompressed = null;
		try {
			compressed = new sun.misc.BASE64Decoder()
					.decodeBuffer(compressedStr);
			in = new ByteArrayInputStream(compressed);
			ginzip = new GZIPInputStream(in);
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ginzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString(charset);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ginzip != null) {
				try {
					ginzip.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return decompressed;
	}



	public static byte[] gzip(byte[] primStr) {
		if ((primStr == null) || (primStr.length == 0)) {
			return primStr;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(primStr);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (gzip != null) {
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return out.toByteArray();
	}

	public static byte[] gunzip(byte[] compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		GZIPInputStream ginzip = null;
		byte[] decompressed = null;
		try {
			in = new ByteArrayInputStream(compressedStr);
			ginzip = new GZIPInputStream(in);

			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ginzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ginzip != null) {
				try {
					ginzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return decompressed;
	}

}

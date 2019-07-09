package com.crt.common.util.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author Administrator
 */
public class HttpUtil {
	private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
	// http连接超时时间
	private static final int CONNECT_TIMEOUT = 30 * 1000;
	// http读取超时时间
	private static final int READ_TIMEOUT = 30 * 1000;
	
	private HttpUtil() {
	}
	
	/**
	 * 使用Get方式获取数据
	 * @param url URL包括参数，http://HOST/XX?XX=XX&XXX=XXX
	 * @param charset
	 * @return
	 */
	public static String sendGet(String url, String charset) {
		return sendGet(url, charset, CONNECT_TIMEOUT, READ_TIMEOUT);
	}
	/**
	 * POST请求，字符串形式数据
	 * @param url 请求地址
	 * @param param 请求数据
	 * @param charset 编码方式
	 */
	public static String sendPostUrl(String url, String param, String charset) {
		return sendPostUrl(url, param, charset, CONNECT_TIMEOUT, READ_TIMEOUT);
	}
	/**
	 * POST请求，Map形式数据
	 * @param url 请求地址
	 * @param param 请求数据
	 * @param charset 编码方式
	 */
	public static String sendPost(String url, Map<String, String> param, String charset) {
		return sendPost(url, param, charset, CONNECT_TIMEOUT, READ_TIMEOUT);
	}
	/**
	 * POST请求，字符串形式数据
	 * @param url 请求地址
	 * @param param 请求数据
	 * @param charset 编码方式
	 */
	public static String sendPostUrl(String url, String param, String charset, int connectTimeout, int readTimeout) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URLConnection conn = getUrlConnection(url, connectTimeout, readTimeout);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			log.error("发送 POST 请求出现异常！{}", e.getMessage(), e);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * POST请求，Map形式数据
	 * @param url 请求地址
	 * @param param 请求数据
	 * @param charset 编码方式
	 */
	public static String sendPost(String url, Map<String, String> param, String charset, int connectTimeout,
			int readTimeout) {
		StringBuffer buffer = new StringBuffer();
		try {
			if (param != null && !param.isEmpty()) {
				for (Map.Entry<String, String> entry : param.entrySet()) {
					buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), charset))
							.append("&");
				}
			}
		} catch (UnsupportedEncodingException e1) {
			log.error("urlencoder编码失败，编码={}", charset, e1);
		}
		buffer.deleteCharAt(buffer.length() - 1);
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URLConnection conn = getUrlConnection(url, connectTimeout, readTimeout);
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(buffer);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			log.error("发送 POST 请求出现异常！{}", e.getMessage(), e);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 使用Get方式获取数据
	 * @param url URL包括参数，http://HOST/XX?XX=XX&XXX=XXX
	 * @param charset
	 * @return
	 */
	public static String sendGet(String url, String charset, int connectTimeout, int readTimeout) {
		String result = "";
		BufferedReader in = null;
		try {
			URLConnection connection = getUrlConnection(url, connectTimeout, readTimeout);
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			log.error("发送GET请求出现异常！{}", e, e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 根据url获取URLConnection,并设置connectTimeout,readTimeout
	 **/
	private static URLConnection getUrlConnection(String url, int connectTimeout, int readTimeout) throws IOException {
		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection connection = realUrl.openConnection();
		// 设置通用的请求属性
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// 设置超时
		if (connectTimeout <= 0) {
			connection.setConnectTimeout(CONNECT_TIMEOUT);
		} else {
			connection.setConnectTimeout(connectTimeout);
		}
		if (readTimeout <= 0) {
			connection.setReadTimeout(READ_TIMEOUT);
		} else {
			connection.setReadTimeout(readTimeout);
		}
		return connection;
	}
}

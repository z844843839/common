package com.crt.common.vo;


import org.springframework.util.StringUtils;

/**
 * Wrapper操作的工具类
 */
public class E6WrapperUtil {

	/**
	 * Instantiates a new wrap mapper.
	 */
	private E6WrapperUtil() {
	}

	private static <E> E6Wrapper<E> wrap(E o) {
		return new E6Wrapper<>(E6Wrapper.OK.value(), E6Wrapper.OK.getReasonPhrase(), o);
	}
	private static <E> E6Wrapper<E> wrap(String message,E o) {
		return new E6Wrapper<>(E6Wrapper.OK.value(), message, o);
	}
	private static <E> E6Wrapper<E> wrap(int code) {
		return wrap(code, null);
	}
	private static <E> E6Wrapper<E> wrap(int code, String message) {
		return wrap(code, message, null);
	}
	private static <E> E6Wrapper<E> wrap(int code, String message, E o) {
		return new E6Wrapper<>(code, message, o);
	}

	/**
	 * 处理成功. default code =200
	 * 如需定义更加细致的成功，每个业务各自定义，但是务必用四位数，2XXX格式
	 */
	public static <E> E6Wrapper<E> ok() {
		return new E6Wrapper<>(E6Wrapper.OK);
	}
	public static <E> E6Wrapper<E> ok(E o) {
		return new E6Wrapper<>(E6Wrapper.OK.value(), E6Wrapper.OK.getReasonPhrase(), o);
	}
	public static <E> E6Wrapper<E> ok(String message, E o) {
		return new E6Wrapper<>(E6Wrapper.OK.value(), message, o);
	}
	public static <E> E6Wrapper<E> ok(int code,String message, E o) {
		return new E6Wrapper<>(code, message, o);
	}

	/**
	 * 参数校验失败，default code = 4001
	 * @return the wrapper
	 * 如果要区分更加细致的场景，每个业务需定义各自的参数校验错误码，但是务必用四位数，4XXX格式
	 */
	public static <E> E6Wrapper<E> paramError() {
		return wrap(E6Wrapper.E6_ERROR_PARAM_ERROR_CODE, E6Wrapper.E6_ERROR_PARAM_ERROR_MSG);
	}
	public static E6Wrapper paramError(String message) {
		return wrap(E6Wrapper.E6_ERROR_PARAM_ERROR_CODE,message);
	}
	public static E6Wrapper paramError(int code,String message) {
		return wrap(code,message);
	}

	/**
	 * 服务端代码异常. default code=500
	 * 用error的情况：
	 * 1.缺少权限校验参数 (需用203)
	 * 2.无权限访问 (需用403)
	 * 3.内部业务逻辑错误（可定义四位数错误码,6XXX格式）
	 * 4.内部其他异常或错误（可定义四位数错误码,5XXX格式）
	 */
	public static <E> E6Wrapper<E> error() {
		return new E6Wrapper(E6Wrapper.INTERNAL_SERVER_ERROR);
	}
	public static <E> E6Wrapper<E> error(Exception e) {
		return wrap(E6Wrapper.INTERNAL_SERVER_ERROR.value(), e.getMessage(),null);
	}
	public static <E> E6Wrapper<E> error(int code,Exception e) {
		return wrap(code, e.getMessage(),null);
	}
	public static <E> E6Wrapper<E> error(String message) {
		return wrap(E6Wrapper.INTERNAL_SERVER_ERROR.value(), StringUtils.isEmpty(message) ? E6Wrapper.INTERNAL_SERVER_ERROR.getReasonPhrase() : message);
	}
	public static <E> E6Wrapper<E> error(Integer code,String message) {
		return wrap(code,message);
	}
	public static <E> E6Wrapper<E> error(String message,E o) {
		return wrap(E6Wrapper.INTERNAL_SERVER_ERROR.value(), StringUtils.isEmpty(message) ? E6Wrapper.INTERNAL_SERVER_ERROR.getReasonPhrase() : message,o);
	}
	public static <E> E6Wrapper<E> error(Integer code,String message,E o) {
		return wrap(code,message,o);
	}


}

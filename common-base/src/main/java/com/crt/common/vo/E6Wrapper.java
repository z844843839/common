package com.crt.common.vo;


import org.springframework.http.HttpStatus;

import java.io.Serializable;


/**
 * RPC接口返回包装类
 * TODO: 内部rpc调用，任何情况下，都要返回一个E6Wrapper
 * 错误码问题：
 * 1. 优先用HttpStatus内的,如 正常200，无识别信息用203，没有权限用403，没有路径用404，内部错误用500
 * 2.
 */
public class E6Wrapper<T> implements Serializable {
	private static final long serialVersionUID = 4893280118017319089L;
	/**
	 * 200 正常
	 */
	public static final HttpStatus OK = HttpStatus.OK;
	/**
	 * 203 缺少鉴权信息
	 */
	public static final HttpStatus NON_AUTHORITATIVE_INFORMATION = HttpStatus.NON_AUTHORITATIVE_INFORMATION;
	/**
	 * 403 拒绝，无权限访问
	 */
	public static final HttpStatus FORBIDDEN = HttpStatus.FORBIDDEN;
	/**
	 * 404 找不到资源
	 */
	public static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
	/**
	 * 500 内部错误
	 */
	public static final HttpStatus INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;

	/**
	 * 自定义错误码 4001 参数校验失败
	 */
	public static final int E6_ERROR_PARAM_ERROR_CODE = 4001;
	/**
	 * 自定义错误码 4001 参数校验失败
	 */
	public static final String E6_ERROR_PARAM_ERROR_MSG = "参数校验失败";

	/**
	 * 编号.
	 */
	private int code;

	/**
	 * 信息.
	 */
	private String message;

	/**
	 * 结果数据
	 */
	private T result;

	/**
	 * Instantiates a new wrapper. default code=200
	 */
	E6Wrapper() {
		this(OK);
	}
	E6Wrapper(HttpStatus httpStatus) {
		this(httpStatus.value(), httpStatus.getReasonPhrase());
	}

	/**
	 * Instantiates a new wrapper.
	 *
	 * @param code    the code
	 * @param message the message
	 */
	E6Wrapper(int code, String message) {
		this(code, message, null);
	}

	/**
	 * Instantiates a new wrapper.
	 *
	 * @param code    the code
	 * @param message the message
	 * @param result  the result
	 */
	E6Wrapper(int code, String message, T result) {
		super();
		this.code(code).message(message).result(result);
	}

	/**
	 * Sets the 编号 , 返回自身的引用.
	 *
	 * @param code the new 编号
	 *
	 * @return the wrapper
	 */
	private E6Wrapper<T> code(int code) {
		this.setCode(code);
		return this;
	}

	/**
	 * Sets the 信息 , 返回自身的引用.
	 *
	 * @param message the new 信息
	 *
	 * @return the wrapper
	 */
	private E6Wrapper<T> message(String message) {
		this.setMessage(message);
		return this;
	}

	/**
	 * Sets the 结果数据 , 返回自身的引用.
	 *
	 * @param result the new 结果数据
	 *
	 * @return the wrapper
	 */
	public E6Wrapper<T> result(T result) {
		this.setResult(result);
		return this;
	}

	/**
	 * 参数校验失败
	 * @param message
	 * @return
	 */
	public static E6Wrapper paramError(String message) {
		return new E6Wrapper(E6_ERROR_PARAM_ERROR_CODE,message);
	}

	/**
	 * 判断是否成功： 依据 E6Wrapper.SUCCESS_CODE == this.code
	 *
	 * @return code =200,true;否则 false.
	 */
	public boolean success() {
		return OK.value() == this.code;
	}

	/**
	 * 判断是否成功： 依据 E6Wrapper.SUCCESS_CODE != this.code
	 *
	 * @return code !=200,true;否则 false.
	 */
	public boolean error() {
		return !success();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "E6Wrapper{" +
				"code=" + code +
				", message='" + message + '\'' +
				", result=" + result +
				'}';
	}
}

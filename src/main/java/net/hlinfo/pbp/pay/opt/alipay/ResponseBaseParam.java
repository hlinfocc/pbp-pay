package net.hlinfo.pbp.pay.opt.alipay;

public class ResponseBaseParam {
	/**
	 * 网关返回码,10000接口调用成功<br>
	 * 更多请参考:https://opendocs.alipay.com/common/02km9f
	 */
	private String code;
	/**
	 * 网关返回码描述,请参考:https://opendocs.alipay.com/common/02km9f
	 */
	private String msg;
	/**
	 * 业务返回码
	 */
	private String sub_code;
	/**
	 * 业务返回码描述
	 */
	private String sub_msg;
	/**
	 * 签名
	 */
	private String sign;
	/**
	 *  Getter method for property <b>code</b>.
	 * @return property value of code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * Setter method for property <b>code</b>.
	 *
	 * @param code value to be assigned to property code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 *  Getter method for property <b>msg</b>.
	 * @return property value of msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * Setter method for property <b>msg</b>.
	 *
	 * @param msg value to be assigned to property msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 *  Getter method for property <b>sub_code</b>.
	 * @return property value of sub_code
	 */
	public String getSub_code() {
		return sub_code;
	}
	/**
	 * Setter method for property <b>sub_code</b>.
	 *
	 * @param sub_code value to be assigned to property sub_code
	 */
	public void setSub_code(String sub_code) {
		this.sub_code = sub_code;
	}
	/**
	 *  Getter method for property <b>sub_msg</b>.
	 * @return property value of sub_msg
	 */
	public String getSub_msg() {
		return sub_msg;
	}
	/**
	 * Setter method for property <b>sub_msg</b>.
	 *
	 * @param sub_msg value to be assigned to property sub_msg
	 */
	public void setSub_msg(String sub_msg) {
		this.sub_msg = sub_msg;
	}
	/**
	 *  Getter method for property <b>sign</b>.
	 * @return property value of sign
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * Setter method for property <b>sign</b>.
	 *
	 * @param sign value to be assigned to property sign
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
}

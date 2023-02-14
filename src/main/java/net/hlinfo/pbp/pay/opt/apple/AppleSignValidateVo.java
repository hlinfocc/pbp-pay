package net.hlinfo.pbp.pay.opt.apple;

public class AppleSignValidateVo {
	/**
	 * 校验结果
	 */
	private boolean success;
	/**
	 * 错误信息
	 */
	private String msg;
	/**
	 * 邮箱
	 */
	private String rsemail;
	/**
	 * appid即是包名
	 */
	private String rsappid;
	/**
	 * 用户标识
	 */
	private String rsopenid;
	private boolean email_verified;
	/**
	 *  Getter method for property <b>success</b>.
	 * @return property value of success
	 */
	public boolean isSuccess() {
		return success;
	}
	/**
	 * Setter method for property <b>success</b>.
	 *
	 * @param success value to be assigned to property success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
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
	 *  Getter method for property <b>rsemail</b>.
	 * @return property value of rsemail
	 */
	public String getRsemail() {
		return rsemail;
	}
	/**
	 * Setter method for property <b>rsemail</b>.
	 *
	 * @param rsemail value to be assigned to property rsemail
	 */
	public void setRsemail(String rsemail) {
		this.rsemail = rsemail;
	}
	/**
	 *  Getter method for property <b>rsappid</b>.
	 * @return property value of rsappid
	 */
	public String getRsappid() {
		return rsappid;
	}
	/**
	 * Setter method for property <b>rsappid</b>.
	 *
	 * @param rsappid value to be assigned to property rsappid
	 */
	public void setRsappid(String rsappid) {
		this.rsappid = rsappid;
	}
	/**
	 *  Getter method for property <b>rsopenid</b>.
	 * @return property value of rsopenid
	 */
	public String getRsopenid() {
		return rsopenid;
	}
	/**
	 * Setter method for property <b>rsopenid</b>.
	 *
	 * @param rsopenid value to be assigned to property rsopenid
	 */
	public void setRsopenid(String rsopenid) {
		this.rsopenid = rsopenid;
	}
	/**
	 *  Getter method for property <b>email_verified</b>.
	 * @return property value of email_verified
	 */
	public boolean isEmail_verified() {
		return email_verified;
	}
	/**
	 * Setter method for property <b>email_verified</b>.
	 *
	 * @param email_verified value to be assigned to property email_verified
	 */
	public void setEmail_verified(boolean email_verified) {
		this.email_verified = email_verified;
	}
	
}

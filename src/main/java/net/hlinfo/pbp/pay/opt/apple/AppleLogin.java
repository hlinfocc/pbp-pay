package net.hlinfo.pbp.pay.opt.apple;

import io.swagger.annotations.ApiModelProperty;

/**
 * 苹果登陆用户信息
 * @author hadoop
 *
 */
public class AppleLogin {
	
	@ApiModelProperty("苹果用户唯一标识符，相当于微信的openid")
	private String user;
	
	@ApiModelProperty("验证信息状态")
	private String state;
	
	@ApiModelProperty("用户共享的可选电子邮件")
	private String email;
	
	@ApiModelProperty("用户共享的可选全名")
	private String fullName;
	
	@ApiModelProperty("授权验证code")
	private String authorizationCode;
	
	@ApiModelProperty("Web令牌(JWT)")
	private String identityToken;
	
	@ApiModelProperty("标识用户是否为真实的人 0：当前平台不支持，忽略该值；1：无法确认；2：用户真实性非常高")
	private String realUserStatus;
	
	@ApiModelProperty("返回信息作用域")
	private String scope;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	public String getIdentityToken() {
		return identityToken;
	}
	public void setIdentityToken(String identityToken) {
		this.identityToken = identityToken;
	}
	public String getRealUserStatus() {
		return realUserStatus;
	}
	public void setRealUserStatus(String realUserStatus) {
		this.realUserStatus = realUserStatus;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	
}

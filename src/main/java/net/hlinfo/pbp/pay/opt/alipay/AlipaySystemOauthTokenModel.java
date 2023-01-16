package net.hlinfo.pbp.pay.opt.alipay;

import com.alipay.api.AlipayObject;
import com.alipay.api.internal.mapping.ApiField;

/**
 * 换取授权访问令牌参数
 *
 */
public class AlipaySystemOauthTokenModel  extends AlipayObject  {

	private static final long serialVersionUID = 1L;
	/**
	 * 授权方式[必填]。支持：
		1.authorization_code，表示换取使用用户授权码code换取授权令牌access_token。
		2.refresh_token，表示使用refresh_token刷新获取新授权令牌。
	 */
	@ApiField("grant_type")
	private String grantType;
	/**
	 * 授权码[可选]，用户对应用授权后得到。本参数在 grant_type 为 authorization_code 时必填；为 refresh_token 时不填。
	 */
	@ApiField("code")
	private String code;
	/**
	 * 刷新令牌[可选]，上次换取访问令牌时得到。本参数在 grant_type 为 authorization_code 时不填；为 refresh_token 时必填，且该值来源于此接口的返回值 app_refresh_token（即至少需要通过 grant_type=authorization_code 调用此接口一次才能获取）。
	 */
	@ApiField("refresh_token")
	private String refreshToken;
	/**
	 * 授权方式[必填]。支持：
		1.authorization_code，表示换取使用用户授权码code换取授权令牌access_token。
		2.refresh_token，表示使用refresh_token刷新获取新授权令牌。
	 * @return property value of grantType
	 */
	public String getGrantType() {
		return grantType;
	}
	/**
	 * 授权方式[必填]。支持：
		1.authorization_code，表示换取使用用户授权码code换取授权令牌access_token。
		2.refresh_token，表示使用refresh_token刷新获取新授权令牌。
	 * @param grantType value to be assigned to property grantType
	 */
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
	/**
	 * 授权码[可选]，用户对应用授权后得到。本参数在 grant_type 为 authorization_code 时必填；为 refresh_token 时不填。
	 * @return property value of code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 授权码[可选]，用户对应用授权后得到。本参数在 grant_type 为 authorization_code 时必填；为 refresh_token 时不填。
	 *
	 * @param code value to be assigned to property code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 刷新令牌[可选]，上次换取访问令牌时得到。本参数在 grant_type 为 authorization_code 时不填；为 refresh_token 时必填，且该值来源于此接口的返回值 app_refresh_token（即至少需要通过 grant_type=authorization_code 调用此接口一次才能获取）。
	 * @return property value of refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}
	/**
	 * 刷新令牌[可选]，上次换取访问令牌时得到。本参数在 grant_type 为 authorization_code 时不填；为 refresh_token 时必填，且该值来源于此接口的返回值 app_refresh_token（即至少需要通过 grant_type=authorization_code 调用此接口一次才能获取）。
	 *
	 * @param refreshToken value to be assigned to property refreshToken
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
}

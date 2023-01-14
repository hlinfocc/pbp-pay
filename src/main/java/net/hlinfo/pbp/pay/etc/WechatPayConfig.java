package net.hlinfo.pbp.pay.etc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="wechat.pay",ignoreInvalidFields=true, ignoreUnknownFields=true)
public class WechatPayConfig {
	/**
	 * 商户号
	 */
	public String merchId;
	/**
	 * 商户证书序列号
	 */
	public String merchSerialNumber;
	/**
	 * 商户私钥apiclient_key.pem路径
	 */
	public String merchPrivateKeyPath;
	
	/**
	 * API v3密钥
	 */
	public String apiV3Key;
	
	/**
	 * 回调地址前缀,结尾不带/，如：https：//jyzdapi.dev.htedu.cc
	 */
	public String redirectUri;
	
	/**
	 * 微信APPID(小程序或公众号)
	 */
	public String appId;
	
	/**
	 * 微信APP密钥(小程序或公众号)
	 */
	public String appSecret;
	
	public String getMerchId() {
		return merchId;
	}

	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}

	public String getMerchSerialNumber() {
		return merchSerialNumber;
	}

	public void setMerchSerialNumber(String merchSerialNumber) {
		this.merchSerialNumber = merchSerialNumber;
	}

	public String getMerchPrivateKeyPath() {
		return merchPrivateKeyPath;
	}

	public void setMerchPrivateKeyPath(String merchPrivateKeyPath) {
		this.merchPrivateKeyPath = merchPrivateKeyPath;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getApiV3Key() {
		return apiV3Key;
	}

	public void setApiV3Key(String apiV3Key) {
		this.apiV3Key = apiV3Key;
	}

	
}

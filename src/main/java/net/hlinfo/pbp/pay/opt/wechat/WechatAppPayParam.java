package net.hlinfo.pbp.pay.opt.wechat;

import java.io.FileInputStream;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.hlinfo.opt.Func;
import net.hlinfo.opt.Jackson;

/**
 * APP调起支付参数
 * @author hlinfo.net
 *
 */
@ApiModel("APP调起支付参数")
public class WechatAppPayParam implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("应用appid,如小程序ID，公众号appid等")
	private String appId;
	
	@ApiModelProperty("商户号")
	private String partnerid;
	
	@ApiModelProperty("预支付交易会话ID ,微信返回的支付交易会话ID，该值有效期为2小时。 ")
	private String prepayid;
	
	@ApiModelProperty("时间戳")
	private String timestamp;
	
	@ApiModelProperty("随机字符串")
	private String noncestr;
	
	@ApiModelProperty("暂填写固定值Sign=WXPay")
	@JsonProperty("package")
	private String packages = "Sign=WXPay";
	
	@ApiModelProperty("签名类型，默认为RSA，仅支持RSA。")
	@JsonIgnore
	private String signType = "RSA";
	
	@ApiModelProperty("签名")
	private String sign;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getPackages() {
		return packages;
	}
	public void setPackages(String packages) {
		this.packages = packages;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String toString() {
		return Jackson.entityToString(this);
	}
	/**
	 * 构造签名串
	 @return
	 */
	private String buildMessage() {
		this.timestamp = new Date().getTime()+"";
		this.noncestr = Func.UUID();
		this.signType = "RSA";
		return this.appId + "\n"
	        + this.timestamp+ "\n"
	        + this.noncestr + "\n"
	        + this.prepayid + "\n";
	}
	/**
	 * 签名,签名信息填入paySign字段
	 @param privateKeyPath 商户私钥apiclient_key.pem路径,可wechatPayConfig.getMerchPrivateKeyPath()获取
	 @throws Exception
	 */
	public void sign(String privateKeyPath) throws Exception {
		//加载商户私钥
    	PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(privateKeyPath));
    	String message = buildMessage();
    	Signature sign = Signature.getInstance("SHA256withRSA");
    	sign.initSign(merchantPrivateKey);
    	sign.update(message.getBytes("utf-8"));
    	String paySignVal = Base64.getEncoder().encodeToString(sign.sign());
    	this.sign = paySignVal;
	}
}

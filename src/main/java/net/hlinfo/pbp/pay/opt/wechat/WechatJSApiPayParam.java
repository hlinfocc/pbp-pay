package net.hlinfo.pbp.pay.opt.wechat;

import java.io.FileInputStream;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.Date;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import net.hlinfo.opt.Func;
import net.hlinfo.opt.Jackson;

/**
 * 小程序/JSAPI调起支付参数
 * @author hadoop
 *
 */
@ApiModel("小程序或JSAPI调起支付参数")
public class WechatJSApiPayParam implements Serializable{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("应用appid,如小程序ID，公众号appid等")
	private String appId;
	
	@ApiModelProperty("时间戳")
	private String timeStamp;
	
	@ApiModelProperty("随机字符串")
	private String nonceStr;
	
	@ApiModelProperty("订单详情扩展字符串,格式如：prepay_id=*** ")
	@JsonProperty("package")
	private String packages;
	
	@ApiModelProperty("签名类型，默认为RSA，仅支持RSA。")
	private String signType;
	
	@ApiModelProperty("签名")
	private String paySign;
	
	@ApiModelProperty("商户订单号,订单表的ID")
	private String outTradeNo;
	
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
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
	public String getPaySign() {
		return paySign;
	}
	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}
	
	public String toString() {
		return Jackson.entityToString(this);
	}
	/**
	 * 构造签名串
	 @return
	 */
	private String buildMessage() {
		this.timeStamp = new Date().getTime()+"";
		this.nonceStr = Func.UUID();
		this.signType = "RSA";
		return this.appId + "\n"
	        + this.timeStamp+ "\n"
	        + this.nonceStr + "\n"
	        + this.packages + "\n";
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
    	this.paySign = paySignVal;
	}
}

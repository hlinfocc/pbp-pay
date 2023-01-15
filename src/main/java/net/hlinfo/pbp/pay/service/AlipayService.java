package net.hlinfo.pbp.pay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;

import net.hlinfo.opt.Func;
import net.hlinfo.opt.RedisUtils;
import net.hlinfo.pbp.pay.etc.AlipayAutoConfig;
import net.hlinfo.pbp.pay.exception.PayException;

@Service
public class AlipayService {
	public static final Logger log = LoggerFactory.getLogger(AlipayService.class);
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private AlipayAutoConfig alipayAutoConfig;
	
	private AlipayClient alipayClient;
	
	private void initAlipayClient() throws AlipayApiException {
		if(alipayClient!=null) {
			AlipayConfig alipayConfig = new AlipayConfig();
			alipayConfig.setServerUrl(alipayAutoConfig.getServerUrl());
			alipayConfig.setAppId(alipayAutoConfig.getAppId());
			alipayConfig.setFormat(alipayAutoConfig.getFormat());
			alipayConfig.setCharset(alipayAutoConfig.getCharset());
			alipayConfig.setSignType(alipayAutoConfig.getSignType());
			alipayConfig.setEncryptType(alipayAutoConfig.getEncryptType());
			if(Func.isNotBlank(alipayAutoConfig.getEncryptKey())) {
				alipayConfig.setEncryptKey(alipayAutoConfig.getEncryptKey());
			}
			if(Func.isNotBlank(alipayAutoConfig.getProxyHost())) {
				alipayConfig.setProxyHost(alipayAutoConfig.getProxyHost());
			}
			if(alipayAutoConfig.getProxyPort()>0 && alipayAutoConfig.getProxyPort()<=65535) {
				alipayConfig.setProxyPort(alipayAutoConfig.getProxyPort());
			}
			if(alipayAutoConfig.getConnectTimeout()>0) {
				alipayConfig.setConnectTimeout(alipayAutoConfig.getConnectTimeout());
			}
			if(alipayAutoConfig.getReadTimeout()>0) {
				alipayConfig.setReadTimeout(alipayAutoConfig.getReadTimeout());
			}
			if(alipayAutoConfig.getMaxIdleConnections()>0) {
				alipayConfig.setMaxIdleConnections(alipayAutoConfig.getMaxIdleConnections());
			}
			if(alipayAutoConfig.getKeepAliveDuration()>0) {
				alipayConfig.setKeepAliveDuration(alipayAutoConfig.getKeepAliveDuration());
			}
			if(alipayAutoConfig.getCustomHeaders()!=null && !alipayAutoConfig.getCustomHeaders().isEmpty()) {
				alipayConfig.setCustomHeaders(alipayAutoConfig.getCustomHeaders());
			}
			if(Func.equals(alipayAutoConfig.getApiMode(), "cert")) {
				if(Func.isNotBlank(alipayAutoConfig.getAppCertContent())) {
					alipayConfig.setAppCertContent(alipayAutoConfig.getAppCertContent());
				}else {
					alipayConfig.setAppCertPath(alipayAutoConfig.getAppCertPath());
				}
				if(Func.isNotBlank(alipayAutoConfig.getAlipayPublicCertContent())) {
					alipayConfig.setAlipayPublicCertPath(alipayAutoConfig.getAlipayPublicCertContent());
				}else {
					alipayConfig.setAlipayPublicCertPath(alipayAutoConfig.getAlipayPublicCertPath());
				}
				
				if(Func.isNotBlank(alipayAutoConfig.getRootCertPath())) {
					alipayConfig.setRootCertPath(alipayAutoConfig.getRootCertPath());
				}
				if(Func.isNotBlank(alipayAutoConfig.getRootCertContent())) {
					alipayConfig.setRootCertContent(alipayAutoConfig.getRootCertContent());
				}
			}else {
				alipayConfig.setAlipayPublicKey(alipayAutoConfig.getAlipayPublicKey());
			}
			alipayConfig.setPrivateKey(alipayAutoConfig.getPrivateKey());
			alipayClient = new DefaultAlipayClient(alipayConfig);
		}
	}
	/**
	 * APP下单
	 @param model app支付接口2.0参数对象
	 @return sdk调用时所传的参数
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为msg+",原因:"+sub_msg,<br>
				  extendData: 属性为接口返回的{@code AlipayTradeAppPayResponse}对象
				 </p>
	 */
	public String tradeAppPay(AlipayTradeAppPayModel model) throws AlipayApiException, PayException {
        this.initAlipayClient();
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setBizModel(model);
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        System.out.println(response.getBody());
        if (response.isSuccess()) {
            return response.getBody();
        } else {
        	throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
        }
	}
	
	/**
	 * H5下单
	 @param model 手机网站支付接口参数对象
	 @return 支付宝收银台表单格式数据，可嵌入页面，具体以返回的结果为准
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为msg+",原因:"+sub_msg,<br>
				  extendData: 属性为接口返回的{@code AlipayTradeWapPayResponse}对象
				 </p>
	 */
	public String tradeH5Pay(AlipayTradeWapPayModel model) throws AlipayApiException, PayException {
        this.initAlipayClient();
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setBizModel(model);
        AlipayTradeWapPayResponse response = alipayClient.sdkExecute(request);
        System.out.println(response.getBody());
        if (response.isSuccess()) {
            return response.getBody();
        } else {
        	throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
        }
	}
	/**
	 * PC电脑端网页下单
	 @param model PC电脑端网页统一收单下单并支付页面接口参数对象
	 @return 支付宝收银台表单格式数据，可嵌入页面，具体以返回的结果为准
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为msg+",原因:"+sub_msg,<br>
				  extendData: 属性为接口返回的{@code AlipayTradePagePayResponse}对象
				 </p>
	 */
	public String tradePCPay(AlipayTradePagePayModel model) throws AlipayApiException, PayException {
		this.initAlipayClient();
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		AlipayTradePagePayResponse response = alipayClient.sdkExecute(request);
		System.out.println(response.getBody());
		if (response.isSuccess()) {
			return response.getBody();
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
}

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
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;

import net.hlinfo.opt.Func;
import net.hlinfo.opt.RedisUtils;
import net.hlinfo.pbp.pay.etc.AlipayAutoConfig;
import net.hlinfo.pbp.pay.exception.PayException;
import net.hlinfo.pbp.pay.opt.alipay.PublicErrorCode;

@Service
public class AlipayService {
	public static final Logger log = LoggerFactory.getLogger(AlipayService.class);
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private AlipayAutoConfig alipayAutoConfig;
	
	private AlipayClient alipayClient;
	
	private void initAlipayClient() throws Exception {
		if(alipayClient!=null) {
			AlipayConfig alipayConfig = new AlipayConfig();
			alipayConfig.setServerUrl(alipayAutoConfig.getServerUrl());
			alipayConfig.setAppId(alipayAutoConfig.getAppId());
			alipayConfig.setFormat(alipayAutoConfig.getFormat());
			alipayConfig.setCharset(alipayAutoConfig.getCharset());
			alipayConfig.setSignType(alipayAutoConfig.getSignType());
			if(Func.equals(alipayAutoConfig.getApiMode(), "cert")) {
				
			}else {				
				alipayConfig.setPrivateKey(alipayAutoConfig.getPrivateKey());
				alipayConfig.setAlipayPublicKey(alipayAutoConfig.getAlipayPublicKey());
			}
			alipayClient = new DefaultAlipayClient(alipayConfig);
		}
	}
	/**
	 * APP下单
	 @param model app支付接口2.0参数对象
	 @return sdk调用时所传的参数
	 @throws AlipayApiException 支付宝的SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为msg+",原因:"+sub_msg,<br>
				  extendData: 属性为接口返回的{@code AlipayTradeAppPayResponse}对象
				 </p>
	 */
	public String tradeAppPay(AlipayTradeAppPayModel model) throws AlipayApiException, PayException {
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
}

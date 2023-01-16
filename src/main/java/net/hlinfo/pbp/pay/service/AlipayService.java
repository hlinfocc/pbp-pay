package net.hlinfo.pbp.pay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayDataBillEreceiptApplyModel;
import com.alipay.api.domain.AlipayDataBillEreceiptQueryModel;
import com.alipay.api.domain.AlipayFundAccountQueryModel;
import com.alipay.api.domain.AlipayFundTransCommonQueryModel;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.domain.AlipayUserInfoAuthModel;
import com.alipay.api.domain.ZolozAuthenticationCustomerFtokenQueryModel;
import com.alipay.api.domain.ZolozAuthenticationSmilepayInitializeModel;
import com.alipay.api.request.AlipayDataBillEreceiptApplyRequest;
import com.alipay.api.request.AlipayDataBillEreceiptQueryRequest;
import com.alipay.api.request.AlipayFundAccountQueryRequest;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.request.AlipayUserInfoAuthRequest;
import com.alipay.api.request.ZolozAuthenticationCustomerFtokenQueryRequest;
import com.alipay.api.request.ZolozAuthenticationSmilepayInitializeRequest;
import com.alipay.api.response.AlipayDataBillEreceiptApplyResponse;
import com.alipay.api.response.AlipayDataBillEreceiptQueryResponse;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.alipay.api.response.AlipayUserInfoAuthResponse;
import com.alipay.api.response.ZolozAuthenticationCustomerFtokenQueryResponse;
import com.alipay.api.response.ZolozAuthenticationSmilepayInitializeResponse;

import net.hlinfo.opt.Func;
import net.hlinfo.opt.RedisUtils;
import net.hlinfo.pbp.pay.etc.AlipayAutoConfig;
import net.hlinfo.pbp.pay.exception.PayException;
import net.hlinfo.pbp.pay.opt.alipay.AlipaySystemOauthTokenModel;

@Service
public class AlipayService {
	public static final Logger log = LoggerFactory.getLogger(AlipayService.class);
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private AlipayAutoConfig alipayAutoConfig;
	
	private AlipayClient alipayClient;
	private AlipayClient alipayClientForceCert;
	
	/**
	 * 初始化alipayClient,通过apiMode配置参数设置是公钥模式还是证书模式
	 @throws AlipayApiException
	 */
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
	 * 初始化alipayClient,强制证书模式,忽略apiMode及公钥模式的配置参数
	 @throws AlipayApiException
	 */
	private void initAlipayClientForceCert() throws AlipayApiException {
		if(alipayClientForceCert!=null) {
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
			alipayConfig.setPrivateKey(alipayAutoConfig.getPrivateKey());
			alipayClientForceCert = new DefaultAlipayClient(alipayConfig);
		}
	}
	/**
	 * 获取alipayClient实例
	 @param forceCert 是否强制使用证书模式,证书模式忽略apiMode及公钥模式的配置参数
	 @return 返回alipayClient实例,若出现异常则返回null
	 */
	public AlipayClient getAlipayClient(boolean forceCert) {
		try {
			if(forceCert) {
				this.initAlipayClientForceCert();
				return alipayClientForceCert;
			}else {
				this.initAlipayClient();
				return alipayClient;
			}
		} catch (AlipayApiException e) {
			log.error(e.getMessage(),e);
			return null;
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
        log.debug("AlipayTradeAppPayResponse:{}",response);
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
        log.debug("AlipayTradeWapPayResponse:{}",response);
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
		log.debug("AlipayTradePagePayResponse:{}",response);
		if (response.isSuccess()) {
			return response.getBody();
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 统一收单交易创建,适用于小程序下单,当面付的扫码支付
	 @param model 统一收单交易创建接口参数对象
	 @return 统一收单交易创建返回对象,主要关注:tradeNo(支付宝交易号)和outTradeNo(商户订单号)
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为msg+",原因:"+sub_msg,<br>
				  extendData: 属性为接口返回的{@code AlipayTradeCreateResponse}对象
				 </p>
	 */
	public AlipayTradeCreateResponse tradeCreate(AlipayTradeCreateModel model) throws AlipayApiException, PayException {
		this.initAlipayClient();
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		request.setBizModel(model);
		AlipayTradeCreateResponse response = alipayClient.sdkExecute(request);
		log.debug("AlipayTradeCreateResponse:{}",response);
		if (response.isSuccess()) {
			return response;
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 统一收单线下交易预创建,适用于当面付的扫码支付<br>
	 * 收银员通过收银台或商户后台调用支付宝接口，生成二维码后，展示给用户，由用户扫描二维码完成订单支付。
	 @param model 统一收单线下交易预创建接口参数对象
	 @return 当前预下单请求生成的二维码码串，有效时间2小时，可以用二维码生成工具根据该码串值生成对应的二维码
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code AlipayTradePrecreateResponse}对象
				 </p>
	 */
	public String tradePrecreate(AlipayTradePrecreateModel model) throws AlipayApiException, PayException {
		this.initAlipayClient();
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		AlipayTradePrecreateResponse response = alipayClient.sdkExecute(request);
		log.debug("AlipayTradePrecreateResponse:{}",response);
		if (response.isSuccess()) {
			return response.getQrCode();
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 付款码支付接口(统一收单交易支付接口)<br>
	 * 收银员使用扫码设备读取用户手机支付宝“付款码”获取设备（如扫码枪）读取用户手机支付宝的付款码信息后，将二维码或条码信息通过本接口上送至支付宝发起支付。
	 @param model 付款码支付接口参数对象
	 @return 付款码支付接口成功返回对象
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code AlipayTradePayResponse}对象
				 </p>
	 */
	public AlipayTradePayResponse tradePaymentCode(AlipayTradePayModel model) throws AlipayApiException, PayException {
		this.initAlipayClient();
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		request.setBizModel(model);
		AlipayTradePayResponse response = alipayClient.sdkExecute(request);
		log.debug("AlipayTradePayResponse:{}",response);
		if (response.isSuccess()) {
			return response;
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 刷脸支付初始化<br>
	 * 通过该接口获取刷脸支付服务的初始化信息，详细描述请参考支付宝api说明文档
	 @param model 刷脸支付初始化参数对象
	 @return 刷脸支付初始化成功返回对象
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code ZolozAuthenticationSmilepayInitializeResponse}对象
				 </p>
	 */
	public ZolozAuthenticationSmilepayInitializeResponse smilepayInit(ZolozAuthenticationSmilepayInitializeModel model) throws AlipayApiException, PayException {
		this.initAlipayClient();
		ZolozAuthenticationSmilepayInitializeRequest request = new ZolozAuthenticationSmilepayInitializeRequest();
		request.setBizModel(model);
		ZolozAuthenticationSmilepayInitializeResponse response = alipayClient.sdkExecute(request);
		log.debug("ZolozAuthenticationSmilepayInitializeResponse:{}",response);
		if (response.isSuccess()) {
			return response;
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 人脸ftoken查询消费
	 @param model 人脸ftoken查询消费参数对象
	 @return 人脸ftoken查询消费成功返回对象
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code ZolozAuthenticationCustomerFtokenQueryResponse}对象
				 </p>
	 */
	public ZolozAuthenticationCustomerFtokenQueryResponse smileFtokenQuery(ZolozAuthenticationCustomerFtokenQueryModel model) throws AlipayApiException, PayException {
		this.initAlipayClient();
		ZolozAuthenticationCustomerFtokenQueryRequest request = new ZolozAuthenticationCustomerFtokenQueryRequest();
		request.setBizModel(model);
		ZolozAuthenticationCustomerFtokenQueryResponse response = alipayClient.sdkExecute(request);
		log.debug("ZolozAuthenticationCustomerFtokenQueryResponse:{}",response);
		if (response.isSuccess()) {
			return response;
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 统一收单交易查询
	 @param model 统一收单交易查询参数对象,支付宝交易号(tradeNo)和商户订单号(outTradeNo)不能同时为空
	 @return 统一收单交易查询成功返回对象
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code AlipayTradeQueryResponse}对象
				 </p>
	 */
	public AlipayTradeQueryResponse tradeQuery(AlipayTradeQueryModel model) throws AlipayApiException, PayException {
		this.initAlipayClient();
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);
		AlipayTradeQueryResponse response = alipayClient.sdkExecute(request);
		log.debug("AlipayTradeQueryResponse:{}",response);
		if (response.isSuccess()) {
			return response;
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 统一收单交易退款接口
	 @param model 退款接口参数对象,支付宝交易号(tradeNo)和商户订单号(outTradeNo)不能同时为空
	 @return 退款接口请求成功返回对象,退款成功判断说明：接口返回fund_change=Y为退款成功，fund_change=N或无此字段值返回时需通过退款查询接口进一步确认退款状态。详见退款成功判断指导。注意，本次退款请求成功，不代表退款成功。
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code AlipayTradeRefundResponse}对象
				 </p>
	 */
	public AlipayTradeRefundResponse tradeRefund(AlipayTradeRefundModel model) throws AlipayApiException, PayException {
		this.initAlipayClient();
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		AlipayTradeRefundResponse response = alipayClient.sdkExecute(request);
		log.debug("AlipayTradeRefundResponse:{}",response);
		if (response.isSuccess()) {
			return response;
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 统一收单交易退款查询
	 @param model 退款查询接口参数对象,支付宝交易号(tradeNo)和商户订单号(outTradeNo)不能同时为空
	 @return 退款查询请求成功返回对象,当接口返回的refund_status值为REFUND_SUCCESS时表示退款成功，否则表示退款没有执行成功
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code AlipayTradeRefundResponse}对象
				 </p>
	 */
	public AlipayTradeFastpayRefundQueryResponse tradeRefundQuery(AlipayTradeFastpayRefundQueryModel model) throws AlipayApiException, PayException {
		this.initAlipayClient();
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);
		AlipayTradeFastpayRefundQueryResponse response = alipayClient.sdkExecute(request);
		log.debug("AlipayTradeFastpayRefundQueryResponse:{}",response);
		if (response.isSuccess()) {
			return response;
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 统一收单交易关闭接口<br>
	 * 用于交易创建后，用户在一定时间内未进行支付，可调用该接口直接将未付款的交易进行关闭。
	 @param model 统一收单交易关闭接口参数对象,支付宝交易号(tradeNo)和商户订单号(outTradeNo)不能同时为空
	 @return 请求成功表示关闭成功
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code AlipayTradeRefundResponse}对象
				 </p>
	 */
	public AlipayTradeCloseResponse tradeClose(AlipayTradeCloseModel model) throws AlipayApiException, PayException {
		this.initAlipayClient();
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizModel(model);
		AlipayTradeCloseResponse response = alipayClient.sdkExecute(request);
		log.debug("AlipayTradeCloseResponse:{}",response);
		if (response.isSuccess()) {
			return response;
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 单笔转账接口,该接口需要配置证书,<br>
	 * 请设置证书模式配置参数(appCertPath,alipayPublicCertPath,rootCertPath(或者appCertContent,alipayPublicCertContent,rootCertContent)等等)<br>
	 * 用于交易创建后，用户在一定时间内未进行支付，可调用该接口直接将未付款的交易进行关闭。
	 @param model 统一收单交易关闭接口参数对象,支付宝交易号(tradeNo)和商户订单号(outTradeNo)不能同时为空
	 @return 请求成功表示关闭成功
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code AlipayFundTransUniTransferResponse}对象
				 </p>
	 */
	public AlipayFundTransUniTransferResponse fundTransUniTransfer(AlipayFundTransUniTransferModel model) throws AlipayApiException, PayException {
		this.initAlipayClientForceCert();
		AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
		request.setBizModel(model);
		AlipayFundTransUniTransferResponse response = alipayClientForceCert.sdkExecute(request);
		log.debug("AlipayFundTransUniTransferResponse:{}",response);
		if (response.isSuccess()) {
			return response;
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 转账业务单据查询,该接口需要配置证书,<br>
	 * 请设置证书模式配置参数(appCertPath,alipayPublicCertPath,rootCertPath(或者appCertContent,alipayPublicCertContent,rootCertContent)等等)<br>
	 * <p>可通过该接口查询转账业务单据的状态，主要应用于统一转账接口(fundTransUniTransfer)、无线转账接口(alipay.fund.trans.app.pay)、单笔转账到支付宝账户接口（alipay.fund.trans.toaccount.transfer）</p>
	 @param model 转账业务单据查询接口参数对象
	 @return status为SUCCESS表示转账成功
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code AlipayFundTransCommonQueryResponse}对象
				 </p>
	 */
	public AlipayFundTransCommonQueryResponse fundTransCommonQuery(AlipayFundTransCommonQueryModel model) throws AlipayApiException, PayException {
		this.initAlipayClientForceCert();
		AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
		request.setBizModel(model);
		AlipayFundTransCommonQueryResponse response = alipayClientForceCert.sdkExecute(request);
		log.debug("AlipayFundTransCommonQueryResponse:{}",response);
		if (response.isSuccess()) {
			return response;
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 查询指定支付宝账户余额信息,该接口需要配置证书,<br>
	 * 请设置证书模式配置参数(appCertPath,alipayPublicCertPath,rootCertPath(或者appCertContent,alipayPublicCertContent,rootCertContent)等等)<br>
	 @param model 支付宝资金账户资产查询接口参数对象
	 @return 账户可用余额，单位元
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code AlipayFundAccountQueryResponse}对象
				 </p>
	 */
	public AlipayFundAccountQueryResponse queryAccountSurplus(AlipayFundAccountQueryModel model) throws AlipayApiException, PayException {
		this.initAlipayClientForceCert();
		AlipayFundAccountQueryRequest request = new AlipayFundAccountQueryRequest();
		request.setBizModel(model);
		AlipayFundAccountQueryResponse response = alipayClientForceCert.sdkExecute(request);
		log.debug("AlipayFundTransUniTransferResponse:{}",response);
		if (response.isSuccess()) {
			return response;
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 申请电子回单,该接口需要配置证书,<br>
	 * 请设置证书模式配置参数(appCertPath,alipayPublicCertPath,rootCertPath(或者appCertContent,alipayPublicCertContent,rootCertContent)等等)<br>
	 * <p><ul>
	 * <li>支持商家下载多种类型支付宝资金凭证。包括：余额收支证明、余额收支流水证明、转入转出收支证明、收支汇总证明（日汇总）以及收支汇总证明（月汇总）。</li>
	 *  <li>账单凭证申请分两个步骤：</li>
	 * 	<li>1、使用本方法创建申请，并获取file_id信息。</li>
	 *  <li>2、使用file_id查询alipay.data.bill.ereceipt.query获取回单信息，回单生成完毕将会返回对应的下载链接，下载链接时效为30秒，过期将无法下载，需重新调用alipay.data.bill.ereceipt.query获取新的下载链接。</li>
	 * </ul></p>
	 @param model 申请电子回单参数对象
	 @return 文件申请号file_id信息。使用file_id可以查询处理状态，有效期：2天
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code AlipayFundAccountQueryResponse}对象
				 </p>
	 */
	public String ereceiptApply(AlipayDataBillEreceiptApplyModel model) throws AlipayApiException, PayException {
		this.initAlipayClientForceCert();
		AlipayDataBillEreceiptApplyRequest request = new AlipayDataBillEreceiptApplyRequest();
		request.setBizModel(model);
		AlipayDataBillEreceiptApplyResponse response = alipayClientForceCert.sdkExecute(request);
		log.debug("AlipayFundTransUniTransferResponse:{}",response);
		if (response.isSuccess()) {
			return response.getFileId();
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 *查询电子回单状态,该接口需要配置证书,<br>
	 * 请设置证书模式配置参数(appCertPath,alipayPublicCertPath,rootCertPath(或者appCertContent,alipayPublicCertContent,rootCertContent)等等)<br>
	 * <p><ul>
	 *  <li>账单凭证申请分两个步骤：</li>
	 * 	<li>1、使用{@code ereceiptApply}创建申请，并获取file_id信息。</li>
	 *  <li>2、使用file_id查询在本方法获取回单信息，回单生成完毕将会返回对应的下载链接，下载链接时效为30秒，过期将无法下载，需重新调用alipay.data.bill.ereceipt.query获取新的下载链接。</li>
	 * </ul></p>
	 @param fileId 使用{@code ereceiptApply}创建申请并获取的file_id
	 @return 电子回单状态对象
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code AlipayDataBillEreceiptQueryResponse}对象
				 </p>
	 */
	public AlipayDataBillEreceiptQueryResponse ereceiptQuery(String fileId) throws AlipayApiException, PayException {
		if(Func.isBlank(fileId)) {
			throw new PayException("fileId不能为空");
		}
		this.initAlipayClientForceCert();
		AlipayDataBillEreceiptQueryRequest request = new AlipayDataBillEreceiptQueryRequest();
		AlipayDataBillEreceiptQueryModel model = new AlipayDataBillEreceiptQueryModel();
		model.setFileId(fileId);
		request.setBizModel(model);
		AlipayDataBillEreceiptQueryResponse response = alipayClientForceCert.sdkExecute(request);
		log.debug("AlipayDataBillEreceiptQueryResponse:{}",response);
		if (response.isSuccess()) {
			return response;
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 用户登录授权,用户进行账密、扫码登录并授权
	 @param model 用户登陆授权参数对象
	 @return 获取需提交的form表单,可通过PayUtils.parseFormAction解析表单地址
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code AlipayUserInfoAuthResponse}对象
				 </p>
	 */
	public String userInfoAuth(AlipayUserInfoAuthModel model) throws AlipayApiException, PayException {
		this.initAlipayClientForceCert();
		AlipayUserInfoAuthRequest request = new AlipayUserInfoAuthRequest();
		request.setBizModel(model);
		AlipayUserInfoAuthResponse response = alipayClientForceCert.sdkExecute(request);
		log.debug("AlipayUserInfoAuthResponse:{}",response);
		if (response.isSuccess()) {
			return response.getBody();
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
	/**
	 * 换取授权访问令牌
	 @param model 换取授权访问令牌参数对象
	 @return 访问令牌响应参数对象
	 @throws AlipayApiException 支付宝的API SDK异常
	 @throws PayException 
				<p>接口调用失败返回异常,返回code大于10000的情况,<br>
				  message: 异常信息,为: msg+",原因:"+sub_msg<br>
				  extendData: 属性为接口返回的{@code AlipaySystemOauthTokenResponse}对象
				 </p>
	 */
	public AlipaySystemOauthTokenResponse oauthToken(AlipaySystemOauthTokenModel model) throws AlipayApiException, PayException {
		this.initAlipayClientForceCert();
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setCode(model.getCode());
       request.setGrantType(model.getGrantType());
       request.setRefreshToken(model.getRefreshToken());
		AlipaySystemOauthTokenResponse response = alipayClientForceCert.sdkExecute(request);
		log.debug("AlipaySystemOauthTokenResponse:{}",response);
		if (response.isSuccess()) {
			return response;
		} else {
			throw new PayException(response.getMsg()+",原因:"+response.getSubMsg(),response);
		}
	}
}

package net.hlinfo.pbp.pay.service;

import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;

import net.hlinfo.opt.Func;
import net.hlinfo.opt.Jackson;
import net.hlinfo.opt.RedisUtils;
import net.hlinfo.pbp.pay.etc.WechatPayConfig;
import net.hlinfo.pbp.pay.exception.PayException;
import net.hlinfo.pbp.pay.opt.wechat.CertificateItem;
import net.hlinfo.pbp.pay.opt.wechat.CertificateList;
import net.hlinfo.pbp.pay.opt.PayRedisKey;
import net.hlinfo.pbp.pay.opt.PayUtils;
import net.hlinfo.pbp.pay.opt.wechat.WechatAppPayParam;
import net.hlinfo.pbp.pay.opt.wechat.WechatJSApiPayParam;
import net.hlinfo.pbp.pay.opt.wechat.WechatPlaceOrderParam;
import net.hlinfo.pbp.pay.opt.wechat.WechatRefundsParam;
import net.hlinfo.pbp.pay.opt.wechat.WechatPlaceOrderParam.SceneInfo;

@Service
public class WechatPayService {
	public static final Logger log = LoggerFactory.getLogger(WechatPayService.class);
	@Autowired
	private WechatPayConfig wpc;
	@Autowired
	private RedisUtils redisUtils;
	
	 private CloseableHttpClient httpClient;

//    private static final HttpHost proxy = null;
    
    private List<X509Certificate> wechatPayCertificates = new ArrayList<>();
    /**
     * 初始化httpClient
     * @throws FileNotFoundException 
     */
    private void initHttpClient() throws Exception {
    	this.initCertificate(false);
    	//加载商户私钥
    	PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(wpc.getMerchPrivateKeyPath()));
    	
       httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(wpc.getMerchId(), wpc.getMerchSerialNumber(), merchantPrivateKey)
                .withWechatPay(wechatPayCertificates)
                	.setConnectionTimeToLive(120, TimeUnit.SECONDS)
                .build();
    }
    /**
     * 定时更新平台证书功能
    * @param ignoreCache 是否忽略缓存数据，定时更新证书时候需要设置为true忽略缓存
     @throws Exception
     */
    private boolean initCertificate(boolean ignoreCache) throws Exception {
	   	if(!ignoreCache) {
	   		if(wechatPayCertificates==null || wechatPayCertificates.isEmpty()) {
		   		String certStr = redisUtils.getObject(PayRedisKey.WECHA_PAY_CERT);
		   		if(Func.isNotBlank(certStr)) {
		   			this.parseCertificate(certStr);
		   			return true;
		   		}
		   	}else {
		   		return true;
		   	}
	   	}
    	// 获取证书管理器实例
    	CertificatesManager certificatesManager = CertificatesManager.getInstance();
    	//加载商户私钥
    	PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(wpc.getMerchPrivateKeyPath()));
    	
    	// 向证书管理器增加需要自动更新平台证书的商户信息
    	certificatesManager.putMerchant(wpc.getMerchId(), new WechatPay2Credentials(wpc.getMerchId(),
    	            new PrivateKeySigner(wpc.getMerchSerialNumber(), merchantPrivateKey)), wpc.getApiV3Key().getBytes(StandardCharsets.UTF_8));

    	// 从证书管理器中获取verifier
    	Verifier verifier = certificatesManager.getVerifier(wpc.getMerchId());
    	WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
    	        .withMerchant(wpc.getMerchId(), wpc.getMerchSerialNumber(), merchantPrivateKey)
    	        .withValidator(new WechatPay2Validator(verifier));

    	// 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签，并进行证书自动更新
    	CloseableHttpClient httpClient = builder.build();

    	URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/certificates");
       HttpGet httpGet = new HttpGet(uriBuilder.build());
       httpGet.addHeader(ACCEPT, APPLICATION_JSON.toString());
       CloseableHttpResponse response = httpClient.execute(httpGet);
       if(response.getStatusLine().getStatusCode()==SC_OK) {
        	String bodyAsString = EntityUtils.toString(response.getEntity());
        	redisUtils.setCacheObject(PayRedisKey.WECHA_PAY_CERT, bodyAsString, 3*24*60, TimeUnit.MINUTES);
        	this.parseCertificate(bodyAsString);
        	return true;
        }
       return false;
    }
   /**
    * 解析解密平台证书信息
    @param json 证书json数据
    @throws GeneralSecurityException
    */
   private void parseCertificate(String json) throws GeneralSecurityException {
	   CertificateList certsList = Jackson.toJavaObject(json, CertificateList.class);
	   	AesUtil decryptor = new AesUtil(wpc.getApiV3Key().getBytes(StandardCharsets.UTF_8));
	   	for (CertificateItem item : certsList.getCerts()) {
	   		String plainCertificate = decryptor.decryptToString(
                 item.getEncryptCertificate().getAssociatedData().getBytes(StandardCharsets.UTF_8),
                 item.getEncryptCertificate().getNonce().getBytes(StandardCharsets.UTF_8),
                 item.getEncryptCertificate().getCiphertext());
   	  
         ByteArrayInputStream inputStream = new ByteArrayInputStream(plainCertificate.getBytes(StandardCharsets.UTF_8));
         X509Certificate x509Cert = PemUtil.loadCertificate(inputStream);
         wechatPayCertificates.add(x509Cert);
	   }
   }
	/**
	 * JSAPI下单
	 * @param param 微信下单参数
	 * @throws Exception 
	 * @return 小程序/JSAPI调起支付签名参数信息 {@link net.hlinfo.pbp.pay.opt.wechat.WechatJSApiPayParam}对象
	 */
	public WechatJSApiPayParam placeOrderJSAPI(WechatPlaceOrderParam param) throws PayException,Exception {
		log.debug("WechatPlaceOrderParam:{}",param);
		if(Func.isBlank(param.getOutTradeNo())) {
			throw new PayException("商户订单号不能为空");
		}
		if(param.getAmount().getTotal()<=0) {
			throw new PayException("支付金额不能为空");
		}
		if(param.getPayer()==null || Func.isBlank(param.getPayer().getOpenid())) {
			throw new PayException("openid不能为空");
		}
		if(param.getScene_info()!=null) {
			WechatPlaceOrderParam.h5Info h5info = null;
			SceneInfo scen = param.getScene_info();
			scen.setH5_info(h5info);
			param.setScene_info(scen);
		}
		if(Func.isBlank(wpc.getMerchId()) || Func.isBlank(wpc.getMerchSerialNumber())
				|| Func.isBlank(wpc.getApiV3Key()) 
				|| Func.isBlank(wpc.getMerchPrivateKeyPath())
				|| Func.isBlank(wpc.getAppId())) {
			throw new PayException("当前应用未开通支付，请联系管理员");
		}
		param.setAppid(wpc.getAppId());
		param.setMchid(wpc.getMerchId());
		if(Func.isBlank(param.getNotify_url()) && Func.isNotBlank(wpc.getRedirectUri())) {
			param.setNotify_url(wpc.getRedirectUri());
		}else if(Func.isBlank(param.getNotify_url()) && Func.isBlank(wpc.getRedirectUri())) {
			throw new PayException("通知地址不能为空");
		}
		if(httpClient==null) {
			this.initHttpClient();
		}
		HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
		httpPost.addHeader("Accept", "application/json");
		httpPost.addHeader("Content-type","application/json; charset=utf-8");
				
		httpPost.setEntity(new StringEntity(param.getByteArrayOutputStream(param).toString("UTF-8"), "UTF-8"));
		CloseableHttpResponse response = httpClient.execute(httpPost);
		if(response.getStatusLine().getStatusCode()==SC_OK) {
			String bodyAsString = EntityUtils.toString(response.getEntity());
			log.debug(bodyAsString);
			JsonNode json = Jackson.toJsonObject(bodyAsString);
			WechatJSApiPayParam result = new WechatJSApiPayParam();
			result.setAppId(wpc.getAppId());
			result.setPackages("prepay_id="+json.get("prepay_id").asText());
			result.sign(wpc.getMerchPrivateKeyPath());
			result.setOutTradeNo(param.getOutTradeNo());
			return result;
		}else {
			log.error(EntityUtils.toString(response.getEntity()));
			throw new PayException("错误码："+response.getStatusLine().getStatusCode());
		}
	}
	
	/**
	 * app下单
	 * @param param 微信下单参数
	 * @throws Exception 
	 * @return APP调起支付签名参数信息 {@link net.hlinfo.pbp.pay.opt.wechat.WechatAppPayParam}对象
	 */
	public WechatAppPayParam placeOrderApp(WechatPlaceOrderParam param) throws PayException,Exception {
		log.debug("WechatPlaceOrderParam:{}",param);
		if(Func.isBlank(param.getOutTradeNo())) {
			throw new PayException("商户订单号不能为空");
		}
		if(param.getAmount().getTotal()<=0) {
			throw new PayException("支付金额不能为空");
		}
		if(param.getScene_info()!=null) {
			WechatPlaceOrderParam.h5Info h5info = null;
			SceneInfo scen = param.getScene_info();
			scen.setH5_info(h5info);
			param.setScene_info(scen);
		}
		WechatPlaceOrderParam.Payer payer = null;
		param.setPayer(payer);
		if(Func.isBlank(wpc.getMerchId()) || Func.isBlank(wpc.getMerchSerialNumber())
				|| Func.isBlank(wpc.getApiV3Key()) 
				|| Func.isBlank(wpc.getMerchPrivateKeyPath())
				|| Func.isBlank(wpc.getAppId())) {
			throw new PayException("当前应用未开通支付，请联系管理员");
		}
		param.setAppid(wpc.getAppId());
		param.setMchid(wpc.getMerchId());
		if(Func.isBlank(param.getNotify_url()) && Func.isNotBlank(wpc.getRedirectUri())) {
			param.setNotify_url(wpc.getRedirectUri());
		}else if(Func.isBlank(param.getNotify_url()) && Func.isBlank(wpc.getRedirectUri())) {
			throw new PayException("通知地址不能为空");
		}
		if(httpClient==null) {
			this.initHttpClient();
		}
		HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/app");
		httpPost.addHeader("Accept", "application/json");
		httpPost.addHeader("Content-type","application/json; charset=utf-8");
		
		httpPost.setEntity(new StringEntity(param.getByteArrayOutputStream(param).toString("UTF-8"), "UTF-8"));
		CloseableHttpResponse response = httpClient.execute(httpPost);
		if(response.getStatusLine().getStatusCode()==SC_OK) {
			String bodyAsString = EntityUtils.toString(response.getEntity());
			log.debug(bodyAsString);
			JsonNode json = Jackson.toJsonObject(bodyAsString);
			WechatAppPayParam result = new WechatAppPayParam();
			result.setAppId(wpc.getAppId());
			result.setPartnerid(wpc.getMerchId());
			result.setPrepayid(json.get("prepay_id").asText());
			result.sign(wpc.getMerchPrivateKeyPath());
			return result;
		}else {
			log.error(EntityUtils.toString(response.getEntity()));
			throw new PayException("错误码："+response.getStatusLine().getStatusCode());
		}
	}
	
	/**
	 * H5下单
	 * @param param 微信下单参数
	 * @throws Exception 
	 * @return 支付跳转链接,可通过访问该url来拉起微信客户端，完成支付，有效期为5分钟。
示例值：https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx2016121516420242444321ca0631331346&package=1405458241 
	 */
	public String placeOrderH5(WechatPlaceOrderParam param) throws PayException,Exception {
		log.debug("WechatPlaceOrderParam:{}",param);
		if(Func.isBlank(param.getOutTradeNo())) {
			throw new PayException("商户订单号不能为空");
		}
		if(param.getAmount().getTotal()<=0) {
			throw new PayException("支付金额不能为空");
		}
		if(param.getScene_info()==null 
				|| Func.isBlank(param.getScene_info().getPayer_client_ip())
				|| param.getScene_info().getH5_info()==null) {			
			throw new PayException("支付场景描述不能为空");
		}
		WechatPlaceOrderParam.Payer payer = null;
		param.setPayer(payer);
		if(Func.isBlank(wpc.getMerchId()) || Func.isBlank(wpc.getMerchSerialNumber())
				|| Func.isBlank(wpc.getApiV3Key()) 
				|| Func.isBlank(wpc.getMerchPrivateKeyPath())
				|| Func.isBlank(wpc.getAppId())) {
			throw new PayException("当前应用未开通支付，请联系管理员");
		}
		param.setAppid(wpc.getAppId());
		param.setMchid(wpc.getMerchId());
		if(Func.isBlank(param.getNotify_url()) && Func.isNotBlank(wpc.getRedirectUri())) {
			param.setNotify_url(wpc.getRedirectUri());
		}else if(Func.isBlank(param.getNotify_url()) && Func.isBlank(wpc.getRedirectUri())) {
			throw new PayException("通知地址不能为空");
		}
		if(httpClient==null) {
			this.initHttpClient();
		}
		HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/h5");
		httpPost.addHeader("Accept", "application/json");
		httpPost.addHeader("Content-type","application/json; charset=utf-8");
		
		httpPost.setEntity(new StringEntity(param.getByteArrayOutputStream(param).toString("UTF-8"), "UTF-8"));
		CloseableHttpResponse response = httpClient.execute(httpPost);
		if(response.getStatusLine().getStatusCode()==SC_OK) {
			String bodyAsString = EntityUtils.toString(response.getEntity());
			log.debug(bodyAsString);
			JsonNode json = Jackson.toJsonObject(bodyAsString);
			return json.get("h5_url").asText();
		}else {
			log.error(EntityUtils.toString(response.getEntity()));
			throw new PayException("错误码："+response.getStatusLine().getStatusCode());
		}
	}
	/**
	 * Native下单
	 * @param param 微信下单参数
	 * @throws Exception 
	 * @return 二维码链接,此URL用于生成支付二维码，然后提供给用户扫码支付。
	 */
	public String placeOrderNative(WechatPlaceOrderParam param) throws PayException,Exception {
		log.debug("WechatPlaceOrderParam:{}",param);
		if(Func.isBlank(param.getOutTradeNo())) {
			throw new PayException("商户订单号不能为空");
		}
		if(param.getAmount().getTotal()<=0) {
			throw new PayException("支付金额不能为空");
		}
		if(param.getScene_info()!=null) {
			WechatPlaceOrderParam.h5Info h5info = null;
			SceneInfo scen = param.getScene_info();
			scen.setH5_info(h5info);
			param.setScene_info(scen);
		}
		WechatPlaceOrderParam.Payer payer = null;
		param.setPayer(payer);
		if(Func.isBlank(wpc.getMerchId()) || Func.isBlank(wpc.getMerchSerialNumber())
				|| Func.isBlank(wpc.getApiV3Key()) 
				|| Func.isBlank(wpc.getMerchPrivateKeyPath())
				|| Func.isBlank(wpc.getAppId())) {
			throw new PayException("当前应用未开通支付，请联系管理员");
		}
		param.setAppid(wpc.getAppId());
		param.setMchid(wpc.getMerchId());
		if(Func.isBlank(param.getNotify_url()) && Func.isNotBlank(wpc.getRedirectUri())) {
			param.setNotify_url(wpc.getRedirectUri());
		}else if(Func.isBlank(param.getNotify_url()) && Func.isBlank(wpc.getRedirectUri())) {
			throw new PayException("通知地址不能为空");
		}
		if(httpClient==null) {
			this.initHttpClient();
		}
		HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/native");
		httpPost.addHeader("Accept", "application/json");
		httpPost.addHeader("Content-type","application/json; charset=utf-8");
		
		httpPost.setEntity(new StringEntity(param.getByteArrayOutputStream(param).toString("UTF-8"), "UTF-8"));
		CloseableHttpResponse response = httpClient.execute(httpPost);
		if(response.getStatusLine().getStatusCode()==SC_OK) {
			String bodyAsString = EntityUtils.toString(response.getEntity());
			log.debug(bodyAsString);
			JsonNode json = Jackson.toJsonObject(bodyAsString);
			return json.get("code_url").asText();
		}else {
			log.error(EntityUtils.toString(response.getEntity()));
			throw new PayException("错误码："+response.getStatusLine().getStatusCode());
		}
	}
	/**
	 * 微信支付申请退款
	 * @param param  微信支付申请退款请求参数
	 * @throws Exception 
	 * @return 返回参数未做处理,请自行解析,详情请参考微信支付申请退款接口说明
	 */
	public String refunds(WechatRefundsParam param) throws PayException,Exception {
		log.debug("WechatPlaceOrderParam:{}",param);
		if(Func.isBlank(param.getOut_trade_no()) && Func.isBlank(param.getTransaction_id())) {
			throw new PayException("微信支付订单号和商户订单号二选一");
		}
		if(Func.isBlank(param.getOut_refund_no())) {
			throw new PayException("商户退款单号不能为空");
		}
		if(param.getAmount()==null 
				|| param.getAmount().getTotal()<=0
				|| param.getAmount().getRefund()<=0) {
			throw new PayException("退款金额信息不能为空");
		}
		if(Func.isNotBlank(param.getTransaction_id())) {
			param.setOut_trade_no(null);
		}
		if(Func.isBlank(wpc.getMerchId()) || Func.isBlank(wpc.getMerchSerialNumber())
				|| Func.isBlank(wpc.getApiV3Key()) 
				|| Func.isBlank(wpc.getMerchPrivateKeyPath())
				|| Func.isBlank(wpc.getAppId())) {
			throw new PayException("当前应用未开通支付，请联系管理员");
		}
		
		if(httpClient==null) {
			this.initHttpClient();
		}
		HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds");
		httpPost.addHeader("Accept", "application/json");
		httpPost.addHeader("Content-type","application/json; charset=utf-8");
		
		httpPost.setEntity(new StringEntity(param.getByteArrayOutputStream(param).toString("UTF-8"), "UTF-8"));
		CloseableHttpResponse response = httpClient.execute(httpPost);
		if(response.getStatusLine().getStatusCode()==SC_OK) {
			String bodyAsString = EntityUtils.toString(response.getEntity());
			log.debug(bodyAsString);
			return bodyAsString;
		}else {
			log.error(EntityUtils.toString(response.getEntity()));
			throw new PayException("错误码："+response.getStatusLine().getStatusCode());
		}
	}
	/**
	 * 查询单笔退款
	 @param outRefundNo 商户退款单号
	 @throws Exception 
	 @return 返回参数未做处理,请自行解析,详情请参考微信支付查询单笔退款接口说明
	 */
	public String queryRefunds(String outRefundNo) throws Exception {
		log.debug("查询单笔退款，商户退款单号：{}",outRefundNo);
		if(httpClient==null) {
			this.initHttpClient();
		}
		String url = "";
		if(Func.isBlank(outRefundNo)) {
			throw new PayException("商户退款单号不能为空");
		}
		url = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds/"+outRefundNo;
		URIBuilder uriBuilder = new URIBuilder(url);
	   HttpGet httpGet = new HttpGet(uriBuilder.build());
	   httpGet.addHeader(ACCEPT, APPLICATION_JSON.toString());
	   CloseableHttpResponse response = httpClient.execute(httpGet);
	   if(response.getStatusLine().getStatusCode()==SC_OK) {
		   String bodyAsString = EntityUtils.toString(response.getEntity());
		   return bodyAsString;
	   }
	   log.error(EntityUtils.toString(response.getEntity()));
	   throw new PayException("查询支付订单失败");
	}
	/**
	 * 查询订单
	 @param outTradeNo 商户订单号,商户系统内部订单号,商户订单号和微信支付订单号任选其一
	 @param transactionId 微信支付订单号, 微信支付系统生成的订单号,商户订单号和微信支付订单号任选其一
	 @throws Exception 
	 */
	public String queryOrder(String outTradeNo,String transactionId) throws Exception {
		log.debug("查询订单，订单号：{}",outTradeNo+","+transactionId);
		if(httpClient==null) {
			this.initHttpClient();
		}
		String url = "";
		if(Func.isNotBlank(outTradeNo)) {
			url = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/"+outTradeNo+"?mchid="+wpc.getMerchId();
		}else if(Func.isNotBlank(transactionId)) {
			url = "https://api.mch.weixin.qq.com/v3/pay/transactions/id/"+transactionId+"?mchid="+wpc.getMerchId();
		}else {
			//商户订单号和微信支付订单号都为空
			throw new PayException("参数商户订单号或微信支付订单号不能为空");
		}
		URIBuilder uriBuilder = new URIBuilder(url);
	   HttpGet httpGet = new HttpGet(uriBuilder.build());
	   httpGet.addHeader(ACCEPT, APPLICATION_JSON.toString());
	   CloseableHttpResponse response = httpClient.execute(httpGet);
	   if(response.getStatusLine().getStatusCode()==SC_OK) {
		   String bodyAsString = EntityUtils.toString(response.getEntity());
		   return bodyAsString;
	   }
	   log.error(EntityUtils.toString(response.getEntity()));
	   throw new PayException("查询支付订单失败");
	}
	/**
	 * 关闭订单
	 @param outTradeNo 商户订单号,商户系统内部订单号
	 @return
	 @throws Exception
	 */
	public boolean closePayOrder(String outTradeNo) throws Exception {
		log.debug("执行关闭订单，订单号：{}",outTradeNo);
		if(httpClient==null) {
			this.initHttpClient();
		}
		String url = "https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/%s/close";
		url = String.format(url, outTradeNo);
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Accept", "application/json");
		httpPost.addHeader("Content-type","application/json; charset=utf-8");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode rootNode = objectMapper.createObjectNode();
		rootNode.put("mchid",wpc.getMerchId());
		objectMapper.writeValue(bos, rootNode);
		
		httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
		CloseableHttpResponse response = httpClient.execute(httpPost);
		log.debug("StatusCode:{}",response.getStatusLine().getStatusCode());
		if(response.getStatusLine().getStatusCode()==204) {
			return true;
		}else {
			if(response.getEntity()!=null) {
				log.error(EntityUtils.toString(response.getEntity()));
			}
			throw new PayException("请求失败，请重试，错误码："+response.getStatusLine().getStatusCode());
		}
	}
	/**
	 * 公共http get请求方法,,适用于本类未实现的接口
	 @param url 请求地址,带上请求参数的完整请求url,
	 @throws Exception 
	 @return 返回参数未做处理,请自行解析,详情请参考微信支付相关请求接口说明
	 */
	public String httpGet(String url) throws Exception {
		log.debug("公共http get请求，请求地址：{}",url);
		if(Func.isBlank(url)) {
			throw new PayException("请求地址不能为空");
		}
		if(httpClient==null) {
			this.initHttpClient();
		}
		URIBuilder uriBuilder = new URIBuilder(url);
	   HttpGet httpGet = new HttpGet(uriBuilder.build());
	   httpGet.addHeader(ACCEPT, APPLICATION_JSON.toString());
	   CloseableHttpResponse response = httpClient.execute(httpGet);
	   if(response.getStatusLine().getStatusCode()==SC_OK) {
		   String bodyAsString = EntityUtils.toString(response.getEntity());
		   return bodyAsString;
	   }
	   log.error(EntityUtils.toString(response.getEntity()));
	   throw new PayException("请求失败");
	}
	/**
	 * 公共http post请求方法,适用于本类未实现的接口
	 * @param data 请求body参数对象,可以是javaBean,Jcakson等对象
	 * @param url 请求地址
	 * @throws Exception 
	 * @return 返回参数未做处理,请自行解析,详情请参考微信支付相关请求接口说明
	 */
	public String httpPost(Serializable data,String url) throws PayException,Exception {
		log.debug("公共http post请求方法:{}",data);
		log.debug("公共http post请求url:{}",url);
		if(data==null || Func.isBlank(url)) {
			throw new PayException("请求参数不能为空");
		}
		
		if(Func.isBlank(url)) {
			throw new PayException("请求地址不能为空");
		}
		
		if(Func.isBlank(wpc.getMerchId()) || Func.isBlank(wpc.getMerchSerialNumber())
				|| Func.isBlank(wpc.getApiV3Key()) 
				|| Func.isBlank(wpc.getMerchPrivateKeyPath())
				|| Func.isBlank(wpc.getAppId())) {
			throw new PayException("当前应用未开通支付，请联系管理员");
		}
		
		if(httpClient==null) {
			this.initHttpClient();
		}
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Accept", "application/json");
		httpPost.addHeader("Content-type","application/json; charset=utf-8");
		
		httpPost.setEntity(new StringEntity(PayUtils.getByteArrayOutputStream(data).toString("UTF-8"), "UTF-8"));
		CloseableHttpResponse response = httpClient.execute(httpPost);
		if(response.getStatusLine().getStatusCode()==SC_OK) {
			String bodyAsString = EntityUtils.toString(response.getEntity());
			log.debug(bodyAsString);
			return bodyAsString;
		}else {
			log.error(EntityUtils.toString(response.getEntity()));
			throw new PayException("错误码："+response.getStatusLine().getStatusCode());
		}
	}
	/**
	 * 验证应答或者支付回调的签名
	 @param request
	 @param body
	 @return
	 @throws Exception
	 */
	public boolean verifiedSign(HttpServletRequest request, String body) throws Exception {
		this.initCertificate(false);
		//微信返回的证书序列号
        String serialNo = request.getHeader("Wechatpay-Serial");
        //微信返回的随机字符串
        String nonceStr = request.getHeader("Wechatpay-Nonce");
        //微信返回的时间戳
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        //微信返回的签名
        String wechatSign = request.getHeader("Wechatpay-Signature");
        //组装签名字符串
        String signStr = timestamp+"\n"
        					+ nonceStr +"\n"
        					+ body +"\n";
        //验证证书序列号
        if(Func.notequals(serialNo, wpc.getMerchSerialNumber())) {
        	return false;
         }
        //SHA256withRSA签名
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(wechatPayCertificates.get(0));
        signature.update(signStr.getBytes());
        //返回验签结果
        return signature.verify(Base64Utils.decodeFromString(wechatSign));
	}
}

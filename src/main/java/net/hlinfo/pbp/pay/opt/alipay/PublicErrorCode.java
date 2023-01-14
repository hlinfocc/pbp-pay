package net.hlinfo.pbp.pay.opt.alipay;

import java.util.HashMap;
import java.util.Map;

public class PublicErrorCode {
	/**
	 * 网关返回码与网关返回码描述
	 */
	public static final Map<String, String> CODE = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
	{
		put("10000","接口调用成功");
		put("20000","服务不可用");
		put("20001","授权权限不足");
		put("40001","缺少必选参数");
		put("40002","非法的参数");
		put("40003","条件异常");
		put("40004","业务处理失败");
		put("40005","调用频次超限");
		put("40006","权限不足");
	}};
	/**
	 * 公共业务返回码与公共业务返回码描述
	 */
	public static final Map<String, String> SUB_CODE = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("isp.unknow-error","服务暂不可用（业务系统不可用）");
			put("aop.unknow-error","服务暂不可用（网关自身的未知错误）");
			put("aop.invalid-auth-token","无效的访问令牌");
			put("aop.auth-token-time-out","访问令牌已过期");
			put("aop.invalid-app-auth-token","无效的应用授权令牌");
			put("aop.invalid-app-auth-token-no-api","商户未授权当前接口");
			put("aop.app-auth-token-time-out","应用授权令牌已过期");
			put("aop.no-product-reg-by-partner","商家未签约任何产品");
			put("isv.missing-method","缺少方法名参数");
			put("isv.missing-signature","缺少签名参数");
			put("isv.missing-signature-type","缺少签名类型参数");
			put("isv.missing-signature-key","缺少签名配置");
			put("isv.missing-app-id","缺少appId参数");
			put("isv.missing-timestamp","缺少时间戳参数");
			put("isv.missing-version","缺少版本参数");
			put("isv.decryption-error-missing-encrypt-type","解密出错，未指定加密算法");
			put("isv.invalid-parameter","参数无效(检查参数，格式不对、非法值、越界等)");
			put("isv.upload-fail","文件上传失败");
			put("isv.invalid-file-extension","文件扩展名无效");
			put("isv.invalid-file-size","文件大小无效");
			put("isv.invalid-method","不存在的方法名");
			put("isv.invalid-format","无效的数据格式");
			put("isv.invalid-signature-type","无效的签名类型");
			put("isv.invalid-signature","无效签名");
			put("isv.invalid-token","无效令牌");
			put("isv.invalid-encrypt-type","无效的加密类型");
			put("isv.invalid-encrypt","解密异常");
			put("isv.invalid-app-id","无效的appId参数");
			put("isv.invalid-timestamp","非法的时间戳参数");
			put("isv.invalid-charset","字符集错误");
			put("isv.invalid-digest","摘要错误");
			put("isv.decryption-error-not-valid-encrypt-type","解密出错，不支持的加密算法");
			put("isv.decryption-error-not-valid-encrypt-key","解密出错，未配置加密密钥或加密密钥格式错误");
			put("isv.decryption-error-unknown","解密出错，未知异常");
			put("isv.missing-signature-config","验签出错, 未配置对应签名算法的公钥或者证书");
			put("isv.not-support-app-auth","本接口不支持第三方代理调用");
			put("isv.suspected-attack","可疑的攻击请求");
			put("isv.forbidden-api","接口被禁用");
			put("app-cert-expired","应用公钥证书已经不在有效期内");
			put("invalid-auth-relations","无效的授权关系");
			put("isv.missing-signature-config","验签出错，未配置对应签名算法的公钥或者证书。");
			put("isv.app-call-limited","应用调用次数超限，包含调用频率超限");
			put("isv.method-call-limited","API调用次数超限，包含调用频率超限");
			put("isv.insufficient-isv-permissions","ISV 权限不足");
			put("isv.insufficient-user-permissions","用户权限不足");
	}};
	/**
	 * app支付接口业务错误码
	 */
	public static final Map<String, String> TRADE_APP_SUB_CODE = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			putAll(SUB_CODE);
			put("ACQ.SYSTEM_ERROR","接口返回错误");
			put("ACQ.SYSTEM_ERROR","接口返回错误");
			put("ACQ.ACCESS_FORBIDDEN","无权限使用接口");
			put("ACQ.EXIST_FORBIDDEN_WORD","订单信息中包含违禁词");
			put("ACQ.PARTNER_ERROR","应用APP_ID填写错误");
			put("ACQ.TOTAL_FEE_EXCEED","订单总金额不在允许范围内");
			put("ACQ.BUYER_SELLER_EQUAL","买卖家不能相同");
			put("ACQ.BUYER_ENABLE_STATUS_FORBID","买家状态非法");
			put("ACQ.SELLER_BEEN_BLOCKED","商家账号被冻结");
			put("ACQ.INVALID_PARAMETER","参数无效");
			put("ACQ.TRADE_HAS_CLOSE","交易已经关闭");
			put("ACQ.CONTEXT_INCONSISTENT","交易信息被篡改");
			put("ACQ.TRADE_BUYER_NOT_MATCH","交易买家不匹配");
			put("ACQ.TRADE_HAS_SUCCESS","交易已被支付");
		}
	};
	/**
	 * 手机网站支付接口2.0业务错误码
	 */
	public static final Map<String, String> TRADE_H5_SUB_CODE = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			putAll(SUB_CODE);
			put("ACQ.SYSTEM_ERROR","接口返回错误");
			put("ACQ.INVALID_PARAMETER","参数无效");
			put("ACQ.ACCESS_FORBIDDEN","无权限使用接口");
			put("ACQ.EXIST_FORBIDDEN_WORD","订单信息中包含违禁词");
			put("ACQ.PARTNER_ERROR","应用APP_ID填写错误");
			put("ACQ.TOTAL_FEE_EXCEED","订单总金额不在允许范围内");
			put("ACQ.CONTEXT_INCONSISTENT","交易信息被篡改");
			put("ACQ.TRADE_HAS_SUCCESS","交易已被支付");
			put("ACQ.TRADE_HAS_CLOSE","交易已经关闭");
			put("ACQ.PAYMENT_REQUEST_HAS_RISK","支付有风险");
		}
	};
	/**
	 * 电脑网站统一收单下单并支付页面接口业务错误码
	 */
	public static final Map<String, String> TRADE_PC_SUB_CODE = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			putAll(SUB_CODE);
			put("ACQ.SYSTEM_ERROR","接口返回错误");
			put("ACQ.INVALID_PARAMETER","参数无效");
			put("ACQ.ACCESS_FORBIDDEN","无权限使用接口");
			put("ACQ.EXIST_FORBIDDEN_WORD","订单信息中包含违禁词");
			put("ACQ.PARTNER_ERROR","应用APP_ID填写错误");
			put("ACQ.TOTAL_FEE_EXCEED","订单总金额不在允许范围内");
			put("ACQ.CONTEXT_INCONSISTENT","交易信息被篡改");
			put("ACQ.TRADE_HAS_SUCCESS","交易已被支付");
			put("ACQ.TRADE_HAS_CLOSE","交易已经关闭");
			put("ACQ.BUYER_BALANCE_NOT_ENOUGH","买家余额不足");
			put("ACQ.BUYER_BANKCARD_BALANCE_NOT_E","用户银行卡余额不足");
			put("ACQ.ERROR_BALANCE_PAYMENT_DISABL","余额支付功能关闭");
			put("ACQ.BUYER_SELLER_EQUAL","买卖家不能相同");
			put("ACQ.TRADE_BUYER_NOT_MATCH","交易买家不匹配");
			put("ACQ.BUYER_ENABLE_STATUS_FORBID","买家状态非法");
			put("ACQ.PAYMENT_FAIL","支付失败");
			put("ACQ.BUYER_PAYMENT_AMOUNT_DAY_LIM","买家付款日限额超限");
			put("ACQ.BUYER_PAYMENT_AMOUNT_MONTH_L","买家付款月额度超限");
			put("ACQ.ERROR_BUYER_CERTIFY_LEVEL_LI","买家未通过人行认证");
			put("ACQ.PAYMENT_REQUEST_HAS_RISK","支付有风险");
			put("ACQ.NO_PAYMENT_INSTRUMENTS_AVAIL","没用可用的支付工具");
			put("ACQ.ILLEGAL_SIGN_VALIDTY_PERIOD","无效的签约有效期");
			put("ACQ.MERCHANT_AGREEMENT_NOT_EXIST","商户协议不存在");
		}
	};
	/**
	 * 小程序统一收单交易创建接口业务错误码
	 */
	public static final Map<String, String> TRADE_MINI_SUB_CODE = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			putAll(SUB_CODE);
			put("ACQ.SYSTEM_ERROR","接口返回错误");
			put("ACQ.INVALID_PARAMETER","参数无效");
			put("ACQ.ACCESS_FORBIDDEN","无权限使用接口");
			put("ACQ.EXIST_FORBIDDEN_WORD","订单信息中包含违禁词");
			put("ACQ.PARTNER_ERROR","应用APP_ID填写错误");
			put("ACQ.TOTAL_FEE_EXCEED","订单总金额超过限额");
			put("ACQ.CONTEXT_INCONSISTENT","交易信息被篡改");
			put("ACQ.TRADE_HAS_SUCCESS","交易已被支付");
			put("ACQ.TRADE_HAS_CLOSE","交易已经关闭");
			put("ACQ.BUYER_SELLER_EQUAL","买卖家不能相同");
			put("ACQ.TRADE_BUYER_NOT_MATCH","交易买家不匹配");
			put("ACQ.BUYER_ENABLE_STATUS_FORBID","买家状态非法");
			put("ACQ.SELLER_BEEN_BLOCKED","商家账号被冻结");
			put("ACQ.ERROR_BUYER_CERTIFY_LEVEL_LIMIT","买家未通过人行认证");
			put("ACQ.SUB_MERCHANT_CREATE_FAIL","二级商户创建失败");
			put("ACQ.SUB_MERCHANT_TYPE_INVALID","二级商户类型非法");
			put("ACQ.SECONDARY_MERCHANT_STATUS_ERROR","商户状态异常");
			put("ACQ.BUYER_NOT_EXIST","买家不存在");
			put("ACQ.PAYER_UNMATCHED","付款人不匹配");
			put("ACQ.SECONDARY_MERCHANT_NOT_MATCH","二级商户信息不匹配");
			put("ACQ.TRADE_SETTLE_ERROR","交易结算异常");
			put("ACQ.SECONDARY_MERCHANT_ID_BLANK","二级商户编号错误");
			put("ACQ.INVALID_RECEIVE_ACCOUNT","收款账户不支持");
			put("ACQ.SECONDARY_MERCHANT_ID_INVALID","二级商户不存在");
			put("ACQ.BEYOND_PER_RECEIPT_SINGLE_RESTRICTION","订单金额超过单笔限额");
			put("ACQ.SECONDARY_MERCHANT_ALIPAY_ACCOUNT_INVALID","二级商户账户异常");
			put("ACQ.SECONDARY_MERCHANT_ISV_PUNISH_INDIRECT","商户状态异常");
			put("ACQ.USER_LOGONID_DUP","用户账号重复");
			put("ACQ.CUSTOMER_VALIDATE_ERROR","客户校验出错");
			put("ACQ.INVALID_STORE_ID","商户门店编号无效");
			put("ACQ.SECONDARY_MERCHANT_CARD_ALIAS_NO_INVALID","二级商户银行卡编号错误");
			put("ACQ.NOT_SUPPORT_PAYMENT_INST","不支持的钱包版本");
			put("ACQ.SELLER_NOT_EXIST","卖家不存在");
			put("ACQ.NOW_TIME_AFTER_EXPIRE_TIME_ERROR","当前时间已超过允许支付的时间");
			put("ACQ.ILLEGAL_ARGUMENT","参数错误");
			put("ACQ.SUB_GOODS_SIZE_MAX_COUNT","子商品明细超长");
			put("ACQ.PLATFORM_BUSINESS_ACQUIRE_MODE_MUST_MERCHANT_ID","二级商户编码为空");
			put("ACQ.ERROR_SELLER_CERTIFY_LEVEL_LIMIT","卖家未通过人行认证");
			put("ACQ.STORE_INFO_INVALID","门店信息错误");
			put("ACQ.DEFAULT_SETTLE_RULE_NOT_EXIST","默认结算条款不存在");
			put("ACQ.MERCHANT_PERM_RECEIPT_SUSPEND_LIMIT","商户暂停收款");
			put("ACQ.MERCHANT_PERM_RECEIPT_SINGLE_LIMIT","超过单笔收款限额");
			put("ACQ.MERCHANT_PERM_RECEIPT_DAY_LIMIT","超过单日累计收款额度");
			put("ACQ.OPEN_ID_NOT_TINY_APP","请求的应用id非小程序应用类型");
		}
	};
}

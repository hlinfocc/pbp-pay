package net.hlinfo.pbp.pay.opt.wechat;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import net.hlinfo.opt.Jackson;

/**
 * 微信支付申请退款请求参数
 *
 */
public class WechatRefundsParam implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final Logger log = LoggerFactory.getLogger(WechatRefundsParam.class);
	/**
	 * 微信支付订单号,原支付交易对应的微信订单号<br>
	 * [必填]微信支付订单号和商户订单号二选一
	 */
	private String transaction_id;
	/**
	 * 商户订单号,原支付交易对应的商户订单号<br>
	 * [必填]微信支付订单号和商户订单号二选一
	 */
	private String out_trade_no;
	/**
	 * [必填]商户退款单号,商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
	 */
	private String out_refund_no;
	/**
	 * 退款原因，若商户传入，会在下发给用户的退款消息中体现退款原因
	 */
	private String reason;
	/**
	 * 退款结果回调url[必填]
	 */
	private String notify_url;
	/**
	 * 退款资金来源,若传递此参数则使用对应的资金账户退款，<br>
	 * 否则默认使用未结算资金退款（仅对老资金流商户适用）<br>
	 * 枚举值：<br>
	 * AVAILABLE：可用余额账户
	 */
	private String funds_account;
	
	/**
	 * 退款金额信息 [必填]
	 */
	private Amount amount;
	/**
	 * 退款商品,指定商品退款需要传此参数，其他场景无需传递
	 */
	private List<GoodsDetail> goods_detail;
	/**
	 * 微信支付订单号,原支付交易对应的微信订单号<br>
	 * [必填]微信支付订单号和商户订单号二选一
	 @return 微信支付订单号
	 */
	public String getTransaction_id() {
		return transaction_id;
	}
	/**
	 * 微信支付订单号,原支付交易对应的微信订单号<br>
	 * [必填]微信支付订单号和商户订单号二选一
	 @param transaction_id 微信支付订单号
	 */
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	/**
	 * 商户订单号,原支付交易对应的商户订单号<br>
	 * [必填]微信支付订单号和商户订单号二选一
	 @return  商户订单号
	 */
	public String getOut_trade_no() {
		return out_trade_no;
	}
	/**
	 * 商户订单号,原支付交易对应的商户订单号<br>
	 * [必填]微信支付订单号和商户订单号二选一
	 @param out_trade_no  商户订单号
	 */
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	/**
	 * [必填]商户退款单号,商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
	 @return 商户退款单号
	 */
	public String getOut_refund_no() {
		return out_refund_no;
	}
	/**
	 * [必填]商户退款单号,商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
	 @param out_refund_no 商户退款单号
	 */
	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}
	/**
	 * 退款原因，若商户传入，会在下发给用户的退款消息中体现退款原因
	 @return 退款原因
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * 退款原因，若商户传入，会在下发给用户的退款消息中体现退款原因
	 @param reason 退款原因
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * 退款结果回调url[必填]
	 @return 退款结果回调url[必填]
	 */
	public String getNotify_url() {
		return notify_url;
	}
	/**
	 * 退款结果回调url[必填]
	 @param notify_url 退款结果回调url[必填]
	 */
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	/**
	 * 退款资金来源,若传递此参数则使用对应的资金账户退款，<br>
	 * 否则默认使用未结算资金退款（仅对老资金流商户适用）<br>
	 * 枚举值：<br>
	 * AVAILABLE：可用余额账户
	 @return 退款资金来源
	 */
	public String getFunds_account() {
		return funds_account;
	}
	/**
	 * 退款资金来源,若传递此参数则使用对应的资金账户退款，<br>
	 * 否则默认使用未结算资金退款（仅对老资金流商户适用）<br>
	 * 枚举值：<br>
	 * AVAILABLE：可用余额账户
	 @param funds_account 退款资金来源,AVAILABLE
	 */
	public void setFunds_account(String funds_account) {
		this.funds_account = funds_account;
	}
	/**
	 * 退款金额信息 [必填]
	 @return 退款金额信息 [必填]
	 */
	public Amount getAmount() {
		return amount;
	}
	/**
	 * 退款金额信息 [必填]
	 @param amount 退款金额信息 [必填]
	 */
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
	/**
	 * 退款商品,指定商品退款需要传此参数，其他场景无需传递
	 @return 退款商品
	 */
	public List<GoodsDetail> getGoods_detail() {
		return goods_detail;
	}
	/**
	 * 退款商品,指定商品退款需要传此参数，其他场景无需传递
	 @param goods_detail 退款商品,指定商品退款需要传此参数，其他场景无需传递
	 */
	public void setGoods_detail(List<GoodsDetail> goods_detail) {
		this.goods_detail = goods_detail;
	}

	/**
	 * 退款金额信息对象
	 *
	 */
	public class Amount{
		/**
		 * 退款金额，单位为分,只能为整数，不能超过原订单支付金额[必填]
		 */
		private int refund;
		/**
		 * 原订单金额,原支付交易的订单总金额，单位为分
		 */
		private int total;
		/**
		 * 退款出资账户及金额
		 *
		 */
		private List<AmountFrom> from;
		/**
		 * 货币类型,CNY：人民币，境内商户号仅支持人民币。该参数无需填写 
		 */
		private String currency = "CNY";
		/**
		 * 
		 * 原订单金额,原支付交易的订单总金额，单位为分。
		 @return 原订单金额
		 */
		public int getTotal() {
			return total;
		}
		/**
		 * 原订单金额,原支付交易的订单总金额，单位为分。
		 @param total 原订单金额,原支付交易的订单总金额，单位为分。
		 */
		public void setTotal(int total) {
			this.total = total;
		}
		/**
		 * 货币类型,CNY：人民币，境内商户号仅支持人民币。该参数无需填写 
		 @return 货币类型
		 */
		public String getCurrency() {
			return currency;
		}
		/**
		 * 货币类型,CNY：人民币，境内商户号仅支持人民币。该参数无需填写
		 @param currency 货币类型
		 */
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		/**
		 * 退款金额，单位为分,只能为整数，不能超过原订单支付金额[必填]
		 @return 退款金额
		 */
		public int getRefund() {
			return refund;
		}
		/**
		 * 退款金额，单位为分,只能为整数，不能超过原订单支付金额[必填]
		 @param refund 退款金额
		 */
		public void setRefund(int refund) {
			this.refund = refund;
		}
		/**
		 * 退款出资账户及金额
		 @return 退款出资账户及金额
		 */
		public List<AmountFrom> getFrom() {
			return from;
		}
		/**
		 * 退款出资账户及金额
		 @param from 退款出资账户及金额
		 */
		public void setFrom(List<AmountFrom> from) {
			this.from = from;
		}
		/**
		 * 退款金额信息对象构造函数
		 */
		public Amount() {
			super();
			// TODO Auto-generated constructor stub
		}
		/**
		 *退款金额信息对象构造函数
		 * @param refund 退款金额
		 * @param total 原订单金额
		 */
		public Amount(int refund,int total) {
			super();
			this.refund = refund;
			this.total = total;
		}
	}
	/**
	 * 退款出资账户及金额
	 *
	 */
	public class AmountFrom{
		/**
		 * 出资账户类型,枚举值选一：<br>
		* AVAILABLE : 可用余额<br>
		* UNAVAILABLE : 不可用余额
		 */
		private String account;
		/**
		 * 出资金额
		 */
		private int amount;
		/**
		 * 出资账户类型,枚举值选一：<br>
		* AVAILABLE : 可用余额<br>
		* UNAVAILABLE : 不可用余额
		* @return 出资账户类型
		 */
		public String getAccount() {
			return account;
		}
		/**
		 * 出资账户类型,枚举值选一：<br>
		* AVAILABLE : 可用余额<br>
		* UNAVAILABLE : 不可用余额
		* @param account 出资账户类型枚举值
		 */
		public void setAccount(String account) {
			this.account = account;
		}
		/**
		 * 出资金额
		 @return 出资金额
		 */
		public int getAmount() {
			return amount;
		}
		/**
		 * 出资金额
		 @param amount 出资金额
		 */
		public void setAmount(int amount) {
			this.amount = amount;
		}
	}
	/**
	 * 退款商品
	 *
	 */
	public class GoodsDetail{
		/**
		 * 商户侧商品编码 
		 */
		private String merchant_goods_id;
		/**
		 * 微信支付商品编码 
		 */
		private String wechatpay_goods_id;
		/**
		 * 商品名称
		 */
		private String goods_name;
		/**
		 * 商品单价 
		 */
		private int unit_price;
		/**
		 * 商品退款金额
		 */
		private int refund_amount;
		/**
		 * 商品退货数量
		 */
		private int refund_quantity;
		
		/**
		 * 商户侧商品编码 
		 @return 商户侧商品编码 
		 */
		public String getMerchant_goods_id() {
			return merchant_goods_id;
		}
		/**
		 * 商户侧商品编码 
		 @param merchant_goods_id 商户侧商品编码 
		 */
		public void setMerchant_goods_id(String merchant_goods_id) {
			this.merchant_goods_id = merchant_goods_id;
		}
		/**
		 * 微信支付商品编码 
		 @return 微信支付商品编码 
		 */
		public String getWechatpay_goods_id() {
			return wechatpay_goods_id;
		}
		/**
		 * 微信支付商品编码 
		 @param wechatpay_goods_id 微信支付商品编码 
		 */
		public void setWechatpay_goods_id(String wechatpay_goods_id) {
			this.wechatpay_goods_id = wechatpay_goods_id;
		}
		/**
		 * 商品名称
		 @return 商品名称
		 */
		public String getGoods_name() {
			return goods_name;
		}
		/**
		 * 商品名称
		 @param goods_name 商品名称
		 */
		public void setGoods_name(String goods_name) {
			this.goods_name = goods_name;
		}
		/**
		 * 商品单价 
		 @return 商品单价 
		 */
		public int getUnit_price() {
			return unit_price;
		}
		/**
		 * 商品单价 
		 @param unit_price 商品单价 
		 */
		public void setUnit_price(int unit_price) {
			this.unit_price = unit_price;
		}
		/**
		 * 商品退款金额
		 @return 商品退款金额
		 */
		public int getRefund_amount() {
			return refund_amount;
		}
		/**
		 * 商品退款金额
		 @param refund_amount 商品退款金额
		 */
		public void setRefund_amount(int refund_amount) {
			this.refund_amount = refund_amount;
		}
		/**
		 * 商品退货数量
		 @return 商品退货数量
		 */
		public int getRefund_quantity() {
			return refund_quantity;
		}
		/**
		 * 商品退货数量
		 @param refund_quantity 商品退货数量
		 */
		public void setRefund_quantity(int refund_quantity) {
			this.refund_quantity = refund_quantity;
		}
	}
	
	/**
	 * 获取当前对象的ByteArrayOutputStream
	 @param data 当前WechatPlaceOrderParam对象实例
	 @return ByteArrayOutputStream
	 */
	public ByteArrayOutputStream getByteArrayOutputStream(WechatRefundsParam data) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Jackson.toOutputStream(bos, data, true, PropertyNamingStrategies.SNAKE_CASE);
       return bos;
	}
	
	@Override
	public String toString() {
		return Jackson.entityToString(this);
	}
	
}

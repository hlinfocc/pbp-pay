package net.hlinfo.pbp.pay.opt.wechat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

import net.hlinfo.opt.Jackson;

/**
 * 微信下单参数
 * @author cy
 *
 */
public class WechatPlaceOrderParam implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final Logger log = LoggerFactory.getLogger(WechatPlaceOrderParam.class);
	/**
	 * 微信公众平台应用ID[无需传参,默认从配置文件读取]
	 */
	private String appid;
	/**
	 * 微信支付平台商户号[无需传参,默认从配置文件读取]
	 */
	private String mchid;
	/**
	 * 商品描述[必填]
	 */
	private String description;
	/**
	 * 商户订单号,商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一 [必填]
	 */
	private String outTradeNo;
	/**
	 * 交易结束时间
	 */
	private String timeExpire;
	/**
	 * 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用，实际情况下只有支付完成状态才会返回该字段。
	 */
	private String attach;
	/**
	 * 通知地址[必填,若为空则从配置文件读取]
	 */
	private String notify_url;
	/**
	 * 订单优惠标记
	 */
	private String goods_tag;
	/**
	 * 电子发票入口开放标识
	 */
	private String support_fapiao;
	/**
	 * 订单金额 [必填]
	 */
	private Amount amount;
	/**
	 * 支付者信息,仅限JSAPI,必填
	 */
	private Payer payer;
	/**
	 * 优惠功能
	 */
	private Payer detail;
	/**
	 * 支付场景描述
	 */
	private SceneInfo scene_info;
	/**
	 * 结算信息
	 */
	private SettleInfo settle_info;
	
	/**
	 * 微信公众平台应用ID[无需传参,默认从配置文件读取]
	 @return 微信公众平台应用ID
	 */
	public String getAppid() {
		return appid;
	}
	/**
	 * 微信公众平台应用ID[无需传参,默认从配置文件读取]
	 @param appid 微信公众平台应用ID[必填]
	 */
	public void setAppid(String appid) {
		this.appid = appid;
	}
	/**
	 * 微信支付平台商户号[无需传参,默认从配置文件读取]
	 @return 微信支付平台商户号[必填]
	 */
	public String getMchid() {
		return mchid;
	}
	/**
	 * 微信支付平台商户号[无需传参,默认从配置文件读取]
	 * @param mchid 商户号
	 */
	public void setMchid(String mchid) {
		this.mchid = mchid;
	}
	/**
	 * 商品描述[必填]
	 @return 商品描述[必填]
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 商品描述[必填]
	 @param description 商品描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 商户订单号,商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一 [必填]
	 @return 商户订单号
	 */
	public String getOutTradeNo() {
		return outTradeNo;
	}
	/**
	 * 商户订单号,商户系统内部订单号,也就是我们程序生成的订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一[必填] 
	 @param outTradeNo 商户订单号
	 */
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	/**
	 * 交易结束时间,订单失效时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE
	 @return 交易结束时间
	 */
	public String getTimeExpire() {
		return timeExpire;
	}
	/**
	 * 交易结束时间,订单失效时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE
	 @param timeExpire 交易结束时间
	 */
	public void setTimeExpire(String timeExpire) {
		this.timeExpire = timeExpire;
	}
	/**
	 * 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用，实际情况下只有支付完成状态才会返回该字段。
	 @return 附加数据
	 */
	public String getAttach() {
		return attach;
	}
	/**
	 * 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用，实际情况下只有支付完成状态才会返回该字段。
	 @param attach 附加数据
	 */
	public void setAttach(String attach) {
		this.attach = attach;
	}
	/**
	 * 通知地址[必填,若为空则从配置文件读取]
	 @return 通知地址[必填]
	 */
	public String getNotify_url() {
		return notify_url;
	}
	/**
	 * 通知地址[必填,若为空则从配置文件读取]
	 @param notify_url 通知地址[必填]
	 */
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	/**
	 * 订单优惠标记
	 @return 订单优惠标记
	 */
	public String getGoods_tag() {
		return goods_tag;
	}
	/**
	 * 订单优惠标记
	 @param goods_tag 订单优惠标记
	 */
	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}
	/**
	 * 电子发票入口开放标识
	 @return 电子发票入口开放标识
	 */
	public String getSupport_fapiao() {
		return support_fapiao;
	}
	/**
	 * 电子发票入口开放标识
	 @param support_fapiao 电子发票入口开放标识
	 */
	public void setSupport_fapiao(String support_fapiao) {
		this.support_fapiao = support_fapiao;
	}
	/**
	 * 订单金额 [必填]
	 @return 订单金额 [必填]
	 */
	public Amount getAmount() {
		return amount;
	}
	/**
	 * 订单金额 [必填]
	 @param amount 订单金额信息对象 [必填]
	 */
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
	/**
	 * 订单金额 [必填]
	 @param amount 订单金额,单位为分 [必填]
	 */
	public void setAmount(int amount) {
		this.amount = this.new Amount(amount);
	}
	/**
	 * 支付者信息,仅限JSAPI,必填
	 @return 支付者信息,仅限JSAPI,必填
	 */
	public Payer getPayer() {
		return payer;
	}
	/**
	 * 支付者信息,仅限JSAPI,必填
	 @param payer 支付者信息,仅限JSAPI,必填
	 */
	public void setPayer(Payer payer) {
		this.payer = payer;
	}
	/**
	 * 支付者信息,仅限JSAPI,必填
	 @param openid 用户的openID,仅限JSAPI,必填
	 */
	public void setPayer(String openid) {
		this.payer = this.new Payer(openid);
	}
	/**
	 * 支付者信息,仅限JSAPI,必填<br>
	 * 同setPayer(String openid)
	 @param openid 用户的openID,仅限JSAPI,必填
	 */
	public void setOpenID(String openid) {
		this.payer = this.new Payer(openid);
	}
	/**
	 * 优惠功能
	 @return 优惠功能
	 */
	public Payer getDetail() {
		return detail;
	}
	/**
	 * 优惠功能
	 @param detail 优惠功能
	 */
	public void setDetail(Payer detail) {
		this.detail = detail;
	}
	/**
	 * 支付场景描述
	 @return 支付场景描述
	 */
	public SceneInfo getScene_info() {
		return scene_info;
	}
	/**
	 * 支付场景描述
	 @param scene_info 支付场景描述
	 */
	public void setScene_info(SceneInfo scene_info) {
		this.scene_info = scene_info;
	}
	/**
	 * 结算信息
	 @return 结算信息
	 */
	public SettleInfo getSettle_info() {
		return settle_info;
	}
	/**
	 * 结算信息
	 @param settle_info 结算信息
	 */
	public void setSettle_info(SettleInfo settle_info) {
		this.settle_info = settle_info;
	}
	/**
	 * 订单金额信息对象
	 *
	 */
	public class Amount{
		/**
		 * 总金额,订单总金额，单位为分。[必填]
		 */
		private int total;
		/**
		 * 货币类型,CNY：人民币，境内商户号仅支持人民币。该参数无需填写 
		 */
		private String currency = "CNY";
		/**
		 * 
		 * 总金额,订单总金额，单位为分。
		 @return 总金额
		 */
		public int getTotal() {
			return total;
		}
		/**
		 * 总金额,订单总金额，单位为分。
		 @param total 总金额,订单总金额，单位为分。
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
		public Amount() {
			super();
			// TODO Auto-generated constructor stub
		}
		public Amount(int total) {
			super();
			this.total = total;
		}
	}
	/**
	 * 支付者信息
	 * @author cy
	 *
	 */
	public class Payer{
		/**
		 * 用户的Openid
		 */
		private String openid;
		/**
		 * 用户的Openid
		 @return 用户的Openid
		 */
		public String getOpenid() {
			return openid;
		}
		/**
		 * 用户的Openid
		 @param openid 用户的Openid
		 */
		public void setOpenid(String openid) {
			this.openid = openid;
		}
		public Payer() {
			super();
			// TODO Auto-generated constructor stub
		}
		public Payer(String openid) {
			super();
			this.openid = openid;
		}
	}
	/**
	 * 优惠功能
	 *
	 */
	public class Detail{
		/**
		 * 订单原价
		 */
		private int cost_price;
		/**
		 * 商品小票ID 
		 */
		private String invoice_id;
		/**
		 * 单品列表信息
		 */
		private List<GoodsDetail> goods_detail;
		/**
		 * 订单原价
		 @return 订单原价
		 */
		public int getCost_price() {
			return cost_price;
		}
		/**
		 * 订单原价
		 @param cost_price 订单原价
		 */
		public void setCost_price(int cost_price) {
			this.cost_price = cost_price;
		}
		/**
		 * 商品小票ID 
		 @return 商品小票ID 
		 */
		public String getInvoice_id() {
			return invoice_id;
		}
		/**
		 * 商品小票ID 
		 @param invoice_id 商品小票ID 
		 */
		public void setInvoice_id(String invoice_id) {
			this.invoice_id = invoice_id;
		}
		/**
		 * 单品列表信息
		 @return 单品列表信息
		 */
		public List<GoodsDetail> getGoods_detail() {
			return goods_detail;
		}
		/**
		 * 单品列表信息
		 @param goods_detail 单品列表信息
		 */
		public void setGoods_detail(List<GoodsDetail> goods_detail) {
			this.goods_detail = goods_detail;
		}
	}
	/**
	 * 单品列表信息
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
		 * 商品数量
		 */
		private int quantity;
		/**
		 * 商品单价 
		 */
		private int unit_price;
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
		 * 商品数量
		 @return 商品数量
		 */
		public int getQuantity() {
			return quantity;
		}
		/**
		 * 商品数量
		 @param quantity 商品数量
		 */
		public void setQuantity(int quantity) {
			this.quantity = quantity;
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
		
	}
	/**
	 * 支付场景描述
	 *
	 */
	public class SceneInfo{
		/**
		 * 用户终端IP 
		 */
		private String payer_client_ip;
		/**
		 * 商户端设备号
		 */
		private String device_id;
		/**
		 * 商户门店信息 
		 */
		private StoreInfo store_info;
		/**
		 * H5场景信息
		 */
		private h5Info h5_info;
		/**
		 * 用户终端IP 
		 @return 用户终端IP 
		 */
		public String getPayer_client_ip() {
			return payer_client_ip;
		}
		/**
		 * 用户终端IP 
		 @param payer_client_ip 用户终端IP 
		 */
		public void setPayer_client_ip(String payer_client_ip) {
			this.payer_client_ip = payer_client_ip;
		}
		/**
		 * 商户端设备号
		 @return 商户端设备号
		 */
		public String getDevice_id() {
			return device_id;
		}
		/**
		 * 商户端设备号
		 @param device_id 商户端设备号
		 */
		public void setDevice_id(String device_id) {
			this.device_id = device_id;
		}
		/**
		 * 商户门店信息 
		 @return 商户门店对象信息
		 */
		public StoreInfo getStore_info() {
			return store_info;
		}
		/**
		 * 商户门店信息 
		 @param store_info 商户门店对象信息 
		 */
		public void setStore_info(StoreInfo store_info) {
			this.store_info = store_info;
		}
		/**
		 * H5场景信息
		 @return H5场景信息
		 */
		public h5Info getH5_info() {
			return h5_info;
		}
		/**
		 * H5场景信息
		 @param h5_info H5场景信息
		 */
		public void setH5_info(h5Info h5_info) {
			this.h5_info = h5_info;
		}
	}
	/**
	 * 商户门店对象 
	 *
	 */
	public class StoreInfo{
		/**
		 * 门店编号
		 */
		private String id;
		/**
		 * 门店名称
		 */
		private String name;
		/**
		 * 地区编码
		 */
		private String area_code;
		/**
		 * 详细地址
		 */
		private String address;
		/**
		 * 门店编号
		 @return 门店编号
		 */
		public String getId() {
			return id;
		}
		/**
		 * 门店编号
		 @param id 门店编号
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * 门店名称
		 @return 门店名称
		 */
		public String getName() {
			return name;
		}
		/**
		 * 门店名称
		 @param name 门店名称
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * 地区编码
		 @return 地区编码
		 */
		public String getArea_code() {
			return area_code;
		}
		/**
		 * 地区编码
		 @param area_code 地区编码
		 */
		public void setArea_code(String area_code) {
			this.area_code = area_code;
		}
		/**
		 * 详细地址
		 @return 详细地址
		 */
		public String getAddress() {
			return address;
		}
		/**
		 * 详细地址
		 @param address 详细地址
		 */
		public void setAddress(String address) {
			this.address = address;
		}
	}
	/**
	 * H5场景信息
	 *
	 */
	public class h5Info {
		/**
		 * 场景类型,[必填],示例值：iOS, Android, Wap 
		 */
		private String type;
		/**
		 * 应用名称
		 */
		private String app_name;
		/**
		 * 网站URL 
		 */
		private String app_url;
		/**
		 * iOS平台BundleID 
		 */
		private String bundle_id;
		/**
		 * Android平台PackageName 
		 */
		private String package_name;
		/**
		 * 场景类型,[必填],示例值：iOS, Android, Wap
		 @return 场景类型,[必填],示例值：iOS, Android, Wap
		 */
		public String getType() {
			return type;
		}
		/**
		 * 场景类型,[必填],示例值：iOS, Android, Wap
		 @param type 场景类型,[必填],示例值：iOS, Android, Wap
		 */
		public void setType(String type) {
			this.type = type;
		}
		/**
		 * 应用名称
		 @return 应用名称
		 */
		public String getApp_name() {
			return app_name;
		}
		/**
		 * 应用名称
		 @param app_name 应用名称
		 */
		public void setApp_name(String app_name) {
			this.app_name = app_name;
		}
		/**
		 * 网站URL
		 @return 网站URL
		 */
		public String getApp_url() {
			return app_url;
		}
		/**
		 * 网站URL
		 @param app_url 网站URL
		 */
		public void setApp_url(String app_url) {
			this.app_url = app_url;
		}
		/**
		 * iOS平台BundleID
		 @return iOS平台BundleID
		 */
		public String getBundle_id() {
			return bundle_id;
		}
		/**
		 * iOS平台BundleID
		 @param bundle_id iOS平台BundleID
		 */
		public void setBundle_id(String bundle_id) {
			this.bundle_id = bundle_id;
		}
		/**
		 * Android平台PackageName
		 @return Android平台PackageName
		 */
		public String getPackage_name() {
			return package_name;
		}
		/**
		 * Android平台PackageName
		 @param package_name Android平台PackageName
		 */
		public void setPackage_name(String package_name) {
			this.package_name = package_name;
		}
	}
	/**
	 * 结算信息
	 *
	 */
	public class SettleInfo{
		/**
		 * 是否指定分账
		 */
		private boolean profit_sharing = false;
		/**
		 * 是否指定分账
		 @return 是否指定分账,默认false
		 */
		public boolean isProfit_sharing() {
			return profit_sharing;
		}
		/**
		 * 是否指定分账
		 @param profit_sharing 是否指定分账
		 */
		public void setProfit_sharing(boolean profit_sharing) {
			this.profit_sharing = profit_sharing;
		}
		
	}
	/**
	 * 获取当前对象的ByteArrayOutputStream
	 @param data 当前WechatPlaceOrderParam对象实例
	 @return ByteArrayOutputStream
	 */
	public ByteArrayOutputStream getByteArrayOutputStream(WechatPlaceOrderParam data) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Jackson.toOutputStream(bos, data, true, PropertyNamingStrategies.SNAKE_CASE);
       return bos;
	}
	
	@Override
	public String toString() {
		return Jackson.entityToString(this);
	}
	
}

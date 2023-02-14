package net.hlinfo.pbp.pay.opt.apple;

import io.swagger.annotations.ApiModelProperty;
import net.hlinfo.opt.Jackson;
/**
 * ios应用内支付校验参数
 *
 */
public class IosIapParam {
	@ApiModelProperty("用户id")
	private String memId;
	
	@ApiModelProperty("交易ID")
	private String transactionIdentifier;
	
	@ApiModelProperty("交易凭证")
	private String transactionReceipt;
	
	@ApiModelProperty("交易时间")
	private String transactionDate;
	
	@ApiModelProperty("订单号")
	private String orderNo;
	
	@ApiModelProperty("附加信息，可用于传递其他参数")
	private String extra;
	
	/**
	 *  Getter method for property <b>memId</b>.
	 * @return property value of memId
	 */
	public String getMemId() {
		return memId;
	}

	/**
	 * Setter method for property <b>memId</b>.
	 *
	 * @param memId value to be assigned to property memId
	 */
	public void setMemId(String memId) {
		this.memId = memId;
	}

	/**
	 *  Getter method for property <b>transactionIdentifier</b>.
	 * @return property value of transactionIdentifier
	 */
	public String getTransactionIdentifier() {
		return transactionIdentifier;
	}

	/**
	 * Setter method for property <b>transactionIdentifier</b>.
	 *
	 * @param transactionIdentifier value to be assigned to property transactionIdentifier
	 */
	public void setTransactionIdentifier(String transactionIdentifier) {
		this.transactionIdentifier = transactionIdentifier;
	}

	/**
	 *  Getter method for property <b>transactionReceipt</b>.
	 * @return property value of transactionReceipt
	 */
	public String getTransactionReceipt() {
		return transactionReceipt;
	}

	/**
	 * Setter method for property <b>transactionReceipt</b>.
	 *
	 * @param transactionReceipt value to be assigned to property transactionReceipt
	 */
	public void setTransactionReceipt(String transactionReceipt) {
		this.transactionReceipt = transactionReceipt;
	}

	/**
	 *  Getter method for property <b>transactionDate</b>.
	 * @return property value of transactionDate
	 */
	public String getTransactionDate() {
		return transactionDate;
	}

	/**
	 * Setter method for property <b>transactionDate</b>.
	 *
	 * @param transactionDate value to be assigned to property transactionDate
	 */
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 *  Getter method for property <b>orderNo</b>.
	 * @return property value of orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * Setter method for property <b>orderNo</b>.
	 *
	 * @param orderNo value to be assigned to property orderNo
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 *  Getter method for property <b>extra</b>.
	 * @return property value of extra
	 */
	public String getExtra() {
		return extra;
	}

	/**
	 * Setter method for property <b>extra</b>.
	 *
	 * @param extra value to be assigned to property extra
	 */
	public void setExtra(String extra) {
		this.extra = extra;
	}

	@Override
	public String toString() {
		return Jackson.entityToString(this);
	}
	
}

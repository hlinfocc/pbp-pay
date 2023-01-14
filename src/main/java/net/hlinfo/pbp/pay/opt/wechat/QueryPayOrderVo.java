package net.hlinfo.pbp.pay.opt.wechat;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class QueryPayOrderVo {
	@ApiModelProperty("商户订单号")
	private String outTradeNo;
	
	@ApiModelProperty("交易类型")
	private String tradeType;
	
	@ApiModelProperty("交易状态")
	private String tradeState;
	
	@ApiModelProperty("交易状态描述")
	private String tradeStateDesc;
	
	@ApiModelProperty("支付完成时间 ")
	private String successTime;
	
	@ApiModelProperty("订单金额")
	private BigDecimal amount ;

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeState() {
		return tradeState;
	}

	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}

	public String getTradeStateDesc() {
		return tradeStateDesc;
	}

	public void setTradeStateDesc(String tradeStateDesc) {
		this.tradeStateDesc = tradeStateDesc;
	}

	public String getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(String successTime) {
		this.successTime = successTime;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}

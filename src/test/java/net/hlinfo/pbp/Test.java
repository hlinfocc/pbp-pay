package net.hlinfo.pbp;

import java.io.ByteArrayOutputStream;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.hlinfo.opt.Func;
import net.hlinfo.opt.Jackson;
import net.hlinfo.pbp.pay.opt.wechat.WechatPlaceOrderParam;

public class Test {
	public static void main(String[] args) {
		WechatPlaceOrderParam w = new WechatPlaceOrderParam();
		w.setAppid("1111");
		w.setMchid("wzxa2323444");
		w.setDescription("ok");
		w.setOutTradeNo(Func.UUID());
		w.setAmount(128);
		w.setOpenID("wxasddff");
		ObjectNode json = Jackson.objectNode();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Jackson.toOutputStream(bos, w, true, PropertyNamingStrategies.SNAKE_CASE);
		System.out.println(bos);
	}
}

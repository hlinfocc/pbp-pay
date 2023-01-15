package net.hlinfo.pbp;

import java.io.ByteArrayOutputStream;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.hlinfo.opt.Func;
import net.hlinfo.opt.Jackson;
import net.hlinfo.pbp.pay.etc.WechatPayConfig;
import net.hlinfo.pbp.pay.opt.wechat.WechatPlaceOrderParam;

public class Test {
	
	public static void main(String[] args) {
		try {
			WechatPlaceOrderParam w = new WechatPlaceOrderParam();
			w.setAppid("111");
			w.setMchid("2222");
			w.setDescription("商品描述");
			w.setOutTradeNo("98125415452214522");//订单号
			w.setAmount(99);//订单金额，单位分
			w.setOpenID("wx8df5hjf8f8sd7x7sdx6sd");
			
			ObjectNode json = Jackson.objectNode();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Jackson.toOutputStream(bos, w, true, PropertyNamingStrategies.SNAKE_CASE);
			System.out.println(bos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

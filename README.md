[![GitHub release](https://img.shields.io/github/v/tag/hlinfocc/pbp-pay.svg?label=%E6%9C%80%E6%96%B0%E7%89%88%E6%9C%AC)](https://github.com/hlinfocc/pbp-pay/releases)
[![MIT License](https://img.shields.io/github/license/hlinfocc/pbp-pay)](https://github.com/hlinfocc/pbp-pay/blob/master/LICENSE)

# 简介

pbp-pay 是基于Spring Boot，集成支付宝支付、微信支付、iOS应用内购支付校验的工具，轻量级、开箱即用、快速使用。

# 特点

轻量级、开箱即用、开源免费

# 快速使用

### 1.引入pom:

>请前往Maven中央库查找最新版本

```xml
<dependency>
  <groupId>net.hlinfo</groupId>
  <artifactId>pbp-pay</artifactId>
  <version>1.0.1</version>
</dependency>
```

### 2在启动类配置扫描本程序包名

在启动类加上如下注解：

```java
@EnableHlinfoPBP
```

加此注解目的在于自动扫描net.hlinfo.pbp包，注入相关@Bean

示例：

```java
package net.hlinfo.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.hlinfo.pbp.pay.opt.EnableHlinfoPBP;

@SpringBootApplication
@EnableHlinfoPBP
public class SpringbootExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootExampleApplication.class, args);
	}

}

```


### 3.微信支付使用

#### 3.1配置

在application.yml中加入以下配置信息，配置值请自行修改为自己的；参数说明：

* merch-id:微信支付商户号
* merch-serial-number：证书序列号，微信支付平台创建证书后可看到
* merch-private-key-path：证书路径
* api-v3-key：apiV3秘钥
* app-id：公众号或小程序appId
* app-secret：公众号或小程序app秘钥
* redirect-uri：应用回调地址，用于支付后通知支付结果，如果这里只配置域名部分，则需要在下单参数类的notify_url属性组装完整url,若这里配置完整url则notify_url属性可为空，（也可以直接在WechatPlaceOrderParam下单参数类的notify_url属性设置）

```yml
wechat: 
  pay:
    merch-id: 14807012454
    merch-serial-number: 11F95FF7B29BE4B5D5B156978987876
    merch-private-key-path: /path/14807012454/apiclient_key.pem
    api-v3-key: 1768e8cc940611edb4ea7ba48587c74f
    app-id: wx2f88cd53200b098567
    app-secret: 8014c2e2940611edaade3be48d6795c8
    redirect-uri: https://api.test.cn/pay/wechat/back
```

#### 3.2在Controller或者service中使用

```java

package net.hlinfo.example.controller;

//省略其他import
import net.hlinfo.pbp.pay.service.WechatPayService;
import net.hlinfo.pbp.pay.exception.PayException;

@RequestMapping("/")
@RestController
public class IndexController {
	@Autowired
	private WechatPayService wechatPayService;
	
	@GetMapping("/jsapiOrder")
	public Resp<WechatJSApiPayParam> jsapiOrder(){
		try {
			WechatPlaceOrderParam param = new WechatPlaceOrderParam();
			param.setDescription("商品描述");
			param.setOutTradeNo("98125415452214522");//订单号
			param.setAmount(99);//订单金额，单位分
			param.setOpenID("wx8df5hjf8f8sd7x7sdx6sd");//用户的openID
			//调用JSAPI下单
			WechatJSApiPayParam result =  wechatPayService.placeOrderJSAPI(param);
			System.out.println(result);
			return new Resp(result);
		} catch (PayException e) {
			// 异常处理
			e.printStackTrace();
		}
		return null;
	}
	
}

```

对于本工具未集成实现的接口，可以通过公共wechatPayService.httpPost请求方法和公共wechatPayService.httpGet请求方法来实现

### 4.支付宝支付使用

#### 4.1配置

在application.yml中加入配置信息

秘钥模式：

```yml
alipay:
  app-id: 2023002185655063
  private-key: "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAb0BBLvx30uWcX180skn839b99L4Ig=="
  alipay-public-key: "MIIBIjANBgkqhkiG9w0wFf4KYcw2AqxufQIDAQAB"
```

说明：
* app-id位支付宝应用ID
* private-key位私钥，也就是通过[支付宝开放平台密钥工具](https://opendocs.alipay.com/common/02kipk "支付宝开放平台密钥工具")生成的秘钥的应用私钥
* alipay-public-key支付宝公钥，将通过[支付宝开放平台密钥工具](https://opendocs.alipay.com/common/02kipk "支付宝开放平台密钥工具")生成的秘钥的应用公钥上传到支付宝开放平台获取
* encrypt-type：接口内容加密方式，默认为AES，可以不设置
* encrypt-key：敏感信息对称加密算法密钥，支付宝开放平台设置接口内容加密方式后可获取,若不设置，则接口内容不会被加密


证书模式：

```yml
alipay:
  app-id: 2023002185655063
  api-mode: cert
  app-private-key: "MIIEvwIBADANBgkqhkiG9w0BAQSDHDGDSHlAgEAAb0BBLvx30uWcX180skn839b99L4IgKJGH=="
  app-cert-path: /path/alipay/appCertPublicKey.crt
  alipay-public-cert-path: /path/alipay/alipayCertPublicKey.crt
  root-cert-path: /path/alipay/alipayRootCert.crt
```

说明：
* app-id：位支付宝应用ID
* api-mode：接口加签方式,可选项:publickey[密钥（普遍适用）],cert[证书（若使用“现金红包”、“单笔转账到支付宝“产品必选 ）]
* app-private-key：应用私钥，也就是通过[支付宝开放平台密钥工具](https://opendocs.alipay.com/common/02kipk "支付宝开放平台密钥工具")生成的CSR文件目录中的应用私钥
* app-cert-path：应用公钥证书路径，支付宝开放平台获取
* alipay-public-cert-path：支付宝公钥证书路径，支付宝开放平台获取
* root-cert-path：支付宝根证书路径，支付宝开放平台获取
* 其中，app-cert-path、alipay-public-cert-path、root-cert-path也可以换成app-cert-content、alipay-public-cert-content、root-cert-content这样就可以直接填写证书内容字符串
* encrypt-type：接口内容加密方式，默认为AES，可以不设置
* encrypt-key：敏感信息对称加密算法密钥，支付宝开放平台设置接口内容加密方式后可获取,若不设置，则接口内容不会被加密

对于单笔转账到支付宝等接口，因为强制使用证书，所以不受api-mode参数限制，只需要配置好app-private-key、app-cert-path、 alipay-public-cert-path、root-cert-path等证书参数即可

更多参数请查看AlipayAutoConfig类

#### 4.2在Controller或者service中使用

```java

package net.hlinfo.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradePagePayModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.hlinfo.example.utils.Resp;
import net.hlinfo.opt.RedisUtils;
import net.hlinfo.pbp.pay.exception.PayException;
import net.hlinfo.pbp.pay.service.AlipayService;

@Api(tags = "支付测试")
@RestController
@RequestMapping("/paytest")
public class PayTestController extends BaseController {
	
	@Autowired
	private AlipayService alipayService;
	
	@ApiOperation(value="PC电脑端网页下单")
	@PostMapping("/tradePCPay")
	public Resp<String> tradePCPay(){
		try {
			AlipayTradePagePayModel model = new AlipayTradePagePayModel();
			model.setOutTradeNo("20230202100101001");
			model.setTotalAmount("0.01");
			model.setSubject("P100 1TB");
			model.setProductCode("FAST_INSTANT_TRADE_PAY");
			
			String rs = alipayService.tradePCPay(model);
			System.out.println(rs);
			return new Resp().ok("操作成功", rs);
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (PayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Resp().error("创建订单失败");
	}
	
}

```

本工具仅集成了常用的接口，其他未实现的接口，可以通过alipayService.getAlipayClient方法获取alipayClient实例，自行实现

# 5.Apple账号登录及iOS内购项目支付

使用示例：

```java
package net.hlinfo.example.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.hlinfo.example.utils.Resp;
import net.hlinfo.opt.Func;
import net.hlinfo.opt.Jackson;
import net.hlinfo.pbp.pay.opt.apple.AppleLoginUserInfo;
import net.hlinfo.pbp.pay.opt.apple.AppleLoginUserInfo.UserInfo;
import net.hlinfo.pbp.pay.opt.apple.AppleSignValidateVo;
import net.hlinfo.pbp.pay.opt.apple.IosIapParam;
import net.hlinfo.pbp.pay.service.AppleService;

@Api(tags = "Apple账号登录及iOS内购项目支付测试")
@RestController
@RequestMapping("/apple")
public class AppleController {
	
	@Autowired
	private AppleService apple;
	
	@ApiOperation(value = "苹果内购支付校验")
	@PostMapping(value = "/iosIapPay")
	public Resp<Object> iosIapPay(@RequestBody IosIapParam iosIapParam,HttpServletRequest request) {
		log.debug("苹果内购校验开始，交易校验数据：", iosIapParam);
        //线上环境验证
        String verifyResult = apple.buyAppVerify(iosIapParam.getTransactionReceipt(), 1);
        if (verifyResult == null) {
            return new Resp<Object>(500, "苹果验证失败，返回数据为空", "");
        } else {
        	  log.debug("线上，苹果平台返回JSON:" + verifyResult);
        	  JsonNode appleReturn = Jackson.toJsonObject(verifyResult);
            int states = appleReturn.get("status").asInt();
              //无数据则沙箱环境验证
            if (states == 21007) {
                verifyResult = apple.buyAppVerify(iosIapParam.getTransactionReceipt(), 0);
                log.debug("沙盒环境，苹果平台返回JSON:" + verifyResult);
                appleReturn =  Jackson.toJsonObject(verifyResult);
                states = appleReturn.get("status").asInt();
             }
            log.debug("苹果平台返回值：appleReturn:,{}" + appleReturn);
              // 前端所提供的收据是有效的    验证成功
            if (states == 0) {
            	   List<String> originalTransactionIds = appleReturn.findValuesAsText("original_transaction_id");
                if(originalTransactionIds.contains(iosIapParam.getTransactionIdentifier())) {
                	 try {
                		 	//处理业务逻辑
                		 	//更新会员有效期，写入支付日志等
                		 	/////   此处自行实现相应逻辑 ////
							return new Resp<Object>(200, "支付成功",null);
					  } catch (Exception e) {
						log.error(e.getMessage(),e);
						return new Resp<Object>(500, "服务器异常，请联系客服，错误信息："+e.getMessage(), "");
					  }
                }else {
                	 return new Resp<Object>(500, "交易校验失败，当前交易不在交易列表中", "");
                   }
                   
            } else {
                return new Resp<Object>(500, "支付失败，错误码：" + states, "");
            }
        }
	}
	
	@ApiOperation("苹果账号登陆")
	@PostMapping("/signInApple")
	public Resp<NutMap> signInApple(@Valid @RequestBody AppleLoginUserInfo userData, HttpServletRequest request) {
		log.debug("苹果账号登陆:");
		log.debug("{}",userData);
		try {
			UserInfo userInfo = userData.getUserInfo();
			
			String fullName = "";
			if(userInfo.getFullName()!=null) {
				if(Func.isNotBlank(userInfo.getFullName().getFamilyName())
						&& Func.isNotBlank(userInfo.getFullName().getGiveName())) {
					fullName = userInfo.getFullName().getFamilyName()+userInfo.getFullName().getGiveName();
				}else if(Func.isNotBlank(userInfo.getFullName().getNickName())) {
					fullName = userInfo.getFullName().getNickName();
				}
			}
			
			//授权验证
			AppleSignValidateVo res = apple.verifyIdentifyToken(userInfo.getIdentityToken());
			if(!res.isSuccess()) {
				return new Resp<>().error(res.getMsg());
			}
			if(Func.isNotBlank(res.getRsemail())) {
				if(Func.isBlank(fullName)) {
					fullName =res.getRsemail();
				}
				userInfo.setEmail(res.getRsemail());
			}
			//查库，验证用户信息
			/////   此处自行实现相应逻辑 ////
			return new Resp<>().ok("登录成功",res);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			String errorMsg = Func.isBlank(e.getMessage())?"空指针异常":e.getMessage();
			return new Resp<>().error("登陆失败，错误原因："+errorMsg);
		}
	}
	
}

```

# 6.许可证
MIT License 

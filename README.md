[![GitHub release](https://img.shields.io/github/v/tag/hlinfocc/pbp-pay.svg?label=%E6%9C%80%E6%96%B0%E7%89%88%E6%9C%AC)](https://github.com/hlinfocc/pbp-pay/releases)
[![MIT License](https://img.shields.io/github/license/hlinfocc/pbp-pay)](https://github.com/hlinfocc/pbp-pay/blob/master/LICENSE)

# 简介

pbp-pay 是基于Spring Boot，集成微信支付和支付宝支付的工具，轻量级、、开箱即用、快速使用。

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
@ComponentScans(value= {@ComponentScan(value = {"net.hlinfo.pbp"})})
```

配置扫描包目的在于注入相关配置和@Bean

示例：

```java
@SpringBootApplication
@ComponentScans(value= {@ComponentScan(value = {"net.hlinfo.pbp"})}) //注意这里要配置上，否则该框架不生效
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
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

### 4.支付宝支付使用

#### 4.1配置

在application.yml中加入配置信息

#### 4.2在Controller或者service中使用


# 许可证
MIT License 

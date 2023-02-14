package net.hlinfo.pbp.pay.opt.apple;


import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwk.Jwk;
import com.fasterxml.jackson.databind.JsonNode;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import net.hlinfo.opt.Func;
import net.hlinfo.opt.Jackson;

public class AppleSignValidateUtils {
	protected static Logger log = LoggerFactory.getLogger(AppleSignValidateUtils.class);
	
	/**
	 * 检查用户身份令牌的有效性和完整性。
	 * @param identityToken 用户身份令牌
	 * @return 校验信息对象
	 */
	@SuppressWarnings("unchecked")
	public AppleSignValidateVo verifyIdentifyToken(String identityToken) {
		  AppleSignValidateVo asv = new AppleSignValidateVo();
	      //向苹果后台获取公钥参数
	      String applePublicKeyResp = null;
	      try {
	          String resp = HttpUtil.get("https://appleid.apple.com/auth/keys");
	          if(resp!=null) {
	        	  applePublicKeyResp = resp;
	          }else {
	        	  	log.error("[校验identifyToken]获取Apple的公钥失败,resp.getContent():"+resp);
	        	  	asv.setMsg("获取Apple的公钥失败,resp.getContent():"+resp);
	        	  	asv.setSuccess(false);
		          return asv;
	          	}
	          log.debug(applePublicKeyResp);
	      } catch (Exception e) {
	          log.error("[校验identifyToken]获取apple公钥失败： " + e.getMessage());
	          asv.setMsg("获取apple公钥失败： " + e.getMessage());
	          asv.setSuccess(false);
	          return asv;
	      }
	      JsonNode appleRespJson = Jackson.toJsonObject(applePublicKeyResp);
	       
	      JsonNode keys = appleRespJson.get("keys");
	      
	      if (identityToken.split("\\.").length < 2) {
	          log.error("[校验identifyToken]获取identifyToken失败，identifyToken格式异常");
	          asv.setMsg("获取identifyToken失败，identifyToken格式异常");
	          asv.setSuccess(false);
	          return asv;
	      }
	      if(!keys.isArray()) {
	    	   log.error("[校验identifyToken]解析apple公钥失败");
	    	   asv.setMsg("获取apple公钥失败");
	          asv.setSuccess(false);
	          return asv;
	      }
	      Map<String, Object> useAppleAuth = new HashMap<String, Object>();
	      
	      String inAuth = new String(Base64.decodeStr(identityToken.split("\\.")[0]));
	      
	      String inKid = Jackson.toJsonObject(inAuth).get("kid").asText();
	      
	      for(JsonNode item : keys){
	    	  if(inKid.equals(item.get("kid").asText())){
              useAppleAuth = Jackson.toJavaObject(Jackson.toJSONString(item),Map.class);
              log.debug("checkIdentifyToken-jsonObject1:"+useAppleAuth);
              break;
	          }
	      }

	      //通过jwks-rsa 包生成publicKey
	      PublicKey publicKey =null;
	      try{
	          Jwk jwa = Jwk.fromValues(useAppleAuth);
	          publicKey = jwa.getPublicKey();
	      } catch (Exception e) {
	          log.error("[校验identifyToken]生成公钥失败: " + e.getMessage());
	          asv.setMsg("生成公钥失败: " + e.getMessage());
	          asv.setSuccess(false);
	          return asv;
	      }

	      //  分割前台传过来的identifyToken（jwt格式的token）用base64解码使用
	      String aud = "";
	      String sub = "";
	      try {
	          String claim = new String(Base64.decodeStr(identityToken.split("\\.")[1]));
	          log.debug("checkIdentifyToken-claim:{}", claim);
	          JsonNode claimObj = Jackson.toJsonObject(claim);
	          aud = claimObj.get("aud")==null?"":claimObj.get("aud").asText();
	          sub = claimObj.get("sub")==null?"":claimObj.get("sub").asText();
	          
	      } catch (Exception e) {
	          log.error("[校验identifyToken]Token解码失败：" + e.getMessage());
	          asv.setMsg("Token解码失败：" + e.getMessage());
	          asv.setSuccess(false);
	          return asv;
	      }
	      return this.verify(publicKey, identityToken, aud, sub);
	  }
	/**
	 * JWT验证
	 * @param key 公钥
	 * @param jwt 客户端SDK登陆返回的IdentityToken
	 * @param audience 苹果应用AppId
	 * @param subject 苹果应用OpenId
	 * @return 校验信息对象
	 */
	  private AppleSignValidateVo verify(PublicKey key, String jwt, String audience, String subject) {
		  AppleSignValidateVo asv = new AppleSignValidateVo();
		  JwtParser jwtParser = Jwts.parser().setSigningKey(key);
	      jwtParser.requireIssuer("https://appleid.apple.com");
	      jwtParser.requireAudience(audience);
	      jwtParser.requireSubject(subject);
	      try {
	          log.debug("[校验identifyToken]验证开始");
	          Jws<Claims> claim = jwtParser.parseClaimsJws(jwt);
	          log.debug("[校验identifyToken]-apple-verify-claim:{}",Jackson.toJSONString(claim));
	          if (claim != null && claim.getBody().containsKey("auth_time")) {
	        	    JsonNode claimBody = Jackson.toJsonObject(claim.getBody());
	        	    asv.setRsemail(claimBody.get("email").asText());
	        	    asv.setRsappid(claimBody.get("aud").asText());
	        	    asv.setRsopenid(claimBody.get("sub").asText());
	              String exp = claimBody.get("exp").asText();
	              long nowTime = System.currentTimeMillis()/1000;
	              long expTime = 	Func.string2Long(exp);
	              if(nowTime > expTime) {
	            	   log.error("[校验identifyToken]令牌已过期: ");
	            	   asv.setMsg("令牌已过期: " );
	            	   asv.setSuccess(false);
	    	          return asv;
	              	 }
	              if(!Func.equals("com.jiudingcheng.ios.policy", asv.getRsappid())) {
	            	  log.error("[校验identifyToken]校验失败，解析Token中的参数不合法 ");
	            	  	asv.setMsg("校验失败，解析Token中的参数不合法 " );
	    	          asv.setSuccess(false);
	    	          return asv;
	                 }
	              asv.setSuccess(true);
    	          return asv;
	          }else {
	        	   log.error("[校验identifyToken]验证失败，identifyToken中没有auth_time等参数 ");
	        	   asv.setMsg("验证失败，identifyToken中没有auth_time等参数");
		          asv.setSuccess(false);
    	          return asv;
	            }
	      } catch (ExpiredJwtException e) {
	          log.error("[校验identifyToken]令牌已过期: " + e.getMessage());
	          asv.setMsg("令牌已过期: " + e.getMessage());
	          asv.setSuccess(false);
	          return asv;
	      } catch (Exception e) {
	          log.error("[校验identifyToken]令牌非法: " + e.getMessage());
	          asv.setMsg("令牌非法: " + e.getMessage());
	          asv.setSuccess(false);
	          return asv;
	      }
	  }
	
}

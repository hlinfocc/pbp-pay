package net.hlinfo.pbp.pay.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.hlinfo.pbp.pay.opt.apple.AppleSignValidateUtils;
import net.hlinfo.pbp.pay.opt.apple.AppleSignValidateVo;



/**
 * ios应用内支付服务器校验及检查用户身份令牌的有效性和完整性。
 * @author cy
 *
 */
@Service
public class AppleService {
	protected static final Logger log = LoggerFactory.getLogger(AppleService.class);
	/**
	 * 沙盒校验地址
	 */
	private static final String url_sandbox = "https://sandbox.itunes.apple.com/verifyReceipt";
	/**
	 * 线上校验地址
	 */
	private static final String url_verify = "https://buy.itunes.apple.com/verifyReceipt";
	
	private static class TrustAnyTrustManager implements X509TrustManager {
       public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    /**
     * ios应用内支付服务器校验
     *
     * @param receipt
     *            账单
     * @param type 类型：0沙盒环境，1线上环境
     * @return null 或返回结果
     *
     */
    public static String buyAppVerify(String receipt,int type) {
        //环境判断 线上/开发环境用不同的请求链接
        String url = "";
        if(type==0){
            url = url_sandbox; //沙盒校验地址
        }else{
            url = url_verify; //线上校验地址
        }

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
            URL console = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "text/json");
            conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            BufferedOutputStream hurlBufOus = new BufferedOutputStream(conn.getOutputStream());

            String str = String.format(Locale.CHINA, "{\"receipt-data\":\"" + receipt + "\"}");//拼成固定的格式传给平台
            hurlBufOus.write(str.getBytes());
            hurlBufOus.flush();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (Exception ex) {
            log.error("苹果服务器异常",ex);
        }
        return null;
    }
    
    /**
     * 用BASE64加密
     *
     * @param str
     * @return
     */
    public static String getBASE64(String str) {
        byte[] b = str.getBytes();
        String s = null;
        if (b != null) {
            s = new sun.misc.BASE64Encoder().encode(b);
        }
        return s;
    }
    /**
	 * 检查用户身份令牌的有效性和完整性。
	 * @param identityToken 用户身份令牌
	 * @return
	 */
    public AppleSignValidateVo verifyIdentifyToken(String identityToken) {
    	AppleSignValidateUtils appleSignValidate= new AppleSignValidateUtils();
    	return appleSignValidate.verifyIdentifyToken(identityToken);
    }
}

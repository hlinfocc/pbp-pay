package net.hlinfo.pbp.pay.opt;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

import net.hlinfo.opt.Func;
import net.hlinfo.opt.Jackson;

public class PayUtils {
	public static final Logger log = LoggerFactory.getLogger(PayUtils.class);
	/**
     * 获取请求body数据
     * @param request
     * @return 请求body数据
     * @throws IOException
     */
    public static String getRequestBody(HttpServletRequest request) throws Exception {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            ServletInputStream stream = request.getInputStream();
            // 获取响应
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new Exception("读取返回支付接口数据流出现异常！");
        } finally {
            reader.close();
        }
        return sb.toString();
    }
    
    /**
	 * 获取对象的ByteArrayOutputStream
	 @param data 序列化参数对象实例
	 @return ByteArrayOutputStream
	 */
	public static ByteArrayOutputStream getByteArrayOutputStream(Serializable data) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Jackson.toOutputStream(bos, data, true, PropertyNamingStrategies.SNAKE_CASE);
       return bos;
	}
	/**
	 * 解析form表单的action地址
	 @param s form表单字符串
	 @return form表单的action地址
	 */
	public static String parseFormAction(CharSequence s) {
		if (null == s || s.length() == 0) {return null;}
		Pattern pattern = Pattern.compile("<form.+?action=['\"]([^'\"]+)[^>]*>");
		Matcher matcher = pattern.matcher(s);
		
		while(matcher.find()) {
			if(Func.isBlank(matcher.group(1))) {
				return null;
			}
			return matcher.group(1);
		}
		return null;
	}
}

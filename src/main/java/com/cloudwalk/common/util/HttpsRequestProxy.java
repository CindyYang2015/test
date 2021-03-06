package com.cloudwalk.common.util;

/**
 * https请求工具类
 * @author zhuyf
 *
 */
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.cloudwalk.common.common.Constants;

public class HttpsRequestProxy {
	
	private static Logger logger = Logger.getLogger(HttpsRequestProxy.class);
	
	/**
     * 连接超时
     */
	private static int CONNECT_TIME_OUT = 2000;
	
	/**
     * 读取数据超时
     */
	private static int READ_TIME_OUT = 5000;
	
    /**
     * 获得KeyStore.
     * @param keyStorePath
     *            密钥库路径
     * @param password
     *            密码
     * @return 密钥库
     * @throws Exception
     */
    public static KeyStore getKeyStore(String password, String keyStorePath)
            throws Exception {
        // 实例化密钥库
        KeyStore ks = KeyStore.getInstance("JKS");
        // 获得密钥库文件流
        FileInputStream is = new FileInputStream(keyStorePath);
        // 加载密钥库
        ks.load(is, password.toCharArray());
        // 关闭密钥库文件流
        is.close();
        return ks;
    }

    /**
     * 获得SSLSocketFactory.
     * @param password
     *            密码
     * @param keyStorePath
     *            密钥库路径
     * @param trustStorePath
     *            信任库路径
     * @return SSLSocketFactory
     * @throws Exception
     */
    public static SSLContext getSSLContext(String password,
            String keyStorePath, String trustStorePath) throws Exception {
        // 实例化密钥库
        KeyManagerFactory keyManagerFactory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        // 获得密钥库
        KeyStore keyStore = getKeyStore(password, keyStorePath);
        // 初始化密钥工厂
        keyManagerFactory.init(keyStore, password.toCharArray());

        // 实例化信任库
        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        // 获得信任库
        KeyStore trustStore = getKeyStore(password, trustStorePath);
        // 初始化信任库
        trustManagerFactory.init(trustStore);
        // 实例化SSL上下文
        SSLContext ctx = SSLContext.getInstance("TLS");
        // 初始化SSL上下文
        ctx.init(keyManagerFactory.getKeyManagers(),
                trustManagerFactory.getTrustManagers(), null);
        // 获得SSLSocketFactory
        return ctx;
    }

    /**
     * 初始化HttpsURLConnection.
     * @param password
     *            密码
     * @param keyStorePath
     *            密钥库路径
     * @param trustStorePath
     *            信任库路径
     * @throws Exception
     */
    public static void initHttpsURLConnection() throws Exception {
        // 声明SSL上下文
        SSLContext sslContext = null;
        // 实例化主机名验证接口
        HostnameVerifier hnv = new MyHostnameVerifier();
        try {
            sslContext = getSSLContext(Constants.Config.HTTPS_PASSWORD, Constants.Config.HTTPS_KEYSTOREPATH, Constants.Config.HTTPS_TRUSTSTOREPATH);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        if (sslContext != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
                    .getSocketFactory());
        }
        HttpsURLConnection.setDefaultHostnameVerifier(hnv);
    }

    /**
	 * 将参数组装成字符串
	 * @param parameters
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private static String buildQueryStr(Map<String, String> paramMap, String encoding) 
			throws UnsupportedEncodingException {
		StringBuilder params = new StringBuilder("");
		
		if (paramMap != null && !paramMap.isEmpty()) {
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				if (StringUtils.hasText(params)) {
					params.append("&");
				}
				
				String val = URLEncoder.encode(entry.getValue(), encoding);
				params.append(entry.getKey()).append("=").append(val);
			}
		}
		
		return params.toString();
	}
	
	/**
	 * 将参数组装成字符串
	 * @param paramMap
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] buildParamter(Map<String, String> paramMap, String encoding) 
			throws UnsupportedEncodingException {
		return buildQueryStr(paramMap, encoding).getBytes(encoding);
	}
	
	/**
     * <pre>
     * 发送带参数的POST的HTTP请求
     * </pre>
     * 
     * @param reqUrl HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    public static String post(String reqUrl, Map<String, String> parameters, String encoding) {
    	
    	HttpsURLConnection conn = null;
		ByteArrayOutputStream bs = null;
		OutputStream os = null;
		InputStream is = null;

		byte[] bytes = new byte[1024];
    	try {   
            conn = (HttpsURLConnection) new URL(reqUrl).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
               
            byte[] b = buildParamter(parameters, encoding);
            if (b != null) {
            	os = conn.getOutputStream();
            	os.write(b, 0, b.length);
            	os.flush();
            }
            
            is = conn.getInputStream();
			bs = new ByteArrayOutputStream();
			
			int len = 0;
			while ((len = is.read(bytes)) != -1) {
				bs.write(bytes, 0, len);
			}
			
			return new String(bs.toByteArray(), encoding);
        }  catch (IOException e) {   
        	logger.error("发送POST请求异常，原因：" + e.getMessage(), e);
        } finally {
        	if (bs != null) {
				try {
					bs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
        	if (os != null) {
        		try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	
        	if (conn != null) {
        		conn.disconnect();
        	}
        }
    	
    	return null;
    }
	
	/**
	 * 发送带参数的GET的HTTP请求
	 * 
	 * @param reqUrl
	 * @param parameters
	 * @param recvEncoding
	 * @return
	 */
    public static byte[] getData(String url, Map<String, String> paramMap, String encoding) {
        
    	HttpsURLConnection conn = null;
    	InputStream is = null;
		ByteArrayOutputStream bs = null;

		byte[] bytes = new byte[1024];
        try {
        	
        	if (paramMap != null && !paramMap.isEmpty()) {
				if (url.indexOf('?') == -1) {
					url += "?" + buildQueryStr(paramMap, encoding);
				} else {
					url += "&" + buildQueryStr(paramMap, encoding);
				}
			}
        	
        	conn = (HttpsURLConnection) new URL(url).openConnection();
        	conn.setRequestMethod("GET");
        	conn.setDoInput(true);
        	conn.setDoOutput(true);
        	conn.setUseCaches(false);
        	conn.setConnectTimeout(CONNECT_TIME_OUT);
        	conn.setReadTimeout(READ_TIME_OUT);
        	
        	is = conn.getInputStream();
			bs = new ByteArrayOutputStream();
			
			int len = 0;
			while ((len = is.read(bytes)) != -1) {
				bs.write(bytes, 0, len);
			}
			
			return bs.toByteArray();
        } catch (IOException e) {
            logger.error("发送GET请求异常，原因：" + e.getMessage(), e);
        } finally {
        	
        	if (bs != null) {
        		try {
					bs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	
        	if (is != null) {
        		try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	
            if (conn != null) {
            	conn.disconnect();
            }
        }
        
        return null;
    }
    
    /**
	 * 发送带参数的GET的HTTP请求
	 * 
	 * @param reqUrl
	 * @param parameters
	 * @param recvEncoding
	 * @return
	 */
    public static String get(String url, Map<String, String> paramMap, String encoding){
    	byte[] reponseData = getData(url,paramMap,encoding);
    	if(reponseData == null) return null;
    	try {
			return new String(reponseData, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return null;
    }

    /**
     * 测试方法.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 密码
//        String password = "123456";
        // 密钥库
//        String keyStorePath = "tomcat.keystore";
        // 信任库
//        String trustStorePath = "tomcat.keystore";
        // 本地起的https服务
//        String httpsUrl = "https://localhost:8443/service/httpsPost";
        // 传输文本
//        String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><fruitShop><fruits><fruit><kind>萝卜</kind></fruit><fruit><kind>菠萝</kind></fruit></fruits></fruitShop>";
//        HttpsRequestProxy.initHttpsURLConnection(password, keyStorePath, trustStorePath);
        // 发起请求
//        HttpsRequestProxy.post(httpsUrl, xmlStr);
    }
}



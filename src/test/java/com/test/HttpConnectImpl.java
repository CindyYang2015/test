package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;
/**
 * java http原生请求封装
 * @author zhuyf
 *
 */
public class HttpConnectImpl {
	
//	private Logger logger = LoggerFactory.getLogger(getClass());
	
//	private String connectTimeout = ;  // 超时时间
	
//	private String readTimeout ;  // 读取超时时间
	
	 /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 {name1:value1;name2:value2...}的格式。
     * @return URL 所代表远程资源的响应结果
     */
    public String sendGet(String url, String param) {
    	HttpURLConnection httpConn = null;
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            httpConn = (HttpURLConnection)realUrl.openConnection();
            // 设置通用的请求属性
//            httpConn.setConnectTimeout(Integer.valueOf(connectTimeout));
//			httpConn.setReadTimeout(Integer.valueOf(readTimeout));
            httpConn.setRequestProperty("accept", "*/*");
            httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            httpConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            httpConn.setRequestProperty("Content-Length", ""+param.getBytes().length);
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset = UTF-8");
            httpConn.setRequestProperty("connection", "Keep-Alive");
            httpConn.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
            // 建立实际的连接
            httpConn.connect();
            // 获取所有响应头字段
//            Map<String, List<String>> map = httpConn.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
            		httpConn.getInputStream(),"UTF-8"));
            String line = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
//            logger.error("发送GET请求出现异常！",e);
        }finally { // 使用finally块来关闭输入流
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
	
    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是{name1:value1;name2:value2...}的格式。
     * @return 所代表远程资源的响应结果
     */
	public String sendPost(String url,String param){
		HttpURLConnection httpConn = null;
		PrintWriter out = null ;
		BufferedReader in = null ;
		String result = "";
		try {
			URL realUrl = new URL(url);
			//打开和URL之间的连接
			httpConn = (HttpURLConnection)realUrl.openConnection();
			httpConn.setRequestMethod("POST");
//			httpConn.setConnectTimeout(Integer.valueOf(connectTimeout));
//			httpConn.setReadTimeout(Integer.valueOf(readTimeout));
			 httpConn.setRequestProperty("accept", "*/*");
            httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            httpConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
//            httpConn.setRequestProperty("Content-Length", ""+param.getBytes().length);
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset = UTF-8");
            httpConn.setRequestProperty("connection", "Keep-Alive");
            httpConn.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
            //发送POST请求必须设置如下两行
            httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			//获取HttpURLConnection对象对应的输出流
			out = new PrintWriter(httpConn.getOutputStream());
			//发送请求参数
			out.print(param);
			//flush输出流的缓冲
			out.flush();
			//定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8")) ;
			String line = "";
			while((line = in.readLine()) != null){
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error("发送 POST 请求出现异常！",e);
		} finally{ //使用finally块来关闭输出流、输入流
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
		return result;
	}
	
	public String sendPost(String url,Map<String,String> paramMap){
		StringBuilder paramString = new StringBuilder();
		if(!paramMap.isEmpty()) {
			Set<String> keys = paramMap.keySet();
			for(String key:keys) {
				if(paramString.length() > 0) {
					paramString.append("&");
				}
				paramString.append(key+"="+paramMap.get(key));
			}
		}
		return this.sendPost(url, paramString.toString());
	}

	public String sendPost(String url, byte[] params) {
		HttpURLConnection httpConn = null;
		OutputStream out = null ;
		BufferedReader in = null ;
		String result = "";
		try {
			URL realUrl = new URL(url);
			//打开和URL之间的连接
			httpConn = (HttpURLConnection)realUrl.openConnection();
			httpConn.setRequestMethod("POST");
//			httpConn.setConnectTimeout(Integer.valueOf(connectTimeout));
//			httpConn.setReadTimeout(Integer.valueOf(readTimeout));
			httpConn.setRequestProperty("Accept-Charset", "utf-8");
			httpConn.setRequestProperty("Connection", "keep-alive");
			httpConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset = UTF-8");
			httpConn.setRequestProperty("Accept", "application/xml");
			httpConn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.803.0 Safari/535.1");
			//发送POST请求必须设置如下两行
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			//获取HttpURLConnection对象对应的输出流
			out = httpConn.getOutputStream() ;
			//发送请求参数
			out.write(params);
			//flush输出流的缓冲
			out.flush();
			//定义BufferedReader输入流来读取URL的响应
			InputStream inputStream = httpConn.getInputStream();
			in = new BufferedReader(new InputStreamReader(inputStream,"UTF-8")) ;
			String line = "";
			while((line = in.readLine()) != null){
				result += line;
			}
		} catch (Exception e) {
//			logger.error("发送 POST 请求出现异常！",e);
		} finally{ //使用finally块来关闭输出流、输入流
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
		return result;
	}
}

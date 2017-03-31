package com.cloudwalk.common.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import org.apache.commons.lang3.StringUtils;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.ibis.socket.SSConst;

/**
 * socket工具类
 * @author zhuyf
 *
 */
public class SocketUtil {

	/**
	 * 通过socket给服务器发送消息
	 * @param ip 
	 * @param port
	 * @param timeout 超时时间（毫秒）
	 * @param requestData
	 * @param head 报文头 说明报文数据的长度
	 * @return
	 */
	public static String sendMsg(String ip,int port,int timeout,String requestData,int head) {
		
		if(StringUtils.isBlank(ip)){
			ip = Constants.SOCKET_IP;
		}
		
		if(timeout == 0) {
			timeout = Constants.SOCKET_TIMEOUT;
		}
		
		if(head == 0) {
			head = Constants.SOCKET_HEAD_LENGTH;
		}
		
		Socket so = null;
		PrintStream ps = null;
		InputStream is = null;
		String retString = null;		
		try {			
			so = new Socket(ip, port);
			so.setSoTimeout(timeout);
			is = so.getInputStream();
			OutputStream os = so.getOutputStream();
			// 客户从server的输入流
			ps = new PrintStream(os);
			ps.print(requestData);
			retString = new String(ObjectUtils.readSocketInputStream(is,head),SSConst.DEFAULT_CHARSET);		
			if(ps != null) ps.close();			
			if(is != null) is.close();
			if(so != null) so.close();	
		} catch(Exception e) {
			e.printStackTrace();
		}
		return retString;
	}
	
	
	
}

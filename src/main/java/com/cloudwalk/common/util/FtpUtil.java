package com.cloudwalk.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * ftp工具类
 * @author zhuyf
 *
 */
public class FtpUtil {
	
	public static Logger logger = Logger.getLogger(FtpUtil.class);
	
	
	
	/** 
	 * Description: 获取FTP客户端对象 
	 * @param host FTP服务器hostname 
	 * @param port FTP服务器端口 
	 * @param username FTP登录账号 
	 * @param password FTP登录密码 
	 * @param basePath FTP服务器基础目录
	 * @return ftp客户端对象 
	 */  
	public static FTPClient getFtpClient(String host, int port, String username, String password, String basePath) {
		
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host, port);// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				logger.error("获取ftp客户端连接失败");
				return null;
			}			
			
			//切换到上传根目录
			if (!ftp.changeWorkingDirectory(basePath)) {				
				logger.error("获取ftp客户端连接对象时，切换到目录："+basePath+"失败");
				return null;						
			}			
			
			//设置上传文件的类型为二进制类型
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			
		} catch (IOException e) {
			logger.error("获取ftp客户端连接对象发生异常",e);
			ftp = null;
		} 
		
		return ftp;
	}
	
	/** 
	 * Description: 向FTP服务器上传文件 	
	 * @param ftp ftp客户端连接对象
	 * @param rootPath 文件根路径，如：window D:/1234 liunx /opt
	 * @param filePath 文件相对路径,如:/111/111.jpg
	 * @return 成功返回true，否则返回false 
	 */  
	public static boolean uploadFile(FTPClient ftp,String rootPath,String filePath) {
		
		boolean result = false;
		if(ftp == null ) return result;
		
		//获取文件对象
		File file = new File(rootPath+filePath);
		if(file == null || !file.exists()) return result;		
		//获取ftp服务器的相对路径
		String ftpFilePath = FilenameUtils.getFullPathNoEndSeparator(filePath);
		if("/".equals(""+ftpFilePath.charAt(0))){
			ftpFilePath = ftpFilePath.substring(1);
		}		
		//文件输入流
		InputStream fileinput = null;
		
		try {				
            
			//文件名
			String fileName = FilenameUtils.getName(filePath);	
			fileName = new String(fileName.getBytes("GBK"),"iso-8859-1");
			
			//获取文件输入流
			fileinput = new FileInputStream(file);
			
			//切换到上传目录
			if (!ftp.changeWorkingDirectory(ftpFilePath)) {
				//如果目录不存在创建目录
				String[] dirs = ftpFilePath.split("/");
				String tempPath = "";
				for (String dir : dirs) {
					if (null == dir || "".equals(dir)) continue;					
					tempPath = dir;				
					if (!ftp.changeWorkingDirectory(tempPath)) {
						if (!ftp.makeDirectory(tempPath)) {
							logger.error("ftp客户端上传文件时，创建目录："+tempPath+"失败");
							return result;
						} else {
							ftp.changeWorkingDirectory(tempPath);
						}
					}
				}
			}
			
			//上传文件
			if (!ftp.storeFile(fileName, fileinput)) {
				return result;
			}					
			result = true;
			
		} catch (IOException e) {
			logger.error("ftp上传文件发生异常",e);
		} finally {
			if(fileinput != null) {
				try {
					fileinput.close();
				} catch (IOException e) {
				}	
			}
		}
		
		return result;
	}
	
	
	/** 
	 * Description: 向FTP服务器上传文件 	
	 * @param ftp ftp客户端连接对象
	 * @param filename 上传到FTP服务器上的文件名 
	 * @param input 输入流 
	 * @return 成功返回true，否则返回false 
	 */  
	public static boolean uploadFile(FTPClient ftp,String filename, InputStream input) {
		
		boolean result = false;
		if(ftp == null || input == null) return result;
		
		try {		
			
			//上传文件
			if (!ftp.storeFile(filename, input)) {
				return result;
			}
			input.close();			
			result = true;
			
		} catch (IOException e) {
			logger.error("ftp上传文件发生异常",e);
		} 
		
		return result;
	}
	
	/**
	 * 关闭ftp客户端连接
	 * @param ftp
	 */
	public static void closeFtpClient(FTPClient ftp) {
		if(ftp == null) return;
		try {
			ftp.logout();
		} catch (IOException e) {
			logger.error("ftp客户端关闭连接发生异常",e);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
	}
	

	/** 
	 * Description: 向FTP服务器上传文件 
	 * @param host FTP服务器hostname 
	 * @param port FTP服务器端口 
	 * @param username FTP登录账号 
	 * @param password FTP登录密码 
	 * @param basePath FTP服务器基础目录
	 * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
	 * @param filename 上传到FTP服务器上的文件名 
	 * @param input 输入流 
	 * @return 成功返回true，否则返回false 
	 */  
	public static boolean uploadFile(String host, int port, String username, String password, String basePath,
			String filePath, String filename, InputStream input) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host, port);// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			//切换到上传目录
			if (!ftp.changeWorkingDirectory(basePath+filePath)) {
				//如果目录不存在创建目录
				String[] dirs = filePath.split("/");
				String tempPath = basePath;
				for (String dir : dirs) {
					if (null == dir || "".equals(dir)) continue;
					tempPath += "/" + dir;
					if (!ftp.changeWorkingDirectory(tempPath)) {
						if (!ftp.makeDirectory(tempPath)) {
							return result;
						} else {
							ftp.changeWorkingDirectory(tempPath);
						}
					}
				}
			}
			//设置上传文件的类型为二进制类型
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			//上传文件
			if (!ftp.storeFile(filename, input)) {
				return result;
			}
			input.close();
			ftp.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}
	
	/** 
	 * Description: 从FTP服务器下载文件 
	 * @param host FTP服务器hostname 
	 * @param port FTP服务器端口 
	 * @param username FTP登录账号 
	 * @param password FTP登录密码 
	 * @param remotePath FTP服务器上的相对路径 
	 * @param fileName 要下载的文件名 
	 * @param localPath 下载后保存到本地的路径 
	 * @return 
	 */  
	public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
			String fileName, String localPath) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host, port);
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());

					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}

			ftp.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}
}

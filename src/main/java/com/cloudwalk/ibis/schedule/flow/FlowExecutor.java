package com.cloudwalk.ibis.schedule.flow;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.cloudwalk.common.common.ScheduleConfig;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.DateUtil;
import com.cloudwalk.common.util.FtpUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.common.util.file.FileUtils;
import com.cloudwalk.ibis.schedule.CWExecutor;
import com.cloudwalk.ibis.service.queryStatisic.ChannelFlowService;


/**
 * 流水数据处理
 * @author zhuyf
 *
 */
public class FlowExecutor extends CWExecutor {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name = "channelFlowService")
	private ChannelFlowService channelFlowService;
		
	@Override
	public void execute() throws Exception {			
		
		//转移多少天以前的数据
		int transferDays = Integer.valueOf(ScheduleConfig.FLOW_TRANSFER_DAYS);
		if(transferDays < 1) {
			logger.info("转移天数不能小于1,执行结束");
			return;
		}		
		
		//计算当前的删除的日期
		Date date = DateUtil.getPreDaysDate(new Date(), transferDays-1);
		logger.info("开始处理"+DateUtil.parseStr(date,DateUtil.DATE_SMALL_STR)+"以前的流水数据");
	
		//转移图片
		this.transferFiles(date);
		
		//转移流水
		this.transferFlows(date);
		
	}
	
	/**
	 * 转移流水数据
	 * @param date
	 */
	public void transferFlows(Date date) {
		//目前流水数据默认为删除
		this.channelFlowService.deleteFlowsByDate(date);
	}
	
	/**
	 * 转移图片
	 * @param date
	 */
	public void transferFiles(Date date) {
		
		if(date == null) return;
		try {			
			
			int year = DateUtil.getYear(date);
			int month = DateUtil.getMonth(date);
			int day = DateUtil.getDay(date);
			//文件分隔符
			String fs = ScheduleConfig.FILE_SEPARATOR;
			//文件根路径
			String rootPath = ScheduleConfig.FILE_ROOT_DIR + ScheduleConfig.FLOW_FILE_SOURCE_DIR;
			
			//获取源根文件目录的文件数量
			List<File> fileSourceYearDirs = FileUtils.visitAll(rootPath);
			if(fileSourceYearDirs.size() < 1) return;
			for(int i=0;i<fileSourceYearDirs.size();i++) {
				File yearFile = fileSourceYearDirs.get(i);
				int curYear = Integer.valueOf(yearFile.getName());
				if(curYear < year) {
					//转移文件
					this.transferFlowFile(rootPath+fs+yearFile.getName());
					continue;
				}
				
				//计算当前年文件夹下面的文件数量
				List<File> fileSourceMonthDirs = FileUtils.visitAll(rootPath+fs+curYear);
				for(int j=0;j<fileSourceMonthDirs.size();j++) {
					File monthFile = fileSourceMonthDirs.get(j);
					int curMonth = Integer.valueOf(monthFile.getName());
					if(curMonth < month) {
						//转移文件
						this.transferFlowFile(rootPath+fs+yearFile.getName()+fs+monthFile.getName());
						continue;
					}
					
					//计算当前月文件夹下面的文件数量
					List<File> fileSourceDayDirs = FileUtils.visitAll(rootPath+fs+yearFile.getName()+fs+monthFile.getName());
					for(int k=0;k<fileSourceDayDirs.size();k++) {
						File dayFile = fileSourceDayDirs.get(k);
						int curDay = Integer.valueOf(dayFile.getName());
						if(curDay < day) {
							//转移文件
							this.transferFlowFile(rootPath+fs+yearFile.getName()+fs+monthFile.getName()+fs+dayFile.getName());
							continue;
						}
					}
				}
				
			}
		} catch(Exception e) {
			logger.error("转移文件发生异常",e);
		}
	}
	
	/**
	 * 转移流水文件目录
	 * @param fileDir
	 * @throws IOException 
	 */
	private void transferFlowFile(String fileDir) throws Exception {
		if(StringUtils.isBlank(fileDir)) return;
		File file = new File(fileDir);
		if(file == null || !file.exists()) return;
		//判断文件处理方式
		if(ScheduleConfig.FLOW_TRANSFER_WAY_REMOVE.equals(ScheduleConfig.FLOW_TRANSFER_WAY)) {
			//删除
			FileUtils.deleteDirectory(file);
		} else if(ScheduleConfig.FLOW_TRANSFER_WAY_FTP.equals(ScheduleConfig.FLOW_TRANSFER_WAY)) {
			//ftp
			this.ftpUploadFile(file);
		}
	}	
	
	/**
	 * ftp上传文件
	 * @param fileDir
	 * @throws ServiceException
	 */
	public void ftpUploadFile(File fileDir) throws Exception {			
			
		//获取ftp客户端连接对象
		FTPClient ftp = FtpUtil.getFtpClient(ScheduleConfig.FLOW_FTP_ADDR, ObjectUtils.objToInt(ScheduleConfig.FLOW_FTP_PORT,0), ScheduleConfig.FLOW_FTP_USERNAME, ScheduleConfig.FLOW_FTP_PASSWORD, ScheduleConfig.FLOW_FTP_PATH);
		if(ftp == null) {
			throw new ServiceException("获取ftp客户端连接对象失败");
		}
			
		//ftp上传文件夹
		this.ftpLoopUploadFile(ftp, fileDir);
			
		//关闭ftp客户端
		FtpUtil.closeFtpClient(ftp);		
	}
	
	/**
	 * 1.通过递归遍历该文件目录下所有的文件
	 * 2.ftp上传文件
	 * @param ftp ftp客户端工具
	 * @param fileDir
	 */
	private void ftpLoopUploadFile(FTPClient ftp,File fileDir) {
		
		//遍历该文件夹下面的所有文件
		if(fileDir == null || !fileDir.exists() || !fileDir.isDirectory()) return;
		String[] fileStrArray = fileDir.list();
		if(fileStrArray == null || fileStrArray.length < 1) {
			return;
		}
		
		//遍历文件
		File file = null;
		for(String fileStr:fileStrArray) {
			file = new File(fileDir,fileStr);
			if (file.exists() && file.isFile()) {
				String curFilePath = file.getAbsolutePath();
				curFilePath = curFilePath.replaceAll("\\\\", "/");
				curFilePath = curFilePath.replaceAll(ScheduleConfig.FILE_ROOT_DIR, "");
				boolean uploadFlag = FtpUtil.uploadFile(ftp, ScheduleConfig.FILE_ROOT_DIR, curFilePath);
				if(!uploadFlag) {
					logger.info("ftp上传文件："+file.getAbsolutePath()+"失败");
				} else {
					file.delete();
				}
				continue;
			} else if(file.isDirectory()) {
				ftpLoopUploadFile(ftp,file);
			}
		}	
		
		//删除空目录
		if(fileDir.list().length < 1) {
			fileDir.delete();
			return;
		}
	}
	

}

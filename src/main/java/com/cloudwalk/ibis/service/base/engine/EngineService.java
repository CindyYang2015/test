package com.cloudwalk.ibis.service.base.engine;

import java.util.List;

import com.cloudwalk.common.engine.face.eoc.Liveness;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.ibis.model.result.ver001.BankCardData;
import com.cloudwalk.ibis.model.result.ver001.IDCardData;

/**
 * 算法引擎服务类
 * @author zhuyf
 *
 */
public interface EngineService {	
	
	/**
	 * 获取生物特征
	 * @param fileData 生物文件数据
	 * @param fileType 文件类型 1高清 2水印
	 * @return
	 */
	public String getFeature(String fileData,int fileType) throws ServiceException;	
	
	/**
	 * 获取生物质量
	 * @param fileData 生物文件数据
	 * @return
	 */
	public double getQuality(String fileData) throws ServiceException;	
	
	/**
	 * 比对两个生物文件
	 * @param fileOneData 生物文件数据
	 * @param fileoneType 文件类型 1高清 2水印
	 * @param fileTwoData 生物文件数据
	 * @param filetwoType 文件类型 1高清 2水印
	 * @return
	 */
	public double compareFile(String fileOneData,int fileoneType,String fileTwoData,int filetwoType) throws ServiceException;
	
	/**
	 * 比对两个生物特征
	 * @param featureOneData 生物文件特征数据
	 * @param featureOneType 特征类型 1高清 2水印
	 * @param featureTwoData 生物文件特征数据
	 * @param featureTwoType 特征类型 1高清 2水印
	 * @return
	 */
	public double compareFeature(String featureOneData,int featureOneType,String featureTwoData,int featureTwoType) throws ServiceException;
	
	/**
	 * 比对两个生物特征(多模型)
	 * @param featureOneData
	 * @param featureOneType
	 * @param featureTwoData
	 * @param fileTwoData
	 * @param featureTwoType
	 * @return
	 */
	public double compareFeatureFile(String featureOneData,int featureOneType,String featureTwoData,String fileTwoData,int featureTwoType) throws ServiceException;
	
	/**
	 * 特征与生物文件比对
	 * @param featureData 生物文件特征数据
	 * @param fileData 生物文件数据
	 * @param featureType 文件类型 1高清 2水印
	 * @param fileDataType 文件类型 1高清 2水印
	 * @return
	 */
	public double compareFeatureFile(String featureData,int featureType,String fileData,int fileDataType) throws ServiceException;

	/**
	 * 根据特征检索客户信息
	 * @param featureData 特征数据
	 * @param featureType 特征类型 1高清 2水印
	 * @param groupId 组ID
	 * @param topN 检索的数量
	 * @return
	 */
	public List<SearchFeature> searchPersonByFeature(String featureData,int featureType,String groupId,int topN) throws ServiceException;

	/**
	 * 注册特征数据到引擎中
	 * @param FileData 生物文件数据
	 * @param FileDataType 文件类型 1高清 2水印
	 * @param featureId 特征ID
	 * @param status 状态 1添加 0更新
	 * @return
	 */
	public int regFeature(String FileData,int FileDataType,String featureId,int status) throws ServiceException;
	
	/**
	 * 识别身份证信息
	 * @param idcardImg 身份证图片
	 * @param type 1正面 0反面
	 * @return
	 */
	public IDCardData ocrIDCardInfo(String idcardImg,int type) throws ServiceException;
	
	/**
	 * 银行卡信息
	 * @param bankcardImg 银行卡图片
	 * @return
	 */
	public BankCardData ocrBankCardInfo(String bankcardImg) throws ServiceException;
	
	/**
	 * 活体检测
	 * @param json 活体数据
	 * @return
	 */
	public Liveness jdugeLiveness(String json) throws ServiceException;
}

package com.cloudwalk.ibis.service.base.engine.ver001;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.common.InterfaceConst;
import com.cloudwalk.common.engine.face.eoc.BankCard;
import com.cloudwalk.common.engine.face.eoc.Compare;
import com.cloudwalk.common.engine.face.eoc.EOCResult;
import com.cloudwalk.common.engine.face.eoc.Face;
import com.cloudwalk.common.engine.face.eoc.Feature;
import com.cloudwalk.common.engine.face.eoc.IDCard;
import com.cloudwalk.common.engine.face.eoc.Identify;
import com.cloudwalk.common.engine.face.eoc.Liveness;
import com.cloudwalk.common.engine.face.eoc.MultimodelFeature;
import com.cloudwalk.common.engine.face.eoc.Quality;
import com.cloudwalk.common.engine.face.recg.RecogniseUtils;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.BeanUtil;
import com.cloudwalk.ibis.model.result.ver001.BankCardData;
import com.cloudwalk.ibis.model.result.ver001.IDCardData;
import com.cloudwalk.ibis.service.base.engine.EngineService;
import com.cloudwalk.ibis.service.base.engine.SearchFeature;
import com.google.common.collect.Lists;

/**
 * 云从人脸识别引擎服务
 * @author zhuyf
 *
 */
public class FaceEngineService implements EngineService{
	
	/**
	 * 初始化云从人脸识别引擎
	 */
	public void init() {
		
		if(Constants.Config.FACE_ENGINE_ISREG.equals(String.valueOf(EnumClass.StatusEnum.YES.getValue()))){
			//初始化组ID
			String faceGroupId = Constants.Config.FACE_GROUP_ID;
			if(!StringUtils.isBlank(faceGroupId)) {
				RecogniseUtils.groupCreate(faceGroupId, faceGroupId);
			}	
		}
			
	}

	@Override
	public String getFeature(String fileData, int fileType) throws ServiceException {		
		
		//人像特征信息
		String featureStr = "";
		if(fileType == EnumClass.FileTypeEnum.HDTV.getValue()) {
			
			//提取高清图片特征
			Feature feature = RecogniseUtils.feature(fileData);
			if(feature != null && feature.getResult() == 0) {
				featureStr = feature.getFeature();
			} else {
				throw new ServiceException(InterfaceConst.ENGINE_EXCE, "人脸识别引擎发生异常，错误码："+feature.getResult()+",错误信息："+feature.getInfo());
			}
		} else if(fileType == EnumClass.FileTypeEnum.WATERMARK.getValue()) {
			
			//提取网纹图片特征
			MultimodelFeature mulFeature = RecogniseUtils.mulFeature(fileData, fileType);
			if(mulFeature != null && mulFeature.getResult() == 0) {
				featureStr = mulFeature.getFeature();
			} else {
				throw new ServiceException(InterfaceConst.ENGINE_EXCE, "人脸识别引擎发生异常，错误码："+mulFeature.getResult()+",错误信息："+mulFeature.getInfo());
			}
		}
		
		return featureStr;
		
	}

	@Override
	public double getQuality(String fileData) throws ServiceException {
		
		//人像质量分数
		double qualityScore = 0.0;
		Quality quality = RecogniseUtils.quality(fileData);
		if(quality != null && quality.getResult() == 0) {
			qualityScore = quality.getScore();
		} else {
			throw new ServiceException(InterfaceConst.ENGINE_EXCE, "人脸识别引擎发生异常，错误码："+quality.getResult()+",错误信息："+quality.getInfo());
		}
		return qualityScore;
	}

	@Override
	public double compareFile(String fileOneData, int fileoneType,
			String fileTwoData, int filetwoType) throws ServiceException {
		
		//人像比对分数
		double cmpScore = 0.0;
		Compare compare = RecogniseUtils.compare(fileOneData, fileTwoData);
		if(compare != null && compare.getResult() == 0) {
			cmpScore = compare.getScore();
		} else {
			throw new ServiceException(InterfaceConst.ENGINE_EXCE, "人脸识别引擎发生异常，错误码："+compare.getResult()+",错误信息："+compare.getInfo());
		}
		return cmpScore;
	}	

	@Override
	public double compareFeature(String featureOneData, int featureOneType,
			String featureTwoData, int featureTwoType) throws ServiceException {
		
		//人像比对分数
		double cmpScore = 0.0;
		Compare compare = RecogniseUtils.similarityByFeature(featureOneData, featureTwoData);
		if(compare != null && compare.getResult() == 0) {
			cmpScore = compare.getScore();
		} else {
			throw new ServiceException(InterfaceConst.ENGINE_EXCE, "人脸识别引擎发生异常，错误码："+compare.getResult()+",错误信息："+compare.getInfo());
		}
		return cmpScore;
	}

	@Override
	public double compareFeatureFile(String featureData, int featureType,
			String fileData, int fileDataType) {
		return 0;
	}

	@Override
	public List<SearchFeature> searchPersonByFeature(String featureData,
			int featureType, String groupId, int topN) throws ServiceException {
		
		//人像检索
		List<SearchFeature> features = Lists.newArrayList();
		Identify identify = RecogniseUtils.identifyByFeature(groupId, featureData, topN);
		if(identify != null && identify.getResult() == 0) {
			for(Face f:identify.getFaces()) {
				SearchFeature sf = new SearchFeature();
				sf.setFeatureId(f.getFaceId());
				sf.setScore(f.getScore());
				features.add(sf);
			}
		} else {
			throw new ServiceException(InterfaceConst.ENGINE_EXCE, "人脸识别引擎发生异常，错误码："+identify.getResult()+",错误信息："+identify.getInfo());
		} 
		
		return features;
	}

	@Override
	public int regFeature(String FileData, int FileDataType, String featureId,
			int status) throws ServiceException {
		
		//判断是否注册到引擎中
		if(!Constants.Config.FACE_ENGINE_ISREG.equals(String.valueOf(EnumClass.StatusEnum.YES.getValue()))){
			return 0;
		}
		
		if(StringUtils.isBlank(featureId) || StringUtils.isBlank(FileData)) {
			return 0;
		}
		
		//人脸组ID
		String faceGroupId = Constants.Config.FACE_GROUP_ID;
		if(EnumClass.StatusEnum.YES.getValue() != status) {
			//删除已存在的人脸
			RecogniseUtils.removeFace(faceGroupId, featureId);	
			RecogniseUtils.faceDelete(featureId);
		} 
		
		//添加人脸
		EOCResult result = RecogniseUtils.faceCreate(featureId, FileData, "");
		if(result != null && result.getResult() == 0) {
			RecogniseUtils.addFace(faceGroupId, featureId);
		} else {
			throw new ServiceException(InterfaceConst.ENGINE_EXCE, "人脸识别引擎发生异常，错误码："+result.getResult()+",错误信息："+result.getInfo());
		} 
		return 1;
	}

	@Override
	public IDCardData ocrIDCardInfo(String idcardImg, int type) throws ServiceException {	
		
		//身份证识别
		IDCardData data = null;
		IDCard idcard = RecogniseUtils.ocrIDCard(idcardImg, type);
		if(idcard != null && idcard.getResult() == 0) {
			data = new IDCardData();
			BeanUtil.copyProperty(data, idcard);
			if(EnumClass.IDCardTypeEnum.FRONT.getValue() == idcard.getType()) {
				//正面
				data.setCtfname(idcard.getName());
				data.setCtfno(idcard.getCardno());
				if(idcard.getFace() != null) {
					data.setHeadImg(idcard.getFace().getImage());
				}			
				
			} else if(EnumClass.IDCardTypeEnum.BLACK.getValue() == idcard.getType()) {
				//反面
				data.setValiddate(idcard.getValiddate1()+Constants.SEPARATOR+idcard.getValiddate2());
			}			
			
		} else {
			throw new ServiceException(InterfaceConst.ENGINE_EXCE, "人脸识别引擎发生异常，错误码："+idcard.getResult()+",错误信息："+idcard.getInfo());
		} 
		
		return data;
	}	
	
	@Override
	public BankCardData ocrBankCardInfo(String bankcardImg) throws ServiceException {
		
		//银行卡识别
		BankCardData bankCardData = null;
		BankCard bankCard = RecogniseUtils.ocrBankCard(bankcardImg);
		if(bankCard != null && bankCard.getResult() == 0) {
			bankCardData = new BankCardData();
			bankCardData.setCardNum(bankCard.getCardNum());
			bankCardData.setBankName(bankCard.getBankName());
			bankCardData.setCardName(bankCard.getCardName());
			bankCardData.setCardType(bankCard.getCardType());
		} else {
			throw new ServiceException(InterfaceConst.ENGINE_EXCE, "人脸识别引擎发生异常，错误码："+bankCard.getResult()+",错误信息："+bankCard.getInfo());
		} 
		
		return bankCardData;
	}

	@Override
	public Liveness jdugeLiveness(String json) throws ServiceException {
		
		//活体检测
		Liveness liveData=RecogniseUtils.jdugeLiveness(json);
		if(liveData != null && liveData.getResult() == 0) {
			return liveData;
		} else {
			throw new ServiceException(InterfaceConst.ENGINE_EXCE, "人脸识别引擎发生异常，错误码："+liveData.getResult()+",错误信息："+liveData.getInfo());
		} 
	}

	@Override
	public double compareFeatureFile(String featureOneData, int featureOneType,
			String featureTwoData, String fileTwoData, int featureTwoType) throws ServiceException {
		
		//人像多模型特征比对
		Compare compare = null;		
		if(featureOneType == EnumClass.FileTypeEnum.HDTV.getValue() && featureTwoType == EnumClass.FileTypeEnum.HDTV.getValue()) {
			//高清
			compare = RecogniseUtils.similarityByFeature(featureOneData, featureTwoData);
		} else {	
			compare = RecogniseUtils.mulSimilarityByFeature(featureOneData, featureOneType, fileTwoData, featureTwoType);
		}
		
		if(compare != null && compare.getResult() == 0) {
			return compare.getScore();
		} else {
			throw new ServiceException(InterfaceConst.ENGINE_EXCE, "人脸识别引擎发生异常，错误码："+compare.getResult()+",错误信息："+compare.getInfo());
		} 
	}
	
	
	
	
	

}

package com.cloudwalk.ibis.service.base.recog;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;
import org.apache.log4j.Logger;

import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.common.InterfaceConst;
import com.cloudwalk.common.engine.face.eoc.Liveness;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.BeanUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.common.util.file.FileUtils;
import com.cloudwalk.ibis.model.RecogRequest;
import com.cloudwalk.ibis.model.TransferObj;
import com.cloudwalk.ibis.model.featurelib.Person;
import com.cloudwalk.ibis.model.featurelib.PersonFeature;
import com.cloudwalk.ibis.model.queryStatisic.ChannelFlow;
import com.cloudwalk.ibis.model.recogSet.BlackPersonControl;
import com.cloudwalk.ibis.model.recogSet.EngineVer;
import com.cloudwalk.ibis.model.recogSet.Recognizerule;
import com.cloudwalk.ibis.model.recogSet.WhitePerson;
import com.cloudwalk.ibis.model.result.ver001.BankCardData;
import com.cloudwalk.ibis.model.result.ver001.IDCardData;
import com.cloudwalk.ibis.model.result.ver001.Response;
import com.cloudwalk.ibis.model.result.ver001.Result;
import com.cloudwalk.ibis.model.system.Organization;
import com.cloudwalk.ibis.service.base.CacheService;
import com.cloudwalk.ibis.service.base.engine.EngineService;
import com.cloudwalk.ibis.service.base.engine.EngineVerManageService;
import com.cloudwalk.ibis.service.base.networkcheck.NetworkCheckService;
import com.cloudwalk.ibis.service.featurelib.PersonFeatureService;
import com.cloudwalk.ibis.service.featurelib.PersonService;
import com.cloudwalk.ibis.service.queryStatisic.ChannelFlowService;
import com.cloudwalk.ibis.service.recogSet.BlackPersonService;
import com.cloudwalk.ibis.service.recogSet.RecognizeruleService;
import com.cloudwalk.ibis.service.recogSet.WhitePersonService;
import com.cloudwalk.ibis.service.system.OrganizationService;

/**
 * 人脸识别服务认证
 * @author zhuyf
 *
 */
public class RecogServiceImpl implements RecogService{
	
	protected final Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 算法引擎管理服务
	 */
	@Resource(name="engineService")
	protected com.cloudwalk.ibis.service.recogSet.EngineService engineManageService;
	
	/**
	 * 引擎版本管理服务
	 */
	@Resource(name="engineVerManage")
	protected EngineVerManageService engineVerManageService;	
	
	/**
	 * 缓存服务
	 */
	@Resource(name="cacheService")
	protected CacheService cacheService;
	
	/**
	 * 黑名单服务
	 */
	@Resource(name="blackPersonService")
	protected BlackPersonService blackPersonService;
	
	/**
	 * 白名单服务
	 */
	@Resource(name="whitePersonService")
	protected WhitePersonService whitePersonService;
	
	/**
	 * 识别规则服务
	 */
	@Resource(name="recognizeruleService")
	protected RecognizeruleService recognizeruleService;
	
	/**
	 * 客户服务
	 */
	@Resource(name="personService")
	protected PersonService personService;
	
	/**
	 * 客户特征服务
	 */
	@Resource(name="personFeatureService")
	protected PersonFeatureService personFeatureService;
	
	/**
	 * 联网核查服务
	 */
	@Resource(name="networkCheckService")
	protected NetworkCheckService networkCheckService;
	
	/**
	 * 流水记录服务
	 */
	@Resource(name="channelFlowService")
	protected ChannelFlowService channelFlowService;
	
	@Resource(name="organizationService")
	protected OrganizationService organizationService;
	
	
	/**
	 * 数据解析对象
	 */
	protected DataParse dataParse;
	
	/**
	 * 初始化服务
	 */
	public void init() {
		dataParse = new DataParse();
		log.info("初始化"+this.getClass().getName()+"完成");
	}

	@Override
	public void execute(TransferObj transferObj) throws ServiceException {		
	}
	
	/**
	 * 参数校验
	 * @param recogRequest
	 * @return
	 * @throws ServiceException 
	 */
	public boolean checkParam(RecogRequest recogRequest) throws ServiceException {
		if(StringUtils.isBlank(recogRequest.getOrgCode())
			|| StringUtils.isBlank(recogRequest.getVerCode())
			|| StringUtils.isBlank(recogRequest.getChannel())
			|| StringUtils.isBlank(recogRequest.getTradingCode())
			|| StringUtils.isBlank(recogRequest.getEngineCode())
			|| StringUtils.isBlank(recogRequest.getBuscode())
			|| StringUtils.isBlank(recogRequest.getTradingFlowNO())) {
			throw new ServiceException("参数不完整");
		}
		return true;
	}
	
	/**
	 * 根据引擎代码和引擎版本代码获取对应的引擎服务
	 * @param recogRequest
	 * @throws ServiceException 
	 */
	public void initEngineService(TransferObj transferObj) throws ServiceException {	
		long startTime1 = new Date().getTime();
		
		//请求对象
		RecogRequest recogRequest = transferObj.getRecogRequest();		
		
		//1.根据机构代码，渠道，交易代码和引擎代码从识别策略中获取对应的算法引擎版本
		EngineVer engineVer = this.cacheService.getEngineVer(recogRequest.getOrgCodePath(), recogRequest.getChannel(), recogRequest.getTradingCode(), recogRequest.getEngineCode());
		if(engineVer == null) {
			
			/**
			 * 获取父级法人机构识别策略信息
			 * 1.获取父级法人机构信息
			 * 2.根据父级机构代码，渠道，交易代码和引擎代码从识别策略中获取对应的算法引擎版本
			 */
			//获取父级法人机构信息
			List<String> parentOrgCodePaths = this.organizationService.selectParentCodes(recogRequest.getOrgCodePath());
			if(!ObjectUtils.isEmpty(parentOrgCodePaths)) {	
				//根据父级机构代码，渠道，交易代码和引擎代码从识别策略中获取对应的算法引擎版本
				for(String orgCodePath:parentOrgCodePaths) {
					engineVer = this.cacheService.getEngineVer(orgCodePath, recogRequest.getChannel(), recogRequest.getTradingCode(), recogRequest.getEngineCode());
					if(engineVer != null) break;
				}
			}
			if(engineVer == null) {
				throw new ServiceException("获取算法引擎版本失败");
			}			
		}			
		//设置当前引擎版本信息
		transferObj.setEngineVer(engineVer);
		
		//2.根据算法引擎代码和版本从算法引擎管理对象中获取对应的算法引擎
		EngineService engineService = this.engineVerManageService.getRecogService(engineVer.getEngineCode(), engineVer.getVerCode());
		if(engineService == null) {
			throw new ServiceException("从算法引擎版本管理中获取引擎服务失败");
		}		
		transferObj.setEngineService(engineService);
		
		long endTime1 = new Date().getTime();
		transferObj.addHandleTimes("初始化算法引擎服务耗时："+(endTime1-startTime1)+"毫秒");
	}		
	
	/**
	 * 检查是否黑名单
	 * @param recogRequest
	 * @return
	 * @throws ServiceException 
	 */
	public void checkBlackPerson(TransferObj transferObj) throws ServiceException {
		long startTime1 = new Date().getTime();
		
		RecogRequest recogRequest = transferObj.getRecogRequest();
		//根据机构，渠道，交易代码，证件类型，证件号码，核心客户号获取黑名单信息
		BlackPersonControl bpc = new BlackPersonControl();
		bpc.setChannel(recogRequest.getChannel());
		bpc.setCtfno(recogRequest.getCtfno());
		bpc.setCtftype(recogRequest.getCtftype());
		bpc.setLegalOrgCode(recogRequest.getOrgCodePath());
		bpc.setTradingCode(recogRequest.getTradingCode());
		List<BlackPersonControl> bpcList = this.blackPersonService.selectBlackPersonBykey(bpc);
		if(!ObjectUtils.isEmpty(bpcList)) {
			throw new ServiceException("该客户被禁止访问");
		}		
		
		long endTime1 = new Date().getTime();
		transferObj.addHandleTimes("检查是否黑名单耗时："+(endTime1-startTime1)+"毫秒");
	}	
	
	/**
	 * 获取客户信息
	 * @param transferObj
	 * @return
	 * @throws ServiceException 
	 */
	public void getPerson(TransferObj transferObj) throws ServiceException {
		long startTime1 = new Date().getTime();
		
		//根据机构，证件类型，证件号码获取客户信息
		Person person = new Person();
		person.setPersonId(null);
		person.setLegalOrgCode(transferObj.getRecogRequest().getOrgCodePath());
		person.setCtftype(transferObj.getRecogRequest().getCtftype());
		person.setCtfno(transferObj.getRecogRequest().getCtfno());
		List<Person> personList = this.personService.searchAll(person);
		if(!ObjectUtils.isEmpty(personList)) {
			person = personList.get(0);
			//判断客户是否已删除
			if(person.getStatus() == 0) {
				throw new ServiceException("该客户已被删除");
			}
			
			//获取客户生物特征信息
			PersonFeature pf = new PersonFeature();
			pf.setFeatureId(null);
			pf.setPartitionId(person.getPartitionId());
			pf.setEngineCode(transferObj.getRecogRequest().getEngineCode());
			pf.setEngineVerCode(transferObj.getEngineVer().getVerCode());
			pf.setPersonId(person.getPersonId());
			pf = this.personFeatureService.selectPersonFeature(pf);			
			person.setPf(pf);
			transferObj.setP(person);
			//设置当前生物特征文件
			if(pf != null) {
				transferObj.setFilePath(pf.getFilePath());
			}
		}		
		
		long endTime1 = new Date().getTime();
		transferObj.addHandleTimes("获取客户信息耗时："+(endTime1-startTime1)+"毫秒");
	}
	
	/**
	 * 获取请求报文的生物文件信息，并转成base64字符串
	 * @param recogRequest
	 * @return
	 * @throws ServiceException 
	 */
	public void getFileBase64String(RecogRequest recogRequest) throws ServiceException {
		
		//将文件相对路径转成base64字符串
		if(StringUtils.isBlank(recogRequest.getFileDataone()) && !StringUtils.isBlank(recogRequest.getFilePathone())) {
			String fileDataone = this.getFileBase64Data(recogRequest.getFilePathone());
			if(StringUtils.isBlank(fileDataone)) {
				throw new ServiceException("获取文件1数据失败");
			}
			recogRequest.setFileDataone(fileDataone);
		}
		
		if(StringUtils.isBlank(recogRequest.getFileDatatwo()) && !StringUtils.isBlank(recogRequest.getFilePathtwo())) {
			String fileDatatwo = this.getFileBase64Data(recogRequest.getFilePathtwo());
			if(StringUtils.isBlank(fileDatatwo)) {
				throw new ServiceException("获取文件2数据失败");
			}
			recogRequest.setFileDatatwo(fileDatatwo);
		}
		
		if(StringUtils.isBlank(recogRequest.getNetCheckFileData()) && !StringUtils.isBlank(recogRequest.getNetCheckFilePath())) {
			String netCheckFileData = this.getFileBase64Data(recogRequest.getNetCheckFilePath());
			if(StringUtils.isBlank(netCheckFileData)) {
				throw new ServiceException("获取联网核查文件数据失败");
			}
			recogRequest.setNetCheckFileData(netCheckFileData);
		}
		
		if(StringUtils.isBlank(recogRequest.getRegfileData()) && !StringUtils.isBlank(recogRequest.getRegFilePath())) {
			String regfileData = this.getFileBase64Data(recogRequest.getRegFilePath());
			if(StringUtils.isBlank(regfileData)) {
				throw new ServiceException("获取注册文件数据失败");
			}
			recogRequest.setRegfileData(regfileData);
		}
		
		if(StringUtils.isBlank(recogRequest.getFrontImgePath()) && !StringUtils.isBlank(recogRequest.getFrontImgePath())) {
			String frontfileData = this.getFileBase64Data(recogRequest.getFrontImgePath());
			if(StringUtils.isBlank(frontfileData)) {
				throw new ServiceException("获取身份证正面照失败");
			}
			recogRequest.setFrontImge(frontfileData);
		}
		
		if(StringUtils.isBlank(recogRequest.getBackImgePath()) && !StringUtils.isBlank(recogRequest.getBackImgePath())) {
			String backfileData = this.getFileBase64Data(recogRequest.getFrontImgePath());
			if(StringUtils.isBlank(backfileData)) {
				throw new ServiceException("获取身份证反面照失败");
			}
			recogRequest.setBackImge(backfileData);
		}
		
		if(StringUtils.isBlank(recogRequest.getBankCardImgPath()) && !StringUtils.isBlank(recogRequest.getBankCardImgPath())) {
			String bankCardImgData = this.getFileBase64Data(recogRequest.getBankCardImgPath());
			if(StringUtils.isBlank(bankCardImgData)) {
				throw new ServiceException("获取银行卡照片失败");
			}
			recogRequest.setBankCardImg(bankCardImgData);
		}
	}
	
	/**
	 * 获取生物文件特征信息
	 * @param fileBase64String
	 * @return
	 * @throws ServiceException 
	 */
	public void getFeature(TransferObj transferObj) throws ServiceException {
		
		//请求报文对象
		RecogRequest recogRequest=transferObj.getRecogRequest();
		
		if(StringUtils.isBlank(recogRequest.getFeatureone()) && !StringUtils.isBlank(recogRequest.getFileDataone())) {
			//获取特征
			String featureone =transferObj.getEngineService().getFeature(recogRequest.getFileDataone(), EnumClass.FileTypeEnum.HDTV.getValue());
			if(StringUtils.isBlank(featureone)) {
				throw new ServiceException("获取文件1特征失败");
			}
			recogRequest.setFeatureone(featureone);
		}
		
		if(StringUtils.isBlank(recogRequest.getFeaturetwo()) && !StringUtils.isBlank(recogRequest.getFileDatatwo())) {
			//获取特征
			String featuretwo = transferObj.getEngineService().getFeature(recogRequest.getFileDatatwo(), EnumClass.FileTypeEnum.HDTV.getValue());
			if(StringUtils.isBlank(featuretwo)) {
				throw new ServiceException("获取文件2特征失败");
			}
			recogRequest.setFeaturetwo(featuretwo);
		}
		
		if(transferObj.getP() == null && StringUtils.isBlank(recogRequest.getNetCheckFeature()) && !StringUtils.isBlank(recogRequest.getNetCheckFileData())) {
			//获取特征
			String netCheckFeature = transferObj.getEngineService().getFeature(recogRequest.getNetCheckFileData(), recogRequest.getNetCheckImgType());
			if(StringUtils.isBlank(netCheckFeature)) {
				throw new ServiceException("获取联网核查文件特征失败");
			}
			recogRequest.setNetCheckFeature(netCheckFeature);
		}	
		
		if(StringUtils.isBlank(recogRequest.getRegfeature()) && !StringUtils.isBlank(recogRequest.getRegfileData())) {
			//获取特征
			String regfeature = transferObj.getEngineService().getFeature(recogRequest.getRegfileData(), EnumClass.FileTypeEnum.HDTV.getValue());
			if(StringUtils.isBlank(regfeature)) {
				throw new ServiceException("获取注册文件特征失败");
			}
			recogRequest.setRegfeature(regfeature);
		}
	}
	
	/**
	 * 添加客户信息
	 * @param recogRequest
	 * @return
	 * @throws ServiceException 
	 */
	public void addPerson(TransferObj transferObj) throws ServiceException {
		long startTime1 = new Date().getTime();
		
		//添加客户信息
		Date curdate = new Date();
		Person person = new Person();
		person.setCtftype(transferObj.getRecogRequest().getCtftype());
		person.setCtfno(transferObj.getRecogRequest().getCtfno());
		person.setCustomerId(transferObj.getRecogRequest().getCustomerId());
		person.setLegalOrgCode(transferObj.getRecogRequest().getOrgCodePath());
		person.setCreateTime(curdate);
		person.setCtfname(transferObj.getRecogRequest().getCtfname());
		person.setProperty(transferObj.getRecogRequest().getProperty());	
		if(null == transferObj.getRecogRequest().getOrgName()) {
			Organization org = this.organizationService.selectByPrimaryKey(transferObj.getRecogRequest().getOrgCode());
			transferObj.getRecogRequest().setOrgName(org.getOrgName());
			person.setOrgName(org.getOrgName());
		}
		int ret = this.personService.insertSelective(person);
		if(ret < 1) {
			throw new ServiceException("添加客户信息失败");
		}		
		transferObj.setP(person);
		
		long endTime1 = new Date().getTime();
		transferObj.addHandleTimes("添加客户信息耗时："+(endTime1-startTime1)+"毫秒");
	}
	
	/**
	 * 新增或更新客户生物特征
	 * @param transferObj
	 * @return
	 * @throws ServiceException 
	 */
	public void savePersonFeature(TransferObj transferObj) throws ServiceException {		
		long startTime1 = new Date().getTime();

		//客户信息
		Person p = transferObj.getP();
		if(p == null) {
			throw new ServiceException("客户信息不存在，不能添加或修改生物特征信息");
		}
		
		//操作状态 1添加 0更新
		int status = 0;
		//保存客户生物特征信息
		Date curDate = new Date();
		if(p.getPf() == null) {
			//新增客户生物特征			
			PersonFeature pf = new PersonFeature();
			transferObj.setFeatureId(pf.getFeatureId());
			pf.setPartitionId(p.getPartitionId());
			pf.setPersonId(p.getPersonId());
			pf.setCreateTime(curDate);
			pf.setEngineCode(transferObj.getRecogRequest().getEngineCode());
			pf.setEngineVerCode(transferObj.getEngineVer().getVerCode());
			pf.setFeature(Base64.decodeBase64(transferObj.getFeature()));
			pf.setFilePath(transferObj.getFilePath());
			pf.setFeatureType(transferObj.getFeatureType());
			pf.setEngineType(transferObj.getEngineVer().getEngineType());
			int ret = this.personFeatureService.insertSelective(pf);
			if(ret < 1) {
				throw new ServiceException("新增客户生物特征信息失败");
			}	
			p.setPf(pf);
			status = 1;
		} else {
			//更新生物特征信息
			p.getPf().setUpdateTime(curDate);
			p.getPf().setFeature(Base64.decodeBase64(transferObj.getFeature()));
			p.getPf().setFilePath(transferObj.getFilePath());
			int ret = this.personFeatureService.updateByPrimaryKeySelective(p.getPf());
			if(ret < 1) {
				throw new ServiceException("更新客户生物特征信息失败");
			}
		}
		
		if(Constants.Config.FACE_ENGINE_ISREG.equals(String.valueOf(EnumClass.StatusEnum.YES.getValue()))){
			//人脸注册到引擎
			transferObj.getEngineService().regFeature(transferObj.getFileData(), transferObj.getFeatureType(), p.getPf().getFeatureId(), status);
		}
		
		long endTime1 = new Date().getTime();
		transferObj.addHandleTimes("新增或更新客户生物特征耗时："+(endTime1-startTime1)+"毫秒");

	}	
	
	/**
	 * 创建客户信息
	 * @param transferObj
	 * @param feature 特征数据
	 * @param fileData 文件二进制数据
	 * @throws ServiceException 
	 */
	public void createPerson(TransferObj transferObj,String feature,String fileData) throws ServiceException {
		
		//请求对象
		RecogRequest recogRequest = transferObj.getRecogRequest();
		//设置注册的特征和文件保存路径
		transferObj.setFileData(fileData);
		transferObj.setFeature(feature);
		String regFilePath = this.saveFileData(recogRequest, EnumClass.LibTypeEnum.LIB_TYPE.getValue(), fileData);		
		transferObj.setFilePath(regFilePath);
		if(transferObj.getP() == null) {
			//添加客户信息
			this.addPerson(transferObj);	
		}
		//添加客户生物特征信息
		this.savePersonFeature(transferObj);	
	}
	
	@Override
	public void saveRecogFlowInfo(TransferObj transferObj) {	
		
		//将请求报文对象转成字符串，并且去掉文件数据
		RecogRequest recogRequest = transferObj.getRecogRequest();			
		//流水记录中清除联网核查文件和特征信息
		if(!StringUtils.isBlank(recogRequest.getNetCheckFileData())) {
			recogRequest.setNetCheckFileData(null);
		}
		if(!StringUtils.isBlank(recogRequest.getNetCheckFeature())) {
			recogRequest.setNetCheckFeature(null);
		}
		//设置不带文件和特征信息的请求报文串
		transferObj.setRequestString(this.dataParse.parseDataStr(recogRequest));		
		
		//设置流水结果
		Result result = transferObj.getResult();
		ChannelFlow flow = transferObj.getFlow();
		if(result != null && InterfaceConst.SUCCESS.equals(result.getCode())) {
			flow.setResult(EnumClass.FlowResultEnum.SUCESS.getValue());
		} else {
			flow.setResult(EnumClass.FlowResultEnum.FAIL.getValue());
		}
		
		//初始化基本的流水信息
		transferObj.initFlowInfo();		
		//保存流水
		this.channelFlowService.insertSelective(transferObj.getFlow());
	}
	
	 
	
	/**
	 * 从白名单或者阈值规则设置里面获取阈值
	 * @param transferObj
	 * @return
	 * @throws ServiceException 
	 */
	public double getThresholdScore(TransferObj transferObj) throws ServiceException {		
		long startTime1 = new Date().getTime();

		//请求对象
		RecogRequest request = transferObj.getRecogRequest();
		if(EnumClass.StatusEnum.YES.getValue() == transferObj.getQueryWhiteStatus()) {
			//根据机构，渠道，交易代码，客户信息获取白名单
			WhitePerson wp = new WhitePerson();
			wp.setLegalOrgCode(request.getOrgCodePath());
			wp.setChannel(request.getChannel());
			wp.setTradingCode(request.getTradingCode());
			wp.setEngineCode(request.getEngineCode());
			wp.setEngineverCode(transferObj.getEngineVer().getVerCode());
			wp.setCtftype(request.getCtftype());
			wp.setCtfno(request.getCtfno());
			wp = this.whitePersonService.getWhitePersonBykey(wp);
			if(wp != null) {
				if(wp.getScore() == null) throw new ServiceException("白名单阈值设置错误");
				return wp.getScore().doubleValue();
			}
		}
		
		//根据机构，渠道，交易获取阈值规则		
		//根据识别策略ID和引擎版本获取阈值规则
		Recognizerule rule = new Recognizerule();
		rule.setEngineCode(transferObj.getEngineVer().getEngineCode());
		rule.setRecogstepId(transferObj.getEngineVer().getRecogstepId());
		rule = this.recognizeruleService.selectRecogsetpRule(rule);
		if(rule == null) {
			throw new ServiceException("识别规则未设置");
		}	
		if(rule.getScore() == null) throw new ServiceException("识别规则阈值设置错误");
		
		long endTime1 = new Date().getTime();
		transferObj.addHandleTimes("从白名单或者阈值规则设置里面获取阈值耗时："+(endTime1-startTime1)+"毫秒");
		
		return rule.getScore().doubleValue();		
	}

	/**
	 * 设置当前交易成功返回结果
	 */
	public void setRetResult(TransferObj transferObj) throws ServiceException{
		
		//请求报文对象
		RecogRequest recogRequest = transferObj.getRecogRequest();
		//设置返回结果对象的请求数据
		RecogRequest request = new RecogRequest();
		request.setCtfno(recogRequest.getCtfno());
		request.setCtfname(recogRequest.getCtfname());
		request.setCtftype(recogRequest.getCtftype());
		request.setCustomerId(recogRequest.getCustomerId());
		transferObj.getResult().setRequest(request);
	}
	
	/**
	 * 设置整个交易返回结果
	 * @param transferObj
	 * @throws ServiceException 
	 */
	public void setTradingResult(TransferObj transferObj) throws ServiceException {		
		long startTime1 = new Date().getTime();
		
		//当前交易结果
		Result result = transferObj.getResult();
		boolean handleStatus = false;		
		
		if(result != null && InterfaceConst.SUCCESS.equals(result.getCode())) {
			
			//当前交易成功时，才去判断整个是否成功
			//请求对象
			RecogRequest request = transferObj.getRecogRequest();
			//获取对应的识别策略信息
			Map<String, Set<String>> stepMap = this.cacheService.getStepMap(transferObj.getEngineVer().getRecogstepId());
			if(stepMap == null || stepMap.isEmpty()) {
				throw new ServiceException("处理交易结果时，不能获取对应的识别策略");
			}		
			
			//设置当前处理成功的识别认证	
			Set<String> evSet = new HashSet<String>();
			String evString = transferObj.getEngineVer().getEngineCode()+Constants.SEPARATOR+transferObj.getEngineVer().getVerCode();
			evSet.add(evString);	
			
			//是否查询已认证成功的交易信息
			boolean isQueryTradingFLow = false;
			
			/*** 
			 * 如果当前已认证成功的引擎满足其中一个策略,整个交易成功
			 * 否则查询已认证成功的交易信息
			 */			
			Collection<Set<String>> stepList = stepMap.values();
			for(Set<String> tempEvSet:stepList) {
				if(tempEvSet.size() == 1 && tempEvSet.containsAll(evSet)) {				
					handleStatus = true;
					break;
				} else if(tempEvSet.size() > 1) {
					isQueryTradingFLow = true;
				}
			}
			
			if(!handleStatus && isQueryTradingFLow) {
				
				//根据业务流水号，接口代码，接口版本代码，识别策略ID获取已经交易成功的识别认证处理
				ChannelFlow record = new ChannelFlow();
				record.setFlowId(null);
				record.setTradingFlowNo(request.getTradingFlowNO());
				List<ChannelFlow> flows = this.channelFlowService.selectTradingFlows(record);
				
				//封装已成功处理的识别认证
				if(!ObjectUtils.isEmpty(flows)) {
					for(ChannelFlow flow:flows) {
						String tempEvString = flow.getEngineCode()+Constants.SEPARATOR+flow.getEngineVerCode();
						evSet.add(tempEvString);
					}
				}							
				
				//获取已经满足的策略			
				for(Set<String> tempEvSet:stepList) {
					if(tempEvSet.size() == evSet.size() && tempEvSet.containsAll(evSet)) {				
						handleStatus = true;
						break;
					}
				}
				
				//当前交易成功，但整个交易失败，才插入当前交易引擎信息
				if(!handleStatus) {
					//插入已认证成功的交易
					ChannelFlow tradingRecord = new ChannelFlow();
					tradingRecord.setTradingFlowNo(request.getTradingFlowNO());
					tradingRecord.setEngineCode(transferObj.getEngineVer().getEngineCode());
					tradingRecord.setEngineVerCode(transferObj.getEngineVer().getVerCode());
					tradingRecord.setCreateTime(new Date());
					this.channelFlowService.insertTradingFlow(tradingRecord);
				}
				
			}
			
		}
		
		//设置返回结果
		Response response = null;
		if(handleStatus) {
			//整个交易处理成功
			response = Response.getSuccessResponse(transferObj.getResult());
		} else {
			//整个交易处理失败
			response = Response.getFailResponse(transferObj.getResult());
		}
		transferObj.setResponse(response);
		
		long endTime1 = new Date().getTime();
		transferObj.addHandleTimes("设置整个交易返回结果耗时："+(endTime1-startTime1)+"毫秒");

	}
	
	
	/**
	 * 获取文件base64字符串
	 * @param url 文件路径
	 * @return
	 */
	public String getFileBase64Data(String url) {
		
		String base64Data = "";
		byte[] binaryData = null;
		//文件存储方式
		String fileStoreway = Constants.Config.FILE_STORAGE_WAY;
		if(EnumClass.FileAccessEnum.LOCAL.getValue().equals(fileStoreway)) {
			//本地获取时，路径为全路径
			binaryData = FileUtils.getFileDataByPath(url);
		} else if(EnumClass.FileAccessEnum.HTTP.getValue().equals(fileStoreway)) {
			binaryData = FileUtils.getFileDataByHttp(url);
		} else if(EnumClass.FileAccessEnum.HTTPS.getValue().equals(fileStoreway)) {
			binaryData = FileUtils.getFileDataByHttps(url);
		}
		if(binaryData != null) {
			base64Data = Base64.encodeBase64String(binaryData);
		}
		return base64Data;
	}
	
	/**
	 * 保存文件数据
	 * @param recogRequest
	 * @param libType 存库类型：库和流水
	 * @param fileBase64Data 文件数据
	 * @return 文件保存的相对路径
	 */
	public String saveFileData(RecogRequest recogRequest,String libType,String fileBase64Data) {
		
		//文件保存路径
		String saveFilePath = "";		
		if(StringUtils.isBlank(fileBase64Data)) return saveFilePath;
		//获取当前文件保存的本地路径
		fileBase64Data=fileBase64Data.replaceAll("\r\n","").replaceAll("\\s", "+");
		String sp = Constants.FILE_SPARATOR;
		String curdate = FileUtils.getCurDatePath();
		saveFilePath = sp+recogRequest.getEngineCode()+sp+libType+curdate+sp
				+recogRequest.getOrgCode()+sp+recogRequest.getChannel()+sp+recogRequest.getTradingCode();
		//保存文件
		saveFilePath = FileUtils.saveFileData(Base64.decodeBase64(fileBase64Data), saveFilePath, null);
		return saveFilePath;
	}
	
	/**
	 * 身份证识别
	 * @param transferObj
	 * @return
	 * @throws ServiceException 
	 */
	public IDCardData ocrIDCard(TransferObj transferObj) throws ServiceException {
		
		//请求报文对象
		RecogRequest request = transferObj.getRecogRequest();	
		//识别结果
		IDCardData data = new IDCardData();		
		
		/**初始化身份证正反面判断参数*/
		boolean isFront = false;
		boolean isBack = false;
		
		//正面识别
		if(!StringUtils.isBlank(request.getFrontImge())) {
			IDCardData frontData =transferObj.getEngineService().ocrIDCardInfo(request.getFrontImge(), EnumClass.IDCardTypeEnum.FRONT.getValue());
			if(frontData == null) {
				throw new ServiceException("身份证正面识别异常");
			}
			if ( 1 == frontData.getType() ) {
				
				isFront = true;
				
			}
			BeanUtil.copyProperty(data, frontData);
			data.setType(EnumClass.IDCardTypeEnum.FRONT.getValue());
		}
		
		//反面识别
		if(!StringUtils.isBlank(request.getBackImge())) {
			IDCardData blackData = transferObj.getEngineService().ocrIDCardInfo(request.getBackImge(), EnumClass.IDCardTypeEnum.BLACK.getValue());
			if(blackData == null) {
				throw new ServiceException("身份证反面识别异常");
			}
			
			if ( 0 == blackData.getType() ) {
				
				isBack = true;
				
			}
			
			BeanUtil.copyProperty(data, blackData);
			data.setType(EnumClass.IDCardTypeEnum.BLACK.getValue());
		}
		
		/**判断输入的照片是否为身份证的正反面*/
		if (  !isFront || !isBack ) {
			
			throw new ServiceException("身份证正反面照片输入有误");
			
		}
		
		if(!StringUtils.isBlank(request.getFrontImge()) && !StringUtils.isBlank(request.getBackImge())) {
			data.setType(EnumClass.IDCardTypeEnum.ALL.getValue());
		}
		
		return data;
	}
	
	/**
	 * 银行卡识别
	 * @param transferObj
	 * @return
	 * @throws ServiceException 
	 */
	public BankCardData ocrBankCard(TransferObj transferObj) throws ServiceException {
		
		//请求报文对象
		RecogRequest request = transferObj.getRecogRequest();	
		//识别结果
		BankCardData bankCardData = new BankCardData();				
		
		//银行卡识别
		if(!StringUtils.isBlank(request.getBankCardImg())) {
			bankCardData =transferObj.getEngineService().ocrBankCardInfo(request.getBankCardImg());
			if(bankCardData == null) {
				throw new ServiceException("银行卡识别异常");
			}			
		}	
		
		return bankCardData;
	}
	
	/**
	 * 活体检测
	 * @param transferObj
	 * @return
	 * @throws ServiceException 
	 */
	public Liveness judgeLiveness(TransferObj transferObj) throws ServiceException {
		
		//请求报文对象
		RecogRequest request = transferObj.getRecogRequest();	
		//识别结果		
		Liveness data =transferObj.getEngineService().jdugeLiveness(request.getLivenessData());
		if(data == null){
			throw new ServiceException("活体检测异常");
		}
		return data;
	}
	
	public EngineVerManageService getEngineVerManageService() {
		return engineVerManageService;
	}

	public void setEngineVerManageService(
			EngineVerManageService engineVerManageService) {
		this.engineVerManageService = engineVerManageService;
	}
}

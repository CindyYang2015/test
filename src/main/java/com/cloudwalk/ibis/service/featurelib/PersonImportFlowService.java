package com.cloudwalk.ibis.service.featurelib;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloudwalk.common.common.Constants;
import com.cloudwalk.common.enums.EnumClass;
import com.cloudwalk.common.exception.ServiceException;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.file.FileType;
import com.cloudwalk.ibis.model.featurelib.Person;
import com.cloudwalk.ibis.model.featurelib.PersonFeature;
import com.cloudwalk.ibis.model.featurelib.PersonImportFlow;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.repository.featurelib.PersonImportFlowMapper;
import com.cloudwalk.ibis.service.base.engine.EngineService;
import com.cloudwalk.ibis.service.base.engine.EngineVerManageService;
import com.cloudwalk.ibis.service.system.DicService;

@Service("personImportFlowService")
public class PersonImportFlowService {
	@Resource(name = "personImportFlowMapper")
	private PersonImportFlowMapper personImportFlowMapper;

	@Resource(name = "dicService")
	private DicService dicService;

	@Resource(name = "personService")
	private PersonService personService;

	@Resource(name = "personFeatureService")
	private PersonFeatureService personFeatureService;

	/*
	 * 引擎版本管理服务
	 */
	@Resource(name = "engineVerManage")
	protected EngineVerManageService engineVerManageService;

	private static Logger logger = LoggerFactory
			.getLogger(PersonImportFlowService.class);

	DecimalFormat df = new DecimalFormat("0.00"); // 保留几位小数

	/*
	 * 分页查询客户特征信息
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<PersonImportFlow> selectAllByPage(Map<String, Object> map) {
		List<PersonImportFlow> list = personImportFlowMapper
				.selectAllByPage(map);
		if (list != null && list.size() > 0) {
			return changeList(list);
		}
		return list;
	}

	/*
	 * 根据客户输入的特征信息查询特征信息列表
	 * 
	 * @param record
	 * 
	 * @return
	 */
	public List<PersonImportFlow> searchAll(PersonImportFlow record) {
		List<PersonImportFlow> list = personImportFlowMapper.searchAll(record);
		if (list != null && list.size() > 0) {
			return changeList(list);
		}
		return list;
	}

	/*
	 * 新增客户特征信息
	 * 
	 * @param record
	 * 
	 * @return
	 */
	public int insertSelective(PersonImportFlow record) {
		return personImportFlowMapper.insertSelective(record);
	}

	/*
	 * 修改客户特征信息
	 * 
	 * @param record
	 * 
	 * @return
	 */
	public int updateByPrimaryKeySelective(PersonImportFlow record) {
		return personImportFlowMapper.updateByPrimaryKeySelective(record);
	}

	/*
	 * 批量导入生物文件
	 * 
	 * @param filein
	 * 
	 * @param request
	 * 
	 * @param person
	 * 
	 * @param personFeature
	 * 
	 * @return
	 * 
	 * @throws ServiceException
	 */
	public JSONObject batchImport(InputStream filein,
			HttpServletRequest request, Person person,
			PersonFeature personFeature, FileType type) throws ServiceException {
		JSONObject json = new JSONObject();
		BufferedInputStream bufferedInputStream = null;
		ArchiveInputStream in = null;
		ByteArrayOutputStream out = null;
		User user = BaseUtil.getCurrentUser(request);
		try {
			// 读取压缩文件
			bufferedInputStream = new BufferedInputStream(filein);
			in = new ArchiveStreamFactory().createArchiveInputStream(
					ArchiveStreamFactory.JAR, bufferedInputStream);

			ArchiveEntry entry = null;
			logger.info("---批量导入人脸数据开始----");

			List<PersonImportFlow> listFail = new ArrayList<PersonImportFlow>();
			List<PersonImportFlow> listSucc = new ArrayList<PersonImportFlow>();
			List<Person> listPerson = new ArrayList<Person>();
			List<PersonFeature> listPersonFeatures = new ArrayList<PersonFeature>();

			// 根据引擎类型以及引擎版本号得到具体的算法 **/
			EngineService engine = this.engineVerManageService.getRecogService(
					personFeature.getEngineCode(),
					personFeature.getEngineVerCode());

			if (engine == null) {
				throw new ServiceException("获取算法引擎服务失败");
			}

			// 循环遍历压缩文件里面的生物特征文件 **/
			while ((entry = (ArchiveEntry) in.getNextEntry()) != null) {

				// 压缩包名称+"/"
				String name = entry.getName();
				if (entry.isDirectory()) {
					continue;
				}
				if (name.length() > 0) {
					// 解析生物文件文件名称 **/

					String[] keyArray = name.split("/");
					int fileLevel = keyArray.length;
					keyArray = keyArray[fileLevel - 1].split("_");

					String ctfno = "";
					String ctfname = "";
					String customerId = "";
					try {
						// 判断文件是否为图片文件目前支持:PNG,BMP,JPEG,JPG **/
						String fileSuffix = name.substring(name
								.lastIndexOf(".") + 1);
						if (!fileSuffix.equalsIgnoreCase("png")
								&& !fileSuffix.equalsIgnoreCase("jpg")
								&& !fileSuffix.equalsIgnoreCase("jpeg")
								&& !fileSuffix.equalsIgnoreCase("bmp")) {

							logger.info("解析生物文件文件名称发生异常：文件不是图片文件");
							continue;

						}
						ctfno = keyArray[0];
						ctfname = keyArray[1];
						customerId = keyArray[2].substring(0,
								keyArray[2].lastIndexOf("."));// 去掉后缀
					} catch (ArrayIndexOutOfBoundsException e) {
						logger.error("解析生物文件文件名称发生异常："
								+ e.getLocalizedMessage());
						listFail.add(makeImportFlowFail(request, person,
								"解析生物文件文件名称发生异常"));
						continue;
					}

					// 将从生物文件文件名称中获取到的值赋值给person **/
					person.setCtfno(ctfno);
					person.setCtfname(ctfname);
					person.setCustomerId(customerId);

					// 获取生物文件byte流 **/
					out = new ByteArrayOutputStream(1024);
					byte[] temp = new byte[1024];
					int size = 0;
					while ((size = in.read(temp)) != -1) {
						out.write(temp, 0, size);
					}
					byte[] outBuf = out.toByteArray();
					if (outBuf.length > 0) {

						// 生物文件大小验证
						long imgSize = Integer
								.valueOf(Constants.Config.BIOLOGY_SIZE) * 1024 * 1024;
						if (outBuf.length > imgSize) {
							logger.info("生物文件太大，不能超过"
									+ Constants.Config.BIOLOGY_SIZE + "M，图片名称："
									+ name);
							listFail.add(makeImportFlowFail(request, person,
									"生物文件太大，不能超过"
											+ Constants.Config.BIOLOGY_SIZE
											+ "M，图片名称：" + name));
						} else {

							// 初始化特征以及质量
							String feature = null;
							double weight = 0.0;
							// 文件保存的路径
							String path = this.personService.saveFile(outBuf,
									personFeature.getEngineCode(),
									user.getLegalCode());
							int featureType = EnumClass.FileTypeEnum.HDTV
									.getValue();
							// 根据算法引擎以及生物文件获取特征以及质量
							feature = engine.getFeature(
									Base64.encodeBase64String(outBuf),
									featureType) == null ? null : engine
									.getFeature(
											Base64.encodeBase64String(outBuf),
											featureType);

							// 如果特征提取失败
							if (feature == null) {
								listFail.add(makeImportFlowFail(request,
										person, "提取特征异常"));
							} else {
								// 得到客户信息
								Person human = makePerson(request, person);
								listPerson.add(human);

								// 得到客户特征信息
								listPersonFeatures.add(makePersonFeature(
										request, human, personFeature, weight,
										feature, path, featureType));

								listSucc.add(makeImportFlowSuccess(request,
										person, personFeature, path));
							}
						}
					} else {
						listFail.add(makeImportFlowFail(request, person,
								"生物文件异常"));
					}
				}
			}
			// 统计成功失败数量
			json.put("msg", "批量导入成功数:" + listSucc.size() + ",批量导入失败数:"
					+ listFail.size() + ",详情请查看导入记录日志");
			return json;
		} catch (Exception e) {
			throw (ServiceException) e;
		} finally {
			try {
				bufferedInputStream.close();
				in.close();
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("---批量导入人脸数据结束----");
		}
	}

	/*
	 * 得到导入失败记录
	 * 
	 * @param request
	 * 
	 * @param person
	 * 
	 * @param reason
	 */
	private PersonImportFlow makeImportFlowFail(HttpServletRequest request,
			Person person, String reason) {
		PersonImportFlow flow = new PersonImportFlow();
		flow.setOrgCode(BaseUtil.getCurrentUser(request).getOrgCodePath());
		flow.setDeptCode(BaseUtil.getCurrentUser(request).getDeptCode());
		flow.setCtftype(person.getCtftype());
		flow.setCtfno(person.getCtfno());
		flow.setCtfname(person.getCtfname());
		flow.setStatus("1");
		flow.setReason(reason);
		flow.setCreator(BaseUtil.getCurrentUser(request).getUserId());
		flow.setCreateTime(new Date());
		this.personImportFlowMapper.insertSelective(flow);
		return flow;
	}

	/*
	 * 得到导入成功记录
	 * 
	 * @param request
	 * 
	 * @param person
	 */
	private PersonImportFlow makeImportFlowSuccess(HttpServletRequest request,
			Person person, PersonFeature engine, String path) {
		PersonImportFlow flow = new PersonImportFlow();

		flow.setOrgCode(BaseUtil.getCurrentUser(request).getOrgCodePath());
		flow.setDeptCode(BaseUtil.getCurrentUser(request).getDeptCode());
		// 文件名称：从path里面截取
		String fileName = path.substring(path.lastIndexOf("/") + "/".length(),
				path.length());
		flow.setFileName(fileName);
		flow.setCtftype(person.getCtftype());
		flow.setCtfno(person.getCtfno());
		flow.setCtfname(person.getCtfname());
		flow.setStatus("0");
		flow.setReason("导入成功");
		flow.setCreator(BaseUtil.getCurrentUser(request).getUserId());
		flow.setCreateTime(new Date());
		flow.setFilePath(path);
		flow.setEngineCode(engine.getEngineCode());
		flow.setEngineverCode(engine.getEngineVerCode());
		flow.setEngineType(engine.getEngineType());
		flow.setCustomerId(person.getCustomerId());
		this.personImportFlowMapper.insertSelective(flow);
		return flow;
	}

	/*
	 * 得到客户信息
	 * 
	 * @param request
	 * 
	 * @param person
	 * 
	 * @return
	 */
	private Person makePerson(HttpServletRequest request, Person person) {
		Person human = new Person();
		human.setCreateTime(new Date());
		human.setCreator(BaseUtil.getCurrentUser(request).getUserId());
		human.setCtfname(person.getCtfname());
		human.setCtfno(person.getCtfno());
		human.setCtftype(person.getCtftype());
		human.setLegalOrgCode(BaseUtil.getCurrentUser(request)
				.getLegalOrgCode());
		human.setOrgName(BaseUtil.getCurrentUser(request).getOrgName());
		human.setCustomerId(person.getCustomerId());
		human.setProperty(person.getProperty());
		human.setRemark("批量导入数据");
		String personId = this.personService.insertOrUpdatePerson(human);
		if (personId == null) {
			return null;
		}
		return human;
	}

	/*
	 * 得到客户特征信息
	 * 
	 * @param request
	 * 
	 * @param person
	 * 
	 * @param weight
	 * 
	 * @param feature
	 * 
	 * @return
	 */
	private PersonFeature makePersonFeature(HttpServletRequest request,
			Person person, PersonFeature engine, double weight, String feature,
			String path, int featureType) {
		PersonFeature personFeature = new PersonFeature();
		personFeature.setPersonId(person.getPersonId());
		personFeature.setEngineType(engine.getEngineType());
		personFeature.setEngineCode(engine.getEngineCode());
		personFeature.setEngineVerCode(engine.getEngineVerCode());
		personFeature.setFeature(Base64.decodeBase64(feature));
		personFeature.setFeatureType(featureType);
		personFeature.setWeight(weight + "");
		// 保存文件路径
		personFeature.setFilePath(path);
		personFeature.setCreateTime(new Date());
		personFeature.setCreator(BaseUtil.getCurrentUser(request).getUserId());
		int i = this.personFeatureService
				.insertOrUpdatePersonFeature(personFeature);
		if (i == 0) {
			return null;
		}
		return personFeature;
	}

	public List<PersonImportFlow> changeList(List<PersonImportFlow> list) {
		String channel = dicService.selectDicValuesByDicType(null);
		JSONArray json = JSON.parseArray(channel);
		json.remove(0);
		if (list != null) {
			for (int j = 0; j < json.size(); j++) {
				for (int i = 0; i < list.size(); i++) {
					if (json.getJSONObject(j).getString("id")
							.equals(list.get(i).getCtftype())) {// 证件类型
						list.get(i).setCtftype(
								json.getJSONObject(j).getString("text"));
					}
				}
			}
		}
		return list;
	}
}

package com.cloudwalk.ibis.controller.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.BeanUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.common.util.StringUtil;
import com.cloudwalk.ibis.model.system.DicValues;
import com.cloudwalk.ibis.model.system.Organization;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.system.DicService;
import com.cloudwalk.ibis.service.system.LogOperService;
import com.cloudwalk.ibis.service.system.OrganizationService;
import com.google.common.collect.Lists;

/**
 * ClassName:OrganizationController <br/>
 * Date: 2015年3月26日 上午11:10:24 <br/>
 *
 * @author lidaiyue
 * @version
 * @since JDK 1.7
 * @see
 */
@Controller
@RequestMapping("/organization")
public class OrganizationController extends BaseController {

	// 列表页面
	private final String LIST_INDEX = "platform/system/organization";
	// 编辑页面
	private final String LIST_EDIT = "platform/system/organizationEdit";

	@Resource(name = "organizationService")
	private OrganizationService organizationService;

	@Resource(name = "logOperService")
	private LogOperService logOperService;
	
	@Resource(name="dicService")
	private DicService dicService;
	/**
	 * index:跳转至单位新增页面方法 <br/>
	 * 适用条件：登录成功，跳转至单位新增页面<br/>
	 * 只能在已存在的机构下面新建机构(初始化)
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @return String 跳转地址
	 * @throws IOException
	 */
	@RequestMapping(value = "/create")
	public String create(HttpServletRequest request,
			HttpServletResponse response, Model model, Organization organization)
					throws IOException {
		String orgCodeString = organization.getOrgCode();
		String parentOrgLevel=organizationService.selectByPrimaryKey(orgCodeString).getOrgLevel();
		if (!ObjectUtils.isEmpty(orgCodeString)) {
			organization.setParentCode(orgCodeString);
			organization.setParentOrgLevel(parentOrgLevel);
			organization.setOrgCode(null);
		}
		model.addAttribute("action", "create");
		model.addAttribute("organization", organization);
		return LIST_EDIT;
	}

	/**
	 *
	 * createorganization:新增一个单位. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 下午4:02:28
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	private void createorganization(HttpServletRequest request,
			HttpServletResponse response, Organization record) {

		//返回结果
		String retJson = "";
		
		// 新增,验证该单位编号是否存在
		String orgCodeString = record.getOrgCode();
		if (ObjectUtils.isEmpty(orgCodeString)) {
			// 异常，单位编码必须输入		
			retJson = ControllerUtil.getFailRetJson("请输入单位编码");
			getPrintWriter(response, retJson);
			return;
		}

		try {			
			Organization organization = this.organizationService
					.selectByPrimaryKey(orgCodeString);
	
			if (organization != null) {
				// 异常，单位编码重复，请重新输入			
				retJson = ControllerUtil.getFailRetJson("单位编码重复，请重新输入");
				getPrintWriter(response, retJson);
				return;
			}
			Organization parentOrganization=organizationService.selectByPrimaryKey(record.getParentCode());
			//异常：非法人机构下面不可以建法人机构
			if ( parentOrganization.getLegalStatus() != 1 && record.getLegalStatus() == 1 ) {
				retJson = ControllerUtil.getFailRetJson("非法人机构下面不能新建法人机构！");
				getPrintWriter(response, retJson);
				return;
			}
			//拼接机构代码全路径  
			String orgCodePath=parentOrganization.getOrgCodePath();
			orgCodePath=orgCodePath+"@"+record.getOrgCode();
			record.setOrgCodePath(orgCodePath);
			
			//拼接机构全名
			String orgAname=parentOrganization.getOrgAname();
			orgAname=orgAname+"-"+record.getOrgName();
			record.setOrgAname(orgAname);
			
			//经营状态：默认为可用: 1可用、 2 暂停
			record.setStatus(1);
			//新增单位信息
			if (this.organizationService.insertSelective(record) < 1) {									
				retJson = ControllerUtil.getFailRetJson("新增单位信息失败");				
			} else {
				//更新节点时，编辑直接更新节点，而不是从数据库查询
				DicValues dic=new DicValues();
				dic.setDicCode(record.getOrgLevel());
				dic=dicService.selectByPrimaryKey(dic);
				record.setOrgLevel(dic.getMeaning());
				
				retJson = ControllerUtil.getSuccessRetJson(record,"1","新增单位信息成功");
				//添加操作日志
				String remark = "新增";
				remark = remark + "单位信息，详情：" + record.toString();
				logOperService.insertLogOper(request, 1, remark);
			}
	
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);

	}

	/**
	 *
	 * deleteJson:删除机构信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月20日 上午9:34:35
	 * @param request
	 * @param response
	 * @param id
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/deleteJson")
	public void deleteJson(HttpServletRequest request,
			HttpServletResponse response, String id) {
		
		//返回结果
		String retJson = "";
		if (ObjectUtils.isEmpty(id)) {			
			retJson = ControllerUtil.getFailRetJson("请至少选中一行记录");
			getPrintWriter(response, retJson);
			return;
		}
		
		try {
			// 判断当前节点是否为根节点，及子节点
			Organization organization = this.organizationService
					.selectByPrimaryKey(id);
	
			if (organization == null) {			
				retJson = ControllerUtil.getFailRetJson("该单位不存在，请重新选择");
				getPrintWriter(response, retJson);
				return;
			}
			
			//判断是是否为根节点
			if (ObjectUtils.isEmpty(organization.getParentCode())) {			
				retJson = ControllerUtil.getFailRetJson("顶级单位不可以删除");
				getPrintWriter(response, retJson);
				return;
			}
			
			// 判断该节点是否有子节点
			if (this.organizationService.isExistsChildOrg(id)) {			
				retJson = ControllerUtil.getFailRetJson("该单位下面有子机构，不能删除");
				getPrintWriter(response, retJson);
				return;
			}
	
			// 判断该节点是否有部门
			if (this.organizationService.isExistsChildDept(organization.getOrgCodePath())) {			
				retJson = ControllerUtil.getFailRetJson("该单位下面有部门，不能删除");
				getPrintWriter(response, retJson);
				return;
			}
	
			int deleteCount = this.organizationService.deleteOrganization(id);
			if(deleteCount > 0) {
				retJson = ControllerUtil.getSuccessRetJson("删除成功");
				//添加操作日志
				String remark = "删除";
				remark = remark + "单位，详情为：" + organization.toString();
				logOperService.insertLogOper(request, 1, remark);
			} else {
				retJson = ControllerUtil.getFailRetJson("删除失败");
			}
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
				
		getPrintWriter(response, retJson);
	}

	/**
	 * index:跳转至单位编辑页面方法 <br/>
	 * 适用条件：登录成功，跳转至单位编辑页面<br/>
	 *
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @return String 跳转地址
	 * @throws IOException
	 */
	@RequestMapping(value = "/edit")
	public String edit(HttpServletRequest request,
			HttpServletResponse response, Model model, Organization organization)
					throws IOException {
		model.addAttribute("action", "edit");
		if (organization == null || organization.getOrgCode() == null
				|| organization.getOrgCode().equals("")) {
			return LIST_EDIT;
		} else {
			organization = organizationService.selectByPrimaryKey(organization
					.getOrgCode());
		}
		model.addAttribute("organization", organization);
		return LIST_EDIT;
	}

	/**
	 * index:跳转至单位页面方法 <br/>
	 * 适用条件：登录成功，跳转至单位页面<br/>
	 * 执行流程：AJAX调用<br/>
	 *
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @return String 跳转地址
	 * @throws IOException
	 */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		return LIST_INDEX;
	}

	/**
	 * insertSelective:(新增机构信息). <br/>
	 *
	 * @author:李戴月 Date: 2015年3月26日 上午11:12:53
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/insertSelective")
	public void insertSelective(HttpServletRequest request,
			HttpServletResponse response, Organization record) {
		getPrintWriter(response, organizationService.insertSelective(record));

	}
	/**
	 * 
	* @Title: selectOrgLevel 
	* @Description: 只查询比自己ORG_LEVEL 值大的字典信息
	* @param @param request
	* @param @param response
	* @param @param dicValues    设定文件 
	* @return void    返回类型 
	* @author huyuxin
	* @throws
	 */
	@RequestMapping(value = "selectOrgLevel")
	public void selectOrgLevel(HttpServletRequest request,
			HttpServletResponse response, DicValues dicValues){
		dicValues.setDicType("7");
		String jsonResult = "";
		try {
		// 调用service方法，得到要返回的数据，拼接成json字符串		
			List<DicValues> list= dicService.selectDicValuesByDicTypeAndDicCode(dicValues);
			if(null!=list){
				jsonResult = JsonUtil.toJSON(list);
			}
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		getPrintWriter(response, jsonResult);
	}
	
	/**
	 *
	 * saveOrganization:保存单位信息（新增，更新）. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 下午4:22:37
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/saveOrganization")
	public void saveOrganization(HttpServletRequest request,
			HttpServletResponse response, Organization record) {
		// 判断当前是更新，还是添加
		String action = request.getParameter("action");
		if ("create".equals(action)) {
			this.createorganization(request, response, record);
		} else if ("edit".equals(action)) {
			this.updateOrganization(request, response, record);
		}		
	}

	/**
	 * selectAllOrganizationInfo:(查询所有机构信息). <br/>
	 *
	 * @author:李戴月 Date: 2015年4月30日 上午9:30:00
	 * @param request
	 * @param response
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "selectAllOrganizationInfo")
	public void selectAllOrganizationInfo(HttpServletRequest request,
			HttpServletResponse response) {
		//返回结果
		String retJson = "";
		try {
			// 调用service方法，得到要返回的数据，拼接成json字符串
			List<Organization> list = organizationService
					.selectAllOrganizationInfo();
			int total = list.size();
			StringBuffer jsonResult = new StringBuffer("{\"total\":" + total
					+ ",\"rows\":");
			// 为了得到easyui显示树形的格式，需要将父级ID替换为"_parentId"
			String str = JsonUtil.toJSON(list).replaceAll(
					"OrganizationParentId", "_parentId");
			jsonResult.append(str);
			jsonResult.append("}");
			retJson = jsonResult.toString();
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		
		getPrintWriter(response, retJson);
	}

	/**
	 * selectBlankOrg:(查询用户机构下属列支机构). <br/>
	 *
	 * @author:李戴月 Date: 2015年3月26日 上午11:12:53
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectOrganizationOrg")
	public void selectBlankOrg(HttpServletRequest request,
			HttpServletResponse response, String organizationId) {
		//返回结果
		String jsonResult = null;
		try {
			if (organizationId != null) {
				List<Organization> organizationList = organizationService
						.selectOrganizationOrg(organizationId);
				if (organizationList != null) {
					jsonResult = JsonUtil.toJSON(organizationList);
				}
			}
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		// 调用service方法，进行删除操作
		getPrintWriter(response, jsonResult);
	}

	/**
	 * selectByPrimaryKey:(查询指定机构的信息). <br/>
	 *
	 * @author:李戴月 Date: 2015年3月26日 上午11:12:53
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectByPrimaryKey")
	public void selectByPrimaryKey(HttpServletRequest request,
			HttpServletResponse response, String organizationId) {
		//返回结果
		String jsonResult = "";
		try {
			// 调用service方法，查询指定机构信息
			Organization organization = organizationService
					.selectByPrimaryKey(organizationId);
			jsonResult = JsonUtil.toJSON(organization);
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		
		getPrintWriter(response, jsonResult);
	}

	/**
	 *
	 * selectPageOrganizationInfoByParent:(根据父节点分页查询所有机构信息). 
	 * hyx:条件查询和父节点查询方法已经分开。本方法为单纯的根据父节点展开子节点操作，所以需要避免文本框的查询条件
	 *
	 * @author:朱云飞 Date: 2015年8月17日 下午4:45:53
	 * @param request
	 * @param response
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "selectPageOrganizationInfoByParent")
	public void selectPageOrganizationInfoByParent(HttpServletRequest request,
			HttpServletResponse response, Organization record,
			PageInfo pageInfo, String istree) {

		List<Organization> list = Lists.newArrayList();
		//返回结果
		String retstring = "";
		record.setOrgLevel("");
		record.setLegalStatus(null);
		try {
			// 获得当前登录对象信息
			User user=BaseUtil.getCurrentUser(request);
			// 用户里面取得的机构代码
			String uOrgCode=user.getOrgCode();
			//首页（初次查询），只显示根节点
			if (!"1".equals(istree)) {
				record.setOrgCode(uOrgCode);
				// 搜索控制
				if (!ObjectUtils.isEmpty(record.getOrgCode())) {
					record.setParentCode("-1");
				}
			}
			// 当为根节点时，给根节点的父节点机构代码设为“0”
			if (record.getParentCode() == null || record.getParentCode().equals("")
					|| record.getParentCode().equals("-1")) {
				list=organizationService.searchAllOrganization(record);
				if(!ObjectUtils.isEmpty(list)) {
					for(Organization org:list) {
						if(ObjectUtils.isEmpty(org.getParentCode())) {
							org.setParentCode("0");
						}
					}
				}
			} else{
				// 当文本框输入输入条件后，再点击节点展开，应该将文本框的值忽略，只需要parentCode
				record.setOrgCode(null);
				record.setOrgName(null);
				record.setLegalStatus(null);
				record.setOrgLevel(null);
				list = organizationService.searchAllOrganization(record);
			}
			retstring = JsonUtil.toJSON(list);
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retstring = JsonUtil.toJSON(list);
		}
		getPrintWriter(response, retstring);
		
	}
	/**
	 * 
	* @Title: selectPageOrganizationInfo 
	* @Description: 根据查询条件分页查询
	* @param @param request
	* @param @param response
	* @param @param record
	* @param @param pageInfo
	* @param @param istree    设定文件 
	* @return void    返回类型 
	* @author huyuxin
	* @throws
	 */
	@RequestMapping(value = "selectPageOrganizationInfo")
	public void selectPageOrganizationInfo(HttpServletRequest request,
			HttpServletResponse response, Organization record,
			PageInfo pageInfo, String istree){
		List<Organization> list = Lists.newArrayList();
		//返回结果
		String retstring = "";
		try {
			// 获得当前登录对象信息
			User user=BaseUtil.getCurrentUser(request);
			// 用户里面取得的机构代码
			String uOrgCode = user.getOrgCode();
			// 用户里面取得的机构代码全路径
			String uOrgCodePath = user.getOrgCodePath();
		   //1.查询全路径是否包含用户里面取得的机构代码：控制用户只能查询自己单位下面的子单位
			record.setOrgCodePath(uOrgCodePath);
			//根据文本框条件查出满足的所有单位
			List<Organization> orgList=organizationService.selectOrgsByEx(record);
			
			if(!ObjectUtils.isEmpty(orgList)) {
				//遍历父节点
				for(int i=0;i<orgList.size();i++) {
					Organization org = orgList.get(i);
					if(org.getOrgCode().equals(uOrgCode)) {
						list.add(org);
						orgList.remove(i);
						break;
					}
				}
				//递归遍历机构列表
				selectOrgsByRecursion(orgList,list);
			}					
			
			retstring = JsonUtil.toJSON(list);
			getPrintWriter(response, retstring);
			return;
			
			
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retstring = JsonUtil.toJSON(list);
		}
		
		getPrintWriter(response, retstring);
	}

	/**
	 * 
	* @Title: selectByRecursion 
	* @Description: 树节点递归
	* @param @param list 结果集
	* @param @param child 子元素
	* @param @param result 最终结果 
	* @return void    返回类型 
	* @author huyuxin
	* @throws
	 */
	public void selectByRecursion(List<Organization> list, Organization child, Organization result){
		// 查询条件为orgCode
		Organization query = new Organization();
		
		// 遍历所有的结果集
		for(Organization organization : list){
			
			// 子元素集
			List<Organization> children = new ArrayList();
			
			// 判断是否存在父元素，如果存在，则继续递归，不存在则子元素集到根元素
			if (StringUtil.isBlank(organization.getParentCode())) {
				if (null != result.getChildren()) {
					result.getChildren().add(child);
				} else {
					BeanUtil.copyProperty(result, organization);
					children.add(child);
					result.setChildren(children);
				}
			} else {
				// 把上一层的元素赋予当前元素
				if (null != child) {
					children.add(child);
					organization.setChildren(children);
				}
				
				// 添加查询条件，查询并进行递归
				query.setOrgCode(organization.getParentCode());
				List<Organization> pList = organizationService.searchAllOrganization(query);
				selectByRecursion(pList, organization, result);
			}
		}
	}	
		
	/**
	 * 递归查询机构信息
	 * @param orglist 待查询的机构信息
	 * @param parentOrglist 当前父节点机构信息
	 */
	public void selectOrgsByRecursion(List<Organization> orglist, List<Organization> parentOrglist){
		if(ObjectUtils.isEmpty(parentOrglist) || ObjectUtils.isEmpty(orglist)) return;
		//遍历父节点机构的子节点信息
		for(Organization parentOrg:parentOrglist) {	
			//当前父节点下的子节点数据列表
			List<Organization> childOrgList = new ArrayList<Organization>();
			for(int i=0;i<orglist.size();i++) {
				Organization org = orglist.get(i);
				if(!StringUtil.isEmpty(org.getParentCode())&&org.getParentCode().equals(parentOrg.getOrgCode())) {
					//如果当前节点被父节点找到，将该节点添加到父节点的子列表数据里面，并从查询机构数据列表中移除
					childOrgList.add(org);
					orglist.remove(i);
					i--;
				} else {
					continue;
				}				
			}
			if(ObjectUtils.isEmpty(childOrgList)) continue;
			parentOrg.setChildren(childOrgList);
			selectOrgsByRecursion(orglist,childOrgList);
		}
	}
	
	/**
	 *
	 * selectTreeOrgAndDeptInfo:查询单位和该单位的部门信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午8:32:35
	 * @param request
	 * @param response
	 * @param orgCode
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectTreeOrgAndDeptInfo")
	public void selectTreeOrgAndDeptInfo(HttpServletRequest request,
			HttpServletResponse response, String orgCode) {
		//返回结果
		String jsonResult = "";
		try {
			//获取当前登录用户的信息
			User user=BaseUtil.getCurrentUser(request);
			// 调用service方法，查询所有机构的信息
			List<Organization> orgList = this.organizationService
					.selectTreeOrgAndDept(user);
			jsonResult = JSONArray.toJSONString(orgList);
			jsonResult = jsonResult.replaceAll("orgName", "text");
			jsonResult = jsonResult.replaceAll("orgCode", "id");
			// jsonResult = jsonResult.replaceAll("fax", "iconCls");
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		// 将机构集合转换为json字符串，响应给客户端
		getPrintWriter(response, jsonResult);
	}

	/**
	 * selectOrganizationInfo:(查询所有机构信息(下拉数查询)). <br/>
	 *
	 * @author:李戴月 Date: 2015年4月30日 上午9:52:56
	 * @param request
	 * @param response
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectTreeOrganizationInfo")
	public void selectTreeOrganizationInfo(HttpServletRequest request,
			HttpServletResponse response) {
		//返回结果
		String jsonResult = "";
		try {
			// 调用service方法，查询所有机构的信息
			// 获得当前登录对象信息
			User user=BaseUtil.getCurrentUser(request);
			String orgCodePath=user.getOrgCodePath();
			//String orgCodePath="0000@333333";
			//封装查询对象
			Organization organization=new Organization();
			organization.setOrgCodePath(orgCodePath);
			//将方法修改为 带参的
			List<Organization> organizations = organizationService
					.selectTreeOrganizationInfo(organization);
			jsonResult = JSONArray.toJSONString(organizations);
			jsonResult = jsonResult.replaceAll("orgName", "text");
			jsonResult = jsonResult.replaceAll("orgCode", "id");
			// jsonResult = jsonResult.replaceAll("fax", "iconCls");
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		// 将机构集合转换为json字符串，响应给客户端
		getPrintWriter(response, jsonResult);
	}

	/**
	 * updateByPrimaryKeySelective:(修改机构信息). <br/>
	 *
	 * @author:李戴月 Date: 2015年3月26日 上午11:12:53
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/updateByPrimaryKeySelective")
	public void updateByPrimaryKeySelective(HttpServletRequest request,
			HttpServletResponse response, Organization record) {
		// 调用service方法，将结果响应给客户端
		getPrintWriter(response,
				organizationService.updateByPrimaryKeySelective(record));
	}

	/**
	 *
	 * updateOrganization:更新单位信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 下午4:05:38
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	private void updateOrganization(HttpServletRequest request,
			HttpServletResponse response, Organization record) {
		//返回结果
		String retJson = "";
		try {			
			Organization parentOrganization=organizationService.selectByPrimaryKey(record.getParentCode());
			//拼接机构全名
			String orgAname=parentOrganization.getOrgAname();
			orgAname=orgAname+"-"+record.getOrgName();
			record.setOrgAname(orgAname);
			//更新
			int status = this.organizationService
					.updateByPrimaryKeySelective(record);
			
			if (status < 1) {				
				retJson = ControllerUtil.getFailRetJson("更新失败");
			} else {
				//更新节点时，编辑直接更新节点，而不是从数据库查询
				DicValues dic=new DicValues();
				dic.setDicCode(record.getOrgLevel());
				dic=dicService.selectByPrimaryKey(dic);
				record.setOrgLevel(dic.getMeaning());
				
				retJson = ControllerUtil.getSuccessRetJson(record,"更新成功");
				//添加操作日志
				String remark = "更新";
				remark = remark + "单位信息，详情：" + record.toString();
				logOperService.insertLogOper(request, 1, remark);
			}
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);

	}

}

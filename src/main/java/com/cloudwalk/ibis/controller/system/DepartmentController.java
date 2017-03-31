package com.cloudwalk.ibis.controller.system;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.system.Department;
import com.cloudwalk.ibis.model.system.Organization;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.system.DepartmentService;
import com.cloudwalk.ibis.service.system.LogOperService;
import com.cloudwalk.ibis.service.system.OrganizationService;

/**
 *
 * ClassName: DepartmentController <br/>
 * Description: 部门. <br/>
 * date: 2015年8月19日 下午1:48:43 <br/>
 *
 * @author Jackson He
 * @version
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/department")
public class DepartmentController extends BaseController {

	// 列表页面
	private final String LIST_INDEX = "platform/system/department";
	// 编辑页面
	private final String LIST_EDIT = "platform/system/departmentEdit";

	@Resource(name = "departmentService")
	private DepartmentService departmentService;

	@Resource(name = "logOperService")
	private LogOperService logOperService;
	
	@Resource(name = "organizationService")
	private OrganizationService organizationService;
	/**
	 * index:跳转至部门新增页面方法 <br/>
	 * 适用条件：登录成功，跳转至部门新增页面<br/>
	 *
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @return String 跳转地址
	 * @throws IOException
	 */
	@RequestMapping(value = "/create")
	public String create(HttpServletRequest request,
			HttpServletResponse response, Model model, Department department)
					throws IOException {
		String orgName = request.getParameter("orgName");
		if (!ObjectUtils.isEmpty(orgName)) {
			orgName = URLDecoder.decode(orgName, "UTF-8");
		}
		department.setOrgName(orgName);
		model.addAttribute("action", "create");
		return LIST_EDIT;
	}

	/**
	 *
	 * createDepartment:新增一个部门. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 下午4:02:28
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	private void createDepartment(HttpServletRequest request,
			HttpServletResponse response, Department record) {

		//返回结果
		String retJson = "";
		// 新增部门编号是否为空
		String deptCodeString = record.getDeptCode();
		if (ObjectUtils.isEmpty(deptCodeString)) {
			// 异常，部门编码必须输入			
			retJson = ControllerUtil.getFailRetJson("请输入部门编码");
			getPrintWriter(response, retJson);
			return;
		}
		
		try {
			//判断部门编码是否重复
			Department department = this.departmentService
					.selectByPrimaryKey(deptCodeString);
	
			if (department != null) {
				// 异常，部门编码重复，请重新输入		
				retJson = ControllerUtil.getFailRetJson("部门代码重复，请重新输入");
				getPrintWriter(response, retJson);
				return;
			}
			
			Organization organization=organizationService.selectByPrimaryKey(deptCodeString);
			if(organization!=null){
				retJson = ControllerUtil.getFailRetJson("部门代码不能与机构代码重复，请重新输入");
				getPrintWriter(response, retJson);
				return;
			}
			
			//从页面获得的机构编码，查询出该机构，给部门机构全路径、机构名称赋值
	//		Organization  organization=organizationService.selectByOrgCodePath(record.getOrgCode());
	//		record.setOrgName(organization.getOrgName());
			if (this.departmentService.insertSelective(record) < 1) {			
				retJson = ControllerUtil.getFailRetJson("新增部门信息失败");				
			} else {
				retJson = ControllerUtil.getSuccessRetJson(department, "新增部门信息成功");
				//添加操作日志
				String remark = "新增";
				remark = remark + "部门，详情为：" + record.toString();
				logOperService.insertLogOper(request, 1, remark);
			}			
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);

	}

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
			Department department = this.departmentService.selectByPrimaryKey(id);
	
			if (department == null) {				
				retJson = ControllerUtil.getFailRetJson("该部门不存在，请重新选择");
				getPrintWriter(response, retJson);
				return;
			}
	
			// 判断该节点是否有人员
			if (this.departmentService.isExistsChildUser(id)) {				
				retJson = ControllerUtil.getFailRetJson("该部门下面有人员，不能删除");
				getPrintWriter(response, retJson);
				return;
			}
	
			int deleteCount = this.departmentService.deleteDepartment(id);
			if(deleteCount > 0) {
				retJson = ControllerUtil.getSuccessRetJson("删除成功");
				//添加操作日志
				String remark = "删除";
				remark = remark + "部门，详情为：" + department.toString();
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
	 * index:跳转至部门编辑页面方法 <br/>
	 * 适用条件：登录成功，跳转至部门编辑页面<br/>
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
			HttpServletResponse response, Model model, Department department)
					throws IOException {

		String orgName = request.getParameter("orgName");
		if (!ObjectUtils.isEmpty(orgName)) {
			orgName = URLDecoder.decode(orgName, "UTF-8");
		}

		model.addAttribute("orgName", orgName);
		model.addAttribute("action", "edit");

		if (department == null || department.getDeptCode() == null
				|| department.getDeptCode().equals("")) {
			return LIST_EDIT;
		} else {
			department = departmentService.selectByPrimaryKey(department
					.getDeptCode());
		}

		model.addAttribute("department", department);
		return LIST_EDIT;
	}

	/**
	 * index:跳转至部门页面方法 <br/>
	 * 适用条件：登录成功，跳转至部门页面<br/>
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
		User u = BaseUtil.getCurrentUser(request);
		model.addAttribute("orgCode", u.getOrgCode());
		return LIST_INDEX;
	}

	/**
	 * insertSelective:(新增机构信息). <br/>
	 * TODO(这里描述这个方法适用条件 – AJAX请求).<br/>
	 *
	 * @author:李戴月 Date: 2015年3月26日 上午11:12:53
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/insertSelective")
	public void insertSelective(HttpServletRequest request,
			HttpServletResponse response, Department record) {
		getPrintWriter(response, departmentService.insertSelective(record));

	}

	/**
	 *
	 * saveDepartment:保存部门信息（新增，更新）. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 下午4:22:37
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/saveDepartment")
	public void saveDepartment(HttpServletRequest request,
			HttpServletResponse response, Department record) {
		// 判断当前是更新，还是添加
		String action = request.getParameter("action");
		if ("create".equals(action)) {
			this.createDepartment(request, response, record);
		} else if ("edit".equals(action)) {
			this.updateDepartment(request, response, record);
		}		
	}

	/**
	 *
	 * selectDepartmentByPage:分页查询部门信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月19日 下午2:20:22
	 * @param request
	 * @param response
	 * @param record
	 * @param pageInfo
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectDepartmentByPage")
	public void selectDepartmentByPage(HttpServletRequest request,
			HttpServletResponse response, Department record, PageInfo pageInfo) {

		this.setPageInfo(request, pageInfo);
		JsonListResult<Department> result = new JsonListResult<Department>();
		//返回结果
		String retJson = "";
		try {
			// 设置分页查询
			Map<String, Object> map = new HashMap<String, Object>();
			// 设置分页信息
			map.put("obj", record);
			map.put("page", pageInfo);
	
			List<Department> list = this.departmentService
					.selectDepartmentByPage(map);
	
			// 设置返回结果
			result.setRows(list);
			result.setTotal(Long.valueOf(pageInfo.getTotalCount()));
			retJson = JsonUtil.toJSON(result);
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);

	}

	/**
	 *
	 * updateDepartment:更新部门信息. <br/>
	 *
	 * @author:朱云飞 Date: 2015年8月18日 下午4:05:38
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	private void updateDepartment(HttpServletRequest request,
			HttpServletResponse response, Department record) {
		//返回结果
		String retJson = "";
		try {
			int status = this.departmentService.updateByPrimaryKeySelective(record);
			if (status < 1) {
				retJson = ControllerUtil.getFailRetJson("部门更新失败");
			} else {
				retJson = ControllerUtil.getSuccessRetJson("部门更新成功");
				//添加操作日志
				String remark = "更新";
				remark = remark + "部门信息，详情：" + record.toString();
				logOperService.insertLogOper(request, 1, remark);
			}
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response, retJson);

	}

}

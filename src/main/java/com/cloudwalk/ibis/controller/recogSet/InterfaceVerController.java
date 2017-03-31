package com.cloudwalk.ibis.controller.recogSet;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.cloudwalk.ibis.model.recogSet.InterfaceVer;
import com.cloudwalk.ibis.model.recogSet.vo.InterfaceVerVo;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.recogSet.InterfaceService;
import com.cloudwalk.ibis.service.recogSet.InterfaceVerService;
import com.cloudwalk.ibis.service.system.LogOperService;

/**
 * 
 * ClassName: InterfaceVerController 
 * Description: 接口管理controller.
 * date: 2016年9月29日 下午1:36:15 
 *
 * @author 胡钰鑫
 * @version 
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/interfaceVer")
public class InterfaceVerController extends BaseController {
	@Resource(name = "interfaceService")
	private InterfaceService interfaceService;
	
	@Resource(name = "interfaceVerService")
	private InterfaceVerService interfaceVerService;
	//日志操作
	@Resource(name = "logOperService")
	private LogOperService logOperService;

	/**
	 * 
	 * interfaceList:跳转到列表页面.
	 * @author:胡钰鑫   Date: 2016年9月29日 下午1:39:10
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/interfaceVerList")
	public String interfaceList(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return  "platform/recogSet/interfaceVer";
	}
	
	/**
	 * 
	 * selectInterfaceVerByPage:精确分页查询.
	 * @author:胡钰鑫   Date: 2016年9月29日 下午1:40:36
	 * @param request
	 * @param response
	 * @param record
	 * @param pageInfo
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectInterfaceVerByPage")
	public void selectInterfaceVerByPage(HttpServletRequest request,
			HttpServletResponse response, InterfaceVer record, PageInfo pageInfo) {
		this.setPageInfo(request, pageInfo);
		JsonListResult<InterfaceVerVo> result = new JsonListResult<InterfaceVerVo>();
		String retJson = "";
		// 封装分页查询所需要的信息
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("obj", record);
		map.put("page", pageInfo);
		try {
			
			List<InterfaceVerVo> list = this.interfaceVerService.selectInterfaceVerByPage(map);
	
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
	 * create:跳转到新增页面.
	 * @author:胡钰鑫   Date: 2016年9月29日 下午1:42:51
	 * @param request
	 * @param response
	 * @param model
	 * @param interfaceVer
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/create")
	public String create(HttpServletRequest request,
			HttpServletResponse response, Model model,InterfaceVer interfaceVer){
		//查询出所有接口信息，提供下拉菜单，interfaceCode
		model.addAttribute("action", "add");	
		model.addAttribute("interfaceVer", interfaceVer);
		return "platform/recogSet/interfaceVerEdit";
	}
	/**
	 * 
	 * edit:跳转到编辑页面（同新增页面）.
	 * @author:胡钰鑫   Date: 2016年9月28日 下午6:48:53
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/edit")
	public String edit(HttpServletRequest request,
			HttpServletResponse response, Model model,InterfaceVer interfaceVer){
		model.addAttribute("action", "edit");	
		//如果传回的id值为空，则直接跳转到编辑页面,做新增理解
		if(interfaceVer == null || StringUtils.isBlank(interfaceVer.getId())){
			return "platform/recogSet/interfaceVerEdit";
		} else {
			//根据id查询出对象
			interfaceVer=interfaceVerService.getInterfaceVer(interfaceVer.getId());
		}
		model.addAttribute("interfaceVer", interfaceVer);
		return "platform/recogSet/interfaceVerEdit";
	}
	
	/**
	 * 
	 * saveInterfaceVer:保存接口信息.
	 * @author:胡钰鑫   Date: 2016年9月28日 下午4:13:54
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/saveInterfaceVer")
	public void saveInterfaceVer(HttpServletRequest request,
			HttpServletResponse response, InterfaceVer interfaceVer){
		//判断当前是新增还是修改
		String action=request.getParameter("action");
		if("add".equals(action)){
			//跳转新增方法
		    addInterfaceVer(request, response, interfaceVer);
		}else if("edit".equals(action)){
			//跳转修改方法
		    updateInterfaceVer(request, response, interfaceVer);
		}
	}
	/**
	 * 
	 * addInterface:新增接口信息.
	 * @author:胡钰鑫   Date: 2016年9月28日 下午4:38:30
	 * @param request
	 * @param response
	 * @param interf
	 * @since JDK 1.7
	 */
	private void addInterfaceVer(HttpServletRequest request,
			HttpServletResponse response,InterfaceVer interfaceVer){
		//返回结果
		String retJson = "";
		//获得当前登录对象信息
		User user=BaseUtil.getCurrentUser(request);
		//写入创建人（修改人）
		interfaceVer.setCreator(user.getUserId());
		interfaceVer.setUpdator(user.getUserId());
		try {
			//写回页面的值
			if(interfaceVerService.insertInterfaceVer(interfaceVer) < 1){				
				retJson = ControllerUtil.getFailRetJson("新增接口版本失败");
			}else{
				//添加日志操作
				String remark="新增接口版本信息，详情："+interfaceVer.toString();
				logOperService.insertLogOper(request, 1, remark);
				retJson = ControllerUtil.getSuccessRetJson(interfaceVer,"新增接口版本成功");
			}			
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response,retJson);
	}
	/**
	 * 
	 * updateInterface:修改接口信息.
	 * @author:胡钰鑫   Date: 2016年9月28日 下午4:40:10
	 * @since JDK 1.7
	 */
	private void updateInterfaceVer(HttpServletRequest request,
			HttpServletResponse response, InterfaceVer interfaceVer){
		//返回结果
		String retJson = "";
		//获得当前登录对象信息
		User user=BaseUtil.getCurrentUser(request);
		//写入修改人、修改时间
		interfaceVer.setUpdator(user.getUserId());
		interfaceVer.setUpdateTime(new Date());
		try {
			if(interfaceVerService.updateInterfaceVer(interfaceVer) < 1){				
				retJson = ControllerUtil.getFailRetJson("更新失败");
			} else {
				//添加操作日志
				String remark ="更新资源信息，详情：" + interfaceVer.toString();
				logOperService.insertLogOper(request, 1, remark);
				retJson = ControllerUtil.getSuccessRetJson(interfaceVer,"更新成功");
			}
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response,retJson);
	}
	/**
	 * 
	 * deleteJson:删除一条接口版本信息.
	 * @author:胡钰鑫   Date: 2016年9月28日 下午7:38:22
	 * @param request
	 * @param response
	 * @param id
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/deleteJson")
	public void deleteJson(HttpServletRequest request,
			HttpServletResponse response, String id){
		//返回结果
		String retJson = "";
		if (ObjectUtils.isEmpty(id)) {
			retJson = ControllerUtil.getFailRetJson("请选中一行记录");
			getPrintWriter(response, retJson);
			return;
		}
		
		try {
			//查询是否存在所删除项
			InterfaceVer interfaceVer=interfaceVerService.getInterfaceVer(id);
			interfaceVerService.deleteInterfaceVer(id);
			//添加操作日志
			String remark = "删除";
			remark = remark + "接口版本，详情为：" + interfaceVer.toString();
			logOperService.insertLogOper(request, 1, remark);
			retJson = ControllerUtil.getSuccessRetJson("删除成功");
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		
		getPrintWriter(response, retJson);
	}
}

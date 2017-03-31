package com.cloudwalk.ibis.controller.recogSet;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.util.BaseUtil;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.ibis.model.recogSet.Interface;
import com.cloudwalk.ibis.model.recogSet.InterfaceVer;
import com.cloudwalk.ibis.model.system.User;
import com.cloudwalk.ibis.service.recogSet.InterfaceService;
import com.cloudwalk.ibis.service.recogSet.InterfaceVerService;
import com.cloudwalk.ibis.service.system.LogOperService;

/**
 * 
 * ClassName: InterfaceController Description: 接口管理controller . date: 2016年9月27日
 * 下午6:13:12
 *
 * @author 胡钰鑫
 * @version
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/interface")
public class InterfaceController extends BaseController {
	
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
	 * 
	 * @author:胡钰鑫 Date: 2016年9月27日 下午6:20:46
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/interfaceList")
	public String interfaceList(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return  "platform/recogSet/interface";
	}
	/**
	 * 
	 * selectInterface:精确查询（不分页），做下拉框使用.
	 * @author:胡钰鑫   Date: 2016年9月29日 下午2:35:55
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectInterface")
	public void selectInterface(HttpServletRequest request,
			HttpServletResponse response, Interface record){
		// 调用service方法，得到要返回的数据，拼接成json字符串
		/*String str=interfaceService.selectInterface(null);*/
		String str = "";
		try{
			str=interfaceService.selectInterface(record);
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
		}
		getPrintWriter(response, str);
	}
	/**
	 * 
	 * selectInterfaceByPage:精确分页查询.
	 * @author:胡钰鑫   Date: 2016年9月28日 下午2:47:10
	 * @param request
	 * @param response
	 * @param record
	 * @param pageInfo
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/selectInterfaceByPage")
	public void selectInterfaceByPage(HttpServletRequest request,
			HttpServletResponse response, Interface record, PageInfo pageInfo) {
		this.setPageInfo(request, pageInfo);
		JsonListResult<Interface> result = new JsonListResult<Interface>();
		String retJson = "";
		// 封装分页查询所需要的信息
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("obj", record);
		map.put("page", pageInfo);
		try {
			List<Interface> list = this.interfaceService.selectInterfaceByPage(map);
	
			// 设置返回结果
			result.setRows(list);
			result.setTotal(Long.valueOf(pageInfo.getTotalCount()));
			retJson = JSONArray.toJSONString(result);
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		getPrintWriter(response, retJson);
	}
	
	/**
	 * 
	 * create:跳转到新增页面.
	 * @author:胡钰鑫   Date: 2016年9月28日 下午3:15:37
	 * @param request
	 * @param response
	 * @param model
	 * @param interf
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/create")
	public String create(HttpServletRequest request,
			HttpServletResponse response, Model model,Interface interf){
		model.addAttribute("action", "add");	
		model.addAttribute("interface", interf);
		return "platform/recogSet/interfaceEdit";
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
			HttpServletResponse response, Model model,Interface interf){
		model.addAttribute("action", "edit");	
		//如果传回的id值为空，则直接跳转到编辑页面,做新增理解
		if(interf==null||interf.getId()==null||interf.getId().equals("")){
			return "platform/recogSet/interfaceEdit";
		} else{
			//根据id查询出对象
			interf=interfaceService.getInterface(interf.getId());
		}
		model.addAttribute("interface", interf);
		return "platform/recogSet/interfaceEdit";
	}
	/**
	 * 
	 * saveInterface:保存接口信息.
	 * @author:胡钰鑫   Date: 2016年9月28日 下午4:13:54
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/saveInterface")
	public void saveInterface(HttpServletRequest request,
			HttpServletResponse response, Interface interf){
		//判断当前是新增还是修改
		String action=request.getParameter("action");
		if("add".equals(action)){
			//跳转新增方法
		    addInterface(request, response, interf);
		}else if("edit".equals(action)){
			//跳转修改方法
		    updateInterface(request, response, interf);
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
	private void addInterface(HttpServletRequest request,
			HttpServletResponse response,Interface interf){
		//返回结果
		String retJson = "";
		//获得当前登录对象信息
		User user=BaseUtil.getCurrentUser(request);
		//写入创建人（修改人）
		interf.setCreator(user.getUserId());
		interf.setUpdator(user.getUserId());
		try {
			//写回页面的值
			if(interfaceService.insertInterface(interf) < 1){			
				retJson = ControllerUtil.getFailRetJson("新增接口信息失败");
			}else{
				//添加日志操作
				String remark="新增接口信息，详情："+interf.toString();
				logOperService.insertLogOper(request, 1, remark);
				retJson = ControllerUtil.getSuccessRetJson(interf,"新增接口信息成功");
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
	private void updateInterface(HttpServletRequest request,
			HttpServletResponse response, Interface interf){
		//返回结果
		String retJson = "";
		try {
			//获得当前登录对象信息，设置最后更新人和时间
			User user = BaseUtil.getCurrentUser(request);
			interf.setUpdator(user.getUserId());
			interf.setUpdateTime(new Date());
			
			if(interfaceService.updateInterface(interf) < 1) {
				retJson = ControllerUtil.getFailRetJson("更新失败");
			} else {
				//添加操作日志
				String remark ="更新资源信息，详情：" + interf.toString();
				logOperService.insertLogOper(request, 1, remark);
				retJson = ControllerUtil.getSuccessRetJson(interf,"更新成功");
			}
		
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);	
		}
		
		getPrintWriter(response,retJson);
	}
	
	/**
	 * 
	 * deleteJson:删除一条接口信息.
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
			Interface interf=interfaceService.getInterface(id);
			//查询是否有关联的接口版本
			InterfaceVer interfaceVer=new InterfaceVer();
			interfaceVer.setInterfaceCode(interf.getInterfaceCode());
			List<InterfaceVer> list=interfaceVerService.selectInterfaceVer(interfaceVer);
			if(list.size()>0){			
				retJson = ControllerUtil.getFailRetJson("请先移除该接口下的所有版本");
				getPrintWriter(response, retJson);
				return;
			}
			interfaceService.deleteInterface(id);
			//添加操作日志
			String remark = "删除";
			remark = remark + "接口，详情为：" + interf.toString();
			logOperService.insertLogOper(request, 1, remark);
			retJson = ControllerUtil.getSuccessRetJson("删除成功");
			
		} catch(Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);		
		}
		
		getPrintWriter(response, retJson);
	}
}

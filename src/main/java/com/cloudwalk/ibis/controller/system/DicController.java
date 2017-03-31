package com.cloudwalk.ibis.controller.system;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cloudwalk.common.base.controller.BaseController;
import com.cloudwalk.common.base.model.PageInfo;
import com.cloudwalk.common.common.JsonListResult;
import com.cloudwalk.common.util.ControllerUtil;
import com.cloudwalk.common.util.JsonUtil;
import com.cloudwalk.common.util.ObjectUtils;
import com.cloudwalk.common.util.file.ExcelUtilsPOI;
import com.cloudwalk.ibis.model.system.DicTypes;
import com.cloudwalk.ibis.model.system.DicValues;
import com.cloudwalk.ibis.service.system.DicService;
import com.cloudwalk.ibis.service.system.LogOperService;

@Controller
@RequestMapping("/dic")
public class DicController extends BaseController {

	@Resource(name = "dicService")
	private DicService dicService;

	@Resource(name = "logOperService")
	private LogOperService logOperService;

	// 列表页面
	private final String LIST_INDEX = "platform/system/dic";
	// 编辑页面
	private final String LIST_EDIT = "platform/system/dicEdit";
	// 批量导入页面
	private final String BATCH_IMPORT = "platform/system/dicBatchImport";

	/**
	 * 跳转到列表页面
	 * 
	 * @param request
	 * @param response
	 * @return String 跳转地址
	 * @throws IOException
	 */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException {
		return LIST_INDEX;
	}

	/**
	 * 初始化下拉框的值
	 * 
	 * @author:张宇超
	 * @param request
	 * @param response
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "dicTypes")
	public void getDicTypes(HttpServletRequest request,
			HttpServletResponse response, DicTypes dicTypes) {
		// 调用service方法，得到要返回的数据，拼接成json字符串
		String str = "";
		try {
			str = dicService.getDicTypes();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
		getPrintWriter(response, str);
	}

	/**
	 * 查询字典所有分类信息，并且分页
	 * 
	 * @author:张宇超
	 * @param request
	 * @param response
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "selectPageDicValues")
	public void selectPageDicValues(HttpServletRequest request,
			HttpServletResponse response, DicValues dicValues, PageInfo pageInfo) {
		this.setPageInfo(request, pageInfo);
		JsonListResult<DicValues> result = new JsonListResult<DicValues>();
		Map<String, Object> map = new HashMap<String, Object>();
		// 返回结果
		String retJson = "";
		try {
			// 设置查询参数
			map.put("obj", dicValues);
			// 设置分页信息
			map.put("page", pageInfo);
			// 调用service方法，得到要返回的数据，拼接成json字符串
			List<DicValues> list = dicService.selectPageDicValues(map);
			result.setRows(list);
			result.setTotal(Long.valueOf(pageInfo.getTotalCount()));
			retJson = JsonUtil.toJSON(result);

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}

		getPrintWriter(response, retJson);

	}

	/**
	 * 根据类型代码，查询字典信息，不分页，下拉框用
	 * 
	 * @author:张宇超
	 * @param request
	 * @param response
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "selectDicValuesByDicType")
	public void selectDicValuesByDicType(HttpServletRequest request,
			HttpServletResponse response, DicValues dicValues) {
		String str = "";
		try {
			// 调用service方法，得到要返回的数据，拼接成json字符串
			str = dicService.selectDicValuesByDicType(dicValues);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
		getPrintWriter(response, str);
	}

	/**
	 * index:跳转至新增页面方法 <br/>
	 * 
	 * @param request
	 * @param response
	 * @return String 跳转地址
	 * @throws IOException
	 */
	@RequestMapping(value = "/create")
	public String create(HttpServletRequest request,
			HttpServletResponse response, Model model, DicValues dicValues)
			throws IOException {
		model.addAttribute("action", "create");
		return LIST_EDIT;
	}

	/**
	 * index:跳转至批量导入方法<br/>
	 * 
	 * @param request
	 * @param response
	 * @return String 跳转地址
	 * @throws IOException
	 */
	@RequestMapping(value = "/batchImport")
	public String batchImport(HttpServletRequest request,
			HttpServletResponse response, Model model, DicValues dicValues)
			throws IOException {
		model.addAttribute("action", "create");
		return BATCH_IMPORT;
	}

	/**
	 * index:跳转至编辑页面方法 <br/>
	 * 
	 * @author 张强
	 * @param request
	 * @param response
	 * @param dicValues
	 * @return String 跳转地址
	 * @throws IOException
	 */
	@RequestMapping(value = "/edit")
	public String edit(HttpServletRequest request,
			HttpServletResponse response, Model model, DicValues dicValues)
			throws IOException {
		model.addAttribute("action", "edit");
		if (dicValues == null || dicValues.getDicCode() == null
				|| "".equals(dicValues.getDicCode())
				|| dicValues.getDicType() == null) {
			model.addAttribute("action", "create");
			return LIST_EDIT;
		} else {
			dicValues = this.dicService.selectByPrimaryKey(dicValues);
		}
		model.addAttribute("dicValues", dicValues);
		return LIST_EDIT;
	}

	/**
	 * 
	 * batchImportDic:(批量导入数据字典). <br/>
	 * 
	 * @author:冯德志 Date: 2016年10月11日 下午4:39:59
	 * @param request
	 * @param response
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/batchImportDic")
	public void batchImportDic(HttpServletRequest request,
			HttpServletResponse response) {

		MultipartFile file = null;// 上传的文件
		String myFileName = null;// 文件名
		String message = null;// 导入结果信息记录
		List<DicValues> dicValuesList = new ArrayList<DicValues>();

		// 返回结果
		String retJson = "";
		try {
			// 判断 request 是否有文件上传,即多部分请求
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					file = multiRequest.getFile(iter.next());
					myFileName = file.getOriginalFilename();
					if (file != null) {
						// 判断文件是否是Excel文件
						if (!myFileName.endsWith(".xlsx")
								&& !myFileName.endsWith(".xls")) { // ".xls"(excel2003扩展名)
																	// ".xlsx"(excel2007扩展名)
							retJson = ControllerUtil
									.getFailRetJson("Excel格式错误！");
							getPrintWriter(response, retJson);
							return;
						}

						// 处理file文件,将excel转换为List<dicValues>
						dicValuesList = excelTodicValues(file, myFileName);

						// 调用dicServer
						int success = this.dicService
								.batchImportDicValues(dicValuesList);
						// 统计结果
						int count = dicValuesList.size();
						message = "上传数据字典信息共" + count + "条,导入成功" + success
								+ "条";
						retJson = ControllerUtil.getSuccessRetJson(message);

						// 添加操作日志
						String remark = "批量添加";
						remark = remark + "数据字典，详情为：" + message;
						logOperService.insertLogOper(request, 1, remark);

					}
				}
			} else {
				retJson = ControllerUtil.getFailRetJson("文件不存在");
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}

		getPrintWriter(response, retJson);

	}

	/**
	 * saveDic:保存单位信息（新增，更新）. <br/>
	 * 
	 * @author:张强
	 * @param request
	 * @param response
	 * @param channelRole
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/saveDic")
	public void saveDic(HttpServletRequest request,
			HttpServletResponse response, DicValues dicValues) {
		// 判断当前是更新，还是添加
		String action = request.getParameter("action");
		if ("create".equals(action)) {
			this.createDicValues(request, response, dicValues);
		} else if ("edit".equals(action)) {
			this.updateDicValues(request, response, dicValues);
		}

	}

	/**
	 * updateDicValues:更新单位信息. <br/>
	 * 
	 * @author:张强
	 * @param request
	 * @param response
	 * @param dicValues
	 * @since JDK 1.7
	 */
	private void updateDicValues(HttpServletRequest request,
			HttpServletResponse response, DicValues dicValues) {
		// 返回结果
		String retJson = "";
		try {
			int status = this.dicService.updateDicValues(dicValues);
			if (status < 1) {
				retJson = ControllerUtil.getFailRetJson("更新失败");
			} else {
				retJson = ControllerUtil.getSuccessRetJson("更新成功");
				// 添加操作日志
				String remark = "更新";
				remark = remark + "数据字典，详情：" + dicValues.toString();
				logOperService.insertLogOper(request, 1, remark);
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}

		getPrintWriter(response, retJson);

	}

	/**
	 * createDicValues:新增一个字典. <br/>
	 * 
	 * @author:张强
	 * @param request
	 * @param response
	 * @param dicValues
	 * @since JDK 1.7
	 */
	private void createDicValues(HttpServletRequest request,
			HttpServletResponse response, DicValues dicValues) {

		// 返回结果
		String retJson = "";
		try {
			DicValues tempDicValue = this.dicService
					.selectByPrimaryKey(dicValues);
			if (tempDicValue != null) {
				retJson = ControllerUtil.getFailRetJson("新增失败，违反字典编码+类型唯一约束！");
				getPrintWriter(response, retJson);
				return;
			}

			if (this.dicService.insertSelective(dicValues) < 1) {
				retJson = ControllerUtil.getFailRetJson("新增失败");
			} else {
				retJson = ControllerUtil.getSuccessRetJson(dicValues, "新增成功");
				// 添加操作日志
				String remark = "新增";
				remark = remark + "数据字典，详情：" + dicValues.toString();
				logOperService.insertLogOper(request, 1, remark);
			}

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			retJson = ControllerUtil.getExceRetJson(e);
		}

		getPrintWriter(response, retJson);

	}

	/**
	 * 删除
	 * 
	 * @author:张强
	 * @param request
	 * @param response
	 * @param record
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/deleteJson")
	public void deleteJson(HttpServletRequest request,
			HttpServletResponse response, String id) {

		// 返回结果
		String retJson = "";
		// 参数判空
		if (ObjectUtils.isEmpty(id)) {
			retJson = ControllerUtil.getFailRetJson("请至少选中一行记录");
			getPrintWriter(response, retJson);
			return;
		}

		List<DicValues> jsonList = JsonUtil.parseList(id, DicValues.class);
		for (DicValues dicValue : jsonList) {
			dicValue = this.dicService.selectByPrimaryKey(dicValue);
			if (this.dicService.deleteDicValues(dicValue) < 1) {
				retJson = ControllerUtil.getFailRetJson("删除失败");
				getPrintWriter(response, retJson);
				return;
			}
			// 添加操作日志
			String remark = "删除";
			remark = remark + "数据字典，详情为：" + dicValue.toString();
			logOperService.insertLogOper(request, 1, remark);
		}

		retJson = ControllerUtil.getSuccessRetJson("删除成功");
		getPrintWriter(response, retJson);
	}

	/**
	 * 
	 * excelTodicValues:(解析excel，封装到list中). <br/>
	 * 
	 * @author:冯德志 Date: 2016年10月12日 上午10:22:06
	 * @param file
	 * @param myFileName
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 * @since JDK 1.7
	 */
	public List<DicValues> excelTodicValues(MultipartFile file,
			String myFileName) throws IllegalStateException, IOException {
		// 获取系统当前路径，临时存放excel文件
		String path = System.getProperty("user.dir");
		File excelFile = new File(path + "/" + myFileName);
		// 解析Excel
		file.transferTo(excelFile);
		List<List<String>> list = ExcelUtilsPOI.readFromExcel(excelFile, 0, 1,
				0);
		// System.out.println("输出读取到的数据"+list);
		// 封装list到对象中
		List<DicValues> dicValuesList = new ArrayList<DicValues>();
		for (List<String> rowValues : list) {
			// 字典类型,字典编码,字典含义,描述,启用标志
			// 如果当前行的数据不足4列，则跳过该行解析
			if (CollectionUtils.isEmpty(rowValues) || rowValues.size() < 4) {

				continue;

			}
			DicValues dicValues = new DicValues();
			dicValues.setDicType(rowValues.get(0));
			dicValues.setDicCode(rowValues.get(1));
			dicValues.setMeaning(rowValues.get(2));
			dicValues.setDescription(rowValues.get(3));

			// 启动状态列，如果为空则默认为启动
			if (rowValues.size() > 4 && !StringUtils.isBlank(rowValues.get(4))) {
				if ("启用".equalsIgnoreCase(rowValues.get(4))
						&& !"1".equalsIgnoreCase(rowValues.get(4))) {

					rowValues.set(4, "1");

				} else {

					rowValues.set(4, "0");

				}

				dicValues.setEnabledFlag(Integer.parseInt(rowValues.get(4)));

			} else {

				dicValues.setEnabledFlag(1);

			}

			if (!StringUtils.isEmpty(dicValues.getDicType())
					&& !StringUtils.isEmpty(dicValues.getDicCode())
					&& !StringUtils.isEmpty(dicValues.getMeaning())) {
				dicValuesList.add(dicValues);
			}
		}

		// 删除临时文件
		excelFile.delete();
		return dicValuesList;
	}

	/**
	 * saveDic:模板下载. <br/>
	 * 
	 * @author:朱云飞
	 * @param request
	 * @param response
	 * @param channelRole
	 * @since JDK 1.7
	 */
	@RequestMapping(value = "/getDicTemplate")
	public void getDicTemplate(HttpServletRequest request,
			HttpServletResponse response, DicValues dicValues) {
		byte[] binaryData = null;
		String downloadfileName = "数据字典导入模板";
		try {
			InputStream in = DicController.class.getClassLoader()
					.getResourceAsStream("template/dicTemplet.xls");
			binaryData = ObjectUtils.readInputStream(in);
			// 返回文件流
			String fileName = new String(downloadfileName.getBytes("UTF-8"),
					"iso-8859-1"); // 解决中文乱码
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ fileName + ".xls\"");
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(binaryData);
			outputStream.flush();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
	}
}

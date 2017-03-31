/*
 * 采用POI操作EXCEL, 支持Excel2003和Excel2007格式
 * 
 * Author: Jackson He
 * Date: 2015年01月17日 下午12:25:44
 * 提供既支持Excel2003和Excel2007格式通用接口，
 * 有提供分别只支持Excel2003或Excel2007格式接口
 * 函数名称带有XLS2003，表示只支持Excel2003
 * 函数名称带有XLSX2007，表示只支持Excel2007
 * 
 * 解析Excel2003比Excel2007效率更高，因此报表模板尽力采用Excel2003
 * 
 * 目前采用用户模式，一次性全部读入内存的，如数据量大的话，需要考虑事件模式
 */
package com.cloudwalk.common.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtilsPOI {
	/**
	 * 获取模板出错信息
	 */
	public static final String ERROR_MESSAGE = "Data [Row %d, Column %s] : %s";

	/**
	 * 当EXCLE为空值
	 */
	public static final String NA_EXCEL = "#N/A!";

	/**
	 * 报表文件后缀名
	 */
	public static String XLS_EXTEND_FILE = ".xls";
	public static String XLSX_EXTEND_FILE = ".xlsx";

	/**
	 * INTEGER_ZERO:整数常量0.
	 */
	public static Integer INTEGER_ZERO = 0;

	/**
	 * modifySheetFromFileToOutputStream:修改Excel格式的某一个sheet并且输出副本. <br/>
	 * 可以自动识别Excel2003或者Excel2007
	 *
	 * @author:Jackson He Date: 2015年01月17日 下午12:25:44
	 * @param datas
	 *            修改的数据
	 * @param excelFile
	 *            修改的文件
	 * @param sheetNumber
	 *            修改第几个Sheet，从0开始计算
	 * @param skipRowIndex
	 *            跳过行数,0~n,0|null表示不跳过
	 * @param skipColIndex
	 *            跳过列数，0~n,0|null表示不跳过
	 * @return OutputStream 返回修改后的输出流
	 */
	public static OutputStream modifySheetFromFileToOutputStream(
			List<LinkedHashMap<String, String>> datas, File excelFile,
			Integer sheetNumber, Integer skipRowIndex, Integer skipColIndex,
			OutputStream outputStream) {
		InputStream inputStream = null; // 输入流对象
		Workbook workbook = null; // 工作薄
		Sheet sheet = null; // 工作表

		sheetNumber = getBeginIndex(sheetNumber);
		int beginRowIndex = getBeginIndex(skipRowIndex);
		int beginColIndex = getBeginIndex(skipColIndex);

		try {
			inputStream = new FileInputStream(excelFile); // 获取文件输入流
			workbook = WorkbookFactory.create(inputStream); // 创建Excel2003/2007文件对象
			sheet = workbook.getSheetAt(sheetNumber); // 取出工作表

			// 生成内容
			int rowIndex = 0;
			int stepData = 0;
			Row row = null;
			Cell cell = null;

			Row rowTitle = null;
			Cell cellTitle;
			int cellType;

			// 标题行
			rowTitle = sheet.getRow(0);

			if (datas != null && datas.size() > 0) {
				for (rowIndex = 0; rowIndex < datas.size(); rowIndex++) {
					row = sheet.getRow(rowIndex + beginRowIndex);
					if (row == null)
						row = sheet.createRow(rowIndex + beginRowIndex);
					if (row != null) {
						LinkedHashMap<String, String> element = datas.get(rowIndex);
						stepData = beginColIndex;

						// 生成每一行的数据
						for (Entry<String, String> entry : element.entrySet()) {
							cellType = -1;

							// 得到标题栏上CELL STYLE
							if (rowTitle != null) {
								cellTitle = rowTitle.getCell(stepData);
								if (cellTitle != null)
									cellType = cellTitle.getCellType();
							}

							cell = row.getCell(stepData);
							if (cell == null)
								cell = row.createCell(stepData);
							if (cell != null) {
								cell.setCellValue(entry.getValue());
								if (cellType != -1)
									cell.setCellType(cellType);
							}
							stepData++;
						}
					}
				} // for(rows...)
			} // if

			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				workbook = null;
			}
		}

		return outputStream;
	}

	/**
	 * modifySheetFromFilePosToOutputStream: 修改Excel的某一个sheet, 按照KEY的位置开始填数据,
	 * 并且输出副本. <br/>
	 * 可以自动识别Excel2003或者Excel2007
	 *
	 * @param datas
	 *            填充的数据
	 * @param excelFile
	 *            修改的文件
	 * @param sheetNumber
	 *            修改第几个Sheet
	 * @return OutputStream 返回修改后的输出流
	 */
	public static OutputStream modifySheetFromFilePosToOutputStream(
			LinkedHashMap<String, Object> datas, File excelFile,
			Integer sheetNumber, OutputStream outputStream) {
		InputStream inputStream = null; // 输入流对象
		Workbook workbook = null; // 工作薄
		Sheet sheet = null; // 工作表
		List<List<String>> rawDataList;

		if (datas == null)
			return null;

		sheetNumber = getBeginIndex(sheetNumber);

		try {
			// 得到模板已有的内容
			rawDataList = readFromExcel(excelFile, sheetNumber, 0, 0);

			// 从Excel里面获取数据放入工作薄里面
			inputStream = new FileInputStream(excelFile); // 获取文件输入流
			workbook = WorkbookFactory.create(inputStream); // 创建Excel2003/2007文件对象
			sheet = workbook.getSheetAt(sheetNumber); // 取出工作表

			Iterator<String> it = datas.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				List<String> list = (List<String>) datas.get(key);

				// 按照KEY的位置开始填数据
				modifyColData(key, list, rawDataList, sheet, 0, 0);
			}

			workbook.write(outputStream);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null)
				workbook = null;
		}

		return outputStream;
	}

	/**
	 * tableSheetFromFileToOutputStream:修改Excel的某一个sheet里面特定文字并且输出副本. <br/>
	 *
	 * 可以自动识别Excel2003或者Excel2007
	 * 
	 * @param datas
	 *            修改的数据
	 * @param file
	 *            修改的文件
	 * @param resultFile
	 *            输出流
	 * @param sheetNumber
	 *            修改第几个Sheet
	 * @return OutputStrem 返回修改后的输出流
	 */
	public static OutputStream tableSheetFromFileToOutputStream(
			Map<String, String> params, File excelFile, Integer sheetNumber,
			OutputStream outputStream) {
		InputStream inputStream = null; // 输入流对象
		Workbook workbook = null; // 获取工作薄
		Sheet sheet = null; // 工作表
		int row_size;
		String cellStr;
		boolean isFind;

		try {
			// 从Excel里面获取数据放入工作薄里面
			inputStream = new FileInputStream(excelFile); // 获取文件输入流
			workbook = WorkbookFactory.create(inputStream); // 创建Excel2003/2007文件对象
			sheet = workbook.getSheetAt(sheetNumber); // 取出工作表

			// 替换里面文字,按照内容一次次在表格中查找替换
			Iterator<String> it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key).toString();
				isFind = false;
				row_size = getLastRowIndex(sheet);

				// 开始循环遍历行
				for (int r = 0; r < row_size; r++) {
					Row row = sheet.getRow(r);// 获取行对象
					if (row == null)
						continue;

					// 循环遍历单元格
					for (int c = row.getFirstCellNum(); c < row
							.getLastCellNum(); c++) {
						Cell cell = row.getCell(c);// 获取单元格对象
						if (cell == null)
							continue;

						cellStr = getCellFormatValue(cell);
						if (cellStr != null && cellStr.length() > 0) {
							int index = cellStr.indexOf(key, 0);
							if (index >= 0) {
								isFind = true;
								cellStr = cellStr.replaceAll(key, value);
								cell.setCellValue(cellStr);
							}
						}
					} // for(int c...)
				} // for(int r...)

				// 没有找到，参数设定错误
				if (!isFind)
					return null;
			} // while

			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null)
				workbook = null;
		}
		return outputStream;

		/*
		 * 先读出全部表格内容，查找替换好再更新到表格里，这方法与按照内容一次次在表格中查找替换性能如何需要测试一下
		 * 数据量小效果比较不出来，但是目前采用用户模式，加载DOCUMENT是全部读进来内存的 下面代码不要删除，建议保留着
		 */
		/*
		 * // 得到模板已有的内容 rawDataList = readFromExcel(excelFile, sheetNumber,
		 * null, null);
		 * 
		 * try{ //从Excel里面获取数据放入工作薄里面 inputStream = new
		 * FileInputStream(excelFile); // 获取文件输入流 workbook =
		 * WorkbookFactory.create(inputStream); // 创建Excel2003/2007文件对象 sheet =
		 * workbook.getSheetAt(sheetNumber); // 取出工作表
		 * 
		 * if( !params.isEmpty() ) { list = replaceCellData(params,
		 * rawDataList); if(list == null) return null; }
		 * 
		 * // 生成内容 if( !list.isEmpty() ) { for(int i = 0; i < list.size(); i++)
		 * { ExcelCellDate item = list.get(i); Row row =
		 * sheet.getRow(item.getRow());// 获取行对象 if (row == null) continue;
		 * 
		 * Cell cell = row.getCell(item.getCol());// 获取单元格对象
		 * cell.setCellValue(item.getCellContents()); } // for } // if(
		 * !list.isEmpty() )
		 * 
		 * workbook.write(outputStream);
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } finally { if(workbook
		 * != null) { try { workbook.close(); } catch (IOException e) {
		 * e.printStackTrace(); } } }
		 */
	}

	/*
	 * replaceCellData():按照key，替换里面文字 为避免多次读sheet,先读出全部，替换好，再去替换sheet对应的内容
	 * 
	 * @param params key和替换的内容
	 * 
	 * @param rawDataList： 原来sheet中内容
	 */
	/*
	 * private static List<ExcelCellDate> replaceCellData(Map<String, String>
	 * params, FastList<List<String>> rawDataList ){ List<ExcelCellDate> list =
	 * new ArrayList<ExcelCellDate>();
	 * 
	 * // 替换里面文字 Iterator<String> it = params.keySet().iterator(); while
	 * (it.hasNext()) { String key = it.next(); String value =
	 * params.get(key).toString(); boolean isFind = false;
	 * 
	 * for(int rowIndex = 0; rowIndex < rawDataList.size(); rowIndex++){
	 * List<String> colList = rawDataList.get(rowIndex); String cellStr = "";
	 * 
	 * for(int colIndex = 0; colIndex < colList.size(); colIndex++){ cellStr =
	 * colList.get(colIndex); if(cellStr != null && cellStr.length() > 0) { int
	 * index = cellStr.indexOf(key, 0); if(index>=0){ isFind = true; cellStr =
	 * cellStr.replaceAll(key, value);
	 * 
	 * ExcelCellDate item = new ExcelCellDate(); item.setRow(rowIndex);
	 * item.setCol(colIndex); item.setCellContents(cellStr); list.add(item); } }
	 * // if(cellStr }//for(int colIndex } // for(int rowIndex
	 * 
	 * // 没有找到，参数设定错误 if(!isFind) return null; } // while
	 * 
	 * return list; }
	 */

	/**
	 * 读取Excel的方法 可以自动识别Excel2003或者Excel2007
	 * 
	 * @param excelFile
	 *            修改的文件
	 * @param sheetNumber
	 *            修改第几个Sheet，从0开始计算
	 * @param skipRowIndex
	 *            跳过行数,0~n,0|null表示不跳过
	 * @param skipColIndex
	 *            跳过列数，0~n,0|null表示不跳过
	 * 
	 * @return FastList<List<String>> 返回二维数组
	 */
	public static List<List<String>> readFromExcel(File excelFile,
			Integer sheetNumber, Integer skipRowIndex, Integer skipColIndex) {
		InputStream inputStream = null; // 输入流对象
		Workbook workbook = null; // 获取工作薄
		Sheet sheet = null; // 工作表

		// 第一个维度是指哪些行，第二个维度是指哪些列
		List<List<String>> reulstFastList = null;
		reulstFastList = new ArrayList<List<String>>();
		int beginRowIndex = getBeginIndex(skipRowIndex);
		int beginColIndex = getBeginIndex(skipColIndex);

		try {
			// 从Excel里面获取数据放入工作薄里面
			inputStream = new FileInputStream(excelFile); // 获取文件输入流
			workbook = WorkbookFactory.create(inputStream); // 创建Excel2003/2007文件对象
			sheet = workbook.getSheetAt(sheetNumber); // 取出工作表

			int row_size = getLastRowIndex(sheet);

			// 开始循环遍历行，表头不处理，从1开始
			for (int r = beginRowIndex; r < row_size; r++) {
				List<String> list = new ArrayList<String>();

				Row row = sheet.getRow(r);// 获取行对象
				if (row == null) {
					// 如果为空行，也需要保留空间节点
					reulstFastList.add(list);
					continue;
				}

				String cellStr = "";

				// 循环遍历单元格
				for (int c = beginColIndex; c < row.getLastCellNum(); c++) {
					Cell cell = row.getCell(c);// 获取单元格对象
					if (cell == null)
						cellStr = NA_EXCEL; // 为空值,也要保留空间
					else
						cellStr = getCellFormatValue(cell);

					list.add(cellStr);
				} // for(int j...)

				reulstFastList.add(list);
			} // for(int i...)

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null)
				workbook = null;
		}
		// 返回我们需要的数组
		return reulstFastList;
	}

	/**
	 * 根据Cell类型设置数据 可以自动识别Excel2003或者Excel2007
	 * 
	 * @param cell
	 * @return
	 */
	private static String getCellFormatValue(Cell cell) {
		String cellvalue = "";

		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				cellvalue = "";
				break;

			// 对布尔值的处理
			case Cell.CELL_TYPE_BOOLEAN:
				cellvalue = String.valueOf(cell.getBooleanCellValue());
				break;

			// 如果当前Cell的Type为NUMERIC
			case Cell.CELL_TYPE_NUMERIC:
			case Cell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (DateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式

					// 方法1：这样子的data格式是带时分秒的：2015-01-17 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2015-01-17
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);
				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}

			// 如果当前Cell的Type为STRIN
			case Cell.CELL_TYPE_STRING:
			default: // 默认的Cell值
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			}
		}

		return cellvalue;
	}

	private static int getLastRowIndex(Sheet sheet) {
		int nIndex = -1;
		if (sheet == null)
			return nIndex;

		if (sheet.getPhysicalNumberOfRows() == 0 && sheet.getLastRowNum() == 0) {
			// 空表
			nIndex = 0;
		} else {
			// 有内容
			nIndex = sheet.getLastRowNum() + 1;
		}
		return nIndex;
	}

	/*
	 * modifyColData 按照KEY的位置开始填数据 可以自动识别Excel2003或者Excel2007
	 * 
	 * @param key: 关键字
	 * 
	 * @param list: 需要填充的数据
	 * 
	 * @param rawDataList: 表格中原始数据
	 * 
	 * @param sheet: Sheet对象
	 * 
	 * @param skipRowIndex 跳过行数,0~n,0|null表示不跳过
	 * 
	 * @param skipColIndex 跳过列数，0~n,0|null表示不跳过
	 * 
	 * @eturn true:替换成功, false:失败
	 */
	private static boolean modifyColData(String key, List<String> list,
			List<List<String>> rawDataList, Sheet sheet,
			Integer skipRowIndex, Integer skipColIndex) {
		boolean bRet = false;
		List<String> rawColList;
		Row row = null;
		Cell cell = null;
		int beginRowIndex = getBeginIndex(skipRowIndex);
		int beginColIndex = getBeginIndex(skipColIndex);

		for (int rowIndex = 0; rowIndex < rawDataList.size(); rowIndex++) {
			// 得到一行原始数据列表
			rawColList = rawDataList.get(rowIndex);
			if (rawColList != null) {
				for (int colIndex = 0; colIndex < rawColList.size(); colIndex++) {
					String strText = rawColList.get(colIndex);
					if (strText.equals(NA_EXCEL))
						continue; // 跳过空值

					if (strText != null && strText.equalsIgnoreCase(key)) {
						int cur_row_index;
						CellStyle style = null;

						for (int index = 0; index < list.size(); index++) {
							cur_row_index = beginRowIndex + rowIndex + index;

							row = sheet.getRow(cur_row_index); // 使用当前已有的行
							if (row == null)
								row = sheet.createRow(cur_row_index); // 创建行

							if (row != null) {
								cell = row.getCell(beginColIndex + colIndex);
								if (cell == null)
									cell = row.createCell(beginColIndex
											+ colIndex);
								else {
									if (index == 0) {
										// 取得cell style
										style = cell.getCellStyle();
									}
								}

								if (cell != null) {
									cell.setCellValue(list.get(index));
									if (style != null && index != 0)
										cell.setCellStyle(style);
								}
							}
						}

						bRet = true;

					} // if
				} // for(int col
			}
		} // for(int row)

		return bRet;
	}

	/*
	 * getBeginIndex 得到启起行
	 */
	private static int getBeginIndex(Integer skipIndex) {
		int beginIndex = 0;

		if (skipIndex != null)
			beginIndex = skipIndex.intValue();
		if (beginIndex < 0)
			beginIndex = 0;

		return beginIndex;
	}

	/**
	 * modifySheetFromFromXLS2003ToOutputStream:修改Excel2003格式的某一个sheet并且输出副本. <br/>
	 *
	 * 支持Excel2003
	 *
	 * @param datas
	 *            修改的数据
	 * @param file
	 *            修改的文件
	 * @param sheetNumber
	 *            修改第几个Sheet，从0开始计算
	 * @param skipRowIndex
	 *            跳过行数,0~n,0|null表示不跳过
	 * @param skipColIndex
	 *            跳过列数，0~n,0|null表示不跳过
	 * @return OutputStream 返回修改后的输出流
	 */
	public static OutputStream modifySheetFromFromXLS2003ToOutputStream(
			List<LinkedHashMap<String, String>> datas, File excelFile,
			Integer sheetNumber, Integer skipRowIndex, Integer skipColIndex,
			OutputStream outputStream) {
		InputStream inputStream = null; // 输入流对象
		HSSFWorkbook workbook = null; // 工作薄
		HSSFSheet sheet = null; // 工作表

		sheetNumber = getBeginIndex(sheetNumber);
		int beginRowIndex = getBeginIndex(skipRowIndex);
		int beginColIndex = getBeginIndex(skipColIndex);

		try {
			inputStream = new FileInputStream(excelFile); // 获取文件输入流
			workbook = new HSSFWorkbook(inputStream); // 创建Excel2003文件对象
			sheet = workbook.getSheetAt(sheetNumber); // 取出工作表

			// 生成内容
			int rowIndex = 0;
			int stepData = 0;
			HSSFRow row = null;
			HSSFCell cell = null;

			HSSFRow rowTitle = null;
			HSSFCell cellTitle;
			int cellType;

			// 标题行
			rowTitle = sheet.getRow(0);

			if (datas != null && datas.size() > 0) {
				for (rowIndex = 0; rowIndex < datas.size(); rowIndex++) {
					row = sheet.getRow(rowIndex + beginRowIndex);
					if (row == null)
						row = sheet.createRow(rowIndex + beginRowIndex);
					if (row != null) {
						LinkedHashMap<String, String> element = datas
								.get(rowIndex);
						stepData = beginColIndex;

						// 生成每一行的数据
						for (Entry<String, String> entry : element.entrySet()) {
							cellType = -1;

							// 得到标题栏上CELL STYLE
							if (rowTitle != null) {
								cellTitle = rowTitle.getCell(stepData);
								if (cellTitle != null)
									cellType = cellTitle.getCellType();
							}

							cell = row.getCell(stepData);
							if (cell == null)
								cell = row.createCell(stepData);
							if (cell != null) {
								cell.setCellValue(entry.getValue());
								if (cellType != -1)
									cell.setCellType(cellType);
							}
							stepData++;
						}
					}
				} // for(rows...)
			} // if

			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if(workbook != null) workbook.close();
			if (workbook != null)
				workbook = null;
		}

		return outputStream;
	}

	/**
	 * modifySheetFromXLS2003PosToOutputStream: 修改Excel的某一个sheet, 按照KEY的位置开始填数据,
	 * 并且输出副本. <br/>
	 * 支持Excel2003
	 *
	 * @param datas
	 *            填充的数据
	 * @param excelFile
	 *            修改的文件
	 * @param sheetNumber
	 *            修改第几个Sheet
	 * @return OutputStream 返回修改后的输出流
	 */
	public static OutputStream modifySheetFromXLS2003PosToOutputStream(
			LinkedHashMap<String, List<String>> datas, File excelFile,
			Integer sheetNumber, OutputStream outputStream) {
		InputStream inputStream = null; // 输入流对象
		HSSFWorkbook workbook = null; // 工作薄
		HSSFSheet sheet = null; // 工作表
		List<List<String>> rawDataList;

		if (datas == null)
			return null;

		sheetNumber = getBeginIndex(sheetNumber);

		try {
			// 得到模板已有的内容
			rawDataList = readFromXLS2003(excelFile, sheetNumber, 0, 0);

			// 从Excel里面获取数据放入工作薄里面
			inputStream = new FileInputStream(excelFile);// 获取文件输入流
			workbook = new HSSFWorkbook(inputStream); // 创建Excel2003文件对象
			sheet = workbook.getSheetAt(sheetNumber); // 取出工作表

			Iterator<String> it = datas.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				List<String> list = (List<String>) datas.get(key);

				// 按照KEY的位置开始填数据
				modifyXLS2003ColData(key, list, rawDataList, sheet, 0, 0);
			}

			workbook.write(outputStream);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null)
				workbook = null;
		}

		return outputStream;
	}

	/**
	 * tableSheetFromXLS2003ToOutputStream:修改Excel的某一个sheet里面特定文字并且输出副本. <br/>
	 *
	 * 支持Excel2003
	 * 
	 * @param datas
	 *            修改的数据
	 * @param file
	 *            修改的文件
	 * @param resultFile
	 *            输出流
	 * @param sheetNumber
	 *            修改第几个Sheet
	 * @return OutputStrem 返回修改后的输出流
	 */
	public static OutputStream tableSheetFromXLS2003ToOutputStream(
			Map<String, String> params, File excelFile, Integer sheetNumber,
			OutputStream outputStream) {
		InputStream inputStream = null; // 输入流对象
		HSSFWorkbook workbook = null; // 获取工作薄
		HSSFSheet sheet = null; // 工作表
		int row_size;
		String cellStr;
		boolean isFind;

		try {
			// 从Excel里面获取数据放入工作薄里面
			inputStream = new FileInputStream(excelFile); // 获取文件输入流
			workbook = new HSSFWorkbook(inputStream); // 创建Excel2003文件对象
			sheet = workbook.getSheetAt(sheetNumber); // 取出工作表

			// 替换里面文字,按照内容一次次在表格中查找替换
			Iterator<String> it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key).toString();
				isFind = false;
				row_size = getLastRowIndex(sheet);

				// 开始循环遍历行
				for (int r = 0; r < row_size; r++) {
					HSSFRow row = sheet.getRow(r);// 获取行对象
					if (row == null)
						continue;

					// 循环遍历单元格
					for (int c = row.getFirstCellNum(); c < row
							.getLastCellNum(); c++) {
						HSSFCell cell = row.getCell(c);// 获取单元格对象
						if (cell == null)
							continue;

						cellStr = getCellFormatValue(cell);
						if (cellStr != null && cellStr.length() > 0) {
							int index = cellStr.indexOf(key, 0);
							if (index >= 0) {
								isFind = true;
								cellStr = cellStr.replaceAll(key, value);
								cell.setCellValue(cellStr);
							}
						}
					} // for(int c...)
				} // for(int r...)

				// 没有找到，参数设定错误
				if (!isFind)
					return null;
			} // while

			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null)
				workbook = null;
		}
		return outputStream;
	}

	/**
	 * readFromXLS2003 读取Excel2003的方法
	 * 
	 * @param excelFile
	 *            修改的文件
	 * @param sheetNumber
	 *            修改第几个Sheet，从0开始计算
	 * @param skipRowIndex
	 *            跳过行数,0~n,0|null表示不跳过
	 * @param skipColIndex
	 *            跳过列数，0~n,0|null表示不跳过
	 * 
	 * @return FastList<List<String>> 返回二维数组
	 */
	private static List<List<String>> readFromXLS2003(File excelFile,
			Integer sheetNumber, Integer skipRowIndex, Integer skipColIndex) {
		InputStream inputStream = null; // 输入流对象
		HSSFWorkbook workbook = null; // 获取工作薄
		HSSFSheet sheet = null; // 工作表

		// 第一个维度是指哪些行，第二个维度是指哪些列
		List<List<String>> reulstFastList = null;
		reulstFastList = new ArrayList<List<String>>();
		int beginRowIndex = getBeginIndex(skipRowIndex);
		int beginColIndex = getBeginIndex(skipColIndex);

		try {
			// 从Excel里面获取数据放入工作薄里面
			inputStream = new FileInputStream(excelFile); // 获取文件输入流
			workbook = new HSSFWorkbook(inputStream); // 创建Excel2003文件对象
			sheet = workbook.getSheetAt(sheetNumber); // 取出工作表

			int row_size = getLastRowIndex(sheet);

			// 开始循环遍历行，表头不处理，从1开始
			for (int r = beginRowIndex; r < row_size; r++) {
				List<String> list = new ArrayList<String>();

				HSSFRow row = sheet.getRow(r);// 获取行对象
				if (row == null) {
					// 如果为空行，也需要保留空间节点
					reulstFastList.add(list);
					continue;
				}

				String cellStr = "";

				// 循环遍历单元格
				for (int c = beginColIndex; c < row.getLastCellNum(); c++) {
					HSSFCell cell = row.getCell(c);// 获取单元格对象
					if (cell == null)
						cellStr = NA_EXCEL; // 为空值,也要保留空间
					else
						cellStr = getXLS2003CellFormatValue(cell);

					list.add(cellStr);
				} // for(int j...)

				reulstFastList.add(list);
			} // for(int i...)

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null)
				workbook = null;
		}
		// 返回我们需要的数组
		return reulstFastList;
	}

	/**
	 * 根据HSSFCell类型设置数据
	 * 
	 * @param cell
	 * @return
	 */
	private static String getXLS2003CellFormatValue(HSSFCell cell) {
		String cellvalue = "";

		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_BLANK:
				cellvalue = "";
				break;

			// 对布尔值的处理
			case HSSFCell.CELL_TYPE_BOOLEAN:
				cellvalue = String.valueOf(cell.getBooleanCellValue());
				break;

			// 如果当前Cell的Type为NUMERIC
			case HSSFCell.CELL_TYPE_NUMERIC:
			case HSSFCell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (DateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式

					// 方法1：这样子的data格式是带时分秒的：2015-01-17 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2015-01-17
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);
				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}

			// 如果当前Cell的Type为STRIN
			case HSSFCell.CELL_TYPE_STRING:
			default: // 默认的Cell值
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			}
		}

		return cellvalue;
	}

	/*
	 * modifyXLS2003ColData 按照KEY的位置开始填数据 支持Excel2003
	 * 
	 * @param key: 关键字
	 * 
	 * @param list: 需要填充的数据
	 * 
	 * @param rawDataList: 表格中原始数据
	 * 
	 * @param sheet: Sheet对象
	 * 
	 * @param skipRowIndex 跳过行数,0~n,0|null表示不跳过
	 * 
	 * @param skipColIndex 跳过列数，0~n,0|null表示不跳过
	 * 
	 * @eturn true:替换成功, false:失败
	 */
	private static boolean modifyXLS2003ColData(String key, List<String> list,
			List<List<String>> rawDataList, HSSFSheet sheet,
			Integer skipRowIndex, Integer skipColIndex) {
		boolean bRet = false;
		List<String> rawColList;
		HSSFRow row = null;
		HSSFCell cell = null;
		int beginRowIndex = getBeginIndex(skipRowIndex);
		int beginColIndex = getBeginIndex(skipColIndex);

		for (int rowIndex = 0; rowIndex < rawDataList.size(); rowIndex++) {
			// 得到一行原始数据列表
			rawColList = rawDataList.get(rowIndex);
			if (rawColList != null) {
				for (int colIndex = 0; colIndex < rawColList.size(); colIndex++) {
					String strText = rawColList.get(colIndex);
					if (strText.equals(NA_EXCEL))
						continue; // 跳过空值

					if (strText != null && strText.equalsIgnoreCase(key)) {
						int cur_row_index;
						CellStyle style = null;

						for (int index = 0; index < list.size(); index++) {
							cur_row_index = beginRowIndex + rowIndex + index;

							row = sheet.getRow(cur_row_index); // 使用当前已有的行
							if (row == null)
								row = sheet.createRow(cur_row_index); // 创建行

							if (row != null) {
								cell = row.getCell(beginColIndex + colIndex);
								if (cell == null)
									cell = row.createCell(beginColIndex
											+ colIndex);
								else {
									if (index == 0) {
										// 取得cell style
										style = cell.getCellStyle();
									}
								}

								if (cell != null) {
									cell.setCellValue(list.get(index));
									if (style != null && index != 0)
										cell.setCellStyle(style);
								}
							}
						}

						bRet = true;

					} // if
				} // for(int col
			}
		} // for(int row)

		return bRet;
	}

	/**
	 * modifySheetFromFromXLSX2007ToOutputStream:修改Excel2007格式的某一个sheet并且输出副本. <br/>
	 *
	 * 支持Excel2007
	 *
	 * @param datas
	 *            修改的数据
	 * @param file
	 *            修改的文件
	 * @param sheetNumber
	 *            修改第几个Sheet，从0开始计算
	 * @param skipRowIndex
	 *            跳过行数,0~n,0|null表示不跳过
	 * @param skipColIndex
	 *            跳过列数，0~n,0|null表示不跳过
	 * @return OutputStream 返回修改后的输出流
	 */
	public static OutputStream modifySheetFromFromXLSX2007ToOutputStream(
			List<LinkedHashMap<String, String>> datas, File excelFile,
			Integer sheetNumber, Integer skipRowIndex, Integer skipColIndex,
			OutputStream outputStream) {
		InputStream inputStream = null; // 输入流对象
		XSSFWorkbook workbook = null; // 工作薄
		XSSFSheet sheet = null; // 工作表

		sheetNumber = getBeginIndex(sheetNumber);
		int beginRowIndex = getBeginIndex(skipRowIndex);
		int beginColIndex = getBeginIndex(skipColIndex);

		try {
			inputStream = new FileInputStream(excelFile); // 获取文件输入流
			workbook = new XSSFWorkbook(inputStream); // 创建Excel2003文件对象
			sheet = workbook.getSheetAt(sheetNumber); // 取出工作表

			// 生成内容
			int rowIndex = 0;
			int stepData = 0;
			XSSFRow row = null;
			XSSFCell cell = null;

			XSSFRow rowTitle = null;
			XSSFCell cellTitle;
			int cellType;

			// 标题行
			rowTitle = sheet.getRow(0);

			if (datas != null && datas.size() > 0) {
				for (rowIndex = 0; rowIndex < datas.size(); rowIndex++) {
					row = sheet.getRow(rowIndex + beginRowIndex);
					if (row == null)
						row = sheet.createRow(rowIndex + beginRowIndex);
					if (row != null) {
						LinkedHashMap<String, String> element = datas
								.get(rowIndex);
						stepData = beginColIndex;

						// 生成每一行的数据
						for (Entry<String, String> entry : element.entrySet()) {
							cellType = -1;

							// 得到标题栏上CELL STYLE
							if (rowTitle != null) {
								cellTitle = rowTitle.getCell(stepData);
								if (cellTitle != null)
									cellType = cellTitle.getCellType();
							}

							cell = row.getCell(stepData);
							if (cell == null)
								cell = row.createCell(stepData);
							if (cell != null) {
								cell.setCellValue(entry.getValue());
								if (cellType != -1)
									cell.setCellType(cellType);
							}
							stepData++;
						}
					}
				} // for(rows...)
			} // if

			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if(workbook != null) workbook.close();
			if (workbook != null)
				workbook = null;
		}

		return outputStream;
	}

	/**
	 * modifySheetFromXLSX2007PosToOutputStream: 修改Excel的某一个sheet,
	 * 按照KEY的位置开始填数据, 并且输出副本. <br/>
	 * 支持Excel2007
	 *
	 * @param datas
	 *            填充的数据
	 * @param excelFile
	 *            修改的文件
	 * @param sheetNumber
	 *            修改第几个Sheet
	 * @return OutputStream 返回修改后的输出流
	 */
	public static OutputStream modifySheetFromXLSX2007PosToOutputStream(
			LinkedHashMap<String, List<String>> datas, File excelFile,
			Integer sheetNumber, OutputStream outputStream) {
		InputStream inputStream = null; // 输入流对象
		XSSFWorkbook workbook = null; // 工作薄
		XSSFSheet sheet = null; // 工作表
		List<List<String>> rawDataList;

		if (datas == null)
			return null;

		sheetNumber = getBeginIndex(sheetNumber);

		try {
			// 得到模板已有的内容
			rawDataList = readFromXLSX2007(excelFile, sheetNumber, 0, 0);

			// 从Excel里面获取数据放入工作薄里面
			inputStream = new FileInputStream(excelFile);// 获取文件输入流
			workbook = new XSSFWorkbook(inputStream); // 创建Excel2007文件对象
			sheet = workbook.getSheetAt(sheetNumber); // 取出工作表

			Iterator<String> it = datas.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				List<String> list = (List<String>) datas.get(key);

				// 按照KEY的位置开始填数据
				modifyXLSX2007ColData(key, list, rawDataList, sheet, 0, 0);
			}

			workbook.write(outputStream);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null)
				workbook = null;
		}

		return outputStream;
	}

	/**
	 * tableSheetFromXLSX2007ToOutputStream:修改Excel的某一个sheet里面特定文字并且输出副本. <br/>
	 *
	 * 支持Excel2007
	 * 
	 * @param datas
	 *            修改的数据
	 * @param file
	 *            修改的文件
	 * @param resultFile
	 *            输出流
	 * @param sheetNumber
	 *            修改第几个Sheet
	 * @return OutputStrem 返回修改后的输出流
	 */
	public static OutputStream tableSheetFromXLSX2007ToOutputStream(
			Map<String, String> params, File excelFile, Integer sheetNumber,
			OutputStream outputStream) {
		InputStream inputStream = null; // 输入流对象
		XSSFWorkbook workbook = null; // 获取工作薄
		XSSFSheet sheet = null; // 工作表
		int row_size;
		String cellStr;
		boolean isFind;

		try {
			// 从Excel里面获取数据放入工作薄里面
			inputStream = new FileInputStream(excelFile); // 获取文件输入流
			workbook = new XSSFWorkbook(inputStream); // 创建Excel2007文件对象
			sheet = workbook.getSheetAt(sheetNumber); // 取出工作表

			// 替换里面文字,按照内容一次次在表格中查找替换
			Iterator<String> it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key).toString();
				isFind = false;
				row_size = getLastRowIndex(sheet);

				// 开始循环遍历行
				for (int r = 0; r < row_size; r++) {
					XSSFRow row = sheet.getRow(r);// 获取行对象
					if (row == null)
						continue;

					// 循环遍历单元格
					for (int c = row.getFirstCellNum(); c < row
							.getLastCellNum(); c++) {
						XSSFCell cell = row.getCell(c);// 获取单元格对象
						if (cell == null)
							continue;

						cellStr = getCellFormatValue(cell);
						if (cellStr != null && cellStr.length() > 0) {
							int index = cellStr.indexOf(key, 0);
							if (index >= 0) {
								isFind = true;
								cellStr = cellStr.replaceAll(key, value);
								cell.setCellValue(cellStr);
							}
						}
					} // for(int c...)
				} // for(int r...)

				// 没有找到，参数设定错误
				if (!isFind)
					return null;
			} // while

			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null)
				workbook = null;
		}
		return outputStream;
	}

	/**
	 * readFromXLSX2007 读取Excel2007的方法
	 * 
	 * @param excelFile
	 *            修改的文件
	 * @param sheetNumber
	 *            修改第几个Sheet，从0开始计算
	 * @param skipRowIndex
	 *            跳过行数,0~n,0|null表示不跳过
	 * @param skipColIndex
	 *            跳过列数，0~n,0|null表示不跳过
	 * 
	 * @return FastList<List<String>> 返回二维数组
	 */
	private static List<List<String>> readFromXLSX2007(File excelFile,
			Integer sheetNumber, Integer skipRowIndex, Integer skipColIndex) {
		InputStream inputStream = null; // 输入流对象
		XSSFWorkbook workbook = null; // 获取工作薄
		XSSFSheet sheet = null; // 工作表

		// 第一个维度是指哪些行，第二个维度是指哪些列
		List<List<String>> reulstFastList = null;
		reulstFastList = new ArrayList<List<String>>();
		int beginRowIndex = getBeginIndex(skipRowIndex);
		int beginColIndex = getBeginIndex(skipColIndex);

		try {
			// 从Excel里面获取数据放入工作薄里面
			inputStream = new FileInputStream(excelFile); // 获取文件输入流
			workbook = new XSSFWorkbook(inputStream); // 创建Excel2007文件对象
			sheet = workbook.getSheetAt(sheetNumber); // 取出工作表

			int row_size = getLastRowIndex(sheet);

			// 开始循环遍历行，表头不处理，从1开始
			for (int r = beginRowIndex; r < row_size; r++) {
				List<String> list = new ArrayList<String>();

				XSSFRow row = sheet.getRow(r);// 获取行对象
				if (row == null) {
					// 如果为空行，也需要保留空间节点
					reulstFastList.add(list);
					continue;
				}

				String cellStr = "";

				// 循环遍历单元格
				for (int c = beginColIndex; c < row.getLastCellNum(); c++) {
					XSSFCell cell = row.getCell(c);// 获取单元格对象
					if (cell == null)
						cellStr = NA_EXCEL; // 为空值,也要保留空间
					else
						cellStr = getXLSX2007CellFormatValue(cell);

					list.add(cellStr);
				} // for(int j...)

				reulstFastList.add(list);
			} // for(int i...)

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null)
				workbook = null;
		}
		// 返回我们需要的数组
		return reulstFastList;
	}

	/**
	 * 根据XSSFCell类型设置数据 支持Excel2007
	 * 
	 * @param cell
	 * @return
	 */
	private static String getXLSX2007CellFormatValue(XSSFCell cell) {
		String cellvalue = "";

		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			case XSSFCell.CELL_TYPE_BLANK:
				cellvalue = "";
				break;

			// 对布尔值的处理
			case XSSFCell.CELL_TYPE_BOOLEAN:
				cellvalue = String.valueOf(cell.getBooleanCellValue());
				break;

			// 如果当前Cell的Type为NUMERIC
			case XSSFCell.CELL_TYPE_NUMERIC:
			case XSSFCell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (DateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式

					// 方法1：这样子的data格式是带时分秒的：2015-01-17 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2015-01-17
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);
				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}

			// 如果当前Cell的Type为STRIN
			case XSSFCell.CELL_TYPE_STRING:
			default: // 默认的Cell值
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			}
		}

		return cellvalue;
	}

	/*
	 * modifyXLSX2007ColData 按照KEY的位置开始填数据 支持Excel2007
	 * 
	 * @param key: 关键字
	 * 
	 * @param list: 需要填充的数据
	 * 
	 * @param rawDataList: 表格中原始数据
	 * 
	 * @param sheet: Sheet对象
	 * 
	 * @param skipRowIndex 跳过行数,0~n,0|null表示不跳过
	 * 
	 * @param skipColIndex 跳过列数，0~n,0|null表示不跳过
	 * 
	 * @eturn true:替换成功, false:失败
	 */
	private static boolean modifyXLSX2007ColData(String key, List<String> list,
			List<List<String>> rawDataList, XSSFSheet sheet,
			Integer skipRowIndex, Integer skipColIndex) {
		boolean bRet = false;
		List<String> rawColList;
		XSSFRow row = null;
		XSSFCell cell = null;
		int beginRowIndex = getBeginIndex(skipRowIndex);
		int beginColIndex = getBeginIndex(skipColIndex);

		for (int rowIndex = 0; rowIndex < rawDataList.size(); rowIndex++) {
			// 得到一行原始数据列表
			rawColList = rawDataList.get(rowIndex);
			if (rawColList != null) {
				for (int colIndex = 0; colIndex < rawColList.size(); colIndex++) {
					String strText = rawColList.get(colIndex);
					if (strText.equals(NA_EXCEL))
						continue; // 跳过空值

					if (strText != null && strText.equalsIgnoreCase(key)) {
						int cur_row_index;
						CellStyle style = null;

						for (int index = 0; index < list.size(); index++) {
							cur_row_index = beginRowIndex + rowIndex + index;

							row = sheet.getRow(cur_row_index); // 使用当前已有的行
							if (row == null)
								row = sheet.createRow(cur_row_index); // 创建行

							if (row != null) {
								cell = row.getCell(beginColIndex + colIndex);
								if (cell == null)
									cell = row.createCell(beginColIndex
											+ colIndex);
								else {
									if (index == 0) {
										// 取得cell style
										style = cell.getCellStyle();
									}
								}

								if (cell != null) {
									cell.setCellValue(list.get(index));
									if (style != null && index != 0)
										cell.setCellStyle(style);
								}
							}
						}

						bRet = true;

					} // if
				} // for(int col
			}
		} // for(int row)

		return bRet;
	}

}

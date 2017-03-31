package com.cloudwalk.common.engine.face.consts;

/**
 * 引擎相关常量对象
 * @author 何春节
 * @version 1.0
 */
public interface EngineConst {

	/**
	 * 云之眼提供的各种接口地址
	 */
	public interface Service {
		
		// 人脸检测
		public static final String FACE_TOOL_DETECT = "/face/tool/detect";
		
		// 人脸属性分析
		public static final String FACE_TOOL_ATTRIBUTE = "/face/tool/attribute";
		
		// 人脸质量分析
		public static final String FACE_TOOL_QUALITY = "/face/tool/quality";
		
		// 人脸关键点分析
		public static final String FACE_TOOL_KEYPT = "/face/tool/keypt";
				
		// 人脸相识度比较
		public static final String FACE_TOOL_COMPARE = "/face/tool/compare";
		
		// 人脸相识度比较扩展1
		public static final String FACE_TOOL_COMPARE_EXT1 = "/face/tool/compare/ext1";
		
		// 人脸相识度比较(图片和视频比对)
		public static final String FACE_TOOL_SIMILARITYBYVIDEO = "/face/tool/similarityByVideo";
		
		// 人脸去网纹
		public static final String FACE_TOOL_RMWATER = "/face/tool/removeWater";
		
		// 人脸特征提取
		public static final String FACE_TOOL_FEATURE = "/face/tool/feature";
		
//		// 人脸特征融合(废弃)
//		public static final String FACE_TOOL_MERGE_FEATURE = "/face/tool/mergeFeature";
		
		// 特征相识度
		public static final String FACE_TOOL_SIMILARITYBYFEATURE = "/face/tool/similarityByFeature";
		
		// 特征提取(模型可选)
		public static final String FACE_TOOL_MULTIMODEL_FEATURE = "/face/tool/multimodel/feature";
		
		// 图片和图片比对相似度(模型可选)
		public static final String FACE_TOOL_MULTIMODEL_SIMILARITYBYIMAGE = "/face/tool/multimodel/similarityByImage";
		
		// 特征和图片比对相似度(模型可选)
		public static final String FACE_TOOL_MULTIMODEL_SIMILARITYBYFEATURE = "/face/tool/multimodel/similarityByFeature";
				
		// 添加人脸
		public static final String FACE_CLUSTERIING_FACE_CREATE = "/face/clustering/face/create";
		
		// 删除人脸
		public static final String FACE_CLUSTERING_FACE_DELETE = "/face/clustering/face/delete";
		
		// 添加组
		public static final String FACE_CLUSTERING_GROUP_CREATE = "/face/clustering/group/create";
		
		// 删除组
		public static final String FACE_CLUSTERING_GROUP_DELETE = "/face/clustering/group/delete";
		
		// 组中添加人脸
		public static final String FACE_CLUSTERING_GROUP_ADDFACE = "/face/clustering/group/addFace";
		
		// 组中删除人脸
		public static final String FACE_CLUSTERING_GROUP_RMFACE = "/face/clustering/group/removeFace";
		
		// 查询组中人脸数目
		public static final String FACE_CLUSTERING_GROUP_QUERY = "/face/clustering/group/query";
		
		// 人脸识别
		public static final String FACE_RECOG_GROUP_IDENTIFY = "/face/recog/group/identify";
		
		// 人脸识别扩展1
		public static final String FACE_RECOG_GROUP_IDENTIFY_EXT1 = "/face/recog/group/identify/ext1";
	
		//OCR识别
		public static final String URL_OCR = "/ocr";
		
		//活体检测
		public static final String FACE_RECOG_LIVE_NESS = "/faceliveness";
		
		//银行卡识别
		public static final String URL_OCR_BANKCARD = "/ocr/bankcard";
	}
}

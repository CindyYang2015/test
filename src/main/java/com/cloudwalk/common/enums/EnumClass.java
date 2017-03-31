package com.cloudwalk.common.enums;

/**
 * 枚举类
 * @author zhuyf
 *
 */
public class EnumClass {

	/**
	 * 缓存类型枚举
	 * @author zhuyf
	 *
	 */
	public enum CacheTypeEnum {
		
		REDIS("jedisCache"),
		LOCAL("localCache"),
		NO("noCache");		
		
		private final String value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		CacheTypeEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/*
	 * 数据类型枚举
	 */
	public enum DataTypeEnum {
		
		XML("xml"),
		JSON("json"),
		OTHER("other"),;		
		
		private final String value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		DataTypeEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/*
	 * 字典类型枚举
	 */
	public enum DicTypeEnum {
		
		/**
		 * 引擎类型
		 */
		ENGINE_TYPE("5"),
		/**
		 * 交易代码
		 */
		TRADING_CODE("6"),
		/**
		 * 渠道
		 */
		CHANNEL("3"),
		;			
		
		private final String value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		DicTypeEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	

	
	
	
	
	
	/*
	 * 文件存取方式枚举
	 */
	public enum FileAccessEnum {
		
		/**
		 * 本地
		 */
		LOCAL("local"),
		/**
		 * http
		 */
		HTTP("http"),
		/**
		 * https
		 */
		HTTPS("https");			
		
		private final String value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		FileAccessEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/*
	 * 图片高清，水印枚举
	 */
	public enum FileTypeEnum {
		
		/**
		 * 高清或者原始
		 */
		HDTV(1),
		/**
		 * 水印
		 */
		WATERMARK(2);
			
		
		private final int value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		FileTypeEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	/*
	 * 审批状态类型枚举
	 */
	public enum DicCheckTypeEnum {
		
		/**
		 * 审批状态：通过
		 */
		CHECK_STATUS_PASS("1"),	
		
		/**
		 * 审批状态：不通过
		 */
		CHECK_STATUS_NO_PASS("0"),
		
		/**
		 * 审批状态：待审核
		 */
		CHECK_STATUS_WAITING("2");
		
		private final String value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		DicCheckTypeEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/*
	 * 联网核查状态枚举
	 */
	public enum NetCheckStatusEnum {
		
		/**
		 * 是
		 */
		YES(1),
		/**
		 * 否
		 */
		NO(0);
			
		
		private final int value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		NetCheckStatusEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	/*
	 * 存库类型枚举
	 */
	public enum LibTypeEnum {
		
		/**
		 * 特征库
		 */
		LIB_TYPE("lib"),
		/**
		 * 流水库
		 */
		FLOW_TYPE("flow");			
		
		private final String value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		LibTypeEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/*
	 * 联网核查状态枚举
	 */
	public enum NetworkCheckCodeEnum {
		
		TIMEOUT("timeout"),
		SUCCESS("success"),
		FAIL("fail"),;		
		
		private final String value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		NetworkCheckCodeEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/*
	 * 操作类型枚举
	 */
	public enum DicOptTypeEnum {
		
		/**
		 * 操作类型：新增
		 */
		OPERATE_STATUS_NEW("1"),	
		
		/**
		 * 操作类型：修改
		 */
		OPERATE_STATUS_MODIFY("2"),
		
		/**
		 * 操作类型：删除
		 */
		OPERATE_STATUS_DELETE("3");
		
		private final String value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		DicOptTypeEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/*
	 * 流水处理结果枚举
	 */
	public enum FlowResultEnum {
		
		/**
		 * 成功
		 */
		SUCESS(1),
		/**
		 * 失败
		 */
		FAIL(0);
			
		
		private final int value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		FlowResultEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	/*
	 * 状态枚举
	 */
	public enum StatusEnum {
		
		/**
		 * 是
		 */
		YES(1),
		/**
		 * 否
		 */
		NO(0);
			
		
		private final int value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		StatusEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	/*
	 * 服务接口枚举
	 */
	public enum InterFaceEnum {
		/**
		 * 注册
		 */
		REG("reg"),
		/**
		 * 证脸比对
		 */
		CHECK_PERSON("checkPerson"),
		/**
		 * 两证一脸比对
		 */
		CHECK_PERSON_EX("checkPersonEx"),
		/**
		 * 脸脸比对
		 */
		COMPARE("compare"),
		/**
		 * 按照客户信息检索
		 */
		SEARCH_BY_PERSON("searchByPerson"),
		/**
		 * 按照图片信息检索
		 */
		SEARCH_BY_IMG("searchByImg"),
		/**
		 * 身份证识别
		 */
		OCR("ocr"),
		/**
		 * 活体检测
		 */
		CHECK_LIVENESS("checkLiveness"),
		/**
		 * 银行卡识别
		 */
		OCR_BANK_CARD("ocrBankCard");
		
		
		
		private final String value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		InterFaceEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/*
	 * 图片名称枚举
	 */
	public enum ImgNameEnum {
		/**
		 * 人脸图片
		 */
		FACE_IMG("faceImg"),
		/**
		 * 证件照
		 */
		IDCARD_IMG("idcardImg"),
		/**
		 * 联网核查图片
		 */
		NET_CHECK_IMG("netCheckImg"),
		/**
		 * 身份证正面照
		 */
		FRONT_IMG("frontImg"),
		/**
		 * 身份证反面照
		 */
		BLACK_IMG("backImg"),
		/**
		 * 银行卡照片
		 */
		BANK_IMG("bankImg");
			
		
		private final String value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		ImgNameEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
		
	/*
	 * 身份证正反面枚举
	 */
	public enum IDCardTypeEnum {
		
		/**
		 * 正面
		 */
		FRONT(1),
		/**
		 * 反面
		 */
		BLACK(0),
		/**
		 * 正反面
		 */
		ALL(2);
			
		
		private final int value;

		// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
		IDCardTypeEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}

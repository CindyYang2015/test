package com.cloudwalk.common.util.file;
/**
 * 文件类型枚举
 * 
 * @author Chunjie He
 * @date 2015-09-02
 */
public enum FileType {
	
	/**
	 * ZIP.
	 */
	ZIP("504B0304"),
	
	/**
	 * RAR.
	 */
	RAR("52617221"),

	/**
	 * JPG.
	 */
	JPG("FFD8FF"),

	/**
	 * PNG.
	 */
	PNG("89504E47"),

	/**
	 * GIF.
	 */
	GIF("47494638"),

	/**
	 * TIFF.
	 */
	TIFF("49492A00"),

	/**
	 * Windows Bitmap.
	 */
	BMP("424D");
	
	private String value = "";

	/**
	 * Constructor.
	 * 
	 * @param type
	 */
	private FileType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

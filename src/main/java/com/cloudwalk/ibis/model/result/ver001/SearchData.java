package com.cloudwalk.ibis.model.result.ver001;

import java.util.List;

import com.cloudwalk.ibis.model.featurelib.PersonFeature;

/**
 * 检索的数据结果
 * @author zhuyf
 *
 */
public class SearchData extends Data {

	private static final long serialVersionUID = 6032015110972567921L;
	
	/**
	 * 客户信息
	 */	
	List<PersonFeature> personList;	

	public SearchData() {}
	
	public SearchData(List<PersonFeature> personList) {
		super();
		this.personList = personList;
	}

	public List<PersonFeature> getPersonList() {
		return personList;
	}

	public void setPersonList(List<PersonFeature> personList) {
		this.personList = personList;
	}	

}

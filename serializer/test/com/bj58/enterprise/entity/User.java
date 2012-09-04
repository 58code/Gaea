package com.bj58.enterprise.entity;

import java.util.HashMap;
import java.util.Map;


import com.bj58.spat.gaea.serializer.component.annotation.GaeaMember;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;

@SuppressWarnings("serial")
@GaeaSerializable
public class User extends EntityBase{


	@GaeaMember
	private Long userID;      // 用户账号ID

	@GaeaMember
	private Long enterpriseID; // 所对应的企业ID
	

	@GaeaMember
	private Integer state;       //  该用户的企业认证是否通过
	

	private transient String params;          //  扩展数据（json字符串, 此值不需要序列化,只用于数据库存储
	
/*	@NotDBColumn
	private List<ParaClass> paraList;          // 扩展数据（集合）
*/	

	@GaeaMember
	private Map<String,String> paraMap;          // 扩展数据（集合）


	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public Long getEnterpriseID() {
		return enterpriseID;
	}

	public void setEnterpriseID(Long enterpriseID) {
		this.enterpriseID = enterpriseID;
	}

	public int getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		/*if(params!=null && params.length()>1){
			jsonToParaList(params);
		}*/
		this.params = params;
	}
	
	
	
	public Map<String, String> getParaMap() {
		return paraMap;
	}

	public void setParaMap(Map<String, String> paraMap) {
		this.paraMap = paraMap;
	}

	
	
	public void validate()throws Exception{
		if(getEnterpriseID() == null) throw new RuntimeException("EnterpriseID不能为空");
	}
	
	
	
	public void setPara(String name,Integer cateID,String value){
		if(name == null || name.length()==0 || cateID == null || value == null || value.length() == 0)
			return;
		if(paraMap == null)
			paraMap = new HashMap<String,String>();
		paraMap.put(name+"_"+cateID,value);
	}
	
	public String getPara(String name,Integer cateID){
		if(paraMap != null)
			return paraMap.get(name+"_"+cateID);
		else 
			return null;
	}
	
	
	@Override
	public String getCacheKey(){
		Long id = getUserID();
		if(id == null){
			return null;
		}
		StringBuilder sb = new StringBuilder(User.class.getName());
		sb.append("_");
		sb.append(id);
		return sb.toString();
	}
	
}

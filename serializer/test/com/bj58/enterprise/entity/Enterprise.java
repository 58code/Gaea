package com.bj58.enterprise.entity;

import java.util.Date;


import com.bj58.spat.gaea.serializer.component.annotation.GaeaMember;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;

@SuppressWarnings("serial")
@GaeaSerializable
public class Enterprise extends EntityBase {


	@GaeaMember
	private long id; // 主键

	@GaeaMember
	private long pid; // 上级单位ID

	@GaeaMember
	private long adminid; // 管理员ID

	@GaeaMember
	private String legalPerson; // 法人

	@GaeaMember
	private String name; // 企业名称

	@GaeaMember
	private int capitalRegistered;// 注册资金

	public int getCapitalRegistered() {
		return capitalRegistered;
	}

	public void setCapitalRegistered(int capitalRegistered) {
		this.capitalRegistered = capitalRegistered;
	}

	@GaeaMember
	private int enterpriseType;// 企业 类型

	public int getEnterpriseType() {
		return enterpriseType;
	}

	public void setEnterpriseType(int enterpriseType) {
		this.enterpriseType = enterpriseType;
	}

	@GaeaMember
	private int cityid; // 所在城市ID

	@GaeaMember
	private String address; // 企业地址


	@GaeaMember
	private String zipcode; // 邮政编码


	@GaeaMember
	private String telphone; // 联系电话


	@GaeaMember
	private String businessField; // 经营范围


	@GaeaMember
	private Date startupdate; // 该企业的成立日期


	@GaeaMember
	private Date fromdate; // 营业期限起始日期


	@GaeaMember
	private Date todate; // 营业期限结束日期


	@GaeaMember
	private String homepage;


	@GaeaMember
	private String logo;


	@GaeaMember
	private String introduction; // 企业简介


	@GaeaMember
	private int authstate; // 审核状态


	@GaeaMember
	private Date updateDate; // 该记录更新时间

	@GaeaMember
	private Date createdate; // 该记录创建时间


	@GaeaMember
	private boolean deleteFlag;

	@GaeaMember
	private String params; // 扩展数据

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getAdminid() {
		return adminid;
	}

	public void setAdminid(long adminid) {
		this.adminid = adminid;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCityid() {
		return cityid;
	}

	public void setCityid(int cityid) {
		this.cityid = cityid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getBusinessField() {
		return businessField;
	}

	public void setBusinessField(String businessField) {
		this.businessField = businessField;
	}

	public Date getStartupdate() {
		return startupdate;
	}

	public void setStartupdate(Date startupdate) {
		this.startupdate = startupdate;
	}

	public Date getFromdate() {
		return fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public Date getTodate() {
		return todate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public int getAuthstate() {
		return authstate;
	}

	public void setAuthstate(int authstate) {
		this.authstate = authstate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public boolean getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public void Commit() {

	}

	@Override
	public String getCacheKey() {
		return EntityBase.generateCacheKey(getId(), this.getClass());
	}

}

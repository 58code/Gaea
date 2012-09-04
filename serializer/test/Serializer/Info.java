package Serializer;

import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;

import java.io.Serializable;
import java.util.Date;

@GaeaSerializable(defaultAll = true)
public class Info implements Serializable {

	public Info() {
	}

	private long InfoID;
	private String Content;
	private String Title;
	private int CateID;
	private long UserID;
	private String UserIP;
	private String Pic;
	private Date AddDate;
	private Date PostDate;
	private Date EffectiveDate;
	private short State;
	private short Source;
	private String Tag;
	private String Phone;
	private String Email;
	private String IM;
	private String HideItem;
	private long SortID;
	private short InfoType;
	private boolean IsBiz;
	private String UserTag;
	private String Url;
	private String Locals;
	private String Params;

	public long getInfoID() {
		return InfoID;
	}

	public void setInfoID(long infoID) {
		InfoID = infoID;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public int getCateID() {
		return CateID;
	}

	public void setCateID(int cateID) {
		CateID = cateID;
	}

	public long getUserID() {
		return UserID;
	}

	public void setUserID(long userID) {
		UserID = userID;
	}

	public String getUserIP() {
		return UserIP;
	}

	public void setUserIP(String userIP) {
		UserIP = userIP;
	}

	public String getPic() {
		return Pic;
	}

	public void setPic(String pic) {
		Pic = pic;
	}

	public Date getAddDate() {
		if (AddDate == null) {
			return new Date();
		}
		return AddDate;
	}

	public void setAddDate(Date addDate) {
		AddDate = addDate;
	}

	public Date getPostDate() {
		if (PostDate == null) {
			return new Date();
		}
		return PostDate;
	}

	public void setPostDate(Date postDate) {
		PostDate = postDate;
	}

	public Date getEffectiveDate() {
		if (EffectiveDate == null) {
			return new Date();
		}
		return EffectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		EffectiveDate = effectiveDate;
	}

	public short getState() {
		return State;
	}

	public void setState(short state) {
		State = state;
	}

	public short getSource() {
		return Source;
	}

	public void setSource(short source) {
		Source = source;
	}

	public String getTag() {
		return Tag;
	}

	public void setTag(String tag) {
		Tag = tag;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getIM() {
		return IM;
	}

	public void setIM(String im) {
		IM = im;
	}

	public String getHideItem() {
		return HideItem;
	}

	public void setHideItem(String hideItem) {
		HideItem = hideItem;
	}

	public long getSortID() {
		return SortID;
	}

	public void setSortID(long sortID) {
		SortID = sortID;
	}

	public short getInfoType() {
		return InfoType;
	}

	public void setInfoType(short infoType) {
		InfoType = infoType;
	}

	public boolean getIsBiz() {
		return IsBiz;
	}

	public void setIsBiz(boolean isBiz) {
		IsBiz = isBiz;
	}

	public String getUserTag() {
		return UserTag;
	}

	public void setUserTag(String userTag) {
		UserTag = userTag;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getLocals() {
		return Locals;
	}

	public void setLocals(String locals) {
		Locals = locals;
	}

	public String getParams() {
		return Params;
	}

	public void setParams(String params) {
		Params = params;
	}
}
package global.services.lib.android;

public class Notification {
	
	private Long id;
	private String userId_;
	private String appId_;
	private String tittle_;
	private String content_;
	private Long fromDate_;
	private Long toDate_;

	public Notification(){}
	
	public Notification(String appId){
		appId_ = appId;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId_;
	}
	public void setUserId(String userId) {
		userId_ = userId;
	}
	public String getTittle() {
		return tittle_;
	}
	public void setTittle(String tittle) {
		tittle_ = tittle;
	}
	public String getContent() {
		return content_;
	}
	public void setContent(String content) {
		content_ = content;
	}
	public Long getFromDate() {
		return fromDate_;
	}
	public void setFromDate(Long fromDate) {
		fromDate_ = fromDate;
	}
	public Long getToDate() {
		return toDate_;
	}
	public void setToDate(Long toDate) {
		toDate_ = toDate;
	}
	public void setAppId(String appId) {
		this.appId_ = appId;
	}
	public String getAppId() {
		return appId_;
	}
	
	
}

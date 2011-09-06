package global.services.lib.android.objects;

public class Notification {
	
	private Long id;
	private String userId_;
	private Long appId_;
	private String title_;
	private String content_;
	private Long fromDate_;
	private Long toDate_;

	public Notification(){}
	
	public Notification(Long appId){
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
	public String getTitle() {
		return title_;
	}
	public void setTitle(String title) {
		title_ = title;
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
	public void setAppId(Long appId) {
		this.appId_ = appId;
	}
	public Long getAppId() {
		return appId_;
	}
	
	
}

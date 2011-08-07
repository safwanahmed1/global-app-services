package global.services.lib.android;
public class Advertisement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String userId_;
	private String appName_;
	private String tittle_;
	private String content_;
	private String type_;
	private Long iconFileId_;
	private String storeUrl_;
	
	public Advertisement(){}
	public Advertisement(String appId){
		setAppName(appId);
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
	public String getAppName() {
		return appName_;
	}
	public void setAppName(String appId) {
		appName_ = appId;
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
	public String getType() {
		return type_;
	}
	public void setType(String type) {
		type_ = type;
	}
	public Long getIconFile() {
		return iconFileId_;
	}
	public void setIconFile(Long iconFileId) {
		iconFileId_ = iconFileId;
	}
	public String getStoreUrl() {
		return storeUrl_;
	}
	public void setStoreUrl(String storeUrl) {
		storeUrl_ = storeUrl;
	}
}

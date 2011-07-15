package global.services.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.view.client.ProvidesKey;
@PersistenceCapable(identityType = IdentityType.APPLICATION)

public class Advertisement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 public static final ProvidesKey<Advertisement> KEY_PROVIDER = new ProvidesKey<Advertisement>() {
      public Object getKey(Advertisement adv) {
        return adv == null ? null : adv.getId();
      }
    };
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String userId_;
	@Persistent
	private String appName_;
	@Persistent
	private String tittle_;
	@Persistent
	private String content_;
	@Persistent
	private String type_;
	@Persistent
	private Long iconFileId_;
	@Persistent
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

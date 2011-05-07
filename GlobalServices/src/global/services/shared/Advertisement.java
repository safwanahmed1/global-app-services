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
	private String appId_;
	@Persistent
	private String tittle_;
	@Persistent
	private String content_;
	@Persistent
	private String type_;
	@Persistent
	private String iconUrl_;
	@Persistent
	private String storeUrl_;
	
	public Advertisement(){}
	public Advertisement(String appId){
		setAppId(appId);
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
	public String getAppId() {
		return appId_;
	}
	public void setAppId(String appId) {
		appId_ = appId;
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
	public String getIconUrl() {
		return iconUrl_;
	}
	public void setIconUrl(String iconUrl) {
		iconUrl_ = iconUrl;
	}
	public String getStoreUrl() {
		return storeUrl_;
	}
	public void setStoreUrl(String storeUrl) {
		storeUrl_ = storeUrl;
	}
}

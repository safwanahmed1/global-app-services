package global.services.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.view.client.ProvidesKey;
@PersistenceCapable(identityType = IdentityType.APPLICATION)

public class Notification implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final ProvidesKey<Notification> KEY_PROVIDER =  new ProvidesKey<Notification>() {
	 public Object getKey(Notification note) {
	        return note == null ? null : note.getId();
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
	private Long fromDate_;
	@Persistent
	private Long toDate_;
	

	public Notification(){}
	public Notification(String appId){
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setAppName(String appId) {
		this.appName_ = appId;
	}
	public String getAppName() {
		return appName_;
	}
	
	
}

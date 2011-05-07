package global.services.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.view.client.ProvidesKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class AppScore implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	@Persistent
	private String userId_;
	@Persistent
	private String appId_;
	@Persistent
	private String appTittle_;
	@Persistent
	private int scoreEntries_;
	
	/**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<AppScore> KEY_PROVIDER = new ProvidesKey<AppScore>() {
      public Object getKey(AppScore app) {
        return app == null ? null : app.getId();
      }
    };
    
    
	public AppScore() {}
	public AppScore(String appID) {
		appId_ = appID;
	}
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getAppId() {
		return appId_;
	}

	public void setAppId(String appId) {
		appId_ = appId;
	}

	public String getAppTittle() {
		return appTittle_;
	}

	public void setAppTittle(String appTittle) {
		appTittle_ = appTittle;
	}
	public void setScoreEntries(int scoreEntries) {
		this.scoreEntries_ = scoreEntries;
	}
	public int getScoreEntries() {
		return scoreEntries_;
	}
	public void setUserId(String userId) {
		this.userId_ = userId;
	}
	public String getUserId() {
		return userId_;
	}

}

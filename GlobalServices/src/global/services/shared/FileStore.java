package global.services.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.view.client.ProvidesKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FileStore implements Serializable  {
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
	private String fileName_;
	@Persistent
	private String fileDescription_;
	@Persistent
	private Long fileSize_;
	@Persistent
	private Long modified_;
	
	
	/**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<FileStore> KEY_PROVIDER = new ProvidesKey<FileStore>() {
      public Object getKey(FileStore app) {
        return app == null ? null : app.getId();
      }
    };
    
    
	public FileStore() {}
	public FileStore(String appID) {
		fileName_ = appID;
	}
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getAppId() {
		return fileName_;
	}

	public void setAppId(String appId) {
		fileName_ = appId;
	}

	public String getAppTittle() {
		return fileDescription_;
	}

	public void setAppTittle(String appTittle) {
		fileDescription_ = appTittle;
	}
	
	public void setUserId(String userId) {
		this.userId_ = userId;
	}
	public String getUserId() {
		return userId_;
	}
	public void setFileSize(long fileSize) {
		this.fileSize_ = fileSize;
	}
	public long getFileSize() {
		return fileSize_;
	}
	public void setModified(long modified) {
		this.modified_ = modified;
	}
	public long getModified() {
		return modified_;
	}

}

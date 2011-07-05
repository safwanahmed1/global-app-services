package global.services.server.database;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.gwt.view.client.ProvidesKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class IconAdvertisement implements Serializable  {
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
	private Blob iconContent_;
	
	
	
	/**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<IconAdvertisement> KEY_PROVIDER = new ProvidesKey<IconAdvertisement>() {
      public Object getKey(IconAdvertisement app) {
        return app == null ? null : app.getId();
      }
    };
    
    
	public IconAdvertisement() {}
	public IconAdvertisement(String fileName, Blob content) {
		fileName_ = fileName;
		iconContent_ = content;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return fileName_;
	}

	public void setName(String fileName) {
		fileName_ = fileName;
	}

	public Blob getContent() {
		return iconContent_;
	}

	public void setContent(Blob iconContent) {
		iconContent_ = iconContent;
	}
	
	public void setUserId(String userId) {
		this.userId_ = userId;
	}
	public String getUserId() {
		return userId_;
	}


}

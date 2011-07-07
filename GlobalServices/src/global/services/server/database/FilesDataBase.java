package global.services.server.database;

import global.services.server.PMF;
import global.services.shared.Advertisement;

import java.io.Serializable;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.gwt.view.client.ProvidesKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FilesDataBase implements Serializable  {
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
	private String fileType_;
	@Persistent
	private Blob fileContent_;
	
	private PersistenceManager pm_;
	
	
	
	
	/**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<FilesDataBase> KEY_PROVIDER = new ProvidesKey<FilesDataBase>() {
      public Object getKey(FilesDataBase app) {
        return app == null ? null : app.getId();
      }
    };
    
    
	public FilesDataBase() {
		pm_ = PMF.get().getPersistenceManager();
		
	}
	public FilesDataBase(String fileName, Blob content) {
		fileName_ = fileName;
		fileContent_ = content;
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
		return fileContent_;
	}

	public void setContent(Blob iconContent) {
		fileContent_ = iconContent;
	}
	
	public void setUserId(String userId) {
		this.userId_ = userId;
	}
	public String getUserId() {
		return userId_;
	}
	public void setFileType(String fileType) {
		this.fileType_ = fileType;
	}
	public String getFileType() {
		return fileType_;
	}
	
	public FilesDataBase InsertFile() {
		return pm_.makePersistent(this);
	}
	
	public FilesDataBase SelectFile(Long fileId) {
		String strQuery = "select from " + FilesDataBase.class.getName();
		Query query = pm_.newQuery(strQuery);
		if ((userId_ != null) && !userId_.isEmpty())
			query.setFilter("userId_ == \"" + userId_ + "\"");
		if (fileId != null) 
			query.setFilter("id == " + fileId );
		List<FilesDataBase> retFile = (List<FilesDataBase>) query.execute();
		return retFile.get(0);
	}
}




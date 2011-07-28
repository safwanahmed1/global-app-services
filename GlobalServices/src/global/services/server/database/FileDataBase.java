package global.services.server.database;

import global.services.server.PMF;
import global.services.shared.Advertisement;
import global.services.shared.AppScore;


import java.io.Serializable;
import java.util.ArrayList;
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
public class FileDataBase implements Serializable {
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
	public static final ProvidesKey<FileDataBase> KEY_PROVIDER = new ProvidesKey<FileDataBase>() {
		public Object getKey(FileDataBase app) {
			return app == null ? null : app.getId();
		}
	};

	public FileDataBase() {
		pm_ = PMF.get().getPersistenceManager();

	}
	public void Finalize() {
		pm_.close();
	}

	public FileDataBase(String fileName, Blob content) {
		pm_ = PMF.get().getPersistenceManager();
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

	public FileDataBase InsertFile() {
		return pm_.makePersistent(this);
	}

	public FileDataBase SelectFile(Long fileId) {
		String strQuery = "select from " + FileDataBase.class.getName();
		Query query = pm_.newQuery(strQuery);
		if ((userId_ != null) && !userId_.isEmpty())
			query.setFilter("userId_ == \"" + userId_ + "\"");
		if (fileId != null)
			query.setFilter("id == " + fileId);
		List<FileDataBase> retFile = (List<FileDataBase>) query.execute();
		if (retFile.size() > 0) {

			setContent(retFile.get(0).getContent());
			setFileType(retFile.get(0).getFileType());
			setName(retFile.get(0).getFileType());
			setUserId(retFile.get(0).getUserId());

			return this;
		} else {
			return null;
		}
	}
	public Long DeleteFile(String userId, Long id) {
		// TODO Auto-generated method stub
		Long ret = null;
		Query query = pm_.newQuery(FileDataBase.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if (id != null)
			query.setFilter("id == " + id);
		ret = query.deletePersistentAll();
		return ret;

	}

	public Long DeleteFiles(String userId) {
		// TODO Auto-generated method stub
		Long ret = null;
		Query query = pm_.newQuery(FileDataBase.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		ret = query.deletePersistentAll();
		return ret;

	}


	public Long UpdateFile(FileDataBase file) {
		// TODO Auto-generated method stub
		Long ret = null;
		return ret;
	}

	public FileDataBase SelectFile(String userId, String id) {
		// TODO Auto-generated method stub
		String strQuery = "select from " + FileDataBase.class.getName();
		// + HighScore.class.getName() ;
		Query query = pm_.newQuery(strQuery);
		// query.setOrdering("highScore desc, during asc");
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if ((id != null) && !id.isEmpty())
			query.setFilter("id == \"" + id + "\"");

		List<FileDataBase> filescores = (List<FileDataBase>) query.execute();

		return filescores.get(0);
	}
	@SuppressWarnings("unchecked")
	public List<FileDataBase> SelectFiles(String userId) {
		// TODO Auto-generated method stub
		String strQuery = "select from " + FileDataBase.class.getName();
		// + HighScore.class.getName() ;
		Query query = pm_.newQuery(strQuery);
		// query.setOrdering("highScore desc, during asc");
		if (userId != null)
			query.setFilter("userId_ == \"" + userId + "\"");
		List<FileDataBase> retFiles = new ArrayList<FileDataBase>();
		List<FileDataBase> selectedFiles = (List<FileDataBase>) query.execute();
		for (FileDataBase file : selectedFiles) {
			retFiles.add(file);
		}
		/*
		 * AppScore oneApp = new AppScore("AppTest"); oneApp.setId((long) 0);
		 * oneApp.setAppTittle("Tittle for one app"); appscores.add(oneApp);
		 */
		return retFiles;
	}

	
}

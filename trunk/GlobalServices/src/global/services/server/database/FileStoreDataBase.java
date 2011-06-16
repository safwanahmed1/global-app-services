package global.services.server.database;

import global.services.server.PMF;
import global.services.shared.AppScore;
import global.services.shared.FileStore;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.repackaged.com.google.common.io.Files;

public class FileStoreDataBase {
	private FileStore file_;
	private PersistenceManager pm_;

	public FileStoreDataBase() {
		pm_ = PMF.get().getPersistenceManager();
	}

	public FileStoreDataBase(FileStore file) {
		file_ = file;
		pm_ = PMF.get().getPersistenceManager();
	}

	public void Finalize() {
		pm_.close();
	}

	public Long DeleteFile(String userId, String fileId) {
		// TODO Auto-generated method stub
		Long ret = null;
		Query query = pm_.newQuery(AppScore.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if ((fileId != null) && !fileId.isEmpty())
			query.setFilter("fileId_ == \"" + fileId + "\"");
		ret = query.deletePersistentAll();
		return ret;

	}

	public Long DeleteFiles(String userId) {
		// TODO Auto-generated method stub
		Long ret = null;
		Query query = pm_.newQuery(FileStore.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		ret = query.deletePersistentAll();
		return ret;

	}

	public Long InsertFile(FileStore file) {
		// TODO Auto-generated method stub
		Long ret = null;

		ret = pm_.makePersistent(file).getId();
		return ret;
	}

	public Long UpdateFile(FileStore file) {
		// TODO Auto-generated method stub
		Long ret = null;
		return ret;
	}

	public FileStore SelectFile(String userId, String fileId) {
		// TODO Auto-generated method stub
		String strQuery = "select from " + FileStore.class.getName();
		// + HighScore.class.getName() ;
		Query query = pm_.newQuery(strQuery);
		// query.setOrdering("highScore desc, during asc");
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if ((fileId != null) && !fileId.isEmpty())
			query.setFilter("fileId_ == \"" + fileId + "\"");

		List<FileStore> filescores = (List<FileStore>) query.execute();

		return filescores.get(0);
	}
	@SuppressWarnings("unchecked")
	public List<FileStore> SelectFiles(String userId) {
		// TODO Auto-generated method stub
		String strQuery = "select from " + FileStore.class.getName();
		// + HighScore.class.getName() ;
		Query query = pm_.newQuery(strQuery);
		// query.setOrdering("highScore desc, during asc");
		if (userId != null)
			query.setFilter("userId_ == \"" + userId + "\"");
		List<FileStore> retFiles = new ArrayList<FileStore>();
		List<FileStore> selectedFiles = (List<FileStore>) query.execute();
		for (FileStore file : selectedFiles) {
			retFiles.add(file);
		}
		/*
		 * AppScore oneApp = new AppScore("AppTest"); oneApp.setId((long) 0);
		 * oneApp.setAppTittle("Tittle for one app"); appscores.add(oneApp);
		 */
		return retFiles;
	}

}

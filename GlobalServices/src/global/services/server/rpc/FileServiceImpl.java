package global.services.server.rpc;

import java.util.List;

import global.services.client.rpc.FileService;
import global.services.server.database.FileStoreDataBase;
import global.services.shared.FileStore;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FileServiceImpl extends RemoteServiceServlet implements
FileService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8196471416318393392L;

	@Override
	public Long DeleteFile(String userId, String fileId) {
		// TODO Auto-generated method stub
		Long ret = null;
		if (userId != null) {
			FileStoreDataBase fileDB = new FileStoreDataBase();
			ret = fileDB.DeleteFile(userId, fileId);
		}
			
		return ret;
	}

	@Override
	public Long DeleteFiles(String userId) {
		// TODO Auto-generated method stub
		Long ret = null;
		if (userId != null) {
			FileStoreDataBase fileDB = new FileStoreDataBase();
			ret = fileDB.DeleteFiles(userId);
		}
			
		return ret;
	}

	@Override
	public Long InsertFile(FileStore file) {
		Long ret = null;

		FileStoreDataBase fileDB = new FileStoreDataBase();
		ret = fileDB.InsertFile(file);
		return ret;
	}

	@Override
	public FileStore SelectFile(String userId, String fileId) {
		// TODO Auto-generated method stub
		FileStoreDataBase fileDB = new FileStoreDataBase();
		return fileDB.SelectFile(userId, fileId);

	}

	@Override
	public List<FileStore> SelectFiles(String userId) {
		// TODO Auto-generated method stub
		FileStoreDataBase fileDB = new FileStoreDataBase();
		return fileDB.SelectFiles(userId);
	}

	@Override
	public Long UpdateFile(FileStore file) {
		// TODO Auto-generated method stub
		return null;

	}


}

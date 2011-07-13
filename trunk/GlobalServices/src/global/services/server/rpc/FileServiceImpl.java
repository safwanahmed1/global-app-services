package global.services.server.rpc;
import java.util.ArrayList;
import java.util.List;
import global.services.client.rpc.FileService;
import global.services.server.database.FileDataBase;
import global.services.shared.FileInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FileServiceImpl extends RemoteServiceServlet implements
FileService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Long DeleteFile(String userId, String fileId) {
		// TODO Auto-generated method stub
		Long ret = null;
		if (userId != null) {
			FileDataBase fileDB = new FileDataBase();
			ret = fileDB.DeleteFile(userId, fileId);
		}
			
		return ret;
	}

	@Override
	public Long DeleteFiles(String userId) {
		// TODO Auto-generated method stub
		Long ret = null;
		if (userId != null) {
			FileDataBase fileDB = new FileDataBase();
			ret = fileDB.DeleteFiles(userId);
			fileDB.Finalize();
		}
			
		return ret;
	}

	public Long InsertFile(FileDataBase file) {
		Long ret = null;

		ret = file.InsertFile().getId();
		file.Finalize();
		return ret;
	}

	@Override
	public FileInfo SelectFile(String userId, String fileId) {
		// TODO Auto-generated method stub
		FileDataBase fileDB = new FileDataBase();
		FileInfo fileInfo = new FileInfo();
		FileDataBase file = fileDB.SelectFile(userId, fileId);
		fileInfo.setId(file.getId());
		fileInfo.setFileName(file.getName());
		fileInfo.setFileType(file.getFileType());
		fileInfo.setFileSize(file.getContent().toString());
		fileDB.Finalize();
		return fileInfo;
	}

	@Override
	public List<FileInfo> SelectFiles(String userId) {
		// TODO Auto-generated method stub
		FileDataBase fileDB = new FileDataBase();
		List<FileInfo> retFiles = new ArrayList<FileInfo>();
		List<FileDataBase> filesList = fileDB.SelectFiles(userId);
		for (FileDataBase file: filesList) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setId(file.getId());
			fileInfo.setFileName(file.getName());
			fileInfo.setFileType(file.getFileType());
			fileInfo.setFileSize(file.getContent().toString());
			retFiles.add(fileInfo);
		}
		fileDB.Finalize();
		return retFiles;
	}

	
	@Override
	public int DeleteFiles(String userId, List<String> listFileId) {
		// TODO Auto-generated method stub
		Long tmpRet = (long) 0;
		int ret = 0;
		for (String fileId : listFileId) {
			tmpRet = DeleteFile(userId, fileId);
			if (tmpRet != null) 
				ret++;
		}
		return ret;
	}

	@Override
	public Long UpdateFile(FileInfo file) {
		// TODO Auto-generated method stub
		return null;
	}


}

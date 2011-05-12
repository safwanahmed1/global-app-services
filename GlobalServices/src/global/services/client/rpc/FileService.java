package global.services.client.rpc;

import global.services.shared.FileStore;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("appScores")
public interface FileService extends RemoteService {

	public Long InsertFile(FileStore file);
	public Long UpdateFile(FileStore file);
	public Long DeleteFile(String userId, String fileId);
	public Long DeleteFiles(String userId);
	public FileStore SelectFile(String userId, String appId);
	public List<FileStore> SelectFiles(String userId);
}

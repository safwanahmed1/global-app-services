package global.services.client.rpc;

import global.services.shared.FileInfo;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("files")
public interface FileService extends RemoteService {

	public Long UpdateFile(FileInfo file);
	public Long DeleteFile(String userId, Long fileId);
	public int DeleteFiles(String userId, List<Long> listFileId);
	public Long DeleteFiles(String userId);
	public FileInfo SelectFile(String userId, String appId);
	public List<FileInfo> SelectFiles(String userId);
}

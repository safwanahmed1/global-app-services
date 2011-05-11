package global.services.client.rpc;

import global.services.shared.Advertisement;
import global.services.shared.AppScore;
import global.services.shared.File;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("appScores")
public interface FileService extends RemoteService {

	public Long UploadFile(File file);
	public Long UpdateFile(File file);
	public Long DeleteFile(String userId, String fileId);
	public Long DeleteFiles(String userId);
	public File SelectFile(String userId, String appId);
	public List<File> SelectFiles(String userId);
}

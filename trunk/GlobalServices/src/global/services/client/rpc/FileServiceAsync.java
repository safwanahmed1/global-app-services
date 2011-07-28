package global.services.client.rpc;



import global.services.shared.FileInfo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FileServiceAsync {
	 public void UpdateFile(FileInfo file, AsyncCallback<Long> callback);
	 public void DeleteFile(String userId, Long fileId, AsyncCallback<Long> callback);
	 public void DeleteFiles(String userId, List<Long> listFileId, AsyncCallback<Integer> callback);
	 public void DeleteFiles(String userId, AsyncCallback<Long> callback);
	 public void SelectFile(String userId, String fileId, AsyncCallback<FileInfo> callback);
	 public void SelectFiles(String userId, AsyncCallback<List<FileInfo>> callback);
}

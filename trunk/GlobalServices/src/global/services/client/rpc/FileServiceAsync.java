package global.services.client.rpc;



import global.services.shared.FileStore;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FileServiceAsync {
	 public void InsertFile(FileStore file, AsyncCallback<Long> callback);
	 public void UpdateFile(FileStore file, AsyncCallback<Long> callback);
	 public void DeleteFile(String userId, String appId, AsyncCallback<Long> callback);
	 public void DeleteFiles(String userId, AsyncCallback<Long> callback);
	 public void SelectFile(String userId, String appId, AsyncCallback<FileStore> callback);
	 public void SelectFiles(String userId, AsyncCallback<List<FileStore>> callback);
}

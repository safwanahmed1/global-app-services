package global.services.client.rpc;



import global.services.shared.Advertisement;
import global.services.shared.File;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FileServiceAsync {
	 public void UploadFile(File file, AsyncCallback<Long> callback);
	 public void UpdateFile(File file, AsyncCallback<Long> callback);
	 public void DeleteFile(String userId, String appId, AsyncCallback<Long> callback);
	 public void DeleteFiles(String userId, AsyncCallback<Long> callback);
	 public void SelectFile(String userId, String appId, AsyncCallback<File> callback);
	 public void SelectFiles(String userId, AsyncCallback<List<File>> callback);
}

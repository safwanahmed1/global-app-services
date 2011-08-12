package global.services.client.rpc;

import global.services.shared.Notification;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NotificationServiceAsync {
	 public void InsertNote(Notification note, AsyncCallback<Long> callback);
	 public void UpdateNote(Notification note, AsyncCallback<Long> callback);
	 public void DeleteNote(String userId, Long appId, AsyncCallback<Long> callback);
	 public void DeleteNotes(String userId, List<Long> listAppId, AsyncCallback<Integer> callback);
	 public void DeleteNotes(String userId, AsyncCallback<Long> callback);
	 public void SelectNote(String userId, Long noteId, AsyncCallback<Notification> callback);
	 public void SelectNotes(String userId, Long appId, AsyncCallback<List<Notification>> callback);
}

package global.services.client.rpc;

import global.services.shared.Notification;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NotificationServiceAsync {
	 public void InsertNote(Notification note, AsyncCallback<Long> callback);
	 public void UpdateNote(Notification note, AsyncCallback<Long> callback);
	 public void DeleteNote(String userId, String appId, AsyncCallback<Long> callback);
	 public void DeleteNotes(String userId, AsyncCallback<Long> callback);
	 public void SelectNote(String userId, String appId, AsyncCallback<Notification> callback);
	 public void SelectNotes(String userId, AsyncCallback<List<Notification>> callback);
}

package global.services.client.rpc;


import global.services.shared.Notification;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("notifications")
public interface NotificationService extends RemoteService {

	public Long InsertNote(Notification note);
	public Long UpdateNote(Notification note);
	public Long DeleteNote(String userId, String appId);
	public int DeleteNotes(String userId, List<String> listAppId);
	public Long DeleteNotes(String userId);
	public Notification SelectNote(String userId, String appId);
	public List<Notification> SelectNotes(String userId);
}
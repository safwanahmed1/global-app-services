package global.services.server.rpc;

import java.util.List;

import global.services.client.rpc.NotificationService;
import global.services.server.database.NotificationDataBase;
import global.services.shared.Notification;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class NotificationServiceImpl extends RemoteServiceServlet implements
NotificationService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Long DeleteNote(String userId, String appId) {
		// TODO Auto-generated method stub
		Long ret = null;
		if (userId != null) {
			NotificationDataBase noteDB = new NotificationDataBase();
			ret = noteDB.DeleteNote(userId, appId);
		}
			
		return ret;
	}

	@Override
	public Long DeleteNotes(String userId) {
		// TODO Auto-generated method stub
		Long ret = null;
		if (userId != null) {
			NotificationDataBase noteDB = new NotificationDataBase();
			ret = noteDB.DeleteNotes(userId);
		}
			
		return ret;
	}

	@Override
	public Long InsertNote(Notification note) {
		// TODO Auto-generated method stub
		Long ret = null;

		NotificationDataBase noteDB = new NotificationDataBase();
		ret = noteDB.InsertNote(note).getId();
		return ret;
	}

	@Override
	public Notification SelectNote(String userId, String appId) {
		// TODO Auto-generated method stub
		NotificationDataBase noteDB = new NotificationDataBase();
		return noteDB.SelectNote(userId, appId);

	}

	@Override
	public List<Notification> SelectNotes(String userId) {
		// TODO Auto-generated method stub
		NotificationDataBase noteDB = new NotificationDataBase();
		return noteDB.SelectNotes(userId);
	}

	@Override
	public Long UpdateNote(Notification note) {
		// TODO Auto-generated method stub
		return null;
	}


}

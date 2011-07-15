package global.services.server.rpc;

import java.util.List;

import global.services.client.rpc.NotificationService;
import global.services.server.database.AppScoreDataBase;
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
			noteDB.Finalize();
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
			noteDB.Finalize();
		}
			
		return ret;
	}

	@Override
	public Long InsertNote(Notification note) {
		// TODO Auto-generated method stub
		Long ret = null;

		NotificationDataBase noteDB = new NotificationDataBase();
		ret = noteDB.InsertNote(note).getId();
		noteDB.Finalize();
		return ret;
	}

	@Override
	public Notification SelectNote(String userId, Long appId) {
		// TODO Auto-generated method stub
		NotificationDataBase noteDB = new NotificationDataBase();
		Notification noteRet = noteDB.SelectNote(userId, appId);
		noteDB.Finalize();
		return noteRet;

	}

	@Override
	public List<Notification> SelectNotes(String userId) {
		// TODO Auto-generated method stub
		NotificationDataBase noteDB = new NotificationDataBase();
		List<Notification> noteListRet = noteDB.SelectNotes(userId);
		noteDB.Finalize();
		return noteListRet;
	}

	@Override
	public Long UpdateNote(Notification note) {
		// TODO Auto-generated method stub
		NotificationDataBase noteDB = new NotificationDataBase();
		Long ret = noteDB.UpdateNote(note);
		noteDB.Finalize();
		return ret;
	}

	@Override
	public int DeleteNotes(String userId, List<String> listAppId) {
		// TODO Auto-generated method stub
		Long tmpRet = (long) 0;
		int ret = 0;
		for (String appId : listAppId) {
			tmpRet = DeleteNote(userId, appId);
			if (tmpRet != null) 
				ret++;
		}
		return ret;
	}


}

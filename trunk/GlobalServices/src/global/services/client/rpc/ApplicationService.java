package global.services.client.rpc;

import global.services.shared.Application;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("appScores")
public interface ApplicationService extends RemoteService {

	public Long InsertApp(Application app);

	public Long UpdateApp(Application app);

	public Long DeleteApp(String userId, Long appId);

	public int DeleteApps(String userId, List<Long> listAppId);

	public Long DeleteApps(String userId);

	public Application SelectApp(String userId, Long appId);

	public List<Application> SelectApps(String userId);
}

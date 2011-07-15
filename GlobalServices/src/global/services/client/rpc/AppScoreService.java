package global.services.client.rpc;

import global.services.shared.AppScore;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("appScores")
public interface AppScoreService extends RemoteService {

	public Long InsertApp(AppScore app);
	public Long UpdateApp(AppScore app);
	public Long DeleteApp(String userId, String appId);
	public int DeleteApps(String userId, List<String> listAppId);
	public Long DeleteApps(String userId);
	public AppScore SelectApp(String userId, Long appId);
	public List<AppScore> SelectApps(String userId);
}

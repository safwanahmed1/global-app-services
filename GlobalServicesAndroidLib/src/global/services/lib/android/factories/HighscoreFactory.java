package global.services.lib.android.factories;

import global.services.lib.android.httpclient.RequestMethod;
import global.services.lib.android.httpclient.RestClient;
import global.services.lib.android.objects.Highscore;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.gson.Gson;

public class HighscoreFactory {

	private static final String HIGHSCORE_SERVLET = "http://global-app-services.appspot.com/globalservices/scoreservlet";
	private static final String REQUEST_TYPE_SUBMIT = "submitscore";
	private static final String REQUEST_TYPE_GET = "getscore";
	private String userId_;
	private Long appId_;
	private int pageIdx_ = 0;
	private int pageSize_ = 15;
	private RestClient highScoreRest;

	public HighscoreFactory(String userId, Long appId) {
		userId_ = userId;
		appId_ = appId;
		highScoreRest = new RestClient(HIGHSCORE_SERVLET);

	}

	public void SubmitScore(Highscore score) {
		highScoreRest.ClearParams();
		highScoreRest.AddParam("requesttype", REQUEST_TYPE_SUBMIT);
		highScoreRest.AddParam("userid", score.getUserID());
		highScoreRest.AddParam("appid", String.valueOf(score.getGameID()));
		highScoreRest.AddParam("subboard", score.getSubBoard());
		highScoreRest.AddParam("player", score.getPlayer());
		highScoreRest.AddParam("score", String.valueOf(score.getHighScore()));
		highScoreRest.AddParam("during", String.valueOf(score.getDuring()));
		highScoreRest.AddParam("location", score.getLocation());
		highScoreRest.AddParam("comment", score.getComment());
		highScoreRest.AddParam("date", String.valueOf(score.getDate()));
		// highScoreRest.AddParam("avatar", score.getAvatar()); Not support yet
		/*
		 * long now = System.currentTimeMillis(); highScoreRest.AddParam("Date",
		 * String.valueOf(now));
		 */

		try {
			highScoreRest.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// textView.setText(e.getMessage());
		}
	}

	public String GetScoresJSONContent() {
		highScoreRest.ClearParams();
		highScoreRest.AddParam("requesttype", REQUEST_TYPE_GET);
		highScoreRest.AddParam("userid", userId_);
		highScoreRest.AddParam("appid", String.valueOf(appId_));
		highScoreRest.AddParam("pageindex", String.valueOf(pageIdx_));
		highScoreRest.AddParam("pagesize", String.valueOf(pageSize_));

		try {
			highScoreRest.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// textView.setText(e.getMessage());
		}
		String strResponse = highScoreRest.getResponse();
		return strResponse;
	}

	public List<Highscore> GetScores() {
		List<Highscore> scoreList = new ArrayList<Highscore>();
		// String avatar; Not support yet

		String strResponse = GetScoresJSONContent();

		try {
			JSONArray scoresList = new JSONArray(strResponse);
			for (int i = 0; i < scoresList.length(); i++) {
				JSONObject jsonScore = scoresList.getJSONObject(i);
				Gson scoreParse = new Gson();
				Highscore scoreObj = scoreParse.fromJson(jsonScore.toString(),
						Highscore.class);
				scoreList.add(scoreObj);
			}

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return scoreList;
	}

	public int getPageIdx() {
		return pageIdx_;
	}

	public void setPageIdx(int pageIdx_) {
		this.pageIdx_ = pageIdx_;
	}

	public int getPageSize() {
		return pageSize_;
	}

	public void setPageSize(int pageSize_) {
		this.pageSize_ = pageSize_;
	}

}

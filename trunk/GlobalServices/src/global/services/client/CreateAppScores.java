package global.services.client;

import global.services.client.rpc.AppScoreService;
import global.services.client.rpc.AppScoreServiceAsync;
import global.services.shared.AppScore;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CreateAppScores {
	private TextBox txtAppId = new TextBox();
	private TextBox txtAppTitle = new TextBox();
	private String userId_ = null;
	private AppScore appObj = null;

	private AppScoreServiceAsync appScoreSvc = GWT.create(AppScoreService.class);
	
	private Button btnAddApp= new Button("Create App", new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			String appID = txtAppId.getText();
			String appTittle = txtAppTitle.getText();
			if ((appID != null) && (appTittle != null)
					&& (!appID.equals("")) && (!appTittle.equals(""))) {

				// Set up the callback object.
				AsyncCallback<Long> callback = new AsyncCallback<Long>() {
					public void onFailure(Throwable caught) {
						// TODO: Do something with errors.
					}

					public void onSuccess(Long result) {
					}
				};
				if (appObj == null) {
					appObj = new AppScore(appID);
					appObj.setAppTittle(appTittle);
					appObj.setUserId(userId_);
					appScoreSvc.InsertApp(appObj, callback);
				} else {
					appObj.setAppId(appID);
					appObj.setAppTittle(appTittle);
					appScoreSvc.UpdateApp(appObj, callback);
				}

				GlobalServices.ComebackHome(true);
			} else {
				Window.alert("Please input fully application information.");
			}
		}
	});

	public CreateAppScores(String userId) {
		userId_ = userId;
	}

	public CreateAppScores(String userId, String appId) {
		userId_ = userId;
		appScoreSvc.SelectApp(userId, appId, new AsyncCallback<AppScore>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
			}

			public void onSuccess(AppScore result) {
				appObj = result;
				btnAddApp.setText("Update app");
				txtAppId.setText(appObj.getAppId());
				txtAppTitle.setText(appObj.getAppTittle());
			}
		});
	}

	public Widget Initialize() {
		VerticalPanel mainContent = new VerticalPanel();
		mainContent.setStyleName("contentBackgroud");
		mainContent.add(new Label("Create new app score"));
		mainContent.add(new Label("App Identifier:"));

		mainContent.add(txtAppId);

		mainContent.add(new Label("App Title:"));

		mainContent.add(txtAppTitle);

		HorizontalPanel controlButton = new HorizontalPanel();
		controlButton.add(btnAddApp);
		controlButton.add(new Button("Cancel", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				GlobalServices.ComebackHome(false);
			}
		}));
		mainContent.add(controlButton);
		return mainContent;

	}
}

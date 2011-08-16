package global.services.client;

import global.services.client.rpc.AppScoreService;
import global.services.client.rpc.AppScoreServiceAsync;
import global.services.shared.AppScore;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CreateAppScores {
	private TextBox txtAppName = new TextBox();
	private TextBox txtAppTitle = new TextBox();
	private String userId_ = null;
	private AppScore appObj = null;

	private AppScoreServiceAsync appScoreSvc = GWT.create(AppScoreService.class);
	
	private Button btnAddApp= new Button("Create App", new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			String appName = txtAppName.getText();
			String appTittle = txtAppTitle.getText();
			if ((appName != null) && (appTittle != null)
					&& (!appName.equals("")) && (!appTittle.equals(""))) {

				// Set up the callback object.
				AsyncCallback<Long> callback = new AsyncCallback<Long>() {
					public void onFailure(Throwable caught) {
						// TODO: Do something with errors.
					}

					public void onSuccess(Long result) {
					}
				};
				if (appObj == null){
					appObj = new AppScore(userId_);
					appObj.setAppName(appName);
					appObj.setAppTittle(appTittle);
					appScoreSvc.InsertApp(appObj, callback);
				} else {
					appObj.setAppName(appName);
					appObj.setAppTittle(appTittle);
					appScoreSvc.UpdateApp(appObj, callback);
				}

				//GlobalServices.ComebackHome(true);
				History.newItem("root-application");
			} else {
				Window.alert("Please input fully application information.");
			}
		}
	});

	public CreateAppScores(String userId) {
		userId_ = userId;
	}

	public CreateAppScores(String userId, Long appId) {
		userId_ = userId;
		appScoreSvc.SelectApp(userId, appId, new AsyncCallback<AppScore>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
			}

			public void onSuccess(AppScore result) {
				appObj = result;
				btnAddApp.setText("Update app");
				txtAppName.setText(appObj.getAppName());
				txtAppTitle.setText(appObj.getAppTittle());
			}
		});
	}

	public Widget Initialize() {
		VerticalPanel mainContent = new VerticalPanel();
		mainContent.setStyleName("contentBackgroud");
		mainContent.add(new Label("Create new app score"));
		mainContent.add(new Label("App Name:"));

		mainContent.add(txtAppName);

		mainContent.add(new Label("App Title:"));

		mainContent.add(txtAppTitle);

		HorizontalPanel controlButton = new HorizontalPanel();
		controlButton.add(btnAddApp);
		controlButton.add(new Button("Cancel", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				//GlobalServices.ComebackHome(false);
				History.newItem("root-application");
			}
		}));
		mainContent.add(controlButton);
		return mainContent;

	}
}

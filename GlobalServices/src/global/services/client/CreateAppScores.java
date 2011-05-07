package global.services.client;


import global.services.client.rpc.AppScoreService;
import global.services.client.rpc.AppScoreServiceAsync;
import global.services.shared.AppScore;
import global.services.shared.LoginInfo;

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
	private TextBox txtAppId;
	private TextBox txtAppTitle;
	private LoginInfo loginInfo = null;
	public LoginInfo getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}

	private AppScoreServiceAsync appScoreSvc;

	public Widget Initialize() {
		VerticalPanel mainContent = new VerticalPanel();
		mainContent.add(new Label("Create new app score"));
		mainContent.add(new Label("You have 7 games remaining."));
		mainContent.add(new Label("App Identifier:"));
		txtAppId = new TextBox();
		mainContent.add(txtAppId);

		mainContent.add(new Label("App Title:"));
		txtAppTitle = new TextBox();
		mainContent.add(txtAppTitle);

		HorizontalPanel controlButton = new HorizontalPanel();
		controlButton.add(new Button("Create App", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				String appID = txtAppId.getText();
				String appTittle = txtAppTitle.getText();
				if (appID != null) {
					AppScore newApp = new AppScore(appID);
					newApp.setAppTittle(appTittle);
					newApp.setUserId(loginInfo.getEmailAddress());

					// Set up the callback object.
					AsyncCallback<Long> callback = new AsyncCallback<Long>() {
						public void onFailure(Throwable caught) {
							// TODO: Do something with errors.
							Window.alert("Fail roi");
						}

						public void onSuccess(Long result) {
							Window.alert("Id: " + result);
						}
					};
					appScoreSvc = GWT.create(AppScoreService.class);
					appScoreSvc.InsertApp(newApp, callback);

					GlobalServices.mainPanel.clear();
					GlobalServices.mainPanel.addNorth(
							GlobalServices.headerPanel, 50);
					GlobalServices.mainPanel.addSouth(
							GlobalServices.footerPanel, 50);
					GlobalServices.mainPanel
							.add(GlobalServices.servicesTabPanel);
				}
			}
		}));
		controlButton.add(new Button("Cancel", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				GlobalServices.mainPanel.clear();
				GlobalServices.mainPanel.addNorth(GlobalServices.headerPanel,
						50);
				GlobalServices.mainPanel.addSouth(GlobalServices.footerPanel,
						50);
				GlobalServices.mainPanel.add(GlobalServices.servicesTabPanel);
			}
		}));
		mainContent.add(controlButton);
		return mainContent;

	}
}

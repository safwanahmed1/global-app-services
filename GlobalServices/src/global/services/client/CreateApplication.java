package global.services.client;

import global.services.client.rpc.ApplicationService;
import global.services.client.rpc.ApplicationServiceAsync;
import global.services.shared.Application;
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

public class CreateApplication {
	private TextBox txtAppName = new TextBox();
	private TextBox txtAppTitle = new TextBox();
	private String userId_ = null;
	private Application appObj = null;

	private ApplicationServiceAsync appScoreSvc = GWT.create(ApplicationService.class);
	
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
					appObj = new Application(userId_);
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

	public CreateApplication(String userId) {
		userId_ = userId;
	}

	public CreateApplication(String userId, Long appId) {
		userId_ = userId;
		appScoreSvc.SelectApp(userId, appId, new AsyncCallback<Application>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
			}

			public void onSuccess(Application result) {
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

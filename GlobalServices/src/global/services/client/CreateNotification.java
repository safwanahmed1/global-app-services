package global.services.client;

import java.util.Date;

import global.services.client.rpc.NotificationService;
import global.services.client.rpc.NotificationServiceAsync;
import global.services.shared.LoginInfo;
import global.services.shared.Notification;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class CreateNotification {
		private TextBox txtAppId;
		private TextBox txtAppTitle;
		private TextBox txtContent;
		private DateBox dateFrom;
		private DateBox dateTo;

		private LoginInfo loginInfo = null;
		public LoginInfo getLoginInfo() {
			return loginInfo;
		}

		public void setLoginInfo(LoginInfo loginInfo) {
			this.loginInfo = loginInfo;
		}

		private NotificationServiceAsync NoteSvc;

		public Widget Initialize() {
			VerticalPanel mainContent = new VerticalPanel();
			mainContent.add(new Label("Create new notification"));
			mainContent.add(new Label("You have 7 notifications remaining."));
			
			mainContent.add(new Label("App Identifier:"));
			txtAppId = new TextBox();
			mainContent.add(txtAppId);

			mainContent.add(new Label("Title:"));
			txtAppTitle = new TextBox();
			mainContent.add(txtAppTitle);
			
			mainContent.add(new Label("Content:"));
			txtContent = new TextBox();
			mainContent.add(txtContent);
			
			mainContent.add(new Label("From:"));
			dateFrom = new DateBox();
			mainContent.add(dateFrom);
			
			mainContent.add(new Label("To:"));
			dateTo = new DateBox();
			mainContent.add(dateTo);

			HorizontalPanel controlButton = new HorizontalPanel();
			controlButton.add(new Button("Create note", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					String appID = txtAppId.getText();
					String appTittle = txtAppTitle.getText();
					
					
					if (appID != null) {
						Notification newNote = new Notification(appID);
						newNote.setUserId(loginInfo.getEmailAddress());
						newNote.setTittle(appTittle);
						newNote.setContent(txtContent.getText());
						newNote.setFromDate(Date.parse(dateFrom.getValue().toString()));
						newNote.setToDate(Date.parse(dateTo.getValue().toString()));
						

						// Set up the callback object.
						AsyncCallback<Long> callback = new AsyncCallback<Long>() {
							public void onFailure(Throwable caught) {
								// TODO: Do something with errors.
							}

							public void onSuccess(Long result) {
							}
						};
						NoteSvc = GWT.create(NotificationService.class);
						NoteSvc.InsertNote(newNote, callback);

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

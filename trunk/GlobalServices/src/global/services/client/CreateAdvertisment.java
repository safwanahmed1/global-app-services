package global.services.client;

import global.services.client.rpc.AdvertisementService;
import global.services.client.rpc.AdvertisementServiceAsync;
import global.services.shared.Advertisement;
import global.services.shared.LoginInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CreateAdvertisment {
		private Image imgIcon;
		private TextBox txtAppId;
		private TextBox txtTitle;
		private TextBox txtContent;
		private TextBox txtType;
		private TextBox txtStoreUrl;
		private LoginInfo loginInfo = null;
		public LoginInfo getLoginInfo() {
			return loginInfo;
		}

		public void setLoginInfo(LoginInfo loginInfo) {
			this.loginInfo = loginInfo;
		}

		private AdvertisementServiceAsync advSvc;

		public Widget Initialize() {
			VerticalPanel mainContent = new VerticalPanel();
			mainContent.add(new Label("Create new advertisment score"));
			mainContent.add(new Label("You have 7 advertisment remaining."));
			
			mainContent.add(new Label("Icon app:"));
			imgIcon = new Image();
			mainContent.add(imgIcon);

			mainContent.add(new Label("App Identifier:"));
			txtAppId = new TextBox();
			mainContent.add(txtAppId);

			mainContent.add(new Label("Title:"));
			txtTitle = new TextBox();
			mainContent.add(txtTitle);
			
			mainContent.add(new Label("Content:"));
			txtContent = new TextBox();
			mainContent.add(txtContent);
			
			mainContent.add(new Label("Type:"));
			txtType = new TextBox();
			mainContent.add(txtType);
			
			mainContent.add(new Label("Store Url"));
			txtStoreUrl = new TextBox();
			mainContent.add(txtStoreUrl);


			HorizontalPanel controlButton = new HorizontalPanel();
			controlButton.add(new Button("Create adv", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					String appID = txtAppId.getText();
					String appTittle = txtTitle.getText();
					if (appID != null) {
						Advertisement newApp = new Advertisement(appID);
						newApp.setUserId(loginInfo.getEmailAddress());
						newApp.setTittle(appTittle);
						newApp.setContent(txtContent.getText());
						newApp.setType(txtType.getText());
						newApp.setStoreUrl(txtStoreUrl.getText());
						newApp.setIconUrl(imgIcon.getUrl());
						
						

						// Set up the callback object.
						AsyncCallback<Long> callback = new AsyncCallback<Long>() {
							public void onFailure(Throwable caught) {
								// TODO: Do something with errors.
							}

							public void onSuccess(Long result) {
							}
						};
						advSvc = GWT.create(AdvertisementService.class);
						advSvc.InsertAdv(newApp, callback);

						GlobalServices.ComebackHome(true);
					}
				}
			}));
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

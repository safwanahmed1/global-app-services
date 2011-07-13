package global.services.client;

import java.util.Date;

import global.services.client.rpc.AdvertisementService;
import global.services.client.rpc.NotificationService;
import global.services.client.rpc.NotificationServiceAsync;
import global.services.shared.Advertisement;
import global.services.shared.LoginInfo;
import global.services.shared.Notification;
import gwtupload.client.PreloadedImage;

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
	private TextBox txtAppId = new TextBox();
	private TextBox txtTitle = new TextBox();
	private TextBox txtContent = new TextBox();
	private DateBox dateFrom = new DateBox();
	private DateBox dateTo = new DateBox();

	private String userId_ = null;
	private Notification noteObj = null;

	private NotificationServiceAsync noteSvc = GWT
			.create(NotificationService.class);

	public CreateNotification(String userId) {
		userId_ = userId;
	}

	public CreateNotification(String userId, String appId) {
		userId_ = userId;
		noteSvc.SelectNote(userId, appId, new AsyncCallback<Notification>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
			}

			public void onSuccess(Notification result) {
				noteObj = result;

				txtAppId.setText(noteObj.getAppId());
				txtContent.setText(noteObj.getContent());
				txtTitle.setText(noteObj.getTittle());
				dateFrom.setValue(new Date(noteObj.getFromDate()));
				dateTo.setValue(new Date(noteObj.getToDate()));
			}
		});
	}

	public Widget Initialize() {
		VerticalPanel mainContent = new VerticalPanel();
		mainContent.add(new Label("Create new notification"));

		mainContent.add(new Label("App Identifier:"));
		mainContent.add(txtAppId);

		mainContent.add(new Label("Title:"));
		mainContent.add(txtTitle);

		mainContent.add(new Label("Content:"));
		mainContent.add(txtContent);

		mainContent.add(new Label("From:"));
		mainContent.add(dateFrom);

		mainContent.add(new Label("To:"));
		mainContent.add(dateTo);

		HorizontalPanel controlButton = new HorizontalPanel();
		controlButton.add(new Button("Create note", new ClickHandler() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				String appID = txtAppId.getText();
				String appTittle = txtTitle.getText();

				if ((appID != null) && (!appID.equals(""))) {

					Notification newNote = new Notification(appID);
					newNote.setUserId(userId_);
					newNote.setTittle(appTittle);
					newNote.setContent(txtContent.getText());
					newNote.setFromDate(Date.parse(dateFrom.getValue()
							.toString()));
					newNote.setToDate(Date.parse(dateTo.getValue().toString()));

					// Set up the callback object.
					AsyncCallback<Long> callback = new AsyncCallback<Long>() {
						public void onFailure(Throwable caught) {
							// TODO: Do something with errors.
						}

						public void onSuccess(Long result) {
						}
					};
					noteSvc.InsertNote(newNote, callback);

					if (noteObj == null) {
						noteObj = new Notification(appID);

						noteObj.setUserId(userId_);
						noteObj.setTittle(txtTitle.getText());
						noteObj.setContent(txtContent.getText());
						noteObj.setFromDate(Date.parse(dateFrom.getValue()
								.toString()));
						noteObj.setToDate(Date.parse(dateTo.getValue()
								.toString()));

						noteSvc.InsertNote(noteObj, callback);
					} else {
						noteObj.setAppId(appID);
						noteObj.setTittle(txtTitle.getText());
						noteObj.setContent(txtContent.getText());
						noteObj.setFromDate(Date.parse(dateFrom.getValue()
								.toString()));
						noteObj.setToDate(Date.parse(dateTo.getValue()
								.toString()));

						noteSvc.UpdateNote(noteObj, callback);
					}

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

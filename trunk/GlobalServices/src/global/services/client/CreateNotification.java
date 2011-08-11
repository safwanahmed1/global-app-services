package global.services.client;

import java.util.Date;

import global.services.client.rpc.NotificationService;
import global.services.client.rpc.NotificationServiceAsync;
import global.services.shared.HighScore;
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
	private TextBox txtAppId = new TextBox();
	private TextBox txtTitle = new TextBox();
	private TextBox txtContent = new TextBox();
	private DateBox dateFrom = new DateBox();
	private DateBox dateTo = new DateBox();

	private String userId_ = null;
	private Long appId_ = null;
	private Notification noteObj = null;
	private Button btnAddNote = new Button("Create note",  new ClickHandler() {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			String appID = txtAppId.getText();
			String appTittle = txtTitle.getText();

			if ((appID != null) && (!appID.equals(""))) {

				// Set up the callback object.
				AsyncCallback<Long> callback = new AsyncCallback<Long>() {
					public void onFailure(Throwable caught) {
						// TODO: Do something with errors.
					}

					public void onSuccess(Long result) {
					}
				};

				if (noteObj == null) {
					noteObj = new Notification(userId_, appId_);

					noteObj.setUserId(userId_);
					noteObj.setTittle(txtTitle.getText());
					noteObj.setContent(txtContent.getText());
					noteObj.setFromDate(Date.parse(dateFrom.getValue()
							.toString()));
					noteObj.setToDate(Date.parse(dateTo.getValue()
							.toString()));

					noteSvc.InsertNote(noteObj, callback);
				} else {
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
	});

	private NotificationServiceAsync noteSvc = GWT
			.create(NotificationService.class);

	public CreateNotification(String userId, Long appId) {
		userId_ = userId;
		appId_ = appId;
	}

	public CreateNotification(Notification note) {
		noteObj = note;
		userId_ = note.getUserId();
		appId_ = note.getAppId();
		btnAddNote.setText("Update note");
		txtTitle.setText(noteObj.getTittle());
		txtContent.setText(noteObj.getContent());
		dateFrom.setValue(new Date(noteObj.getFromDate()));
		dateTo.setValue(new Date(noteObj.getToDate()));
		
	}

	public Widget Initialize() {
		VerticalPanel mainContent = new VerticalPanel();
		mainContent.setStyleName("contentBackgroud");
		mainContent.add(new Label("Create new notification"));

		mainContent.add(new Label("App Name:"));
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
		controlButton.add( btnAddNote);
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

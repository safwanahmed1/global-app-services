package global.services.client;

import global.services.client.rpc.HighScoreService;
import global.services.client.rpc.HighScoreServiceAsync;

import global.services.shared.HighScore;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Date;

public class CreateHighScore {
	private TextBox txtSubBoard = new TextBox();
	private TextBox txtPlayer = new TextBox();
	private TextBox txtLocation = new TextBox();
	private TextBox txtScore = new TextBox();
	private TextBox txtDuring = new TextBox();
	private TextBox txtComment = new TextBox();

	private String userId_ = null;
	private Long appId_ = null;
	private FlowPanel panelImages = new FlowPanel();
	private HighScore scoreObj = null;

	private HighScoreServiceAsync scoreSvc = GWT.create(HighScoreService.class);

	private Button btnAddScore = new Button("Create entry", new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub

			// formUpload.submit();
			AsyncCallback<Long> callback = new AsyncCallback<Long>() {
				public void onFailure(Throwable caught) {
					// TODO: Do something with errors.
					Window.alert("Creating score has NOT successful.");
					GlobalServices.HighScorePage(appId_);
				}

				public void onSuccess(Long result) {
					Window.alert("Creating score has successful.");
					GlobalServices.HighScorePage(appId_);
				}
			};
			if (scoreObj == null) {
				scoreObj = new HighScore(userId_,appId_);
				scoreObj.setPlayer(txtPlayer.getText());
				scoreObj.setSubBoard(txtSubBoard.getText());
				scoreObj.setLocation(txtLocation.getText());
				scoreObj.setHighScore(Integer.valueOf(txtScore.getText()));
				scoreObj.setDuring(Long.valueOf(txtDuring.getText()));
				scoreObj.setComment(txtComment.getText());
				scoreObj.setDate(new Date().getTime());
				scoreObj.setUserID(userId_);

				scoreSvc.InsertScore(scoreObj, callback);
			} else {
				scoreObj.setPlayer(txtPlayer.getText());
				scoreObj.setGameID(appId_);
				scoreObj.setSubBoard(txtSubBoard.getName());
				scoreObj.setLocation(txtLocation.getText());
				scoreObj.setHighScore(Integer.valueOf(txtScore.getText()));
				scoreObj.setDuring(Long.valueOf(txtDuring.getText()));
				scoreObj.setComment(txtComment.getText());
				scoreObj.setDate(new Date().getTime());

				scoreSvc.UpdateScore(scoreObj, callback);
			}
			
		}
	});

	public CreateHighScore(String userId, Long appId) {
		userId_ = userId;
		appId_ = appId;
	}

	public CreateHighScore(HighScore object) {
		btnAddScore.setText("Update score");
		scoreObj = object;
		userId_ = scoreObj.getUserID();
		appId_ = scoreObj.getGameID();
		txtPlayer.setText(scoreObj.getPlayer());
		txtDuring.setText(String.valueOf(scoreObj.getDuring()));
		txtSubBoard.setText(scoreObj.getSubBoard());
		txtScore.setText(String.valueOf(scoreObj.getHighScore()));
		txtLocation.setText(scoreObj.getLocation());
		txtComment.setText(scoreObj.getComment());

	}

	public Widget Initialize() {
		VerticalPanel mainContent = new VerticalPanel();
		mainContent.setStyleName("contentBackgroud");
		mainContent.add(new Label("Create new score entry"));

		mainContent.add(new Label("Level:"));
		mainContent.add(txtSubBoard);

		mainContent.add(new Label("Player:"));
		mainContent.add(txtPlayer);

		mainContent.add(new Label("Location:"));
		mainContent.add(txtLocation);

		mainContent.add(new Label("Score:"));
		mainContent.add(txtScore);

		mainContent.add(new Label("During:"));
		mainContent.add(txtDuring);

		mainContent.add(new Label("Comment:"));
		mainContent.add(txtComment);

		HorizontalPanel controlButton = new HorizontalPanel();
		controlButton.add(btnAddScore);
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

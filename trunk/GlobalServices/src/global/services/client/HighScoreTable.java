package global.services.client;

import global.services.client.rpc.AppScoreService;
import global.services.client.rpc.AppScoreServiceAsync;
import global.services.client.rpc.HighScoreService;
import global.services.client.rpc.HighScoreServiceAsync;
import global.services.shared.AppScore;
import global.services.shared.HighScore;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;

public class HighScoreTable {

	private String userId_ = null;
	private Long appId_ = null;
	private String appName_ = null;

	private CellTable<HighScore> scoreCellTable = new CellTable<HighScore>();
	private List<Long> selectedScores = new ArrayList<Long>();
	private VerticalPanel mainContent = new VerticalPanel();
	private List<HighScore> listScore = null;
	private Label lblAppInfo =new Label("Highscore table of ... application.");
	static HighScoreServiceAsync scoreSvc = GWT.create(HighScoreService.class);
	static AppScoreServiceAsync appSvc = GWT.create(AppScoreService.class);

	public HighScoreTable(String userId, Long appId) {
		userId_ = userId;
		appId_ = appId;
		appSvc.SelectApp(userId, appId, new AsyncCallback<AppScore>() {
					public void onFailure(Throwable caught) {
						// TODO: Do something
						// with
						// errors.
					}

					public void onSuccess(AppScore result) {

						appName_ = result.getAppName();
						lblAppInfo.setText("Highscore table of " + appName_ + " application.");
					}
				});
		RefreshHighScoreTbl();
	}

	public Widget Initialize() {

		mainContent.setStyleName("contentBackgroud");
		mainContent.add(lblAppInfo );

		final SelectionModel<HighScore> selectionAppModel = new MultiSelectionModel<HighScore>(
				HighScore.KEY_PROVIDER);
		scoreCellTable.setSelectionModel(selectionAppModel,
				DefaultSelectionEventManager
						.<HighScore> createCheckboxManager());

		Column<HighScore, Boolean> checkColumn = new Column<HighScore, Boolean>(
				new CheckboxCell(true, false)) {

			@Override
			public Boolean getValue(HighScore score) {
				// TODO Auto-generated method stub
				if (selectionAppModel.isSelected(score)) {
					if (!selectedScores.contains(score.getId()))
						selectedScores.add(score.getId());
				} else {
					if (selectedScores.contains(score.getId()))
						selectedScores.remove(score.getId());
				}
				return selectionAppModel.isSelected(score);
			}
		};
		scoreCellTable.addColumn(checkColumn,
				SafeHtmlUtils.fromSafeConstant("<br/>"));
		scoreCellTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// Create scoreId column.
		Column<HighScore, String> scoreIdColumn = new Column<HighScore, String>(
				new ClickableTextCell()) {

			@Override
			public void render(Context context, HighScore object,
					SafeHtmlBuilder sb) {
				// TODO Auto-generated method stub
				super.render(context, object, sb);
				if (object != null) {
					sb.appendHtmlConstant("<div class=\"clickableanchor\">");
					sb.appendEscaped(String.valueOf(object.getId()));
					sb.appendHtmlConstant("</div>");
				}
			}

			@Override
			public void onBrowserEvent(Context context, Element elem,
					HighScore object, NativeEvent event) {
				// TODO Auto-generated method stub
				super.onBrowserEvent(context, elem, object, event);
				if (event.getType().equals("click")) {

					CreateHighScore createScore = new CreateHighScore(object);
					mainContent.clear();
					mainContent.add(createScore.Initialize());
				}
				//History.newItem(GlobalServices.createHighScoreToken);
			}

			@Override
			public String getValue(HighScore object) {
				// TODO Auto-generated method stub
				return null;
			}

		};
		scoreIdColumn.setSortable(true);
		scoreCellTable.addColumn(scoreIdColumn, "Score Id");

		// Create subBoard column.
		TextColumn<HighScore> levelColumn = new TextColumn<HighScore>() {
			@Override
			public String getValue(HighScore score) {
				return score.getSubBoard();
			}
		};
		levelColumn.setSortable(true);
		scoreCellTable.addColumn(levelColumn, "Level");

		// Create player column.
		TextColumn<HighScore> playerColumn = new TextColumn<HighScore>() {
			@Override
			public String getValue(HighScore score) {
				// TODO Auto-generated method stub
				return score.getPlayer();
			}

		};
		playerColumn.setSortable(true);
		scoreCellTable.addColumn(playerColumn, "Player");

		// Create score column.
		TextColumn<HighScore> scoreColumn = new TextColumn<HighScore>() {
			@Override
			public String getValue(HighScore score) {
				// TODO Auto-generated method stub
				return String.valueOf(score.getHighScore());
			}

		};
		scoreColumn.setSortable(true);
		scoreCellTable.addColumn(scoreColumn, "Score");

		// Create during column.
		TextColumn<HighScore> duringColumn = new TextColumn<HighScore>() {
			@Override
			public String getValue(HighScore score) {
				// TODO Auto-generated method stub
				return new Time(score.getDuring()).toString();
			}

		};
		duringColumn.setSortable(true);
		scoreCellTable.addColumn(duringColumn, "During");

		// Create location column.
		TextColumn<HighScore> locationColumn = new TextColumn<HighScore>() {
			@Override
			public String getValue(HighScore score) {
				// TODO Auto-generated method stub
				return String.valueOf(score.getLocation());
			}

		};
		locationColumn.setSortable(true);
		scoreCellTable.addColumn(locationColumn, "Location");

		// Create comment column.
		TextColumn<HighScore> commentColumn = new TextColumn<HighScore>() {
			@Override
			public String getValue(HighScore score) {
				// TODO Auto-generated method stub
				return score.getComment();
			}

		};
		commentColumn.setSortable(true);
		scoreCellTable.addColumn(commentColumn, "Comment");
		// Create date column.
		TextColumn<HighScore> dateColumn = new TextColumn<HighScore>() {
			@Override
			public String getValue(HighScore score) {
				// TODO Auto-generated method stub
				return new Date(score.getDate()).toString();
			}

		};
		dateColumn.setSortable(true);
		scoreCellTable.addColumn(dateColumn, "Date");

		// Create a data provider.
		ListDataProvider<HighScore> dataProvider = new ListDataProvider<HighScore>();

		// Connect the table to the data provider.
		dataProvider.addDataDisplay(scoreCellTable);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget.
		listScore = dataProvider.getList();
		RefreshHighScoreTbl();

		mainContent.add(scoreCellTable);

		HorizontalPanel tableGamesCtrPanel = new HorizontalPanel();
		tableGamesCtrPanel.add(new Button("Create score", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				CreateHighScore createScore = new CreateHighScore(userId_, appId_);
				mainContent.clear();
				mainContent.add(createScore.Initialize());
				//History.newItem(GlobalServices.createHighScoreToken);
			}
		}));

		tableGamesCtrPanel.add(new Button("Delete score", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (selectedScores.size() == 0) {
					Window.alert("You have to chose at least an entry to delete.");
				} else {
					if (Window.confirm("Would you want to delete entries")) {
						HighScoreServiceAsync scoreService = GWT
								.create(HighScoreService.class);
						scoreService.DeleteScores(userId_, selectedScores,
								new AsyncCallback<Integer>() {
									public void onFailure(Throwable caught) {
									}

									public void onSuccess(Integer result) {
										// TODO Auto-generated method stub
										Window.alert(result
												+ " Scores have been deleted successful.");
										RefreshHighScoreTbl();
									}
								});
						selectedScores.clear();
					}
				}

			}
		}));
		mainContent.add(tableGamesCtrPanel);

		return mainContent;

	}

	private void RefreshHighScoreTbl() {
		// TODO Auto-generated method stub
		scoreSvc.SelectScores(userId_, appId_,
				new AsyncCallback<List<HighScore>>() {
					public void onFailure(Throwable caught) {
						// TODO: Do something
						// with
						// errors.
					}

					public void onSuccess(List<HighScore> result) {

						listScore.clear();
						listScore.addAll(result);

					}
				});
	}

	public void setAppName(String appName) {
		this.appName_ = appName;
	}

	public String getAppName() {
		return appName_;
	}

}

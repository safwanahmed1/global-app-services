package global.services.client;

import global.services.client.rpc.AppScoreService;
import global.services.client.rpc.AppScoreServiceAsync;
import global.services.client.rpc.HighScoreService;
import global.services.client.rpc.HighScoreServiceAsync;
import global.services.client.rpc.NotificationService;
import global.services.client.rpc.NotificationServiceAsync;
import global.services.shared.AppScore;
import global.services.shared.HighScore;
import global.services.shared.Notification;

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

public class NotificationTable {
	private String userId_ = null;
	private Long appId_ = null;
	private String appName_ = null;

	private VerticalPanel mainContent = new VerticalPanel();
	private List<Notification> listNotes = null;
	private Label lblAppInfo = new Label("Highscore table of ... application.");
	static NotificationServiceAsync noteSvc = GWT.create(NotificationService.class);
	static AppScoreServiceAsync appSvc = GWT.create(AppScoreService.class);

	private CellTable<Notification> notesCellTable = new CellTable<Notification>();
	private List<Long> selectedNotes = new ArrayList<Long>();

	public NotificationTable(String userId, Long appId) {
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
				lblAppInfo.setText("Notification table of " + appName_
						+ " application.");
			}
		});
		RefreshNotificationTbl();
	}

	public Widget Initialize() {

		mainContent.setStyleName("contentBackgroud");
		mainContent.add(lblAppInfo);

		final SelectionModel<Notification> selectionNoteModel = new MultiSelectionModel<Notification>(
				Notification.KEY_PROVIDER);
		notesCellTable.setSelectionModel(selectionNoteModel,
				DefaultSelectionEventManager
						.<Notification> createCheckboxManager());

		Column<Notification, Boolean> checkColumn = new Column<Notification, Boolean>(
				new CheckboxCell(true, false)) {

			@Override
			public Boolean getValue(Notification note) {
				// TODO Auto-generated method stub
				if (selectionNoteModel.isSelected(note)) {
					if (!selectedNotes.contains(note.getId()))
						selectedNotes.add(note.getId());
				} else {
					if (selectedNotes.contains(note.getId()))
						selectedNotes.remove(note.getId());
				}
				return selectionNoteModel.isSelected(note);
			}
		};
		notesCellTable.addColumn(checkColumn,
				SafeHtmlUtils.fromSafeConstant("<br/>"));
		notesCellTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// Create appId column.
		// Create appId column.
		Column<Notification, String> noteIdColumn = new Column<Notification, String>(
				new ClickableTextCell()) {
			/*
			 * @Override public String getValue(AppScore app) { return
			 * app.getAppId(); }
			 */

			@Override
			public void render(Context context, Notification object,
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
					Notification object, NativeEvent event) {
				// TODO Auto-generated method stub
				super.onBrowserEvent(context, elem, object, event);
				if (event.getType().equals("click")) {
					CreateNotification createNote = new CreateNotification(object.getUserId(), object.getId());
					mainContent.clear();
					createNote.setMainContent(mainContent);
					createNote.Initialize();

				}
			}

			@Override
			public String getValue(Notification object) {
				// TODO Auto-generated method stub
				return null;
			}

		};
		noteIdColumn.setSortable(true);
		notesCellTable.addColumn(noteIdColumn, "NoteId");

		// Create appTittle column.
		TextColumn<Notification> noteTittleColumn = new TextColumn<Notification>() {
			@Override
			public String getValue(Notification note) {
				return note.getTittle();
			}
		};
		noteTittleColumn.setSortable(true);
		notesCellTable.addColumn(noteTittleColumn, "Tittle");

		// Create Content column.
		TextColumn<Notification> contentColumn = new TextColumn<Notification>() {
			@Override
			public String getValue(Notification adv) {
				return String.valueOf(adv.getContent());
			}
		};
		contentColumn.setSortable(true);
		notesCellTable.addColumn(contentColumn, "Content");

		// Create from date column.
		TextColumn<Notification> fromDateColumn = new TextColumn<Notification>() {
			@Override
			public String getValue(Notification note) {
				Date fromDate = new Date(note.getFromDate());
				return fromDate.toString();
			}
		};
		fromDateColumn.setSortable(true);
		notesCellTable.addColumn(fromDateColumn, "From");

		// Create to date column.
		TextColumn<Notification> toDateColumn = new TextColumn<Notification>() {
			@Override
			public String getValue(Notification note) {
				return new Date(note.getToDate()).toString();
			}
		};
		toDateColumn.setSortable(true);
		notesCellTable.addColumn(fromDateColumn, "To");

		// Create a data provider.
		ListDataProvider<Notification> dataProvider = new ListDataProvider<Notification>();

		// Connect the table to the data provider.
		dataProvider.addDataDisplay(notesCellTable);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget.
		listNotes = dataProvider.getList();

		RefreshNotificationTbl();

		mainContent.add(notesCellTable);
		HorizontalPanel tableNotesCtrPanel = new HorizontalPanel();
		tableNotesCtrPanel.add(new Button("Create note", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
					CreateNotification createScore = new CreateNotification(userId_, appId_);
					mainContent.clear();
					createScore.setMainContent(mainContent);
					createScore.Initialize();

		
			}
		}));

		tableNotesCtrPanel.add(new Button("Delete notes", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (selectedNotes.size() == 0) {
					Window.alert("You have to chose at least a notification to delete.");
				} else {
					if (Window
							.confirm("Would you want to delete notifications")) {
						NotificationServiceAsync noteService = GWT
								.create(NotificationService.class);
						noteService.DeleteNotes(userId_,
								selectedNotes, new AsyncCallback<Integer>() {
									public void onFailure(Throwable caught) {
									}

									public void onSuccess(Integer result) {
										// TODO Auto-generated method stub
										Window.alert(result
												+ " Notifications have been deleted successful.");
										RefreshNotificationTbl();
									}
								});
						selectedNotes.clear();
					}
				}
			}
		}));
		mainContent.add(tableNotesCtrPanel);

		return mainContent;
	}

	private void RefreshNotificationTbl() {
		// TODO Auto-generated method stub
		noteSvc.SelectNotes(userId_,appId_,
				new AsyncCallback<List<Notification>>() {
					public void onFailure(Throwable caught) {
						// TODO: Do something
						// with
						// errors.
					}

					public void onSuccess(List<Notification> result) {
						listNotes.clear();
						listNotes.addAll(result);
						
					}
				});
	}

	public void setAppName(String appName) {
		this.appName_ = appName;
	}

	public String getAppName() {
		return appName_;
	}
	protected void CreateNotificationPage(Notification note) {
		// TODO Auto-generated method stub
		mainContent.clear();
		CreateNotification createNote;
		if (note == null) {
			createNote = new CreateNotification(userId_, appId_);
		} else {
			createNote = new CreateNotification(note);
		}
		createNote.setMainContent(mainContent);
		createNote.Initialize();
	}

}

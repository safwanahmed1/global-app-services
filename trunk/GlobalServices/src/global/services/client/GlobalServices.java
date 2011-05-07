package global.services.client;

import global.services.client.rpc.AdvertisementService;
import global.services.client.rpc.AdvertisementServiceAsync;
import global.services.client.rpc.AppScoreService;
import global.services.client.rpc.AppScoreServiceAsync;
import global.services.client.rpc.LoginService;
import global.services.client.rpc.LoginServiceAsync;
import global.services.client.rpc.NotificationService;
import global.services.client.rpc.NotificationServiceAsync;
import global.services.shared.Advertisement;
import global.services.shared.AppScore;
import global.services.shared.LoginInfo;
import global.services.shared.Notification;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GlobalServices implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to conktact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */

	/**
	 * This is the entry point method.
	 */
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Google Account to access the StockWatcher application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");

	private RootPanel rootPanel;
	public static DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
	public static TabLayoutPanel servicesTabPanel = new TabLayoutPanel(1.5,
			Unit.EM);
	private CellTable<AppScore> gamesCellTable = new CellTable<AppScore>();
	private CellTable<Advertisement> advsCellTable = new CellTable<Advertisement>();
	private CellTable<Notification> notesCellTable = new CellTable<Notification>();
	public static HorizontalPanel headerPanel = new HorizontalPanel();
	private HorizontalPanel accountPanel = new HorizontalPanel();
	public static HorizontalPanel footerPanel = new HorizontalPanel();
	private VerticalPanel scoresPanel = new VerticalPanel();
	private VerticalPanel gamesPanel = new VerticalPanel();

	private AppScoreServiceAsync appScoreSvc;
	private AdvertisementServiceAsync advSvc;
	private NotificationServiceAsync noteSvc;

	private List<AppScore> listApp;
	private List<Advertisement> listAdvs;
	private List<Notification> listNotes;
	
	private List<String> selectedApps = new ArrayList<String>();
	private List<String> selectedAdvs = new ArrayList<String>();
	private List<String> selectedNotes = new ArrayList<String>();
	public void onModuleLoad() {

		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						if (loginInfo.isLoggedIn()) {
							moduleLoad();
						} else {
							loadLogin();
						}
					}
				});
	}

	private void loadLogin() {
		// Assemble login panel.
		// Window.open(loginInfo.getLoginUrl(),null, null);
		Window.Location.assign(loginInfo.getLoginUrl());
		/*
		 * signInLink.setHref(loginInfo.getLoginUrl());
		 * 
		 * loginPanel.add(loginLabel); loginPanel.add(signInLink);
		 * RootPanel.get("content").add(loginPanel);
		 */
	}

	private void moduleLoad() {

		// Create header panel
		Image logo = new Image("images/GlobalAppServices.png");
		headerPanel.add(logo);
		// Create account panel
		accountPanel.add(new Label(loginInfo.getNickname()));
		accountPanel.add(new Label(" | "));
		signOutLink.setHref(loginInfo.getLogoutUrl());
		accountPanel.add(signOutLink);
		accountPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		headerPanel.add(accountPanel);

		// Create footer panel
		footerPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		footerPanel.add(new Label("Copyright 2011 Global App Services"));

		// Building services tabs
		// HighScore tab
		CreateHighScorePanel();

		// Advertisement tab
		CreateAdvertisementPanel();

		// Notification tab
		CreateNotificationPanel();

		// Add panels to RootLayout
		mainPanel.addNorth(headerPanel, 50);
		mainPanel.addSouth(footerPanel, 50);
		mainPanel.add(servicesTabPanel);
		rootPanel = RootPanel.get("content");

		RootLayoutPanel.get().add(mainPanel);

	}

	public void CreateHighScorePanel() {
		VerticalPanel highScorePanel = new VerticalPanel();
		highScorePanel.setStyleName("tabBackgroud");

		HorizontalPanel tableGamesHeaderPanel = new HorizontalPanel();

		tableGamesHeaderPanel.add(new Label("‹ Prev 20 1-3 of 3 Next 20 ›"));
		VerticalPanel tableGamesFooterPanel = new VerticalPanel();
		HorizontalPanel tableGamesInfoPanel = new HorizontalPanel();
		tableGamesInfoPanel.add(new Label("You have 7 games remaining."));
		tableGamesInfoPanel.add(new Label("‹ Prev 20 1-3 of 3 Next 20 ›"));
		HorizontalPanel tableGamesCtrPanel = new HorizontalPanel();
		tableGamesCtrPanel.add(new Button("Create app", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mainPanel.clear();
				mainPanel.addNorth(headerPanel, 50);
				mainPanel.addSouth(footerPanel, 50);
				CreateAppScores createApp = new CreateAppScores();
				createApp.setLoginInfo(loginInfo);
				mainPanel.add(createApp.Initialize());

			}
		}));

		tableGamesCtrPanel.add(new Button("Delete app", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Window.alert("Delete: userId: " + loginInfo.getEmailAddress()
						+ ",appId: " + selectedApps.get(0));
				AppScoreServiceAsync appScoreService = GWT
						.create(AppScoreService.class);
				appScoreService.DeleteApp(loginInfo.getEmailAddress(),
						selectedApps.get(0), new AsyncCallback<Long>() {
							public void onFailure(Throwable caught) {
							}

							public void onSuccess(Long result) {
								// TODO Auto-generated method stub

							}
						});

			}
		}));
		tableGamesFooterPanel.add(tableGamesInfoPanel);
		tableGamesFooterPanel.add(tableGamesCtrPanel);

		highScorePanel.add(tableGamesHeaderPanel);

			final SelectionModel<AppScore> selectionModel = new MultiSelectionModel<AppScore>(
				AppScore.KEY_PROVIDER);
		gamesCellTable
				.setSelectionModel(selectionModel, DefaultSelectionEventManager
						.<AppScore> createCheckboxManager());

		Column<AppScore, Boolean> checkColumn = new Column<AppScore, Boolean>(
				new CheckboxCell(true, false)) {

			@Override
			public Boolean getValue(AppScore app) {
				// TODO Auto-generated method stub
				if (selectionModel.isSelected(app)) {
					if (!selectedApps.contains(app.getAppId()))
						selectedApps.add(app.getAppId());
				} else {
					if (selectedApps.contains(app.getAppId()))
						selectedApps.remove(app.getAppId());
				}
				return selectionModel.isSelected(app);
			}
		};
		gamesCellTable.addColumn(checkColumn, SafeHtmlUtils
				.fromSafeConstant("<br/>"));
		gamesCellTable.setColumnWidth(checkColumn, 40, Unit.PX);

		// Create appId column.
		TextColumn<AppScore> appIdColumn = new TextColumn<AppScore>() {
			@Override
			public String getValue(AppScore app) {
				return app.getAppId();
			}
		};
		appIdColumn.setSortable(true);
		gamesCellTable.addColumn(appIdColumn, "AppId");

		// Create appTittle column.
		TextColumn<AppScore> appTittleColumn = new TextColumn<AppScore>() {
			@Override
			public String getValue(AppScore app) {
				return app.getAppTittle();
			}
		};
		appIdColumn.setSortable(true);
		gamesCellTable.addColumn(appTittleColumn, "Tittle");

		// Create entries column.
		TextColumn<AppScore> entriesColumn = new TextColumn<AppScore>() {
			@Override
			public String getValue(AppScore app) {
				return String.valueOf(app.getScoreEntries());
			}
		};
		appIdColumn.setSortable(true);
		gamesCellTable.addColumn(entriesColumn, "Entries");

		// Create a data provider.
		ListDataProvider<AppScore> dataProvider = new ListDataProvider<AppScore>();

		// Connect the table to the data provider.
		dataProvider.addDataDisplay(gamesCellTable);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget.
		listApp = dataProvider.getList();

		// Set up the callback object.
		AsyncCallback<List<AppScore>> callback = new AsyncCallback<List<AppScore>>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
				Window.alert("Fail roi");
			}

			public void onSuccess(List<AppScore> result) {
				Window.alert("List count: " + result.size());
				listApp.clear();
				listApp.addAll(result);
			}
		};
		appScoreSvc = GWT.create(AppScoreService.class);
		appScoreSvc.SelectApps(loginInfo.getEmailAddress(), callback);

		highScorePanel.add(gamesCellTable);
		highScorePanel.add(tableGamesFooterPanel);
		servicesTabPanel.add(highScorePanel, "HighScores");
	}

	public void CreateAdvertisementPanel() {
		VerticalPanel advsPanel = new VerticalPanel();
		advsPanel.setStyleName("tabBackgroud");

		HorizontalPanel tableAdvsHeaderPanel = new HorizontalPanel();

		tableAdvsHeaderPanel.add(new Label("‹ Prev 20 1-3 of 3 Next 20 ›"));
		VerticalPanel tableAdvsFooterPanel = new VerticalPanel();
		HorizontalPanel tableAdvsInfoPanel = new HorizontalPanel();
		tableAdvsInfoPanel.add(new Label("You have 7 advertisement remaining."));
		tableAdvsInfoPanel.add(new Label("‹ Prev 20 1-3 of 3 Next 20 ›"));
		HorizontalPanel tableAdvsCtrPanel = new HorizontalPanel();
		tableAdvsCtrPanel.add(new Button("Create adv", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mainPanel.clear();
				mainPanel.addNorth(headerPanel, 50);
				mainPanel.addSouth(footerPanel, 50);
				CreateAdvertisment createAdv = new CreateAdvertisment();
				createAdv.setLoginInfo(loginInfo);
				mainPanel.add(createAdv.Initialize());

			}
		}));

		tableAdvsCtrPanel.add(new Button("Delete advs", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				AppScoreServiceAsync appScoreService = GWT
						.create(AppScoreService.class);
				appScoreService.DeleteApp(loginInfo.getEmailAddress(),
						selectedApps.get(0), new AsyncCallback<Long>() {
							public void onFailure(Throwable caught) {
							}

							public void onSuccess(Long result) {
								// TODO Auto-generated method stub

							}
						});

			}
		}));
		tableAdvsFooterPanel.add(tableAdvsInfoPanel);
		tableAdvsFooterPanel.add(tableAdvsCtrPanel);

		advsPanel.add(tableAdvsHeaderPanel);

		final SelectionModel<Advertisement> selectionModel = new MultiSelectionModel<Advertisement>(
				Advertisement.KEY_PROVIDER);
		advsCellTable
				.setSelectionModel(selectionModel, DefaultSelectionEventManager
						.<Advertisement> createCheckboxManager());

		Column<Advertisement, Boolean> checkColumn = new Column<Advertisement, Boolean>(
				new CheckboxCell(true, false)) {

			@Override
			public Boolean getValue(Advertisement adv) {
				// TODO Auto-generated method stub
				if (selectionModel.isSelected(adv)) {
					if (!selectedAdvs.contains(adv.getAppId()))
						selectedAdvs.add(adv.getAppId());
				} else {
					if (selectedAdvs.contains(adv.getAppId()))
						selectedAdvs.remove(adv.getAppId());
				}
				return selectionModel.isSelected(adv);
			}
		};
		advsCellTable.addColumn(checkColumn, SafeHtmlUtils
				.fromSafeConstant("<br/>"));
		advsCellTable.setColumnWidth(checkColumn, 40, Unit.PX);
		// Create icon column.
		/* Tam thoi gem lai xem sao
		Column<Advertisement, Image> iconColumn = new Column<Advertisement, Image> (null) {

			@Override
			public Image getValue(Advertisement adv) {
				// TODO Auto-generated method stub
				Image img = new Image(adv.getIconUrl());
				return img;
			}
			
		};
		advsCellTable.addColumn(iconColumn);
		*/
		// Create appId column.
		TextColumn<Advertisement> appIdColumn = new TextColumn<Advertisement>() {
			@Override
			public String getValue(Advertisement adv) {
				return adv.getAppId();
			}
		};
		appIdColumn.setSortable(true);
		advsCellTable.addColumn(appIdColumn, "AppId");

		// Create appTittle column.
		TextColumn<Advertisement> appTittleColumn = new TextColumn<Advertisement>() {
			@Override
			public String getValue(Advertisement adv) {
				return adv.getTittle();
			}
		};
		appTittleColumn.setSortable(true);
		advsCellTable.addColumn(appTittleColumn, "Tittle");

		// Create Content column.
		TextColumn<Advertisement> contentColumn = new TextColumn<Advertisement>() {
			@Override
			public String getValue(Advertisement adv) {
				return String.valueOf(adv.getContent());
			}
		};
		contentColumn.setSortable(true);
		advsCellTable.addColumn(contentColumn, "Content");
		
		// Create Type column.
		TextColumn<Advertisement> typeColumn = new TextColumn<Advertisement>() {
			@Override
			public String getValue(Advertisement adv) {
				return String.valueOf(adv.getType());
			}
		};
		typeColumn.setSortable(true);
		advsCellTable.addColumn(typeColumn, "Type");

		

		// Create a data provider.
		ListDataProvider<Advertisement> dataProvider = new ListDataProvider<Advertisement>();

		// Connect the table to the data provider.
		dataProvider.addDataDisplay(advsCellTable);

		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget.
		listAdvs = dataProvider.getList();

		// Set up the callback object.
		AsyncCallback<List<Advertisement>> callback = new AsyncCallback<List<Advertisement>>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
				Window.alert("Fail roi");
			}

			public void onSuccess(List<Advertisement> result) {
				Window.alert("List count: " + result.size());
				listAdvs.clear();
				listAdvs.addAll(result);
			}
		};
		advSvc = GWT.create(AdvertisementService.class);
		advSvc.SelectAdvs(loginInfo.getEmailAddress(), callback);

		advsPanel.add(advsCellTable);
		advsPanel.add(tableAdvsFooterPanel);
		servicesTabPanel.add(advsPanel, "Advertisement");
	}

	public void CreateNotificationPanel() {
			VerticalPanel notesPanel = new VerticalPanel();
			notesPanel.setStyleName("tabBackgroud");

			HorizontalPanel tableNotesHeaderPanel = new HorizontalPanel();

			tableNotesHeaderPanel.add(new Label("‹ Prev 20 1-3 of 3 Next 20 ›"));
			VerticalPanel tableNotesFooterPanel = new VerticalPanel();
			HorizontalPanel tableNotesInfoPanel = new HorizontalPanel();
			tableNotesInfoPanel.add(new Label("You have 7 notification remaining."));
			tableNotesInfoPanel.add(new Label("‹ Prev 20 1-3 of 3 Next 20 ›"));
			HorizontalPanel tableNotesCtrPanel = new HorizontalPanel();
			tableNotesCtrPanel.add(new Button("Create note", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					mainPanel.clear();
					mainPanel.addNorth(headerPanel, 50);
					mainPanel.addSouth(footerPanel, 50);
					CreateNotification createNote = new CreateNotification();
					createNote.setLoginInfo(loginInfo);
					mainPanel.add(createNote.Initialize());

				}
			}));

			tableNotesCtrPanel.add(new Button("Delete notes", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					
					NotificationServiceAsync noteService = GWT
							.create(NotificationService.class);
					noteService.DeleteNote(loginInfo.getEmailAddress(),
							selectedNotes.get(0), new AsyncCallback<Long>() {
								public void onFailure(Throwable caught) {
								}

								public void onSuccess(Long result) {
									// TODO Auto-generated method stub

								}
							});

				}
			}));
			tableNotesFooterPanel.add(tableNotesInfoPanel);
			tableNotesFooterPanel.add(tableNotesCtrPanel);

			notesPanel.add(tableNotesHeaderPanel);

			final SelectionModel<Notification> selectionModel = new MultiSelectionModel<Notification>(
					Notification.KEY_PROVIDER);
			notesCellTable
					.setSelectionModel(selectionModel, DefaultSelectionEventManager
							.<Notification> createCheckboxManager());

			Column<Notification, Boolean> checkColumn = new Column<Notification, Boolean>(
					new CheckboxCell(true, false)) {

				@Override
				public Boolean getValue(Notification note) {
					// TODO Auto-generated method stub
					if (selectionModel.isSelected(note)) {
						if (!selectedNotes.contains(note.getAppId()))
							selectedNotes.add(note.getAppId());
					} else {
						if (selectedNotes.contains(note.getAppId()))
							selectedNotes.remove(note.getAppId());
					}
					return selectionModel.isSelected(note);
				}
			};
			notesCellTable.addColumn(checkColumn, SafeHtmlUtils
					.fromSafeConstant("<br/>"));
			notesCellTable.setColumnWidth(checkColumn, 40, Unit.PX);
		
			// Create appId column.
			TextColumn<Notification> appIdColumn = new TextColumn<Notification>() {
				@Override
				public String getValue(Notification adv) {
					return adv.getAppId();
				}
			};
			appIdColumn.setSortable(true);
			notesCellTable.addColumn(appIdColumn, "AppId");

			// Create appTittle column.
			TextColumn<Notification> appTittleColumn = new TextColumn<Notification>() {
				@Override
				public String getValue(Notification note) {
					return note.getTittle();
				}
			};
			appTittleColumn.setSortable(true);
			notesCellTable.addColumn(appTittleColumn, "Tittle");

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
					return String.valueOf(note.getFromDate());
				}
			};
			fromDateColumn.setSortable(true);
			notesCellTable.addColumn(fromDateColumn, "From");

			
			// Create to date column.
			TextColumn<Notification> toDateColumn = new TextColumn<Notification>() {
				@Override
				public String getValue(Notification note) {
					return String.valueOf(note.getToDate());
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

			// Set up the callback object.
			AsyncCallback<List<Notification>> callback = new AsyncCallback<List<Notification>>() {
				public void onFailure(Throwable caught) {
					// TODO: Do something with errors.
					Window.alert("Fail roi");
				}

				public void onSuccess(List<Notification> result) {
					Window.alert("List count: " + result.size());
					listNotes.clear();
					listNotes.addAll(result);
				}
			};
			noteSvc = GWT.create(NotificationService.class);
			noteSvc.SelectNotes(loginInfo.getEmailAddress(), callback);

			notesPanel.add(notesCellTable);
			notesPanel.add(tableNotesFooterPanel);
			servicesTabPanel.add(notesPanel, "Notification");

	}
}

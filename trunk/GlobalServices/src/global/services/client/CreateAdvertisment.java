package global.services.client;

import global.services.client.rpc.AdvertisementService;

import global.services.client.rpc.AdvertisementServiceAsync;
import global.services.shared.Advertisement;
import global.services.shared.LoginInfo;
import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import gwtupload.client.SingleUploader;

import com.google.gwt.user.client.Window;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CreateAdvertisment {
	private TextBox txtAppId;
	private TextBox txtTitle;
	private TextBox txtContent;
	private TextBox txtType;
	private TextBox txtStoreUrl;
	private LoginInfo loginInfo = null;
	private String iconFileId = null;
	private FlowPanel panelImages = new FlowPanel();

	public LoginInfo getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}

	private AdvertisementServiceAsync advSvc;
	private FormPanel formUpload;

	public Widget Initialize() {
		VerticalPanel mainContent = new VerticalPanel();
		Button btnAddAdv = new Button("Create adv", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				String appID = txtAppId.getText();
				String appTittle = txtTitle.getText();

				if (appID != null) {
					// formUpload.submit();
					Advertisement newApp = new Advertisement(appID);
					newApp.setUserId(loginInfo.getEmailAddress());
					newApp.setTittle(appTittle);
					newApp.setContent(txtContent.getText());
					newApp.setType(txtType.getText());
					newApp.setStoreUrl(txtStoreUrl.getText());
					newApp.setIconFile(Long.valueOf(iconFileId));

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
		});
		mainContent.add(new Label("Create new advertisment score"));
		mainContent.add(new Label("You have 7 advertisment remaining."));
		SingleUploader iconUploader = new SingleUploader(FileInputType.LABEL);
		iconUploader.setAutoSubmit(true);
		mainContent.add(new Label("Icon app:"));
		// imgIcon = new Image();
		// mainContent.add(imgIcon);
		iconUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		mainContent.add(panelImages);
		mainContent.add(iconUploader);
		formUpload = (FormPanel) FileUploader.getFileUploaderWidget();
		mainContent.add(formUpload);

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
		controlButton.add(btnAddAdv);
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

	// Load the image in the document and in the case of success attach it to
	// the viewer
	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		public void onFinish(IUploader uploader) {
			// Window.alert("Upload da finish");
			if (uploader.getStatus() == Status.SUCCESS) {
				
				iconFileId = uploader.getServerResponse();
							
				UrlBuilder filesUrl = new UrlBuilder();
				filesUrl.setHost(GWT.getHostPageBaseURL());
				filesUrl.setPath("download");
				filesUrl.setParameter("fileid", iconFileId);
				
				Window.alert("Image url: " + filesUrl.buildString());
				PreloadedImage image = new PreloadedImage(filesUrl.buildString(), showImage);
				// The server sends useful information to the client by default
				UploadedInfo info = uploader.getServerInfo();
				System.out.println("File name " + info.name);
				System.out.println("File content-type " + info.ctype);
				System.out.println("File size " + info.size);

				// You can send any customized message and parse it
				System.out.println("Server message " + info.message);
			}
		}
	};

	// Attach an image to the pictures viewer
	private OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {
		public void onLoad(PreloadedImage image) {
			image.setWidth("75px");
			panelImages.add(image);
		}
	};

}

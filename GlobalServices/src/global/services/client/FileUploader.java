package global.services.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.AttachEvent;

public class FileUploader {

	static public Widget getFileUploaderWidget() {
		final FormPanel form = new FormPanel();
		final FileUpload fu = new FileUpload();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		// form.setAction(/* WHAT SHOULD I PUT HERE */);
		form.setAction(GWT.getModuleBaseURL() + "fileupload");

		VerticalPanel holder = new VerticalPanel();

		fu.setName("upload");
		holder.add(fu);
		/*
		holder.add(new Button("Submit", new ClickHandler() {
			public void onClick(ClickEvent event) {
				GWT.log("You selected: " + fu.getFilename(), null);
				form.submit();
			}
		}));
		*/
		fu.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				// TODO Auto-generated method stub
				GWT.log("AttachEvent", null);
			}});
		fu.addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				GWT.log("ChangeHandler", null);
				form.submit();
				
			}});
		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				if (!"".equalsIgnoreCase(fu.getFilename())) {
					GWT.log("UPLOADING FILE????", null);
					// NOW WHAT????
				} else {
					event.cancel(); // cancel the event
				}

			}
		});

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// TODO Auto-generated method stub

			}
		});

		form.add(holder);

		return form;
	}

}

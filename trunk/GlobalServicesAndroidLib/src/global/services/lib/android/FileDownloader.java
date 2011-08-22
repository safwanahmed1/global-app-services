package global.services.lib.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileDownloader {
	private static final String DOWNLOAD_SERVLET = "http://global-app-services.appspot.com/globalservices/fileservlet";
	private String userId_;
	private Long fileId_;
	private String fileName_;
	private RestClient downloadRest;

	public FileDownloader(String userId, Long fileId) {
		userId_ = userId;
		fileId_ = fileId;
		downloadRest = new RestClient(DOWNLOAD_SERVLET);

	}

	public File Download() {

		downloadRest.ClearParams();
		downloadRest.AddHeader("requesttype", "download");
		downloadRest.AddHeader("userid", userId_);
		downloadRest.AddHeader("fileid", String.valueOf(fileId_));
		File file = null;
		try {
			downloadRest.Execute(RequestMethod.POST);

			String strResponse = downloadRest.getResponse();
			fileName_ = (String) downloadRest.getHttpResponse().getFirstHeader("filename").getValue();
			file = new File(fileName_);

			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(strResponse.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return file;
	}
}

package global.services.lib.android;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.ByteArrayBuffer;

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

	public File Download(String fileName) {

		downloadRest.ClearParams();
		downloadRest.AddParam("requesttype", "download");
		downloadRest.AddParam("userid", userId_);
		downloadRest.AddParam("fileid", String.valueOf(fileId_));
		File file = null;
		try {
			downloadRest.Execute(RequestMethod.POST);
			InputStream is = downloadRest.getInstream();

			BufferedInputStream bis = new BufferedInputStream(is);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(1024);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			if (fileName == null) {
				fileName_ = (String) downloadRest.getHttpResponse()
						.getFirstHeader("filename").getValue();
				file = new File(fileName_);
			} else

				file = new File(fileName);
			/* Convert the Bytes read to a String. */
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
			is.close();

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

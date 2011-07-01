package global.services.server.servlet;

import global.services.server.PMF;
import global.services.shared.IconAdvertisement;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.server.gae.AppEngineUploadAction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import gwtupload.server.UploadAction;


public class FilesUploadServlet extends AppEngineUploadAction {

	private static final long serialVersionUID = 1L;

	Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
	/**
	 * Maintain a list with received files and their content types.
	 */
	Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

	/**
	 * Override executeAction to save the received files in a custom place and
	 * delete this items from session.
	 */
	@Override
	public String executeAction(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
		String response = "";
		int cont = 0;
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				/*
				cont++;
				try {
					// / Create a new file based on the remote file name in the
					// client
					// String saveName =
					// item.getName().replaceAll("[\\\\/><\\|\\s\"'{}()\\[\\]]+",
					// "_");
					// File file =new File("/tmp/" + saveName);

					// / Create a temporary file placed in /tmp (only works in
					// unix)
					// File file = File.createTempFile("upload-", ".bin", new
					// File("/tmp"));

					// / Create a temporary file placed in the default system
					// temp folder
					File file = File.createTempFile("tmp_global_upload", ".bin");
					item.write(file);

					// / Save a list with the received files
					receivedFiles.put(item.getFieldName(), file);
					receivedContentTypes.put(item.getFieldName(),
							item.getContentType());

					// / Send a customized message to the client.
					response += "File saved as " + file.getAbsolutePath();

				} catch (Exception e) {
					throw new UploadActionException(e);
				}
				*/
				PersistenceManager pm = PMF.get().getPersistenceManager(); 
                Transaction tx = pm.currentTransaction(); 
                try { 
                    // Start the transaction 
                    tx.begin(); 
                    InputStream imgStream = item.getInputStream(); 
                        Blob blob = new Blob(IOUtils.toByteArray(imgStream)); 
                        IconAdvertisement imageBlob = new IconAdvertisement(item.getName(), blob); 
                        response = String.valueOf(pm.makePersistent(imageBlob).getId()); 
                    // Commit the transaction, flushing the object to the datastore 
                    tx.commit(); 
                } 
                catch(Exception e) { 
                        e.printStackTrace(); 
                } 
                finally { 
                    if(tx.isActive()) { 
                        tx.rollback(); 
                    } 
                    pm.close(); 
                } 
			}
		}

		// / Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		// / Send your customized message to the client.
		return response;
	}

	/**
	 * Get the content of an uploaded file.
	 */
	@Override
	public void getUploadedFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String fieldName = request.getParameter(PARAM_SHOW);
		File f = receivedFiles.get(fieldName);
		if (f != null) {
			response.setContentType(receivedContentTypes.get(fieldName));
			FileInputStream is = new FileInputStream(f);
			copyFromInputStreamToOutputStream(is, response.getOutputStream());
		} else {
			renderXmlResponse(request, response, ERROR_ITEM_NOT_FOUND);
		}
	}

	/**
	 * Remove a file when the user sends a delete request.
	 */
	@Override
	public void removeItem(HttpServletRequest request, String fieldName)
			throws UploadActionException {
		File file = receivedFiles.get(fieldName);
		receivedFiles.remove(fieldName);
		receivedContentTypes.remove(fieldName);
		if (file != null) {
			file.delete();
		}
	}
}
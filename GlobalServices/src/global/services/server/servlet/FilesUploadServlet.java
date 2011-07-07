package global.services.server.servlet;

import global.services.server.PMF;
import global.services.server.database.FilesDataBase;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.server.gae.AppEngineUploadAction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.ByteArrayOutputStream;

import com.google.appengine.api.datastore.Blob;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
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

				PersistenceManager pm = PMF.get().getPersistenceManager();
				try {
					InputStream inStream = item.getInputStream();

					ByteArrayOutputStream outStream = new ByteArrayOutputStream();
					int len;
					byte[] buffer = new byte[8192];
					while ((len = inStream.read(buffer, 0, buffer.length)) != -1) {
						outStream.write(buffer, 0, len);
					}

					Blob blob = new Blob(outStream.toByteArray());
					FilesDataBase imageBlob = new FilesDataBase(
							item.getName(), blob);
					response = String.valueOf(pm.makePersistent(imageBlob)
							.getId());
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					
					pm.close();
				}
			}
		}

		// / Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		// / Send your customized message to the client.
		return response;
	}
	
	

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		// TODO Auto-generated method stub
		//super.doPost(request, response);
		ServletFileUpload upload = new ServletFileUpload();
        //PersistenceManager pm = PMF.get().getPersistenceManager(); 
        try{
            FileItemIterator iter = upload.getItemIterator(request);

            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                
                String name = item.getFieldName();
                InputStream stream = item.openStream();


                // Process the input stream
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int len;
                byte[] buffer = new byte[8192];
                while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                }
                int maxFileSize = 2*1024*1024; //10 megs max 
                if (out.size() > maxFileSize) { 
                    System.out.println("File is > than " + maxFileSize);
                    return;
                }
                
                Blob blob = new Blob(out.toByteArray()); 
                FilesDataBase fileBlob = new FilesDataBase(item.getName(), blob); 
                fileBlob.setFileType(item.getContentType());
                fileBlob.InsertFile();
                //pm.makePersistent(fileBlob);
                response.getWriter().print(fileBlob.getId());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
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

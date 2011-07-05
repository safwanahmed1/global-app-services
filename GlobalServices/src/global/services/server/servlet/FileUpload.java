package global.services.server.servlet;

import global.services.server.PMF;
import global.services.server.database.IconAdvertisement;

import java.io.IOException;
import java.io.InputStream;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.ByteArrayOutputStream;

import com.google.appengine.api.datastore.Blob;



public class FileUpload extends HttpServlet {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
	        ServletFileUpload upload = new ServletFileUpload();
	        PersistenceManager pm = PMF.get().getPersistenceManager(); 
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
	                Blob blob = new Blob(out.toByteArray()); 
                    IconAdvertisement imageBlob = new IconAdvertisement(item.getName(), blob); 
                    pm.makePersistent(imageBlob);
                    response.getWriter().print(out.toByteArray());
                    
	                int maxFileSize = 10*(1024*2); //10 megs max 
	                if (out.size() > maxFileSize) { 
	                    System.out.println("File is > than " + maxFileSize);
	                    return;
	                }
	            }
	        }
	        catch(Exception e){
	            e.printStackTrace();
	        }

	    }
}

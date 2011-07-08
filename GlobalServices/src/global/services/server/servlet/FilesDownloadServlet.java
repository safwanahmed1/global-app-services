package global.services.server.servlet;

import global.services.server.database.FilesDataBase;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class FilesDownloadServlet extends HttpServlet {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userId = req.getParameter("userid");
		String fileId = req.getParameter("fileid");
		
        FilesDataBase fileDownload = new FilesDataBase();
        fileDownload.SelectFile(Long.parseLong(fileId));
        resp.reset();
        resp.setContentType(fileDownload.getFileType()); 
        ServletOutputStream  outStream = resp.getOutputStream(); 
        byte [] fileData = fileDownload.getContent().getBytes(); 
        outStream.write(fileData); 
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
			String userId = request.getParameter("userid");
			String fileId = request.getParameter("fileid");
			
	        FilesDataBase fileDownload = new FilesDataBase();
	        fileDownload.SelectFile(Long.parseLong(fileId));
	        response.reset();
	        response.setContentType(fileDownload.getFileType()); 
	        ServletOutputStream  outStream = response.getOutputStream(); 
	        byte [] fileData = fileDownload.getContent().getBytes(); 
	        outStream.write(fileData); 

	    }
}

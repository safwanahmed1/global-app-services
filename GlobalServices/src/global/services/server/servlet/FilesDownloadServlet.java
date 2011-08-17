package global.services.server.servlet;

import global.services.server.database.FileDataBase;

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

		FileDataBase fileDownload = new FileDataBase();
		fileDownload.SelectFile(Long.parseLong(fileId));
		resp.reset();
		resp.setContentType(fileDownload.getFileType());
		ServletOutputStream outStream = resp.getOutputStream();
		byte[] fileData = fileDownload.getContent().getBytes();
		outStream.write(fileData);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		String userId = request.getHeader("userid");
		String fileId = request.getHeader("fileid");

		FileDataBase fileDownload = new FileDataBase();
		fileDownload.SelectFile(Long.parseLong(fileId));
		response.reset();
		response.setContentType(fileDownload.getFileType());
		response.setHeader("filename", fileDownload.getName());
		ServletOutputStream outStream;
		try {
			outStream = response.getOutputStream();
			byte[] fileData = fileDownload.getContent().getBytes();
			outStream.write(fileData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

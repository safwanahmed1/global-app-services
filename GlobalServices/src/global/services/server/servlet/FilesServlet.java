package global.services.server.servlet;

import global.services.server.database.FileDataBase;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FilesServlet extends HttpServlet {
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
		fileDownload.SelectFile(userId, Long.parseLong(fileId));
		resp.reset();
		resp.setContentType(fileDownload.getFileType());
		ServletOutputStream outStream = resp.getOutputStream();
		byte[] fileData = fileDownload.getContent().getBytes();
		outStream.write(fileData);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		String reqType = request.getHeader("requesttype");
		String userId = request.getHeader("userid");
		String fileId = request.getHeader("fileid");
		if (reqType == "download") {
			Download(userId, Long.parseLong(fileId), response);
		}
		if (reqType == "getinfo") {
			GetFileInfos(userId, response);
		}
	}

	private void Download(String userId, Long fileId,
			HttpServletResponse response) {
		FileDataBase fileDownload = new FileDataBase();
		fileDownload.SelectFile(userId, fileId);
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

	private void GetFileInfos(String userId, HttpServletResponse response) {
		FileDataBase fileDB = new FileDataBase();
		List<FileDataBase> fileList = fileDB.SelectFiles(userId);
		
		response.setContentType("text/xml; charset=UTF-8");
		try {
			ServletOutputStream outStream = response.getOutputStream();
			outStream.print("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");

			outStream.print("<files>");
			for (FileDataBase file : fileList) {

				outStream.print("<file ");
				if (file.getId() != null)
					outStream.print(" id=\"" + file.getId() + "\"");
				if (file.getName() != null)
					outStream.print(" name=\"" + file.getName() + "\"");
				if (file.getFileType() != null)
					outStream.print(" type=\"" + file.getFileType() + "\"");
				if (file.getUserId() != null)
					outStream.print(" user=\"" + file.getUserId() + "\"");
				outStream.println("/>");

			}
			outStream.print("</files>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

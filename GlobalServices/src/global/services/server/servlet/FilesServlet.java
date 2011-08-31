package global.services.server.servlet;

import global.services.server.database.FileDataBase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

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
	private static Logger logger = Logger.getLogger(FilesServlet.class
			.getName());
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
		String reqType = request.getParameter("requesttype");
		String userId = request.getParameter("userid");
		String fileId = request.getParameter("fileid");
		logger.warning("Do post method of FilesServlet.");
		logger.warning("requesttype: " + reqType);
		logger.warning("userId: " + userId);
		
		if ((reqType != null) && ("download".equals(reqType))) {
			Download(userId, Long.parseLong(fileId), response);
		}
		if ((reqType != null) && ("getinfo".equals(reqType))) {
			GetFileInfos(userId, response);
		}
	}

	private void Download(String userId, Long fileId,
			HttpServletResponse response) {
		logger.warning("Do post method of FilesServlet::Download");
		logger.warning("userId: " + userId);
		logger.warning("fileId: " + fileId);
		FileDataBase fileDownload = new FileDataBase();
		fileDownload.SelectFile(userId, fileId);
		response.reset();
		response.setContentType(fileDownload.getFileType());
		response.setHeader("Content-Disposition","attachment; filename=\"" + fileDownload.getName() + "\"");
		response.setHeader("filename", fileDownload.getName());
		ServletOutputStream outStream;
		try {
			/*
			outStream = response.getOutputStream();
			
			outStream.write(fileData);
			*/
			byte[] fileData = fileDownload.getContent().getBytes();
			//StringBuffer sb = new StringBuffer("whatever string you like");
			InputStream in = new ByteArrayInputStream(fileData);
			ServletOutputStream out = response.getOutputStream();
			 
			byte[] outputByte = new byte[1024];
			//copy binary contect to output stream
			while(in.read(outputByte, 0, 1024) != -1)
			{
				out.write(outputByte, 0, 1024);
			}
			in.close();
			out.flush();
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void GetFileInfos(String userId, HttpServletResponse response) {
		logger.warning("Do post method of FilesServlet::GetFileInfos");
		logger.warning("userId: " + userId);
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
				if (file.getUserId() != null)
					outStream.print(" userid=\"" + file.getUserId() + "\"");
				if (file.getName() != null)
					outStream.print(" name=\"" + file.getName() + "\"");
				if (file.getFileType() != null)
					outStream.print(" type=\"" + file.getFileType() + "\"");
				if (file.getContent() != null)
					outStream.print(" size=\"" + file.getContent().toString()
							+ "\"");

				outStream.println("/>");

			}
			outStream.print("</files>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

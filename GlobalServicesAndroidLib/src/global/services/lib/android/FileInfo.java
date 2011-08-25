package global.services.lib.android;

import java.io.File;



public class FileInfo {

	private Long id;
	private String userId_;
	private String fileName_;
	private String fileType_;
	private String fileSize_;
	private File file_;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId_;
	}

	public void setUserId(String userId) {
		this.userId_ = userId;
	}

	public String getFileName() {
		return fileName_;
	}

	public void setFileName(String fileName) {
		this.fileName_ = fileName;
	}

	public String getFileSize() {
		return fileSize_;
	}

	public void setFileSize(String fileSize) {
		this.fileSize_ = fileSize;
	}

	public void setFileType(String fileType) {
		this.fileType_ = fileType;
	}

	public String getFileType() {
		return fileType_;
	}

	public void setFile(File file) {
		this.file_ = file;
	}

	public File getFile() {
		return file_;
	}

}
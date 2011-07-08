package global.services.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.view.client.ProvidesKey;

public class FileInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final ProvidesKey<FileInfo> KEY_PROVIDER = new ProvidesKey<FileInfo>() {
		public Object getKey(FileInfo file) {
			return file == null ? null : file.getId();
		}
	};

	/**
	 * 
	 */
	private Long id;
	private String userId_;
	private String fileName_;
	private String fileType_;
	private String fileSize_;

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

}

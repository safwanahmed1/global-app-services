package com.vandgoo.tv;

public class TVChannel {
	private String id_;
	private String name_;
	private String url_;
	private String file_;
	private String catalog_;
	private String schedule_;
	public void setName(String name) {
		this.name_ = name;
	}
	public String getName() {
		return name_;
	}
	public String getId() {
		return id_;
	}
	public void setId(String id) {
		id_ = id;
	}
	
	public String getUrl() {
		return url_;
	}
	public void setUrl(String url) {
		url_ = url;
	}
	public String getFile() {
		return file_;
	}
	public void setFile(String file) {
		file_ = file;
	}
	public String getCatalog() {
		return catalog_;
	}
	public void setCatalog(String catalog) {
		catalog_ = catalog;
	}
	public String getSchedule() {
		return schedule_;
	}
	public void setSchedule(String schedule) {
		schedule_ = schedule;
	}
	
	
	

}

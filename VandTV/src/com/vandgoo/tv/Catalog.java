package com.vandgoo.tv;

import java.util.ArrayList;
import java.util.List;

public class Catalog {
	private String id_;
	private String name_;
	private ArrayList<TVChannel> channels_;

	public Catalog(String id, String name) {
		id_ = id;
		name_ = name;
		channels_ = new ArrayList<TVChannel>();
	}
	

	public String getId() {
		return id_;
	}


	public void setId(String id) {
		id_ = id;
	}


	public String getName() {
		return name_;
	}


	public void setName(String name) {
		name_ = name;
	}


	public ArrayList<TVChannel> getChannels() {
		return channels_;
	}


	public void setChannels(ArrayList<TVChannel> channels) {
		channels_ = channels;
	}


	public int GetCount() {
		return channels_.size();
	}

	public void AddCatalog(TVChannel channel) {
		channels_.add(channel);
	}

	public void RemoveCatalog(int position) {
		channels_.remove(position);
	}

	public void RemoveCatalog(TVChannel channel) {
		channels_.remove(channel);
	}

}

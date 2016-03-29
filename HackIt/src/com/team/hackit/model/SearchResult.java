package com.team.hackit.model;

public class SearchResult {

	String videoId;
	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	String title;
	String thumbnail;
	String description;
	
	public SearchResult(String videoId,String title,String description,String thumbnail){
		this.videoId = videoId;
		this.title = title;
		this.thumbnail = thumbnail;
		this.description = description;
	}
}

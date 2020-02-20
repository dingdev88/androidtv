package com.selecttvapp.youtube;

import java.util.ArrayList;
import java.util.List;

public class VideosData {
	
	private List<Video> videos;

	public boolean add(Video object) {
		return videos.add(object);
	}

	public int size() {
		return videos.size();
	}

	public VideosData(){
		this.videos = new ArrayList<Video>();
	}

	public Video get(int i) {
		return videos.get(i);
	}

}

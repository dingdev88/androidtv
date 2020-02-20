package com.selecttvapp.youtube;

import android.net.Uri;
import android.net.Uri.Builder;

import java.util.Map;

public class VideoInfo {
	private String token;
	private String videoId;

	public VideoInfo(String videoId, String token){
		this.setToken(token);
		this.setVideoId(videoId);
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getVideoId() {
		return videoId;
	}

	public Uri getUri(){
		Builder b = new Builder();
		b.scheme("http")
			.authority("www.youtube.com")
			.path("get_video")
			.appendQueryParameter("video_id", getVideoId())
			.appendQueryParameter("t", getToken())
		;
		return b.build();
	}

	public static VideoInfo parse(String in) {
		Map<String, String> params = QueryUtility.getQueryParams(in);
		return new VideoInfo(params.get("video_id"), params.get("token"));
	}
}

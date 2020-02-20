package com.selecttvapp.youtube;

import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Log;

import com.selecttvapp.youtube.http.Http;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class VideoSearch {
	private String keyword;
	public VideoSearch(String keyword){
		this.setKeyword(keyword);
	}
	public Uri getRequestUri(){
		Builder b = new Builder();
		b.scheme("http")
			.authority("gdata.youtube.com")
			.path("feeds/api/videos")
			.appendQueryParameter("vq", keyword);
		return b.build();
	}
	public String getRequestUriString(){
		return getRequestUri().toString();
	}
	public VideosData execute() throws IOException{
		InputStream input = null;
		VideosData data = null;
		VideosDataParser parser = new VideosDataParser();
		
		try{
			input = Http.get(getRequestUriString());
			data =  parser.parse(input);
		} catch(MalformedURLException e){
			Log.e("VideoSearch", "MalformedURLException", e);
		}
		
		return data;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getKeyword() {
		return keyword;
	}
}

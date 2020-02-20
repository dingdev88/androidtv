package com.demo.network.common;

import com.selecttvapp.RabbitTvApplication;

import java.io.File;

public class AppFolders {
	
	public static final String THUMBS = "/thumbs";
	
	private static final String USER_THUMBS = "/user-infor/thumbs";
	
	private static final String TEMP_IMAGE_FOLDER = "/.temp_image/";
	
	public static String getUserThumbnailPath() {
		return getRootPath() + AppFolders.USER_THUMBS;
	}
	
	public static String getTempImageFolder() {
		return getRootPath() + AppFolders.TEMP_IMAGE_FOLDER;
	}
	
	public static String getRootPath() {
		File path = RabbitTvApplication.getInstance().getApplicationContext().getExternalFilesDir(null);
		if (path == null) {
			path = RabbitTvApplication.getInstance().getApplicationContext().getFilesDir();
		}
		return path.toString();
	}
	
	public static String getRandomImageFileName(){
		return getRandomImageFileName(ImageType.JPG);
	}
	
	public static String getRandomImageFileName(ImageType type){
		return "image_" + String.valueOf(System.currentTimeMillis() / 1000) + type.value();
	}
}
package com.selecttvapp.youtube;

import android.net.Uri;
import android.net.Uri.Builder;

import com.selecttvapp.youtube.http.StringHttp;
import com.selecttvapp.youtube.http.URLParser;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class VideoInfoSearch {
	private Video video;

	public VideoInfoSearch(Video video) {
		this.setVideo(video);
	}

	public VideoInfo execute() throws IOException{
		String in = null;

				in = StringHttp.get(URLParser.parse(getRequestUriString()));
		return VideoInfo.parse(in);
	}

	public Map<String, String> getVideos() throws IOException
	{

		String in = StringHttp.get(URLParser.parse(getRequestUriString()));
		Map<String, String> params = QueryUtility.getQueryParams(in);
		if(params != null)
		{
			String fmtStreamMapString = params.get("url_encoded_fmt_stream_map");
			if(fmtStreamMapString!= null && fmtStreamMapString.length() > 0)
			{
				String[] fmtStreamMapArray = fmtStreamMapString.split(",");
				Map<String, String> videoDictionary = new HashMap<String, String>();
				for(String videoEncodedString :fmtStreamMapArray) {
					Map<String, String> videoComponents = QueryUtility.getQueryParams(videoEncodedString);
					String type = videoComponents.get("type");
					String signature = null;
					if(videoComponents.get("stereo3d") == null)
					{
						if( videoComponents.get("s") != null ){
							signature = videoComponents.get("s");
							//s.slice(2, 8).join('') + s[0] + s.slice(9, 21).join('') + s[65] + s.slice(22, 65).join('') + s[84] + s.slice(66, 82).join('') + s[21]

						}
						else if (videoComponents.get("itag") != null) {
							signature = videoComponents.get("itag");
						}
						if(signature != null && !signature.isEmpty() && type.contains("mp4"))
						{
							String url = URLDecoder.decode(videoComponents.get("url"), "UTF-8");;
							url = url + "&signature=" + signature;
							String quality = URLDecoder.decode(videoComponents.get("quality"), "UTF-8");
							videoDictionary.put(quality, url);
						}
					}
				}
				return videoDictionary;
			}
		}
		return null;
	}
	private String getRequestUriString() {
		return getRequestUri().toString();
	}

	private Uri getRequestUri() {
		//&el=vevo&el=embedded&asv=3&sts=15902
		Builder b = new Builder();
		b.scheme("https")
			.authority("www.youtube.com")
			.path("get_video_info")
			.appendQueryParameter("video_id", video.getVideoId())
			.appendQueryParameter("el", "vevo");
			//.appendQueryParameter("el", "embedded")
			//.appendQueryParameter("asv", "3")
			//	.appendQueryParameter("sts", "15902");
		return b.build();
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public Video getVideo() {
		return video;
	}
}

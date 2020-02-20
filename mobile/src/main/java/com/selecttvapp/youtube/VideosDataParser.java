package com.selecttvapp.youtube;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

public class VideosDataParser {
	final String xmlns = "http://www.w3.org/2005/Atom";
	final String xmlnsMedia = "http://search.yahoo.com/mrss/";
	
	Video video = null;
	VideosData data = null;

	public VideosData parse(InputStream input) {
		data = new VideosData();
		RootElement root = new RootElement(xmlns, "feed");
		Element entry = root.getChild(xmlns, "entry");
		Element title = entry.getChild(xmlns, "title");
		Element mediaGroup = entry.getChild(xmlnsMedia, "group");
		Element mediaPlayer = mediaGroup.getChild(xmlnsMedia, "player");
		entry.setStartElementListener(new StartElementListener() {
			
			@Override
			public void start(Attributes attributes) {
				video = new Video();
			}
		});
		entry.setEndElementListener(new EndElementListener() {
			
			@Override
			public void end() {
				data.add(video);
			}
		});
		title.setEndTextElementListener(new EndTextElementListener() {
			
			@Override
			public void end(String body) {
				video.setTitle(body);
			}
		});
		mediaPlayer.setStartElementListener(new StartElementListener() {
			
			@Override
			public void start(Attributes attributes) {
				video.setMediaPlayerUrl(attributes.getValue("url"));
			}
		});
		try {
			Xml.parse(input, Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("VideosData", "IOException", e);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			Log.e("VideosData", "SaxException", e);
		}
		return data;
	}

}

package com.codepath.apps.simpletodo;

import java.io.Serializable;
import java.util.StringTokenizer;

import android.util.Log;

public class TodoEntry implements Serializable{
	private static final long serialVersionUID = 5341844224316456032L;

	private static final Character SEPARATOR = '\u0000';
	
	public String value;
	public String image;
	public String audio;
	
	public TodoEntry() {}
	
	public TodoEntry(String value) {
		this.value = value;
	}

	public String serialize() { 
		String data = String.format("%s%s%s%s%s", value, SEPARATOR, image, SEPARATOR, audio);
		Log.i(getClass().getName(), "Serializing to " + data);
		return data; 
	}
	
	public static TodoEntry deserialize(String data) {
		try {
			Log.i(TodoEntry.class.getName(), "Derializing " + data);
			
			StringTokenizer tok = new StringTokenizer(data, SEPARATOR.toString());
			
			TodoEntry entry = new TodoEntry();
			entry.value = tok.nextToken();
			entry.image = tok.nextToken();
			entry.audio = tok.nextToken();
			
			if(entry.image.equals("null")) {
				entry.image = null;
			}
			
			if(entry.audio.equals("null")) {
				entry.audio = null;
			}
			
			return entry;
		} catch (Throwable th) {
			Log.e(TodoEntry.class.getName(), th.getMessage(), th);
			
			return null;
		}
	}
	
	@Override
	public String toString() {
		return value;
	}
}
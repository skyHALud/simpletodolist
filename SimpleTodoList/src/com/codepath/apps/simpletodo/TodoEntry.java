package com.codepath.apps.simpletodo;

import java.io.Serializable;
import java.util.StringTokenizer;

import android.util.Log;

public class TodoEntry implements Serializable{
	private static final long serialVersionUID = 5341844224316456032L;

	private static final Character SEPARATOR = '\u0000';
	
	public String value;
	public String image;
	
	public TodoEntry() {}
	
	public TodoEntry(String value) {
		this.value = value;
	}

	public String serialize() { return String.format("%s%s%s", value, SEPARATOR, image); }
	
	public static TodoEntry deserialize(String data) {
		try {
			StringTokenizer tok = new StringTokenizer(data, SEPARATOR.toString());
			
			TodoEntry entry = new TodoEntry();
			entry.value = tok.nextToken();
			entry.image = tok.nextToken();
			
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
package com.codepath.apps.simpletodo;

import java.io.Serializable;

public class TodoEntry implements Serializable{
	private static final long serialVersionUID = 5341844224316456032L;

	public String value;
	public String image;
	
	public TodoEntry(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
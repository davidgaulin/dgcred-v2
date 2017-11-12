package com.dgcdevelopment.domain;

import java.util.Date;

public class Event {
	private Date date;
	private String title;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String description;
	private EventType type;

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Event() {
	}
	
	public Event(Date date, String title, String description, EventType type) {
		this.date = date;
		this.description = description;
		this.type = type;
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

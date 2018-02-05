package com.dgcdevelopment.domain;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Event {
	private Date date;
	private String title;
	private String description;
	private EventType type;

	public Event(Date date, String title, String description, EventType type) {
		this.date = date;
		this.description = description;
		this.type = type;
		this.title = title;
	}

}

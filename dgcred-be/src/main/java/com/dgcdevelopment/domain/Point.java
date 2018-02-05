package com.dgcdevelopment.domain;

import lombok.Data;

@Data
public class Point {
	private String x;
	private String y;

	public Point(String x, String y) {
		this.x = x;
		this.y = y;
	}

	public Point() {
	}

}

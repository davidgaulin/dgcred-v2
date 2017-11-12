package com.dgcdevelopment.domain.insurance;

import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.dgcdevelopment.domain.PaymentPeriod;
import com.dgcdevelopment.domain.property.Property;

public class Policy {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int eid;
	
	@ManyToMany
	private List<Property> properties;
	
	private Date startDate;
	
	private double price;
	
	private PaymentPeriod pricePeriod;
	
	private String insurer;
	
	private double duration;
	
	private PaymentPeriod durationPeriod;

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public PaymentPeriod getPricePeriod() {
		return pricePeriod;
	}

	public void setPricePeriod(PaymentPeriod pricePeriod) {
		this.pricePeriod = pricePeriod;
	}

	public String getInsurer() {
		return insurer;
	}

	public void setInsurer(String insurer) {
		this.insurer = insurer;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public PaymentPeriod getDurationPeriod() {
		return durationPeriod;
	}

	public void setDurationPeriod(PaymentPeriod durationPeriod) {
		this.durationPeriod = durationPeriod;
	}
	
	
}

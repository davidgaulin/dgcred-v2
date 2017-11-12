package com.dgcdevelopment.domain.property;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.dgcdevelopment.domain.AreaUnits;
import com.dgcdevelopment.domain.Document;
import com.dgcdevelopment.domain.RentPeriod;

@Entity
public class Unit {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long eid;
	
	/** Apartment number of letter or id */
	private String number;
	
	private double area;
	
	/** square feet, square meter, etc. */
	private AreaUnits areaUnit;
	
	private double projectedRent;
	
	@OneToMany(cascade=CascadeType.DETACH)
	private List<Document> pictures;

	private RentPeriod rentPeriod;
	
	public double getProjectedRent() {
		return projectedRent;
	}

	public void setProjectedRent(double projectedRent) {
		this.projectedRent = projectedRent;
	}

	public RentPeriod getRentPeriod() {
		return rentPeriod;
	}

	public void setRentPeriod(RentPeriod rentPeriod) {
		this.rentPeriod = rentPeriod;
	}

	public Long getEid() {
		return eid;
	}

	public void setEid(Long eid) {
		this.eid = eid;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public AreaUnits getAreaUnit() {
		return areaUnit;
	}

	public void setAreaUnit(AreaUnits areaUnit) {
		this.areaUnit = areaUnit;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public List<Document> getPictures() {
		return pictures;
	}

	public void setPictures(List<Document> pictures) {
		this.pictures = pictures;
	}
	
	public void addPicture(Document picture) {
		if (this.pictures == null) {
			this.pictures = new ArrayList<>();
		}
		this.pictures.add(picture);
	}
	
	private String bedrooms;
	
	private String bathrooms;
	
	private String description;

	public String getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(String bedrooms) {
		this.bedrooms = bedrooms;
	}

	public String getBathrooms() {
		return bathrooms;
	}

	public void setBathrooms(String bathrooms) {
		this.bathrooms = bathrooms;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}

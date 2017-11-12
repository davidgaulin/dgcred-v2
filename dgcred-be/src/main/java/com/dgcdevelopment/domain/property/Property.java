package com.dgcdevelopment.domain.property;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.joda.time.DateTime;
import org.joda.time.Months;

import com.dgcdevelopment.domain.Address;
import com.dgcdevelopment.domain.Document;
import com.dgcdevelopment.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
public class Property {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty("eid")
    private Long eid;
	private double evaluation;
	private Date evaluationDate;
	
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public Date getEvaluationDate() {
		return evaluationDate;
	}

	public void setEvaluationDate(Date evaluationDate) {
		this.evaluationDate = evaluationDate;
	}

	private String name;
	private int constructionYear;
	
	private double yearlyAppreciationPercentage = 3;
	
	public double getYearlyAppreciationPercentage() {
		return yearlyAppreciationPercentage;
	}

	public void setYearlyAppreciationPercentage(double yearlyAppreciationPercentage) {
		this.yearlyAppreciationPercentage = yearlyAppreciationPercentage;
	}

	private Date purchaseDate = new Date();
	
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	private double purchasePrice;
	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public boolean isFinanced() {
		return financed;
	}

	public void setFinanced(boolean financed) {
		this.financed = financed;
	}

	private boolean financed;
	

	@OneToOne(cascade=CascadeType.ALL, optional=true)
	private Address address;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	private User user;
	
	@OneToMany(cascade=CascadeType.ALL)
	private Set<Unit> units = new HashSet<>();
	
	@OneToMany(cascade=CascadeType.DETACH)
	private Set<Document> documents = new HashSet<>();	
	

	public int getConstructionYear() {
		return constructionYear;
	}

	public void setConstructionYear(int constructionYear) {
		this.constructionYear = constructionYear;
	}

	public Set<Unit> getUnits() {
		return units;
	}

	public void setUnits(Set<Unit> units) {
		this.units = units;
	}	

	public void addUnit(Unit ru) {
		if (this.units == null) {
			this.units = new HashSet<Unit>();
		}
		this.units.add(ru);
	}

	public Long getEid() {
		return eid;
	}

	public void setEid(Long eid) {
		this.eid = eid;
	}

	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Address getAddress() {
		return address;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public double getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(double evaluation) {
		this.evaluation = evaluation;
	}
	
	private int getNumOfMonthSinceLastEvaluation(Date aDate) {
		if (this.evaluationDate != null) {
			return Months.monthsBetween(
				new DateTime(this.evaluationDate.getTime()), 
				new DateTime(aDate.getTime())).getMonths();
		} else {
			return Months.monthsBetween(
					new DateTime(this.purchaseDate.getTime()), 
					new DateTime(aDate.getTime())).getMonths();
		}	
	}
	
	public double caculateCurrentEvaluation() {
		return calculateEvaluationAt(new Date());
		
	}
	
	public double calculateEvaluationAt(Date aDate) {
		if (aDate.getTime() < this.purchaseDate.getTime()) {
			return 0;
		}
		double ma = yearlyAppreciationPercentage / 12 / 100;
		int nomsp = getNumOfMonthSinceLastEvaluation(aDate);
		double evaluatedValue = 0;
		if (this.evaluationDate != null) {
			evaluatedValue = this.evaluation;
		} else {
			evaluatedValue = this.purchasePrice;
		}
		// Property value should be decreasing
		if ( nomsp  > 0 ) {
			for (int x = 0; x < nomsp; x++) {
				evaluatedValue = evaluatedValue + (evaluatedValue * ma); 
			}
		// Going back in the past, property value should decrease
		} else {
			for (int x = nomsp; x < 0; x++) {
				evaluatedValue = evaluatedValue - (evaluatedValue * ma); 
			}
		}
		if (evaluatedValue > 0) {
			return Math.round(evaluatedValue * 100) / 100D;
		} else {
			return 0;
		}
	}
	
	private double longitude;
	private double latitude;
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
}

package com.dgcdevelopment.domain.lease;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import com.dgcdevelopment.domain.Document;

@Entity
public class CreditCheck {
	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int eid;
	
	@ManyToOne
	private Tenant tenant;
	
	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public double getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(double creditScore) {
		this.creditScore = creditScore;
	}

	private double creditScore;
	
	@OneToMany(cascade=CascadeType.DETACH)
	private Set<Document> documents = new HashSet<>();

	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	@PreRemove
	public void preRemove() {
		this.tenant = null;
	}
	
}

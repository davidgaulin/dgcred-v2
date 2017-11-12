package com.dgcdevelopment.domain.lease;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;

import com.dgcdevelopment.domain.property.Property;
import com.dgcdevelopment.domain.property.Unit;

@Entity
public class Notification {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long eid;
	
	private String text;
	
	public Long getEid() {
		return eid;
	}

	public void setEid(Long eid) {
		this.eid = eid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Lease getLease() {
		return lease;
	}

	public void setLease(Lease lease) {
		this.lease = lease;
	}

	public Set<Tenant> getTenants() {
		return tenants;
	}

	public void setTenants(Set<Tenant> tenants) {
		this.tenants = tenants;
	}

	private Date sentDate;
	
	@OneToOne
	private Property property;
	
	@OneToOne
	private Unit unit;
	
	@OneToOne
	private Lease lease;
	
	@OneToMany
	private Set<Tenant> tenants = new HashSet<>();
	
	@PreRemove
	private void preRemove() {
		unit = null;
		tenants = null;
		property = null;
		lease = null;
	}
}

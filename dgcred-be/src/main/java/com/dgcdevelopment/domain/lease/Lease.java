package com.dgcdevelopment.domain.lease;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;

import org.joda.time.DateTime;

import com.dgcdevelopment.domain.BaseEntity;
import com.dgcdevelopment.domain.Document;
import com.dgcdevelopment.domain.RentPeriod;
import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.property.Property;
import com.dgcdevelopment.domain.property.Unit;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@Getter
@Setter
public class Lease extends BaseEntity {

	private boolean active = true;

	private LeaseRenewalOption leaseRenewalOption = LeaseRenewalOption.FULL_LENGTH;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date startDate;

	private int duration = 12;

	private boolean autoRenew = true;

	private int terminationNoticeLength = 3;

	public void addTenant(Tenant tenant) {
		if (this.tenants == null) {
			this.tenants = new HashSet<>();
		}
		this.tenants.add(tenant);
	}

	public void addTenants(Set<Tenant> tenants) {
		if (this.tenants == null) {
			this.tenants = new HashSet<>();
		}
		this.tenants.addAll(tenants);
	}

	private RentPeriod terminationNoticePeriod = RentPeriod.MONTHS;

	private RentPeriod durationUnit = RentPeriod.MONTHS;

	@OneToOne(cascade = CascadeType.DETACH)
	private Unit unit;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<Tenant> tenants = new HashSet<>();

	@OneToMany(cascade = CascadeType.DETACH)
	private Set<Document> documents = new HashSet<>();

	@PreRemove
	private void removePropertyAndFinancialInstitution() {
		unit = null;
		tenants = null;
		property = null;
	}

	/**
	 * Returns the end date of the lease.
	 * 
	 * @return
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Montreal")
	public Date getEndDate() {
		if (this.startDate == null) {
			log.error("Trying to calculate lease end date without start date." + " Lease eid: " + this.getEid());
			return null;
		}
		switch (durationUnit) {
		case MONTHS:
			return new DateTime(this.startDate).plusMonths(duration).minusSeconds(1).toDate();
		case DAYS:
			return new DateTime(this.startDate).plusDays(duration).minusSeconds(1).toDate();
		case YEARS:
			return new DateTime(this.startDate).plusYears(duration).minusSeconds(1).toDate();
		case WEEKS:
			return new DateTime(this.startDate).plusWeeks(duration).minusSeconds(1).toDate();
		default:
			log.error("There is a lease without a proper Duration Unit." + " Lease id: " + this.getEid());
			break;
		}
		return null;
	}

	// public Date getLastRenewedStartDate() {
	// if (this.startDate == null) {
	// log.error("Trying to calculate lease last renewed date without a start
	// date." + " Lease eid: "
	// + this.getEid());
	// return null;
	// }
	//
	//
	// }

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Montreal")
	public Date getLeaseRenewalNoticationDate() {
		if (this.getStartDate() == null) {
			log.error(
					"Trying to calculate lease notification date without start date." + " Lease eid: " + this.getEid());
			return null;
		}

		switch (terminationNoticePeriod) {
		case MONTHS:
			return new DateTime(this.getEndDate()).minusMonths(duration).toDate();
		case DAYS:
			return new DateTime(this.getEndDate()).minusMonths(duration).toDate();
		case YEARS:
			return new DateTime(this.getEndDate()).minusMonths(duration).toDate();
		case WEEKS:
			return new DateTime(this.getEndDate()).minusMonths(duration).toDate();
		default:
			log.error("There is a lease without a proper Notification Period." + " Lease id: " + this.getEid());
			break;
		}
		return null;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private User user;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<LeaseRate> rates = new ArrayList<>();

	@ManyToOne(cascade = CascadeType.DETACH)
	private Property property;

	private double rent;
	private String rentPeriod;

}

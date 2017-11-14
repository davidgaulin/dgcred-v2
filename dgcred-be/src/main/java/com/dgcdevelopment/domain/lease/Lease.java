package com.dgcdevelopment.domain.lease;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.PreRemove;
import javax.persistence.Transient;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dgcdevelopment.domain.Document;
import com.dgcdevelopment.domain.RentPeriod;
import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.property.Property;
import com.dgcdevelopment.domain.property.Unit;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Lease {

	@Transient
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long eid;

	private boolean active = true;

	private LeaseRenewalOption leaseRenewalOption = LeaseRenewalOption.FULL_LENGTH;

	public LeaseRenewalOption getLeaseRenewalOption() {
		return leaseRenewalOption;
	}

	public void setLeaseRenewalOption(LeaseRenewalOption leaseRenewalOption) {
		this.leaseRenewalOption = leaseRenewalOption;
	}

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

	public boolean isAutoRenew() {
		return autoRenew;
	}

	public void setAutoRenew(boolean autoRenew) {
		this.autoRenew = autoRenew;
	}

	public int getTerminationNoticePeriod() {
		return terminationNoticeLength;
	}

	public void setTerminationNoticePeriod(int noticePeriod) {
		this.terminationNoticeLength = noticePeriod;
	}

	public RentPeriod getTerminationNoticePeriodUnit() {
		return terminationNoticePeriod;
	}

	public void setTerminationNoticePeriodUnit(RentPeriod noticePeriodUnit) {
		this.terminationNoticePeriod = noticePeriodUnit;
	}

	public Set<Tenant> getTenants() {
		return tenants;
	}

	public void setTenants(Set<Tenant> tenants) {
		this.tenants = tenants;
	}

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

	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	private RentPeriod terminationNoticePeriod = RentPeriod.MONTHS;

	private RentPeriod durationUnit = RentPeriod.MONTHS;

	@OneToOne(cascade = CascadeType.DETACH)
	private Unit unit;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<Tenant> tenants = new HashSet<>();

	@OneToMany(cascade = CascadeType.DETACH)
	private Set<Document> documents = new HashSet<>();

	public Long getEid() {
		return eid;
	}

	public void setEid(Long eid) {
		this.eid = eid;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public RentPeriod getDurationUnit() {
		return durationUnit;
	}

	public void setDurationUnit(RentPeriod durationUnit) {
		this.durationUnit = durationUnit;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@PreRemove
	private void removePropertyAndFinancialInstitution() {
		unit = null;
		tenants = null;
		property = null;
	}

	public Date calculateLeaseEndDate() {
		if (this.startDate == null) {
			log.error("Trying to calculate lease end date without start date." + " Lease eid: " + this.eid);
			return null;
		}
		switch (durationUnit) {
		case MONTHS:
			return new DateTime(this.startDate).plusMonths(duration).toDate();
		case DAYS:
			return new DateTime(this.startDate).plusDays(duration).toDate();
		case YEARS:
			return new DateTime(this.startDate).plusYears(duration).toDate();
		case WEEKS:
			return new DateTime(this.startDate).plusWeeks(duration).toDate();
		default:
			log.error("There is a lease without a proper Duration Unit." + " Lease id: " + this.eid);
			break;
		}
		return null;
	}

	public Date calculateTerminationNotificationDate() {
		if (this.startDate == null) {
			log.error("Trying to calculate lease notification date without start date." + " Lease eid: " + this.eid);
			return null;
		}
		switch (terminationNoticePeriod) {
		case MONTHS:
			return new DateTime(this.calculateLeaseEndDate()).minusMonths(duration).toDate();
		case DAYS:
			return new DateTime(this.calculateLeaseEndDate()).minusMonths(duration).toDate();
		case YEARS:
			return new DateTime(this.calculateLeaseEndDate()).minusMonths(duration).toDate();
		case WEEKS:
			return new DateTime(this.calculateLeaseEndDate()).minusMonths(duration).toDate();
		default:
			log.error("There is a lease without a proper Notification Period." + " Lease id: " + this.eid);
			break;
		}
		return null;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<LeaseRate> rates = new ArrayList<>();

	public List<LeaseRate> getRates() {
		return rates;
	}

	public void setRates(List<LeaseRate> rates) {
		this.rates = rates;
	}

	@ManyToOne(cascade = CascadeType.DETACH)
	private Property property;

	public int getTerminationNoticeLength() {
		return terminationNoticeLength;
	}

	public void setTerminationNoticeLength(int terminationNoticeLength) {
		this.terminationNoticeLength = terminationNoticeLength;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public void setTerminationNoticePeriod(RentPeriod terminationNoticePeriod) {
		this.terminationNoticePeriod = terminationNoticePeriod;
	}

	private double rent;
	private String rentPeriod;

	public String getRentPeriod() {
		return rentPeriod;
	}

	public void setRentPeriod(String rentPeriod) {
		this.rentPeriod = rentPeriod;
	}

	public double getRent() {
		return rent;
	}

	public void setRent(double rent) {
		this.rent = rent;
	}

}

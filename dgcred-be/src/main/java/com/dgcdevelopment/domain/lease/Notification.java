package com.dgcdevelopment.domain.lease;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;

import com.dgcdevelopment.domain.BaseEntity;
import com.dgcdevelopment.domain.property.Property;
import com.dgcdevelopment.domain.property.Unit;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Notification extends BaseEntity {

	private String text;

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

package com.dgcdevelopment.domain.lease;

import java.util.Date;

import javax.persistence.Entity;

import com.dgcdevelopment.domain.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LeaseRate extends BaseEntity {

	private double rate;

	private Date startDate;

	private Date endDate;

}

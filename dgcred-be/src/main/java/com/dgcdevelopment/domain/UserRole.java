package com.dgcdevelopment.domain;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserRole extends BaseEntity {

	private String name;

	private String description;

}

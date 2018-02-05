package com.dgcdevelopment.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.PreRemove;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User extends BaseEntity {

	private String username;
	private String password;
	private String fullname;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<UserRole> roles = new HashSet<UserRole>();

	@PreRemove
	private void removeRoles() {
		roles = new HashSet<UserRole>();
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name = "name")
	@Column(name = "value")
	@CollectionTable(name = "user_preferences", joinColumns = @JoinColumn(name = "user_eid"))
	private Map<String, String> preferences = new HashMap<String, String>();

}

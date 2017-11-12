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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.PreRemove;

@Entity
public class User {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long eid;
	
	private String username;
	private String password;
	private String fullname;
	
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<UserRole> roles;
	
	public Set<UserRole> getRoles() {
		if (roles == null) {
			return new HashSet<UserRole>();
		} else {
			return roles;
		}
	}
	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	@PreRemove
	private void removeRoles() {
		roles = new HashSet<UserRole>();
	}

	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="user_preferences", joinColumns=@JoinColumn(name="user_eid"))
	private Map<String, String> preferences = new HashMap<String, String>();

	public Long getEid() {
		return eid;
	}
	public void setEid(Long eid) {
		this.eid = eid;
	}
	public Map<String, String> getPreferences() {
		return preferences;
	}
	public void setPreferences(Map<String, String> preferences) {
		this.preferences = preferences;
	}
}

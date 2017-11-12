package com.dgcdevelopment.domain.lease;

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

import com.dgcdevelopment.domain.Address;
import com.dgcdevelopment.domain.Document;
import com.dgcdevelopment.domain.Telephone;
import com.dgcdevelopment.domain.User;

@Entity
public class Tenant {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long eid;
	
	public Long getEid() {
		return eid;
	}
	public void setEid(Long eid) {
		this.eid = eid;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Address getPreviousAddress() {
		return previousAddress;
	}
	public void setPreviousAddress(Address previousAddress) {
		this.previousAddress = previousAddress;
	}
	public String getSinSsn() {
		return sinSsn;
	}
	public void setSinSsn(String sinSsn) {
		this.sinSsn = sinSsn;
	}
	public Document getPicture() {
		return picture;
	}
	public void setPicture(Document picture) {
		this.picture = picture;
	}
	private String firstName;
	private String lastName;
	
	@OneToMany(cascade=CascadeType.ALL)
	private Set<Telephone> telephones = new HashSet<>();	
	
	public Set<Telephone> getTelephones() {
		return telephones;
	}
	public void setTelephones(Set<Telephone> telephones) {
		this.telephones = telephones;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	private String email;
	private Date birthday;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Address previousAddress;
	
	private String sinSsn;
	
	@OneToOne(cascade=CascadeType.DETACH)
	private Document picture;		

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	private User user;

	@OneToMany(cascade=CascadeType.DETACH)
	private Set<Document> documents = new HashSet<>();

	public Set<Document> getDocuments() {
		return documents;
	}
	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}
	
}

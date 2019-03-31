package com.dgcdevelopment.domain.lease;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.dgcdevelopment.domain.Address;
import com.dgcdevelopment.domain.Document;
import com.dgcdevelopment.domain.PhoneType;
import com.dgcdevelopment.domain.Telephone;
import com.dgcdevelopment.domain.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Setter
@Getter
public class Tenant {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long eid;
	private String firstName;
	private String lastName;

	@OneToMany(cascade = CascadeType.ALL)
	@MapKey(name = "type")
	private Map<PhoneType, Telephone> telephones = new HashMap<>();

	private String email;
	private Date birthday;

	@OneToOne(cascade = CascadeType.ALL)
	private Address previousAddress;

	private String sinSsn;

	@OneToOne(cascade = CascadeType.DETACH)
	private Document picture;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private User user;

	@OneToMany(cascade = CascadeType.DETACH)
	private Set<Document> documents = new HashSet<>();

	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	public void add(Telephone p) {
		if (p.getType() == null) {
			throw new RuntimeException("Phone Type must be specified");
		}
		this.telephones.put(p.getType(), p);
	}

	public void add(PhoneType pt, Telephone p) {
		this.telephones.put(pt, p);
	}
}

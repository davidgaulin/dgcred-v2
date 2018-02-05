package com.dgcdevelopment.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Document extends BaseEntity {

	@ManyToOne(optional = false)
	private User user;

	private String contentType;

	private String fileName;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] content;

}

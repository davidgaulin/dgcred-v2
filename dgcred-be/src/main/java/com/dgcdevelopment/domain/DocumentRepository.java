package com.dgcdevelopment.domain;

import org.springframework.data.repository.CrudRepository;

public interface DocumentRepository extends CrudRepository<Document, Long> {
	
	public Iterable<Document> findByOrderByEidAsc();
	public Iterable<Document> findByUserOrderByEidAsc(User u);
	public Document findOneByUserAndEid(User u, long eid);
	public void deleteByEidAndUser(long eid, User u);

}
package com.dgcdevelopment.domain.property;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

import com.dgcdevelopment.domain.User;

public interface PropertyRepository extends CrudRepository<Property, Long> {
	
	public Iterable<Property> findByOrderByEidAsc();
	public Iterable<Property> findByAddressCity(String city);
	public Iterable<Property> findByUserOrderByEidAsc(User u);
	public Iterable<Property> findByUserOrderByEidAsc(User u, PageRequest pageRequest);
	public Iterable<Property> findByUserEid(long eid);
	public Property findOneByUserAndEid(User u, long eid);
	public void deleteByEidAndUser(long eid, User u);

}
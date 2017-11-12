package com.dgcdevelopment.domain.lease;


import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

import com.dgcdevelopment.domain.User;

public interface TenantRepository extends CrudRepository<Tenant, Long> {

//	@NamedQuery(name="Tenant.Search",
//			query="SELECT * from tenant "
//					+ "where LOWER(firstName) like lower(concat('%',:searchTerm,'%')) "
//					+ "OR    lower(lastName)  like lower(concat('%',:searchTerm,'%'))")
	public Iterable<Tenant> findByOrderByEidAsc();
	public Iterable<Tenant> findByUserOrderByEidAsc(User u);
	public Iterable<Tenant> findByUserOrderByEidAsc(User u, PageRequest pageRequest);
	public Iterable<Tenant> findByUserEid(long eid);
	public Tenant findOneByUserAndEid(User u, long eid);
	public void deleteByEidAndUser(long eid, User u);
	
	// TODO have one search query combining all three
	//public Set<Tenant> findByUserAndFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrTelephonesStrContainingIgnoreCase(User u, String term);
	public Set<Tenant> findByUserAndFirstNameContainingIgnoreCase(User u, String term);
	public Set<Tenant> findByUserAndLastNameContainingIgnoreCase(User u, String term);
	public Set<Tenant> findByUserAndTelephonesStrContainingIgnoreCase(User u, String term);
}
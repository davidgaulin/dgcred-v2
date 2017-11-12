package com.dgcdevelopment.domain.lease;



import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

import com.dgcdevelopment.domain.User;

public interface LeaseRepository extends CrudRepository<Lease, Long> {

	public Iterable<Lease> findByOrderByEidAsc();
	public Iterable<Lease> findByUserOrderByEidAsc(User u);
	public Iterable<Lease> findByUserOrderByEidAsc(User u, PageRequest pageRequest);
	public Iterable<Lease> findByUserEid(long eid);
	public Lease findOneByUserAndEid(User u, long eid);
	public void deleteByEidAndUser(long eid, User u);
	
	public List<Lease> findByTenantsEidAndUser(long eid, User u);
	
	public List<Lease> findByActiveTrueAndUser(User u);

}
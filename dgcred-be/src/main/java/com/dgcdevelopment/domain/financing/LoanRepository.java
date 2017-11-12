package com.dgcdevelopment.domain.financing;


import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;

import com.dgcdevelopment.domain.User;

public interface LoanRepository extends CrudRepository<Loan, Long> {
	
	public Iterable<Loan> findByOrderByEidAsc();
	public Iterable<Loan> findByUserOrderByEidAsc(User u);
	public Iterable<Loan> findByUserOrderByEidAsc(User u, PageRequest pageRequest);
	public Iterable<Loan> findByUserEid(long eid);
	public Loan findOneByUserAndEid(User u, long eid);
	public List<Loan> findByPropertyEidAndUser(long eid, User u);
	public void deleteByEidAndUser(long eid, User u);
}
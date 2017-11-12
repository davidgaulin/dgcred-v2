package com.dgcdevelopment.domain.financing;


import org.springframework.data.repository.CrudRepository;

public interface FinancialInstitutionRepository extends CrudRepository<FinancialInstitution, Long> {
	
	public FinancialInstitution findOneByEid(Long eid);

}
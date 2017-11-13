package com.dgcdevelopment.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.exceptions.MissingEntityException;
import com.dgcdevelopment.domain.financing.Loan;


@RestController
@CrossOrigin
public class LoanController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
	private com.dgcdevelopment.domain.financing.LoanRepository loanRepo;
	
	@GetMapping("/api/loan")
    public Iterable<Loan> getLoans(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Thread.sleep(1000);
		log.info("Retrieving all loans...");
		Iterable<Loan> loans = loanRepo.findByUserOrderByEidAsc((User) request.getAttribute("user"));
		log.info("Retrieve all loans completed");
		
		return loans;
    }
	
	@GetMapping("/api/loan/{eid}")
    public Loan getLoan(@PathVariable Long eid, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Thread.sleep(1000);
		log.info("Retrieving one loan: " + eid);
		Loan loan = loanRepo.findOneByUserAndEid((User) request.getAttribute("user"), eid);

		// Not found
		if (loan == null) {
			log.info("Loan not found: " + eid);
			throw new MissingEntityException("Can't find loan for eid: " + eid);
		}
		log.info("Retrieve loan completed");
		
		return loan;
    }	

	@PostMapping("/api/loan")
    public Loan saveLoan(HttpServletRequest request, 
    		@RequestBody Loan loan) throws Exception {
		log.info("Saving building...");
		loan.setUser((User) request.getAttribute("user"));
		Thread.sleep(1000);
		Loan b = loanRepo.save(loan);
		log.info("Save building completed");
		return b;
    }
	
	@PostMapping("/api/loan/{eid}")
    public Loan updateLoan(HttpServletRequest request, 
    		@RequestBody Loan building) throws Exception {
		log.info("Saving building...");
		building.setUser((User) request.getAttribute("user"));
		Thread.sleep(1000);
		Loan b = loanRepo.save(building);
		log.info("Save building completed");
		return b;
    }
	
	@RequestMapping(value="/api/loan/range/{start}/{count}")
	public Iterable<Loan> getRangeOfLoans(HttpServletRequest request, 
			@PathVariable("start") int start, 
			@PathVariable("count") int count) throws Exception {
		
		PageRequest pageRequest = new PageRequest(start, count);
		Iterable<Loan> loans = loanRepo.findByUserOrderByEidAsc((User) request.getAttribute("user"), pageRequest);
				
		return loans;
		
		//return buildings;
	}
		
	@DeleteMapping("/api/loan/{id}")
	@Transactional
    public User deleteOneLoan(HttpServletRequest request, @PathVariable("id") long id) throws Exception {
		User u = (User) request.getAttribute("user");
		log.info("Deleting loan id: " + id + " User: " + u.getUsername());
		loanRepo.deleteByEidAndUser(id, u);
		log.info("Building " + id + " for user " + u.getUsername() + " is deleted");
		return u;

    }
}

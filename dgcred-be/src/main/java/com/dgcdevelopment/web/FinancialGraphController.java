package com.dgcdevelopment.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dgcdevelopment.domain.Point;
import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.exceptions.InvalidInformationException;
import com.dgcdevelopment.domain.financing.Loan;
import com.dgcdevelopment.domain.financing.LoanBalanceGraphWrapper;
import com.dgcdevelopment.domain.financing.LoanRepository;
import com.dgcdevelopment.domain.property.Property;
import com.dgcdevelopment.domain.property.PropertyRepository;


@RestController
@CrossOrigin
public class FinancialGraphController {

	private final Logger log = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
	private LoanRepository loanRepo;
	
	@Autowired
	private PropertyRepository propertyRepo;
	
	private static final DecimalFormat DF = new DecimalFormat("#.00"); 
	
	/**
	 * Start can't be bigger than end.
	 * 
	 * @param start number of years to start from now.  -5 to go back 5 years, 0 to start today
	 * @param end Number of years from start to finish. 0 finish today, 5 to finish 5 years from now.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/fg/loanbalance/previsions/years/{start}/{end}")
    public Iterable<LoanBalanceGraphWrapper> getLoans(@PathVariable int start, @PathVariable int end, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (start > end) {
			throw new InvalidInformationException("Start must be bigger than end: Example: "
					+ "/api/fg/loan/balance/previsions/years/-5/5");
		}
		log.info("Graphing Loan Balances from %i to %i", start, end);
		Iterable<Loan> loans = loanRepo.findByUserOrderByEidAsc((User) request.getAttribute("user"));
		List<LoanBalanceGraphWrapper> lgws = new ArrayList<>();
		for (Loan l : loans) {
			LoanBalanceGraphWrapper lgw = new LoanBalanceGraphWrapper();
			if (l.getProperty() == null || l.getProperty().getName() == null) {
				lgw.setKey(l.getFinancialInstitution().getName());
			} else {
				lgw.setKey(l.getProperty().getName());
			}
			for (int x = start; x < end; x++) {
				int year = DateTime.now().plusYears(x).getYear();
				String balance = DF.format(new Double(l.calculateBalanceAt(new DateTime().withYear(year).withMonthOfYear(12).withDayOfMonth(31).toDate())));
				lgw.getValues().add(new Point(Integer.toString(year), balance));
			}
			lgws.add(lgw);
		}

		log.info("Graph Data Generated!");
		
		return lgws;
    }
	
	/**
	 * Start can't be bigger than end.
	 * 
	 * @param start number of years to start from now.  -5 to go back 5 years, 0 to start today
	 * @param end Number of years from start to finish. 0 finish today, 5 to finish 5 years from now.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/fg/overview/loan/value/capital/previsions/years/{start}/{end}")
    public Iterable<LoanBalanceGraphWrapper> overviewLoanValueCapital(@PathVariable int start, @PathVariable int end, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (start > end) {
			throw new InvalidInformationException("Start must be bigger than end: Example: "
					+ "/api/fg/loan/balance/previsions/years/-5/5");
		}
		log.info("Graphing Loan Balances, Property Value, and available capital, from %i to %i", start, end);
		Iterable<Loan> loans = loanRepo.findByUserOrderByEidAsc((User) request.getAttribute("user"));
		Iterable<Property> properties = propertyRepo.findByUserOrderByEidAsc((User) request.getAttribute("user"));
		List<LoanBalanceGraphWrapper> lgws = new ArrayList<>();
		LoanBalanceGraphWrapper loanLgw = new LoanBalanceGraphWrapper();
		LoanBalanceGraphWrapper propertyLgw = new LoanBalanceGraphWrapper();
		LoanBalanceGraphWrapper capitalLgw = new LoanBalanceGraphWrapper();
		loanLgw.setKey("Loan Balance");
		propertyLgw.setKey("Property Value");
		capitalLgw.setKey("Available Capital");
		for (int x = start; x < end; x++) {
			double balance = 0;
			double value = 0;
			double capital = 0;
			int year = DateTime.now().plusYears(x).getYear();
			
			for (Loan l : loans) {
				balance += l.calculateBalanceAt(new DateTime().withYear(year).withMonthOfYear(12).withDayOfMonth(31).toDate());
			}
			loanLgw.getValues().add(new Point(Integer.toString(year), DF.format(balance)));
			
			for (Property p : properties) {
				value += p.calculateEvaluationAt(new DateTime().withYear(year).withMonthOfYear(12).withDayOfMonth(31).toDate());
			}
			propertyLgw.getValues().add(new Point(Integer.toString(year), DF.format(value)));
			
			capital = value - balance * 0.8;
			capitalLgw.getValues().add(new Point(Integer.toString(year), DF.format(capital)));
			
		}
		lgws.add(propertyLgw);
		lgws.add(capitalLgw);
		lgws.add(loanLgw);
		log.info("Graph Data Generated!");
		
		return lgws;
    }	
	
	
	/**
	 * Start can't be bigger than end.
	 * 
	 * @param start number of years to start from now.  -5 to go back 5 years, 0 to start today
	 * @param end Number of years from start to finish. 0 finish today, 5 to finish 5 years from now.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/fg/overallvalue/previsions/years/{start}/{end}")
    public Iterable<LoanBalanceGraphWrapper> getOverAllYearlyValue(@PathVariable int start, @PathVariable int end, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (start > end) {
			throw new InvalidInformationException("Start must be bigger than end: Example: "
					+ "/api/fg/overallvalue/previsions/years/-10/10");
		}
		
		log.info("Graphing Overall Value Balances from %i to %i", start, end);
		Iterable<Loan> loans = loanRepo.findByUserOrderByEidAsc((User) request.getAttribute("user"));
		List<LoanBalanceGraphWrapper> lgws = new ArrayList<>();
		LoanBalanceGraphWrapper lgw = new LoanBalanceGraphWrapper();
		lgw.setKey("Debt");
		for (int x = start; x < end; x++) {
			double balance = 0;
			int year = DateTime.now().plusYears(x).getYear();
			for (Loan l : loans) {
				balance += new Double(l.calculateBalanceAt(new DateTime().withYear(year).withMonthOfYear(12).withDayOfMonth(31).toDate()));
			}
			lgw.getValues().add(new Point(Integer.toString(year), Double.toString(Math.round(balance * 100) / 100D)));
		}
		lgws.add(lgw);
		lgw = new LoanBalanceGraphWrapper();
		lgw.setKey("Value");
		Iterable<Property> ps = propertyRepo.findByUserOrderByEidAsc((User) request.getAttribute("user")); 
		for (int x = start; x < end; x++) {
			double value = 0;
			int year = DateTime.now().plusYears(x).getYear();
			for (Property p : ps) {
				value += new Double(p.calculateEvaluationAt(new DateTime().withYear(year).withMonthOfYear(12).withDayOfMonth(31).toDate()));
			}
			lgw.getValues().add(new Point(Integer.toString(year), Double.toString(Math.round(value * 100) / 100D)));
		}
		log.info("Graph Data Generated!");
		lgws.add(lgw);
		return lgws;
    }
		
	
//	@GetMapping("/api/loan/{eid}")
//    public Loan getLoan(@PathVariable Long eid, HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//		Thread.sleep(1000);
//		log.info("Retrieving one loan: " + eid);
//		Loan loan = loanRepo.findOneByUserAndEid((User) request.getAttribute("user"), eid);
//		// HATEOAS??
////		loan.add(ControllerLinkBuilder.linkTo(LoanController.class, 
////				LoanController.class.getMethod("getLoan", 
////						Long.class, HttpServletRequest.class, 
////						HttpServletResponse.class), loan.getEid(), request, response).withSelfRel());
//
//		// Not found
//		if (loan == null) {
//			log.info("Loan not found: " + eid);
//			throw new MissingEntityException("Can't find loan for eid: " + eid);
//		}
//		log.info("Retrieve loan completed");
//		
//		return loan;
//    }	
//
//	@PostMapping("/api/loan")
//    public Loan saveLoan(HttpServletRequest request, 
//    		@RequestBody Loan loan) throws Exception {
//		log.info("Saving building...");
//		loan.setUser((User) request.getAttribute("user"));
//		Thread.sleep(1000);
//		Loan b = loanRepo.save(loan);
//		log.info("Save building completed");
//		return b;
//    }
//	
//	@PostMapping("/api/loan/{eid}")
//    public Loan updateLoan(HttpServletRequest request, 
//    		@RequestBody Loan building) throws Exception {
//		log.info("Saving building...");
//		building.setUser((User) request.getAttribute("user"));
//		Thread.sleep(1000);
//		Loan b = loanRepo.save(building);
//		log.info("Save building completed");
//		return b;
//    }
//	
//	@RequestMapping(value="/api/loan/range/{start}/{count}")
//	public Iterable<Loan> getRangeOfLoans(HttpServletRequest request, 
//			@PathVariable("start") int start, 
//			@PathVariable("count") int count) throws Exception {
//		
//		PageRequest pageRequest = new PageRequest(start, count);
//		Iterable<Loan> loans = loanRepo.findByUserOrderByEidAsc((User) request.getAttribute("user"), pageRequest);
//				
//		return loans;
//		
//		//return buildings;
//	}
//		
//	@DeleteMapping("/api/loan/{id}")
//	@Transactional
//    public User deleteOneLoan(HttpServletRequest request, @PathVariable("id") long id) throws Exception {
//		User u = (User) request.getAttribute("user");
//		log.info("Deleting loan id: " + id + " User: " + u.getUsername());
//		loanRepo.deleteByEidAndUser(id, u);
//		log.info("Building " + id + " for user " + u.getUsername() + " is deleted");
//		return u;
//
//    }
}

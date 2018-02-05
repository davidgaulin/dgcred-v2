package com.dgcdevelopment.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dgcdevelopment.domain.Event;
import com.dgcdevelopment.domain.EventType;
import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.financing.Loan;
import com.dgcdevelopment.domain.financing.LoanRepository;
import com.dgcdevelopment.domain.lease.Lease;
import com.dgcdevelopment.domain.lease.LeaseRepository;

//import net.fortuna.ical4j.data.CalendarOutputter;
//import net.fortuna.ical4j.model.Calendar;
//import net.fortuna.ical4j.model.ComponentList;
//import net.fortuna.ical4j.model.Date;
//import net.fortuna.ical4j.model.component.CalendarComponent;
//import net.fortuna.ical4j.model.component.VEvent;
//import net.fortuna.ical4j.model.property.CalScale;
//import net.fortuna.ical4j.model.property.ProdId;
//import net.fortuna.ical4j.model.property.Uid;
//import net.fortuna.ical4j.model.property.Version;

@RestController
@CrossOrigin
public class CalendarController {

	private final Logger log = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
	private LoanRepository loanRepo;
	
	@Autowired
	private LeaseRepository leaseRepo;
	
	private static final DecimalFormat DF = new DecimalFormat("#.00"); 
	
	private static final int LOAN_RENEWAL_NOTIFICATION_PERIOD = 3;
	
	private static final String LOAN_RENEWAL_NOTIFICATION_PERIOD_PREF_KEY = "global.loanRenewalNotificationPeriod";
	
	
//	/**
//	 * TODO move to its own Service with separate "Authentication"
//	 * @author infor
//	 *
//	 */
//	@GetMapping("/api/loanRenewalCalendar")
//    public void getLoans(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		User u = (User) request.getAttribute("user");
//		Iterable<Loan> loans = loanRepo.findByUserOrderByEidAsc(u);
//		ComponentList<CalendarComponent> cl = new ComponentList<>();
//		for (Loan loan : loans) {
//			StringBuilder summary = new StringBuilder();
//			summary.append("Loan from ");
//			summary.append(loan.getFinancialInstitution().getName());
//			summary.append(" contracted on ");
//			summary.append(DateFormatUtils.format(loan.getLoanCreationDate(), "yyyy-MM-dd"));
//			summary.append(" for property at ");
//			summary.append(loan.getProperty().getAddress().getAddress1());
//			summary.append(" is up for renewal in three (3) months");
//			VEvent loanRenewal = new VEvent(new Date(loan.getRenewalDate()), summary.toString());
//			// initialise as an all-day event..
//			//loanRenewal.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE);
//			Uid uid = new Uid(u.getEid() + "-" + loan.getClass().getName() + "-" + loan.getEid());
//			loanRenewal.getProperties().add(uid);
//			
//			// TODO
//			// 1- Change date to be 3 months in past
//			// 2- Ensure that in 3 months the balance is not supposed to be 0
//			cl.add(loanRenewal);
//		}
//		Calendar calendar = new Calendar(cl);
//		calendar.getProperties().add(new ProdId("-//DGC R.E.D.//iCal4j 1.0//EN"));
//		calendar.getProperties().add(Version.VERSION_2_0);
//		calendar.getProperties().add(CalScale.GREGORIAN);
//		CalendarOutputter co = new CalendarOutputter();
//		co.output(calendar, response.getOutputStream());
//    }
	
	@GetMapping("/api/calendar/renewals/{year}/{month}")
    public List<Event> getRenewals(HttpServletRequest request, HttpServletResponse response, @PathVariable int year, @PathVariable int month) throws Exception {
		User u = (User) request.getAttribute("user");
		int lrnpm = LOAN_RENEWAL_NOTIFICATION_PERIOD;
		if (u.getPreferences() != null && u.getPreferences().get(LOAN_RENEWAL_NOTIFICATION_PERIOD_PREF_KEY) != null) {
			try {
				lrnpm = Integer.parseInt(u.getPreferences().get(LOAN_RENEWAL_NOTIFICATION_PERIOD_PREF_KEY));
			} catch (NumberFormatException nfe) {
				log.error("User preference for " + LOAN_RENEWAL_NOTIFICATION_PERIOD_PREF_KEY
						+ " for user: " + u.getUsername() + "is set to an invalid value: " 
						+ u.getPreferences().get(LOAN_RENEWAL_NOTIFICATION_PERIOD_PREF_KEY));
			}
		}
		Iterable<Loan> loans = loanRepo.findByUserOrderByEidAsc(u);
		Date firstDayCurrentMonth = DateUtils.parseDate(year + "-" + month + "-01 12:00:00", "yyyy-MM-dd hh:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(firstDayCurrentMonth);
		Date lastDayCurrentMonth = DateUtils.parseDate(year + "-" + month + "-" + cal.getActualMaximum(Calendar.DAY_OF_MONTH)
			+ " 23:59:59", "yyyy-MM-dd hh:mm:ss");
		List<Event> events = new ArrayList<>();
		for (Loan loan : loans) {
			// Put notifications for renewals
			if (DateUtils.addMonths(loan.getRenewalDate(), -lrnpm).getTime() < lastDayCurrentMonth.getTime()
					&& DateUtils.addMonths(loan.getRenewalDate(), -lrnpm).getTime() > firstDayCurrentMonth.getTime()
					&& loan.calculateBalanceAt(DateUtils.addMonths(lastDayCurrentMonth, -lrnpm)) > 0) {
				StringBuilder summary = new StringBuilder();
				summary.append("Loan from ");
				summary.append(loan.getFinancialInstitution().getName());
				summary.append(" contracted on ");
				summary.append(DateFormatUtils.format(loan.getLoanCreationDate(), "yyyy-MM-dd"));
				summary.append(" for property at ");
				summary.append(loan.getProperty().getAddress().getAddress1());
				summary.append(" is up for renewal in " + lrnpm + " months. ");
				summary.append("Current interest rate: " + loan.getInterestRate() + "%. ");
				summary.append("Approximate loan balance at renewal: " + DF.format(loan.calculateBalanceAt(loan.getRenewalDate())));
				summary.append("$.");
				String title = "Renewal mortage " + loan.getProperty().getAddress().getAddress1();
				Event e = new Event(DateUtils.addMonths(loan.getRenewalDate(), -lrnpm), title, summary.toString(), EventType.NOTIFICATIONS);
				events.add(e);
			}
			

			// Put renewals
			if (loan.getRenewalDate().getTime() < lastDayCurrentMonth.getTime()
					&& loan.getRenewalDate().getTime() > firstDayCurrentMonth.getTime()
					&& loan.calculateBalanceAt(lastDayCurrentMonth) > 0) {
				StringBuilder summary = new StringBuilder();
				summary.append("Mortage from ");
				summary.append(loan.getFinancialInstitution().getName());
				summary.append(" contracted on ");
				summary.append(DateFormatUtils.format(loan.getLoanCreationDate(), "yyyy-MM-dd"));
				summary.append(" for property at ");
				summary.append(loan.getProperty().getAddress().getAddress1());
				summary.append(" is up for renewal today!!! ");
				summary.append("Current interest rate: " + loan.getInterestRate() + "%. ");
				summary.append("Approximate loan balance at renewal: " + DF.format(loan.calculateBalanceAt(loan.getRenewalDate())));
				summary.append("$.");
				String title = "Renewal mortage " + loan.getProperty().getAddress().getAddress1(); 
				Event e = new Event(loan.getRenewalDate(), title, summary.toString(), EventType.FINANCING);
				events.add(e);
			}	
		}
		
		Iterable<Lease> leases = leaseRepo.findByUserEid(u.getEid());
		for (Lease l : leases) {
			if (l.getEndDate().getTime() < lastDayCurrentMonth.getTime()
					&& l.getEndDate().getTime() > firstDayCurrentMonth.getTime()) {
				StringBuilder summary = new StringBuilder();
				summary.append("Lease on ");
				summary.append(l.getProperty().getAddress().getAddress1());
				summary.append(" unit ");
				summary.append(l.getUnit().getNumber());
				summary.append(" by tenant ");
				summary.append(l.getTenants().stream().map(i -> i.getFirstName() + " " + i.getLastName()).collect(Collectors.joining(", ")));
				summary.append(" is ending today!");
				String title = "Lease on " + l.getProperty().getName() + " unit " + l.getUnit().getNumber() + " ends today!";  
				Event e = new Event(l.getEndDate(), title, summary.toString(), EventType.LEASING);
				events.add(e);
			}
			if (l.getLeaseRenewalNoticationDate().getTime() < lastDayCurrentMonth.getTime()
					&& l.getLeaseRenewalNoticationDate().getTime() > firstDayCurrentMonth.getTime()) {
				StringBuilder summary = new StringBuilder();
				summary.append("You must notify the tenant at ");
				summary.append(l.getProperty().getAddress().getAddress1());
				summary.append(" unit ");
				summary.append(l.getUnit().getNumber());
				summary.append(" today if you want to resign their lease.");
				String title = "Last day to terminate lease on " + l.getProperty().getName() + " unit " + l.getUnit().getNumber();  
				Event e = new Event(l.getEndDate(), title, summary.toString(), EventType.LEASING);
				events.add(e);
			}
		}
				
		
		return events;
	}
	
}

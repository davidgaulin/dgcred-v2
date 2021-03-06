package com.dgcdevelopment.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dgcdevelopment.domain.Document;
import com.dgcdevelopment.domain.DocumentRepository;
import com.dgcdevelopment.domain.RentPeriod;
import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.exceptions.MissingEntityException;
import com.dgcdevelopment.domain.lease.Lease;
import com.dgcdevelopment.domain.lease.LeaseRepository;
import com.dgcdevelopment.domain.lease.Tenant;

@RestController
@CrossOrigin
public class LeaseController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DocumentRepository documentRepo;

	@Autowired
	private LeaseRepository leaseRepo;

	@GetMapping("/api/lease")
	public Iterable<Lease> getLeases(HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info("Retrieving all leases...");
		Iterable<Lease> leases = leaseRepo.findByUserOrderByEidAsc((User) request.getAttribute("user"));
		log.info("Retrieve all leases completed");
		return leases;
	}

	@GetMapping("/api/lease/active")
	public Iterable<Lease> getActiveLeases(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User u = (User) request.getAttribute("user");
		log.info("Retrieving all active leases for user: " + u.getUsername());
		Iterable<Lease> leases = leaseRepo.findByActiveTrueAndUser(u);
		log.info("Retrieve all active leases completed");
		return leases;
	}

	@GetMapping("/api/lease/{eid}")
	public Lease getLease(@PathVariable Long eid, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		log.info("Retrieving one lease: " + eid);
		Lease lease = leaseRepo.findOneByUserAndEid((User) request.getAttribute("user"), eid);

		// Not found
		if (lease == null) {
			log.info("Lease not found: " + eid);
			throw new MissingEntityException("Can't find lease for eid: " + eid);
		}
		log.info("Retrieve lease completed");

		return lease;
	}

	@PostMapping("/api/lease")
	public Lease saveLease(HttpServletRequest request, @RequestBody Lease lease) throws Exception {

		log.info("Saving Lease : " + lease.getUnit().getNumber());
		lease.setUser((User) request.getAttribute("user"));

		// Grab the document and put them in
		if (lease.getEid() != null && lease.getEid() > 0) {
			System.out.println(lease.getEid());
			Lease tmpP = leaseRepo.findOne(lease.getEid());
			if (tmpP != null) {
				lease.setDocuments(tmpP.getDocuments());
			}
		}

		for (Tenant t : lease.getTenants()) {
			t.setUser((User) request.getAttribute("user"));
		}

		// Save the lease
		Lease t = leaseRepo.save(lease);
		log.info("Save lease completed");
		return t;
	}

	@GetMapping("/api/lease/upcomingRenewal/{duration}/{unit}")
	public List<Lease> getUpcomingRenewal(HttpServletRequest request, @PathVariable int duration,
			@PathVariable String unit) throws Exception {
		log.info("Retrieving all leases...");
		List<Lease> leases = leaseRepo.findByUserOrderByEidAsc((User) request.getAttribute("user"));

		List<Lease> upcoming = null;
		RentPeriod rp = RentPeriod.MONTHS;
		try {
			rp = RentPeriod.valueOf(unit.toUpperCase());
		} catch (IllegalArgumentException iae) {
			log.info("Invalid unit: {}. Defaulting to months", unit);
		}

		switch (rp) {
		case WEEKS:
			upcoming = leases.stream().filter(
					l -> l.getLeaseRenewalNoticationDate().getTime() > new DateTime().minusWeeks(duration).getMillis())
					.collect(Collectors.toList());
			break;

		case YEARS:
			upcoming = leases.stream().filter(
					l -> l.getLeaseRenewalNoticationDate().getTime() > new DateTime().minusYears(duration).getMillis())
					.collect(Collectors.toList());
			break;
		case MONTHS:
		default:
			upcoming = leases.stream().filter(
					l -> l.getLeaseRenewalNoticationDate().getTime() > new DateTime().minusMonths(duration).getMillis())
					.collect(Collectors.toList());
			break;
		}
		return upcoming;
	}

	@GetMapping("/api/lease/addDocument/{peid}/{deid}")
	public Lease updateLeaseDoc(HttpServletRequest request, @PathVariable Long deid, @PathVariable Long peid)
			throws Exception {
		log.info("Attaching doc: " + deid + " to lease: " + peid + "...");
		Lease p = leaseRepo.findOneByUserAndEid((User) request.getAttribute("user"), peid);
		boolean found = false;
		for (Document doc : p.getDocuments()) {
			if (doc.getEid().equals(deid)) {
				found = true;
			}
		}

		if (!found) {
			Document d = documentRepo.findOneByUserAndEid((User) request.getAttribute("user"), deid);
			if (d != null && d.getEid() != null) {
				p.getDocuments().add(d);
				leaseRepo.save(p);
			}
		}
		log.info("Doc: " + deid + " attached to lease: " + peid + "...");
		return p;
	}

	@GetMapping("/api/lease/deleteDocument/{peid}/{deid}")
	public Lease deleteLeaseDoc(HttpServletRequest request, @PathVariable Long deid, @PathVariable Long peid)
			throws Exception {
		log.info("Deleting doc: " + deid + " from lease: " + peid + "...");
		Lease p = leaseRepo.findOneByUserAndEid((User) request.getAttribute("user"), peid);
		boolean found = false;
		for (Document doc : p.getDocuments()) {
			if (doc.getEid().equals(deid)) {
				p.getDocuments().remove(doc);
				leaseRepo.save(p);
				found = true;
			}
		}
		if (found) {
			documentRepo.deleteByEidAndUser(deid, (User) request.getAttribute("user"));
		}
		log.info("Doc: " + deid + " deleted from lease: " + peid + "...");
		return p;
	}

	@DeleteMapping("/api/lease/{eid}")
	@Transactional
	public User deleteOneLease(HttpServletRequest request, @PathVariable("eid") long eid) throws Exception {
		User u = (User) request.getAttribute("user");
		log.info("Deleting lease eid: " + eid + " User: " + u.getUsername());

		leaseRepo.deleteByEidAndUser(eid, u);
		log.info("Lease " + eid + " for user " + u.getUsername() + " is  deleted");
		return u;
	}

	@GetMapping("/api/lease/inactivate/{eid}")
	@Transactional
	public User markAsInactive(HttpServletRequest request, @PathVariable("eid") long eid) throws Exception {
		User u = (User) request.getAttribute("user");
		log.info("Deleting lease eid: " + eid + " User: " + u.getUsername());
		Lease l = leaseRepo.findOneByUserAndEid(u, eid);
		l.setActive(false);
		leaseRepo.save(l);
		log.info("Lease " + eid + " for user " + u.getUsername() + " is  marked inactive.");
		return u;
	}

}

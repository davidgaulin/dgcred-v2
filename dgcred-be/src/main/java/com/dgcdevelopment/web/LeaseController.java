package com.dgcdevelopment.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

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
import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.exceptions.MissingEntityException;
import com.dgcdevelopment.domain.lease.Lease;
import com.dgcdevelopment.domain.lease.LeaseRepository;

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
		if (lease.getEid() != null) {
			lease.setDocuments(leaseRepo.findOne(lease.getEid()).getDocuments());
		}

		// Save the lease
		Lease t = leaseRepo.save(lease);
		log.info("Save lease completed");
		return t;
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

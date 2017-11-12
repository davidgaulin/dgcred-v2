package com.dgcdevelopment.web;

import java.util.List;
import java.util.Set;

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
import com.dgcdevelopment.domain.exceptions.DataConflictException;
import com.dgcdevelopment.domain.exceptions.MissingEntityException;
import com.dgcdevelopment.domain.lease.Lease;
import com.dgcdevelopment.domain.lease.LeaseRepository;
import com.dgcdevelopment.domain.lease.Tenant;
import com.dgcdevelopment.domain.lease.TenantRepository;

@RestController
@CrossOrigin
public class TenantController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TenantRepository tenantRepo;

	@Autowired
	private DocumentRepository documentRepo;
	
	@Autowired
	private LeaseRepository leaseRepo;

	@GetMapping("/api/tenant")
	public Iterable<Tenant> getTenants(HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info("Retrieving all tenants...");
		Iterable<Tenant> tenants = tenantRepo.findByUserOrderByEidAsc((User) request.getAttribute("user"));
		log.info("Retrieve all tenants completed");
		return tenants;
	}

	// TODO Make search accent insensitive
	@GetMapping("/api/tenant/search/{q}")
	public Iterable<Tenant> getTenants(@PathVariable String q, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		log.info("Retrieving tenant for query: " + q);
		User u = (User) request.getAttribute("user");
		Set<Tenant> tenants = tenantRepo.findByUserAndFirstNameContainingIgnoreCase(u, q);
		tenants.addAll(tenantRepo.findByUserAndLastNameContainingIgnoreCase(u, q));
		tenants.addAll(tenantRepo.findByUserAndTelephonesStrContainingIgnoreCase(u, q));
		log.info("Tenants search completed");
		return tenants;
	}

	@GetMapping("/api/tenant/{eid}")
	public Tenant getTenant(@PathVariable Long eid, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		log.info("Retrieving one tenant: " + eid);
		Tenant tenant = tenantRepo.findOneByUserAndEid((User) request.getAttribute("user"), eid);

		// Not found
		if (tenant == null) {
			log.info("Tenant not found: " + eid);
			throw new MissingEntityException("Can't find tenant for eid: " + eid);
		}
		log.info("Retrieve tenant completed");

		return tenant;
	}

	@PostMapping("/api/tenant")
	public Tenant saveTenant(HttpServletRequest request, @RequestBody Tenant tenant) throws Exception {
		log.info("Saving Tenant : " + tenant.getFirstName() + " " + tenant.getLastName());
		tenant.setUser((User) request.getAttribute("user"));

		// Grab the document and put them in
		if (tenant.getEid() != null) {
			tenant.setDocuments(tenantRepo.findOne(tenant.getEid()).getDocuments());
		}

		// Save the tenant
		Tenant t = tenantRepo.save(tenant);
		log.info("Save tenant completed");
		return t;
	}

	@GetMapping("/api/tenant/addDocument/{peid}/{deid}")
	public Tenant updateTenantDoc(HttpServletRequest request, @PathVariable Long deid, @PathVariable Long peid)
			throws Exception {
		log.info("Attaching doc: " + deid + " to tenant: " + peid + "...");
		Tenant p = tenantRepo.findOneByUserAndEid((User) request.getAttribute("user"), peid);
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
				tenantRepo.save(p);
			}
		}
		log.info("Doc: " + deid + " attached to tenant: " + peid + "...");
		return p;
	}

	@GetMapping("/api/tenant/deleteDocument/{peid}/{deid}")
	public Tenant deleteTenantDoc(HttpServletRequest request, @PathVariable Long deid, @PathVariable Long peid)
			throws Exception {
		log.info("Deleting doc: " + deid + " from tenant: " + peid + "...");
		Tenant p = tenantRepo.findOneByUserAndEid((User) request.getAttribute("user"), peid);
		boolean found = false;
		for (Document doc : p.getDocuments()) {
			if (doc.getEid().equals(deid)) {
				p.getDocuments().remove(doc);
				tenantRepo.save(p);
				found = true;
			}
		}
		if (found) {
			documentRepo.deleteByEidAndUser(deid, (User) request.getAttribute("user"));
		}
		log.info("Doc: " + deid + " deleted from tenant: " + peid + "...");
		return p;
	}

	@DeleteMapping("/api/tenant/{eid}")
	@Transactional
	public User deleteOneTenant(HttpServletRequest request, @PathVariable("eid") long eid) throws Exception {
		User u = (User) request.getAttribute("user");
		log.info("Deleting tenant eid: " + eid + " User: " + u.getUsername());
		
		// check if there is a lease with this tenant
		List<Lease> leases = leaseRepo.findByTenantsEidAndUser(eid, u);
		if (!leases.isEmpty()) {
			log.error("Can't delete a tenant if it has a lease attached to it.");
			throw new DataConflictException("Can't delete a tenant if it has a lease attached to it.");
		}
		
		tenantRepo.deleteByEidAndUser(eid, u);
		log.info("Tenant " + eid + " for user " + u.getUsername() + " is  deleted");
		return u;
	}
	
	//
	// @GetMapping("/api/tenant/midPoint")
	// public double[] getMidPoint(HttpServletRequest request) throws Exception
	// {
	// log.info("Retrieving properties to calculate midpoint");
	// Iterable<Tenant> properties =
	// tenantRepo.findByUserOrderByEidAsc((User)
	// request.getAttribute("user"));
	// double latSum = 0;
	// double latMin = 0;
	// double latMax = 0;
	// double longMin = 0;
	// double longMax = 0;
	// double longSum = 0;
	// double count = 0;
	// // Not found
	// for (Tenant p : properties) {
	// latSum += p.getLatitude();
	// longSum += p.getLongitude();
	// if (p.getLatitude() > latMax) {
	// latMax = p.getLatitude();
	// }
	// if (p.getLatitude() < latMin) {
	// latMin = p.getLatitude();
	// }
	// if (p.getLongitude() > longMax) {
	// longMax = p.getLongitude();
	// }
	// if (p.getLatitude() < longMin) {
	// longMin = p.getLongitude();
	// }
	// count++;
	// }
	// log.info("Retrieve tenant completed");
	// double[] longLat = new double[3];
	// if (count > 0) {
	// longLat[0] = longSum / count;
	// longLat[1] = latSum / count;
	// } else {
	// longLat[1] = 64.7511111;
	// longLat[0] = -147.3494444;
	// }
	// // todo calculate zoom based on long lat;
	// longLat[2] = 9;
	// return longLat;
	//
	// }
}

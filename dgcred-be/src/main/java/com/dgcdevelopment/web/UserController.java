package com.dgcdevelopment.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.UserRepository;

@RestController
@CrossOrigin
public class UserController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepo;

//	@GetMapping("/api/users")
//	public Iterable<Tenant> getTenants(HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//		log.info("Retrieving all tenants...");
//		Iterable<Tenant> tenants = tenantRepo.findByUserOrderByEidAsc((User) request.getAttribute("user"));
//		log.info("Retrieve all tenants completed");
//		return tenants;
//	}

//	// TODO Make search accent insensitive
//	@GetMapping("/api/tenant/search/{q}")
//	public Iterable<Tenant> getTenants(@PathVariable String q, HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//
//		log.info("Retrieving tenant for query: " + q);
//		User u = (User) request.getAttribute("user");
//		Set<Tenant> tenants = tenantRepo.findByUserAndFirstNameContainingIgnoreCase(u, q);
//		tenants.addAll(tenantRepo.findByUserAndLastNameContainingIgnoreCase(u, q));
//		tenants.addAll(tenantRepo.findByUserAndTelephonesStrContainingIgnoreCase(u, q));
//		log.info("Tenants search completed");
//		return tenants;
//	}

//	@GetMapping("/api/tenant/{eid}")
//	public Tenant getTenant(@PathVariable Long eid, HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//
//		log.info("Retrieving one tenant: " + eid);
//		Tenant tenant = tenantRepo.findOneByUserAndEid((User) request.getAttribute("user"), eid);
//
//		// Not found
//		if (tenant == null) {
//			log.info("Tenant not found: " + eid);
//			throw new MissingEntityException("Can't find tenant for eid: " + eid);
//		}
//		log.info("Retrieve tenant completed");
//
//		return tenant;
//	}

//	@PostMapping("/api/tenant")
//	public Tenant saveTenant(HttpServletRequest request, @RequestBody Tenant tenant) throws Exception {
//		log.info("Saving Tenant : " + tenant.getFirstName() + " " + tenant.getLastName());
//		tenant.setUser((User) request.getAttribute("user"));
//
//		// Grab the document and put them in
//		if (tenant.getEid() != null) {
//			tenant.setDocuments(tenantRepo.findOne(tenant.getEid()).getDocuments());
//		}
//
//		// Save the tenant
//		Tenant t = tenantRepo.save(tenant);
//		log.info("Save tenant completed");
//		return t;
//	}

	@PostMapping("/api/users/preferences/{eid}")
	public User updateUserDoc(HttpServletRequest request, @PathVariable Long eid, @RequestBody User u)
			throws Exception {
		User user = userRepo.findOne(eid);
		if (user == null) {
			log.error("Trying to update user preference for user that doesn't exists");
		} else {
			user.setPreferences(u.getPreferences());
			userRepo.save(user);
		}
		log.info("User preferences updated for user: " + user.getUsername());
		return user;
	}

//	@GetMapping("/api/tenant/deleteDocument/{peid}/{deid}")
//	public Tenant deleteTenantDoc(HttpServletRequest request, @PathVariable Long deid, @PathVariable Long peid)
//			throws Exception {
//		log.info("Deleting doc: " + deid + " from tenant: " + peid + "...");
//		Tenant p = tenantRepo.findOneByUserAndEid((User) request.getAttribute("user"), peid);
//		boolean found = false;
//		for (Document doc : p.getDocuments()) {
//			if (doc.getEid().equals(deid)) {
//				p.getDocuments().remove(doc);
//				tenantRepo.save(p);
//				found = true;
//			}
//		}
//		if (found) {
//			documentRepo.deleteByEidAndUser(deid, (User) request.getAttribute("user"));
//		}
//		log.info("Doc: " + deid + " deleted from tenant: " + peid + "...");
//		return p;
//	}
//
//	@DeleteMapping("/api/tenant/{eid}")
//	@Transactional
//	public User deleteOneTenant(HttpServletRequest request, @PathVariable("eid") long eid) throws Exception {
//		User u = (User) request.getAttribute("user");
//		log.info("Deleting tenant eid: " + eid + " User: " + u.getUsername());
//		
//		// check if there is a lease with this tenant
//		List<Lease> leases = leaseRepo.findByTenantsEidAndUser(eid, u);
//		if (!leases.isEmpty()) {
//			log.error("Can't delete a tenant if it has a lease attached to it.");
//			throw new DataConflictException("Can't delete a tenant if it has a lease attached to it.");
//		}
//		
//		tenantRepo.deleteByEidAndUser(eid, u);
//		log.info("Tenant " + eid + " for user " + u.getUsername() + " is  deleted");
//		return u;
//	}
	
}

package com.dgcdevelopment.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dgcdevelopment.domain.Document;
import com.dgcdevelopment.domain.DocumentRepository;
import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.exceptions.DataConflictException;
import com.dgcdevelopment.domain.exceptions.MissingEntityException;
import com.dgcdevelopment.domain.financing.Loan;
import com.dgcdevelopment.domain.financing.LoanRepository;
import com.dgcdevelopment.domain.property.IGeoLocator;
import com.dgcdevelopment.domain.property.Property;
import com.dgcdevelopment.domain.property.PropertyRepository;
import com.dgcdevelopment.domain.property.Unit;

@RestController
@CrossOrigin
public class PropertyController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PropertyRepository propertyRepo;

	@Autowired
	private DocumentRepository documentRepo;

	@Autowired
	private LoanRepository loanRepo;

	@Autowired
	private IGeoLocator geoLocator;

	@GetMapping("/api/property")
	public Iterable<Property> getProperties(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Thread.sleep(1000);
		log.info("Retrieving all buildings...");
		Iterable<Property> propeties = propertyRepo.findByUserOrderByEidAsc((User) request.getAttribute("user"));
		// HATEOAS??
		// for (Property property : propeties) {
		// property.add(ControllerLinkBuilder.linkTo(PropertyController.class,
		// PropertyController.class.getMethod("getProperty",
		// Long.class, HttpServletRequest.class,
		// HttpServletResponse.class), property.getEid(), request,
		// response).withSelfRel());
		// }
		log.info("Retrieve all buildings completed");

		return propeties;
	}

	@GetMapping("/api/property/{eid}")
	public Property getProperty(@PathVariable Long eid, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		log.info("Retrieving one property: " + eid);
		Property property = propertyRepo.findOneByUserAndEid((User) request.getAttribute("user"), eid);
		// HATEOAS??
		// property.add(ControllerLinkBuilder.linkTo(PropertyController.class,
		// PropertyController.class.getMethod("getProperty",
		// Long.class, HttpServletRequest.class,
		// HttpServletResponse.class), property.getEid(), request,
		// response).withSelfRel());

		// Not found
		if (property == null) {
			log.info("Property not found: " + eid);
			throw new MissingEntityException("Can't find property for eid: " + eid);
		}
		log.info("Retrieve property completed");

		return property;
	}

	@PostMapping("/api/property")
	public Property saveProperty(HttpServletRequest request, @RequestBody Property property) throws Exception {
		log.info("Saving building...");
		property.setUser((User) request.getAttribute("user"));

		// Grab the document and put them in
		property.getUnits().toArray(new Unit[0])[0].getEid();
		if (property.getEid() != null && property.getEid() > 0) {
			System.out.println(property.getEid());
			Property tmpP = propertyRepo.findOne(property.getEid());
			if (tmpP != null) {
				property.setDocuments(tmpP.getDocuments());
			}
		}

		// Grab the long/lat
		double[] longLat = geoLocator.getLongLat(property.getAddress());
		property.setLongitude(longLat[0]);
		property.setLatitude(longLat[1]);

		// Save the property
		Property b = propertyRepo.save(property);
		log.info("Save building completed");
		return b;
	}

	@GetMapping("/api/property/addDocument/{peid}/{deid}")
	public Property updatePropertyDoc(HttpServletRequest request, @PathVariable Long deid, @PathVariable Long peid)
			throws Exception {
		log.info("Attaching doc: " + deid + " to property: " + peid + "...");
		Property p = propertyRepo.findOneByUserAndEid((User) request.getAttribute("user"), peid);
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
				propertyRepo.save(p);
			}
		}
		log.info("Doc: " + deid + " attached to property: " + peid + "...");
		return p;
	}

	@GetMapping("/api/property/deleteDocument/{peid}/{deid}")
	public Property deletePropertyDoc(HttpServletRequest request, @PathVariable Long deid, @PathVariable Long peid)
			throws Exception {
		log.info("Deleting doc: " + deid + " from property: " + peid + "...");
		Property p = propertyRepo.findOneByUserAndEid((User) request.getAttribute("user"), peid);
		boolean found = false;
		for (Document doc : p.getDocuments()) {
			if (doc.getEid().equals(deid)) {
				p.getDocuments().remove(doc);
				propertyRepo.save(p);
				found = true;
			}
		}
		if (found) {
			documentRepo.deleteByEidAndUser(deid, (User) request.getAttribute("user"));
		}
		log.info("Doc: " + deid + " deleted from property: " + peid + "...");
		return p;
	}

	@PutMapping("/api/property/put")
	public Property putProperty(HttpServletRequest request) throws Exception {

		// TODO
		Property b = new Property();
		b.setName("asdf");
		return b;
	}

	@RequestMapping(value = "/api/property/range/{start}/{count}")
	public Iterable<Property> getRangeOfProperties(HttpServletRequest request, @PathVariable("start") int start,
			@PathVariable("count") int count) throws Exception {

		PageRequest pageRequest = new PageRequest(start, count);
		Iterable<Property> properties = propertyRepo.findByUserOrderByEidAsc((User) request.getAttribute("user"),
				pageRequest);

		return properties;

		// return buildings;
	}

	@DeleteMapping("/api/property/{eid}")
	@Transactional
	public User deleteOneProperty(HttpServletRequest request, @PathVariable("eid") long eid) throws Exception {
		User u = (User) request.getAttribute("user");
		log.info("Deleting building eid: " + eid + " User: " + u.getUsername());

		// Find if property is attached to a loan
		List<Loan> loans = loanRepo.findByPropertyEidAndUser(eid, u);
		if (loans.size() > 0) {
			throw new DataConflictException("Can't delete a property that has loan attached to it. "
					+ " If you want to keep the loans, update them to remove the property link "
					+ " or delete the loans first.");
		}
		propertyRepo.deleteByEidAndUser(eid, u);

		log.info("Building " + eid + " for user " + u.getUsername() + " is deleted");
		return u;

	}

	@GetMapping("/api/property/midPoint")
	public Map<String, Double> getMidPoint(HttpServletRequest request) throws Exception {
		return getMidPoint(0, request);
	}

	@GetMapping("/api/property/midPoint/{pixelWidth}")
	public Map<String, Double> getMidPoint(@PathVariable int pixelWidth, HttpServletRequest request) throws Exception {
		log.info("Retrieving properties to calculate midpoint");
		Iterable<Property> properties = propertyRepo.findByUserOrderByEidAsc((User) request.getAttribute("user"));
		double latSum = 0;
		double latMin = 0;
		double latMax = 0;
		double longMin = 0;
		double longMax = 0;
		double longSum = 0;
		double count = 0;
		// Not found
		for (Property p : properties) {
			latSum += p.getLatitude();
			longSum += p.getLongitude();
			if (p.getLatitude() > latMax) {
				latMax = p.getLatitude();
			}
			if (p.getLatitude() < latMin) {
				latMin = p.getLatitude();
			}
			if (p.getLongitude() > longMax) {
				longMax = p.getLongitude();
			}
			if (p.getLatitude() < longMin) {
				longMin = p.getLongitude();
			}
			count++;
		}
		log.info("Retrieve property completed");
		Map<String, Double> mapMarker = new HashMap<>();

		if (count > 0) {
			mapMarker.put("lng", longSum / count);
			mapMarker.put("lat", latSum / count);
			// longLat[0] = longSum / count;
			// longLat[1] = latSum / count;
		} else {
			mapMarker.put("lng", 64.7511111);
			mapMarker.put("lat", -147.3494444);
			// longLat[1] = 64.7511111;
			// longLat[0] = -147.3494444;
		}
		// TODO calculate zoom based on long lat;
		if (pixelWidth == 0) {
			mapMarker.put("zoom", new Double(9));
			// longLat[2] = 9;
		} else {
			mapMarker.put("zoom", new Double(getZoomLevel(longMax, longMin, pixelWidth)));
			// longLat[2] = getZoomLevel(longMax, longMin, pixelWidth);
			System.out.println(getZoomLevel(longMax, longMin, pixelWidth));
		}
		return mapMarker;
	}

	public int getZoomLevel(double west, double east, int pixelWidth) {
		int GLOBE_WIDTH = 256;
		double angle = east - west;
		if (angle < 0) {
			angle += 360;
		}
		long zoom = Math.round(Math.log(pixelWidth * 360 / angle / GLOBE_WIDTH) / 0.6931471805599453);
		return (int) zoom;
	}
}

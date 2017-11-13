package com.dgcdevelopment.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dgcdevelopment.domain.AreaUnits;
import com.dgcdevelopment.domain.Lookup;
import com.dgcdevelopment.domain.RentPeriod;
import com.dgcdevelopment.domain.financing.FinancialInstitution;
import com.dgcdevelopment.domain.financing.FinancialInstitutionRepository;

@RestController
@CrossOrigin
public class LookupController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FinancialInstitutionRepository fir;

	@GetMapping("/api/lookup/country")
	public List<Lookup> getCountry(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// To do get this form DB
		List<Lookup> countries = new ArrayList<>();
		countries.add(new Lookup("CA", "Canada"));
		countries.add(new Lookup("US", "United States"));
		return countries;
	}

	@GetMapping("/api/lookup/provinces")
	public List<Lookup> getProvinces(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// To do get this form DB
		List<Lookup> provinces = new ArrayList<>();
		provinces.add(new Lookup("QC", "Quebec"));
		provinces.add(new Lookup("ON", "Ontario"));
		provinces.add(new Lookup("AL", "Alberta"));
		provinces.add(new Lookup("BC", "British Columbia"));
		provinces.add(new Lookup("NT", "Newfoundland"));
		provinces.add(new Lookup("OT", "..."));
		return provinces;
	}

	@GetMapping("/api/lookup/propertyTypes")
	public List<Lookup> getPropertyTypes(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// To do get this form DB
		List<Lookup> provinces = new ArrayList<>();
		provinces.add(new Lookup("1", "House"));
		provinces.add(new Lookup("2", "Condo"));
		provinces.add(new Lookup("3", "Plex"));
		return provinces;
	}
	
	@GetMapping("/api/lookup/paymentFrequency")
	public List<Lookup> getPaymentFrequency(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// To do get this form DB
		List<Lookup> paymentFrequency = new ArrayList<>();
		paymentFrequency.add(new Lookup("1", "Monthly"));
		paymentFrequency.add(new Lookup("2", "Weekly"));
		paymentFrequency.add(new Lookup("3", "Bi-weekly"));
		return paymentFrequency;
	}
	
	
	@GetMapping("/api/lookup/financialInstitutions")
	public Iterable<FinancialInstitution> getFinancialInstitutions(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return fir.findAll();
	}	
	
	@GetMapping("/api/lookups")
	public Map<String, Iterable> getLookups(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("Retreiving all lookups");
		Map<String, Iterable> lookups = new HashMap<>();
		lookups.put("provinces", getProvinces(request, response));
		lookups.put("countries", getCountry(request, response));
		lookups.put("areaUnits", getAreaUnits(request, response));
		lookups.put("rentPeriods", getRentPeriodss(request, response));
		lookups.put("propertyTypes", getPropertyTypes(request, response));
		lookups.put("paymentFrequencies", getPaymentFrequency(request, response));
		lookups.put("financialInstitutions", getFinancialInstitutions(request, response));
		return lookups;
	}

	@GetMapping("/api/lookup/areaUnits")
	public List<Lookup> getAreaUnits(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// To do get this form DB
		List<Lookup> as = new ArrayList<>();
		for (AreaUnits a : AreaUnits.values()) {
			as.add(new Lookup(a.getValue(), a.getKey()));
		}
		return as;
	}

	@GetMapping("/api/lookup/durationUnits")
	public List<Lookup> getRentPeriodss(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// To do get this form DB
		List<Lookup> as = new ArrayList<>();
		for (RentPeriod d : RentPeriod.values()) {
			as.add(new Lookup(d.getValue(), d.getKey()));
		}
		return as;
	}
}

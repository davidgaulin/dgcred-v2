package com.dgcdevelopment.domain;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.dgcdevelopment.domain.lease.Tenant;

public class TenantTest {

	Tenant t;
	
	@Before
	public void setup() throws Exception {
		t = new Tenant();
		t.setBirthday(new Date());
		t.setEmail("a@a.com");
		t.setFirstName("David");
		t.setLastName("Gaulin");
		Telephone p = new Telephone();
		p.setAreaCode("819");
		p.setCountryCode("1");
		p.setNumber("639-0999");
	}
	
	@Test
	public void testTest() {
		assertTrue(true);
	}
}

package com.dgcdevelopment.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
   AuthenticationControllerTest.class,
   DocumentControllerTest.class,
   PropertyControllerTest.class,
   LoanControllerTest.class,
   TenantControllerTest.class
})

public class WebControllerTestSuite {

}

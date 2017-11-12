package com.dgcdevelopment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.dgcdevelopment.domain.DomainTestSuite;
import com.dgcdevelopment.web.WebControllerTestSuite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
   WebControllerTestSuite.class,
   DomainTestSuite.class
})

public class AllTest {

}

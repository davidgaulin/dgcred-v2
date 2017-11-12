package com.dgcdevelopment.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class ExposeIdConfiguration extends RepositoryRestConfigurerAdapter {

//		@Override
//		public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
//			config.exposeIdsFor(Property.class, Document.class);
//		}
}

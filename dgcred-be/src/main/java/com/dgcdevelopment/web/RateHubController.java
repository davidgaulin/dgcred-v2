package com.dgcdevelopment.web;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@RestController
@CrossOrigin
public class RateHubController {
	
	
	private final Logger log = LoggerFactory.getLogger(this.getClass()); 
	
	
	@GetMapping("/api/rates/fixed/{years}/years")
    public String getRates(HttpServletRequest request, HttpServletResponse response, @PathVariable int years) throws Exception {
		
		log.info("Retrieving all rates...");
		
        URL url = new URL("https://www.ratehub.ca/best-mortgage-rates/" + years + "-year/fixed?callback=call&operation=getAllRatesTableData&_=" + new Date().getTime());
        URLConnection hc = url.openConnection();
        hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

        String rates = IOUtils.toString(hc.getInputStream());
        
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(rates.replaceAll("^call\\(", "").replaceAll("\\)$", "")); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object. 
		
		log.info("Retrieved all rates");
		
		return rootobj.toString();
    }
}

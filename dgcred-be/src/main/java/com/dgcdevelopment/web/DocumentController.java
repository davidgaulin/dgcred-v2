package com.dgcdevelopment.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dgcdevelopment.domain.Document;
import com.dgcdevelopment.domain.DocumentRepository;
import com.dgcdevelopment.domain.User;
import com.dgcdevelopment.domain.exceptions.MissingEntityException;
import com.dgcdevelopment.domain.exceptions.UnauthenticatedException;

@RestController
@CrossOrigin
public class DocumentController {
	
	@Autowired
	private DocumentRepository docRepo;
	
	@PostMapping("/api/document")
	public Document handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
		
		Document doc = new Document();
		doc.setContentType(file.getContentType());
		doc.setFileName(file.getOriginalFilename());
		doc.setUser((User) request.getAttribute("user"));
		doc.setContent(file.getBytes());
		doc = docRepo.save(doc);
		
		Document toReturnDoc = new Document();
		toReturnDoc.setContentType(doc.getContentType());
		toReturnDoc.setFileName(doc.getFileName());
		toReturnDoc.setEid(doc.getEid());
		
		return toReturnDoc;
		
	}

	@GetMapping("/api/document/{eid}")
	public ResponseEntity<byte[]> handleFileUpload(@PathVariable long eid, HttpServletRequest request)
		throws Exception {
		User u = (User) request.getAttribute("user");
		Document doc = docRepo.findOneByUserAndEid(u, eid);
		if (doc == null) {
			// should never get there and be filtered by jwt filter but ....
			if (u == null) {
				throw new UnauthenticatedException("Can't find user information on request");
			} else {
				throw new MissingEntityException("Document id: " 
						+ eid + " not found for user " + u.getUsername());
			}
		}
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType(doc.getContentType()));
	    String filename = doc.getFileName();
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		headers.setContentDispositionFormData(filename, filename);
	    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(doc.getContent(), headers, HttpStatus.OK);
	    return response;
	}
}

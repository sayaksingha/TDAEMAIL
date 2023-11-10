package com.tmt.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.tmt.evr.EventRecord;
import com.tmt.evr.EvrWriter;
import com.tmt.model.EmailRequest;
import com.tmt.service.EmailService;


@RestController
public class EmailController {
	
	@Autowired
	private EmailService serv;

	private static final Logger requestLogger = Logger.getLogger(EmailController.class);

	@PostMapping("/sendEmail") //   http://localhost:9090/sendEmail
	public ResponseEntity<Map<String, Object>> sendEmail(@RequestBody EmailRequest model) {
		
		EventRecord evr= new EventRecord();
		evr.setDate(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.println(model);
			if (serv.sendEmail(model)) {
				// log for success
				System.out.println("EmailController.sendEmail()");
				map.put("status", "success");
				map.put("statusCode", "0");
				map.put("errorCode", "0");
				
				try {
					
					evr.setEvrEvent(evr.EVR_EVENT_SUCCESS);
					evr.setEvrCause(evr.EVR_CAS_SUCCESS);
					evr.setEvrReason(evr.EVR_RSN_SUCCESS);
					evr.setEmailRequest(model);
					evr.setResponse("true");
					System.out.println("success => "+evr);
					EvrWriter.WriteEvr(evr.toEvrString());
					
				} catch (Exception e) {
					e.getMessage();
				}

			} else {
				
				map.put("status", "failure");
				map.put("statusCode", "1");
				map.put("errorCode", "");
				map.put("description", "no valid email address for To & Bcc");
				
				requestLogger.debug("Email Fail");
				
				evr.setEvrEvent(evr.EVR_EVENT_FAILURE);
				evr.setEvrCause(evr.EVR_CAS_FAILURE);
				evr.setEvrReason(evr.EVR_RSN_FAILED);
				evr.setEmailRequest(model);
				evr.setResponse("false");
				System.out.println("faliure =>"+evr);
				EvrWriter.WriteEvr(evr.toEvrString());
				return ResponseEntity.unprocessableEntity().body(map);
				
			}
		} catch (Exception e) {
			
			System.err.println("69");
			map.put("status", "failure");
			map.put("statusCode", "1");
			map.put("errorCode", e.hashCode());
			map.put("description", e.getMessage());
			
			evr.setEvrEvent(evr.EVR_EVENT_FAILURE);
			evr.setEvrCause(evr.EVR_CAS_FAILURE);
			evr.setEvrReason(evr.EVR_RSN_FAILED);
			evr.setEmailRequest(model);
			evr.setResponse("false");
			EvrWriter.WriteEvr(evr.toEvrString());
			
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(map);

		}
		return ResponseEntity.ok().body(map);
	}

}


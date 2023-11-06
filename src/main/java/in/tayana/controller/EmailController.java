package in.tayana.controller;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.tayana.evr.EventRecord;
import in.tayana.model.EmailRequest;
import in.tayana.service.EmailServices;

@RestController
public class EmailController {
	
	@Autowired
	private EmailServices services;

	private static final Logger requestLogger = LogManager.getLogger(EmailServices.class);

	@PostMapping("/sendEmail")
	public ResponseEntity<Map<String, Object>> sendEmail(@RequestBody EmailRequest model) {
		
		EventRecord evr= new EventRecord();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			System.out.println(model);
			if (services.sendEmail(model)) {
				// log for success
				System.out.println("EmailController.sendEmail()");
				map.put("status", "success");
				map.put("statusCode", "0");
				map.put("errorCode", "0");
				
				evr.setEvrEvent(evr.EVR_EVENT_SUCCESS);
				evr.setEvrCause(evr.EVR_CAS_SUCCESS);
				evr.setEvrReason(evr.EVR_RSN_SUCCESS);
				evr.setRequest(model.toString());
				evr.setResponse("true");
				System.out.println(evr);
			} else {
				map.put("status", "failure");
				map.put("statusCode", "1");
				map.put("errorCode", "");
				map.put("description", "no valid email address for To & Bcc");
				requestLogger.debug("Email Fail");
				
				evr.setEvrEvent(evr.EVR_EVENT_FAILURE);
				evr.setEvrCause(evr.EVR_CAS_FAILURE);
				evr.setEvrReason(evr.EVR_RSN_FAILED);
				evr.setRequest(model.toString());
				evr.setResponse("false");
				System.out.println(evr);
				return ResponseEntity.unprocessableEntity().body(map);
			}
		} catch (Exception e) {
			map.put("status", "failure");
			map.put("statusCode", "1");
			map.put("errorCode", e.hashCode());
			map.put("description", e.getMessage());
			return ResponseEntity.internalServerError().body(map);

		}
		return ResponseEntity.ok().body(map);
	}

}

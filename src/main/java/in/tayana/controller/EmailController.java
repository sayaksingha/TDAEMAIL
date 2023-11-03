package in.tayana.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.tayana.model.EmailRequest;
import in.tayana.service.EmailServices;

@RestController
public class EmailController {
	@Autowired
	EmailServices services;
	
	@PostMapping("/sendEmail")
	public ResponseEntity<Map<String, Object>> sendEmail(@RequestBody EmailRequest model) {
		Map<String, Object> map =new HashMap<String, Object>();
		try {
			System.out.println(model);
			if(services.sendEmail(model)) {
				//log for success
				System.out.println("EmailController.sendEmail()");
				map.put("status","success");
				map.put("statusCode","0");
				map.put("errorCode","0");
			}
			else {
				map.put("status","failure");
				map.put("statusCode","1");
				map.put("errorCode","");
				map.put("description", "no valid email address for To & Bcc");
				return ResponseEntity.internalServerError().body(map);
			}
		} catch (Exception e) {
			map.put("status","failure");
			map.put("statusCode","1");
			map.put("errorCode",e.hashCode());
			map.put("description", e.getMessage());
			return ResponseEntity.internalServerError().body(map);

		}
		return ResponseEntity.ok().body(map);
	}

}

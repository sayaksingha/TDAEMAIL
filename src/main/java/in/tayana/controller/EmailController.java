package in.tayana.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
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
	public RequestEntity<?> sendEmail(@RequestBody EmailRequest model) {
		try {
			System.out.println(model);
			if(services.sendEmail(model)) {
				//log for success
				System.out.println("EmailController.sendEmail()");
				//Response for success
			}
			else {
				System.out.println("Emai");
			}
		} catch (Exception e) {
			//log for failure
			//Response for failure
			e.printStackTrace();
		}
		return null;
	}

}

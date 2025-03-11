package com.example.demo2.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/view")
public class ViewController {

	@GetMapping("/emailsender")
	public ResponseEntity<String> showEmailSenderPage(@RequestParam String email, @RequestParam String token) {
		// Pass the email and token as model attributes if needed
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/member/uploadImage");
		return new ResponseEntity<String>(headers, HttpStatus.FOUND);
//		return new ResponseEntity<String>(headers, HttpStatus.FOUND);
//		return "emailsender";  // Spring will resolve this to /WEB-INF/jsp/emailsender.jsp
	}

}

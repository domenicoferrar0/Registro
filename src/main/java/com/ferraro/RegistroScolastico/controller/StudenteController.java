package com.ferraro.RegistroScolastico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.service.JwtService;
import com.ferraro.RegistroScolastico.service.StudenteService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "rest/api/v0/studente")
@Slf4j
public class StudenteController {

	@Autowired
	private StudenteService studenteService;
	
	@Autowired 
	private JwtService jwtService;

	@GetMapping("/summary")
	public ResponseEntity<?> studenteDashboard(@NonNull @RequestHeader("Authorization") String authorization){
		log.info("api summary studente {}", authorization);
		String token = authorization.substring(7);
		String email = jwtService.extractUsername(token);
		StudenteDTO studente = studenteService.findByEmail(email);
		return ResponseEntity.ok().body(studente);
	}

}

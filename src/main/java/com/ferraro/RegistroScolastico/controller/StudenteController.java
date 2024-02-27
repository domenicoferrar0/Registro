package com.ferraro.RegistroScolastico.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.dto.StudenteForm;
import com.ferraro.RegistroScolastico.entities.Role;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.service.RoleService;
import com.ferraro.RegistroScolastico.service.StudenteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "rest/api/v0")
public class StudenteController {
	
	@Autowired
	private StudenteService studenteService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PasswordEncoder encoder;
		
	
	@PostMapping(value = "/signin-studente")
	public ResponseEntity<?> saveStudente(@RequestBody @Valid StudenteForm form){
		if(form == null) {
			return ResponseEntity.badRequest().body("Richiesta formata in maniera inappropriata, il form non può essere null");
		}
		Studente studente = studenteService.formToStudente(form);
		
		Role studentRole = roleService.findByName("ROLE_STUDENT");
		//Impostando il ruolo di studente
		studente.getUser().setRoles(Collections.singleton(studentRole));
		//Cryptando la password inserita dal form
		String encodedPassword = encoder.encode(studente.getUser().getPassword());
		//Aggiornando la password con quella cryptata
		studente.getUser().setPassword(encodedPassword);
		StudenteDTO nuovoStudente;
		try {
			nuovoStudente = studenteService.saveStudente(studente);
		} catch(DataAccessException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore nel server, riprova più tardi");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(nuovoStudente);
	}
	
	@GetMapping(value = "/get-studenti")
	public ResponseEntity<List<StudenteDTO>> getAllStudenti(){
		return ResponseEntity.ok(studenteService.findAll());
	}
}

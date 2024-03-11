package com.ferraro.RegistroScolastico.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferraro.RegistroScolastico.dto.DocenteDTO;
import com.ferraro.RegistroScolastico.dto.LoginDTO;
import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.dto.RegistrationForm;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Role;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.service.DocenteService;
import com.ferraro.RegistroScolastico.service.JwtService;
import com.ferraro.RegistroScolastico.service.MailService;
import com.ferraro.RegistroScolastico.service.MyUserDetails;
import com.ferraro.RegistroScolastico.service.RoleService;
import com.ferraro.RegistroScolastico.service.StudenteService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/rest/api/v0/home")
@Slf4j
public class HomeController {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private StudenteService studenteService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private MyUserDetails userService;

	@Autowired
	private MailService mailService;

	@Autowired
	private DocenteService docenteService;
	
	

	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody @NonNull LoginDTO loginDTO) {
		System.out.println("82");
		UserDetails user = userService.loadUserByUsername(loginDTO.getEmail());
		try {
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

			log.info("authorities empty {} :", authentication.getAuthorities().isEmpty());

		}

		catch (BadCredentialsException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email o password non validi");
		}

		return ResponseEntity.ok(jwtService.generateToken(user));
	}

	@PostMapping(value = "/signin-studente")
	public ResponseEntity<?> saveStudente(@RequestBody @NonNull @Valid RegistrationForm form) {
		log.info("inside the api");
		String plainPassword = userService.generatePassword();
		// IL MAPPER HA CONTROLLI SULLE UNIQUE KEY
		Studente studente = studenteService.formToStudente(form);

		log.info("studente creato");
		Role studentRole = roleService.findByName("ROLE_STUDENT");
		// Impostando il ruolo di studente
		studente.getUser().setRoles(Collections.singleton(studentRole));
		// Cryptando la password inserita dal form
		log.info("ruolo impostato");
		String encodedPassword = encoder.encode(plainPassword);
		// Aggiornando la password con quella cryptata
		studente.getUser().setPassword(encodedPassword);
		StudenteDTO nuovoStudente;
		try {
			nuovoStudente = studenteService.saveStudente(studente);
			mailService.sendRegistrationEmail(form, plainPassword);
		} catch (Exception e) {
			log.error("Exception inside signin studente endpoint {}", studente, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Si è verificato un errore nel server, riprova più tardi");
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(nuovoStudente);
	}

	@PostMapping(value = "/signin-docente")
	public ResponseEntity<?> saveDocente(@RequestBody @NonNull @Valid RegistrationForm form) {
		log.info("inside the api");
		
		if(form.getMateria() == null) {
			return ResponseEntity.unprocessableEntity().body("Inserisci una materia valida");
		}
		
		String plainPassword = userService.generatePassword();
		// IL MAPPER HA CONTROLLI SULLE UNIQUE KEY
		Docente docente = docenteService.formToDocente(form);

		log.info("docente creato");
		Role docenteRole = roleService.findByName("ROLE_DOCENTE");
		// Impostando il ruolo di docente
		docente.getUser().setRoles(Collections.singleton(docenteRole));
		// Cryptando la password inserita dal form
		log.info("ruolo impostato");
		String encodedPassword = encoder.encode(plainPassword);
		// Aggiornando la password con quella cryptata
		docente.getUser().setPassword(encodedPassword);
		DocenteDTO nuovoDocente;
		try {
			nuovoDocente = docenteService.saveDocente(docente);
			mailService.sendRegistrationEmail(form, plainPassword);
		} catch (Exception e) {
			log.error("Exception inside signin docente endpoint {}", docente, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Si è verificato un errore nel server, riprova più tardi");
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(nuovoDocente);
	}

	

}
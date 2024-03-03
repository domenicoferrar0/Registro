package com.ferraro.RegistroScolastico.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferraro.RegistroScolastico.dto.LoginDTO;
import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.dto.StudenteForm;
import com.ferraro.RegistroScolastico.entities.Role;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.service.JwtService;
import com.ferraro.RegistroScolastico.service.RoleService;
import com.ferraro.RegistroScolastico.service.StudenteService;
import com.ferraro.RegistroScolastico.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "rest/api/v0")
public class StudenteController {
	
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
	private UserDetailsService userService;
		
	
	@PostMapping(value = "/signin-studente")
	public ResponseEntity<?> saveStudente(@RequestBody @NonNull @Valid StudenteForm form){
		System.out.println("ENTRATO NELL'API");
							//IL MAPPER HA CONTROLLI SULLE UNIQUE KEY
		Studente studente = studenteService.formToStudente(form);
		System.out.println("riga 56");
		Role studentRole = roleService.findByName("ROLE_STUDENT");
		//Impostando il ruolo di studente
		studente.getUser().setRoles(Collections.singleton(studentRole));
		//Cryptando la password inserita dal form
		System.out.println("riga 61");
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
	
	@GetMapping(value = "/admin/get-studenti")
	public ResponseEntity<List<StudenteDTO>> getAllStudenti(){
		return ResponseEntity.ok(studenteService.findAll());
	}
	
	@PostMapping(value = "/home/login")
	public ResponseEntity<?> loginStudente(@RequestBody @NonNull LoginDTO loginDTO){
		System.out.println("82");
		UserDetails user = userService.loadUserByUsername(loginDTO.getEmail());
		try {
		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
		
		System.out.println("AUTENTICAT"+authentication.getAuthorities().isEmpty());
		
		}
		
		catch(BadCredentialsException e ) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email o password non validi");
					}
		System.out.println("return");
		return ResponseEntity.ok(jwtService.generateToken(user));
	}
}

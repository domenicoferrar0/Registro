package com.ferraro.RegistroScolastico.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ferraro.RegistroScolastico.dto.DocenteDTO;
import com.ferraro.RegistroScolastico.dto.LoginDTO;
import com.ferraro.RegistroScolastico.dto.LoginResponse;
import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.dto.RegistrationForm;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Role;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.entities.User;
import com.ferraro.RegistroScolastico.service.DocenteService;
import com.ferraro.RegistroScolastico.service.JwtService;
import com.ferraro.RegistroScolastico.service.MailService;
import com.ferraro.RegistroScolastico.service.UserDetailsImpl;
import com.ferraro.RegistroScolastico.service.RoleService;
import com.ferraro.RegistroScolastico.service.StudenteService;

import jakarta.mail.MessagingException;
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
	private UserDetailsImpl userService;

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
		String jwt = jwtService.generateToken(user);
		log.info("JWT {}", jwt);
		return ResponseEntity.ok(LoginResponse.login(jwt, user.getAuthorities()));
	}

	@PostMapping(value = "/registration/studenti")
	public ResponseEntity<?> saveStudente(@RequestBody @NonNull @Valid RegistrationForm form)
			throws MessagingException {
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
		StudenteDTO nuovoStudente = studenteService.saveStudente(studente, form, plainPassword);
		return ResponseEntity.status(HttpStatus.CREATED).body(nuovoStudente);
	}

	@PostMapping(value = "/registration/docenti")
	public ResponseEntity<?> saveDocente(@RequestBody @NonNull @Valid RegistrationForm form) {
		log.info("inside the api");
		log.info("MATERIA {}", form.getMateria());
		if (form.getMateria() == null || form.getMateria().isEmpty()){
			String messaggio = "Inserisci una materia valida";
			return ResponseEntity.unprocessableEntity().body(Collections.singletonMap("materia", messaggio));
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
		DocenteDTO nuovoDocente = docenteService.saveDocente(docente, form, plainPassword);

		return ResponseEntity.status(HttpStatus.CREATED).body(nuovoDocente);
	}

	@PutMapping("/confirm")
	public ResponseEntity<String> confirmEmail(@RequestParam(value = "token", required = true) String token) {
		log.info("INSIDE API CONFIRM TOKEN {}", token);
		if (!mailService.confirmEmail(token)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Si è verificato un errore, non è stato possibile confermare la mail");
		}
		return ResponseEntity.ok("Congratulazione, la tua email è stata verificata");
	}

	/*
	@PutMapping("/confirm-all")
	public ResponseEntity<List<User>> confirmAll() {
		return ResponseEntity.ok(userService.findAll());

	}
	
	@PostMapping("/new-admin")
	public User newAdmin() {
		Role role = roleService.findByName("ROLE_ADMIN");
		Set<Role> roles = Collections.singleton(role);
		User user = new User();
		user.setRoles(roles);
		user.setEmail("admin@mail.com");
		user.setPassword(encoder.encode("admin"));
		user.setEnabled(true);
		return userService.saveUser(user);
	}
*/
}

package com.ferraro.RegistroScolastico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferraro.RegistroScolastico.dto.LoginDTO;
import com.ferraro.RegistroScolastico.service.JwtService;
import com.ferraro.RegistroScolastico.service.UserDetailsImpl;
import com.ferraro.RegistroScolastico.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest/api/v0/user")
public class UserController {
	
	@Autowired 
	private JwtService jwtService;
		
	
	@Autowired
	private UserService userService;
	
	@PutMapping("/password")
	public ResponseEntity<String> changePassword(@NonNull @RequestHeader("Authorization") String authorization,
			@NonNull @Valid @RequestBody LoginDTO loginDTO ){
		String token = authorization.substring(7);
		String email = jwtService.extractUsername(token);
		if(!userService.changePw(email, loginDTO)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non è stato possibile cambiare la password, controlla le tue credenziali");
		}
		return ResponseEntity.ok("Password cambiata correttamente");
		
	}
}

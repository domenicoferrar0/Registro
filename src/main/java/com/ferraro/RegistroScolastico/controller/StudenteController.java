package com.ferraro.RegistroScolastico.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferraro.RegistroScolastico.dto.AssenzaDTO;
import com.ferraro.RegistroScolastico.dto.ClasseDTOFull;
import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.dto.VotoDTO;
import com.ferraro.RegistroScolastico.entities.Studente;
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
	public ResponseEntity<StudenteDTO> studenteSummary(@NonNull @RequestHeader("Authorization") String authorization){
		log.info("api summary studente {}", authorization);
		
		//Estraggo lo studente attraverso il Token
		String token = authorization.substring(7);
		String email = jwtService.extractUsername(token);		
		StudenteDTO studente = studenteService.findDtoByEmail(email); //404 GESTITO
		return ResponseEntity.ok().body(studente);
	}
	
	@GetMapping("/classe")
	public ResponseEntity<ClasseDTOFull> studenteGetClasse(@NonNull @RequestHeader("Authorization") String authorization){
		String token = authorization.substring(7);
		String email = jwtService.extractUsername(token);		
		Studente studente = studenteService.findByEmail(email); //404 GESTITI
		ClasseDTOFull classe = studenteService.getClasse(studente);
		return ResponseEntity.ok(classe);
	}
	
	@GetMapping("/voti")
	public ResponseEntity<List<VotoDTO>> studenteGetVoti(@NonNull @RequestHeader("Authorization") String authorization){
		String token = authorization.substring(7);
		String email = jwtService.extractUsername(token);		
		Studente studente = studenteService.findByEmail(email);
		List<VotoDTO> voti = studenteService.getVoti(studente);
		return ResponseEntity.ok(voti);
	}
	
	@GetMapping("/assenze")
	public ResponseEntity<List<AssenzaDTO>> studenteGetAssenze(@NonNull @RequestHeader("Authorization") String authorization){
		String token = authorization.substring(7);
		String email = jwtService.extractUsername(token);		
		Studente studente = studenteService.findByEmail(email);
		List<AssenzaDTO> assenze = studenteService.getAssenze(studente);
		return ResponseEntity.ok(assenze);
	}

}

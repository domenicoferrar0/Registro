package com.ferraro.RegistroScolastico.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ferraro.RegistroScolastico.dto.AssenzaDTO;
import com.ferraro.RegistroScolastico.dto.ClasseDTOFull;
import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.dto.VotoDTO;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.service.AssenzaService;
import com.ferraro.RegistroScolastico.service.JwtService;
import com.ferraro.RegistroScolastico.service.StudenteService;
import com.ferraro.RegistroScolastico.service.VotoService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/rest/api/v0/studente")
@Slf4j
public class StudenteController {

	@Autowired
	private StudenteService studenteService;
	
	@Autowired 
	private JwtService jwtService;
	
	@Autowired 
	private AssenzaService assenzaService;
	
	@Autowired 
	private VotoService votoService;

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
	public ResponseEntity<Page<VotoDTO>> studenteGetVoti(@NonNull @RequestHeader("Authorization") String authorization,
			@RequestParam("page") int page, @RequestParam(value = "searchTerm", required = false) String searchTerm){
		int pageSize = 3;
		
		log.info("page {}", page);
		log.info("term {}", searchTerm);
		String token = authorization.substring(7);
		String email = jwtService.extractUsername(token);		
		Studente studente = studenteService.findByEmail(email);
				//dove page è la pagina corrente e 10 i max elements per pagina
		Page<VotoDTO> voti = votoService.getVotiStudente(studente, page-1, pageSize, searchTerm); 
		log.info("voti {}", voti.isEmpty());
		return ResponseEntity.ok(voti);
	}
	
	@GetMapping("/assenze")
	public ResponseEntity<Page<AssenzaDTO>> studenteGetAssenze(@NonNull @RequestHeader("Authorization") String authorization,
			@RequestParam("page") int page, @RequestParam(value ="startRange", required = false) LocalDate startRange){
		int pageSize = 3;
		String token = authorization.substring(7);
		String email = jwtService.extractUsername(token);		
		Studente studente = studenteService.findByEmail(email);
		Page<AssenzaDTO> assenze = assenzaService.getAssenzeStudente(studente, page-1, pageSize, startRange);
		
		return ResponseEntity.ok(assenze);
	}

}

package com.ferraro.RegistroScolastico.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ferraro.RegistroScolastico.dto.AssenzaDTO;
import com.ferraro.RegistroScolastico.dto.ClasseDTOFull;
import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.dto.VotoDTO;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.enums.Materia;
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
	
	/*NOTA:
	 *In questo caso il JWT è usato per verificare l'entità dello studente e fornirgli il contenuto
	 *a lui legato.
	 *La maggior parte delle fetch su un'entità specifica vengono gestite da eccezioni custom ed ExceptionHandler   */
	

	@GetMapping("/summary")
	public ResponseEntity<StudenteDTO> studenteSummary(@NonNull @RequestHeader("Authorization") String authorization){		
		String token = authorization.substring(7);
		String email = jwtService.extractUsername(token);		
		StudenteDTO studente = studenteService.findDtoByEmail(email);
		return ResponseEntity.ok().body(studente);
	}
	
	@GetMapping("/voti/media")
	public ResponseEntity<List<VotoDTO>> studentGetMediaVoti(@NonNull @RequestHeader("Authorization") String authorization){
				
		Studente studente = studenteService.extractStudente(authorization);
		return ResponseEntity.ok(studenteService.getMediaVoti(studente));
		
	}
	
	@GetMapping("/voti/media/{materia}")
	public ResponseEntity<VotoDTO> studentGetMediaMateria(@NonNull @RequestHeader("Authorization") String authorization,
			@PathVariable("materia") Materia materia){
		Studente studente = studenteService.extractStudente(authorization);
		return ResponseEntity.ok(studenteService.votoMedio(studente, materia));
	}
	
	@GetMapping("/classe")
	public ResponseEntity<ClasseDTOFull> studenteGetClasse(@NonNull @RequestHeader("Authorization") String authorization){
		log.info("AUTORIZZAZIONE {}",authorization);
		Studente studente = studenteService.extractStudente(authorization);
		ClasseDTOFull classe = studenteService.getClasse(studente);
		return ResponseEntity.ok(classe);
	}
	
	@GetMapping("/voti")
	public ResponseEntity<Page<VotoDTO>> studenteGetVoti(@NonNull @RequestHeader("Authorization") String authorization,
			@RequestParam("page") int page, @RequestParam(value = "searchTerm", required = false) String searchTerm){
		int pageSize = 3;
		Studente studente = studenteService.extractStudente(authorization);
		Page<VotoDTO> voti = votoService.getVotiStudente(studente, page-1, pageSize, searchTerm); 
		return ResponseEntity.ok(voti);
	}
	
	@GetMapping("/assenze")
	public ResponseEntity<Page<AssenzaDTO>> studenteGetAssenze(@NonNull @RequestHeader("Authorization") String authorization,
			@RequestParam("page") int page, @RequestParam(value ="startRange", required = false) LocalDate startRange){
		int pageSize = 3;
		Studente studente = studenteService.extractStudente(authorization);
		Page<AssenzaDTO> assenze = assenzaService.getAssenzeStudente(studente, page-1, pageSize, startRange);		
		return ResponseEntity.ok(assenze);
	}

}

package com.ferraro.RegistroScolastico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferraro.RegistroScolastico.dto.ApiResponse;
import com.ferraro.RegistroScolastico.dto.AssenzaDTO;
import com.ferraro.RegistroScolastico.dto.AssenzaRequest;
import com.ferraro.RegistroScolastico.dto.DocenteDTO;
import com.ferraro.RegistroScolastico.dto.VotoDTO;
import com.ferraro.RegistroScolastico.dto.VotoRequest;
import com.ferraro.RegistroScolastico.entities.Assenza;
import com.ferraro.RegistroScolastico.entities.Classe;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.entities.Voto;
import com.ferraro.RegistroScolastico.service.AssenzaService;
import com.ferraro.RegistroScolastico.service.ClasseService;
import com.ferraro.RegistroScolastico.service.DocenteService;
import com.ferraro.RegistroScolastico.service.JwtService;
import com.ferraro.RegistroScolastico.service.StudenteService;
import com.ferraro.RegistroScolastico.service.VotoService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/rest/api/v0/docente")
@Slf4j
public class DocenteController {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private DocenteService docenteService;

	@Autowired
	private StudenteService studenteService;

	@Autowired
	private VotoService votoService;

	@Autowired
	private AssenzaService assenzaService;
	
	@Autowired
	private ClasseService classeService;
	
	@GetMapping(value = "/summary")
	public ResponseEntity<?> docenteSummary(@NonNull @RequestHeader("Authorization") String authorization){
		String token = authorization.substring(7);
		String email = jwtService.extractUsername(token);
		DocenteDTO docente = docenteService.findDtoByEmail(email); //404
		return ResponseEntity.status(HttpStatus.FOUND).body(docente);
	}
	
	@GetMapping(value = "/classe/{classeId}")
	public ResponseEntity<?> docenteGetClasse(@NonNull @RequestHeader("Authorization") String authorization, 
			@PathVariable("classeId") Long classeId){
		String token = authorization.substring(7);		
		// risale al docente dal token
		String email = jwtService.extractUsername(token); 
		Docente docente = docenteService.findByEmail(email); //404		
		
		Classe classe = classeService.findById(classeId); //404
		if (!docente.getClassi().contains(classe)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.unauthorized("Questo docente non è autorizzato a visualizzare la classe "+classe.getNome(),
							docenteService.docenteToDto(docente) ));
		}
		 return ResponseEntity.ok(classeService.classeToDtoFull(classe));
	}

	@PostMapping(value = "/inserimento-voto")
	public ResponseEntity<?> assegnaVoto(@NonNull @RequestHeader("Authorization") String authorization,
			@RequestBody @NonNull @Valid VotoRequest request) {
		
		//Risaliamo a chi sta facendo la richiesta attraverso il token di autorizzazione
		String token = authorization.substring(7);
		log.info("inserimento voto {}", token);
		String email = jwtService.extractUsername(token);
		
		//I metodi di fetch sono gestiti da ExceptionHandler in caso di not found
		Docente docente = docenteService.findByEmail(email);
		Studente studente = studenteService.findByCf(request.getStudenteCF());
		
		//Exception gestita se lo studente non ha classe o se il docente non è un suo docente
		Voto voto = votoService.creaVoto(docente, studente, request);
		VotoDTO newVoto;
		try {
			newVoto = votoService.salvaVoto(voto);
		} catch (DataAccessException e) {
			log.error("Eccezione al salvataggio voto", e);
			return ResponseEntity.internalServerError().body("Si è verificato un errore, impossibile salvare il voto");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(newVoto);

	}

	@PostMapping(value = "/inserimento-assenza")
	public ResponseEntity<?> inserisciAssenza(@RequestBody @NonNull @Valid AssenzaRequest request) {
		log.info("inserimento assenza");
		
		Studente studente = studenteService.findByCf(request.getStudenteCF()); //404 Gestito
		AssenzaDTO nuovaAssenza;
		try {
			//IllegalArgument se lo studente non ha ancora una classe o se è già stata segnata un'assenza quel giorno
			Assenza assenza = assenzaService.creaAssenza(studente, request);
			nuovaAssenza = assenzaService.salvaAssenza(assenza);

		} catch (IllegalArgumentException e) {
			
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(e.getMessage());
		} catch (Exception e) {
			log.error("Eccezione generica salvataggio assenza ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Si è verificato un errore, riprova più tardi");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(nuovaAssenza);

	}

	@DeleteMapping(value = "/delete-voto/{id}")
	public ResponseEntity<?> deleteVoto(@NonNull @RequestHeader("Authorization") String authorization,
			@PathVariable("id") Long id) {

		Voto voto = votoService.findById(id); // 404 SE NON LO TROVA
		String token = authorization.substring(7);
		log.info("delete voto {}", token);
		
		// risale al docente dal token
		String email = jwtService.extractUsername(token); 
		Docente docente = docenteService.findByEmail(email);
		
		//Solo chi ha inserito il voto può rimuoverlo
		if (!voto.getDocente().equals(docente)) {
			log.error("docenti non combaciano {}", voto.getDocente().equals(docente));
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Spiacente, solo il docente che ha inserito il voto può eliminarlo");
		}
		//Booleano dal repository che verifica se l'operazione ha successo
		if (!votoService.deleteVoto(id)) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Non è stato possibile cancellare questo voto");
		}
		return ResponseEntity.ok().body("Voto eliminato correttamente");
	}
	
	@DeleteMapping(value = "/delete-assenza/{id}")
	public ResponseEntity<?> deleteAssenza(@PathVariable("id") Long id){
		//anche qui booleano dal repository
		System.out.println(id);
		if(!assenzaService.deleteAssenza(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Si è verificato un errore, l'assenza potrebbe non esistere");
		}
		return ResponseEntity.ok().body("Assenza eliminata correttamente");
		
	}
}

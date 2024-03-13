package com.ferraro.RegistroScolastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ferraro.RegistroScolastico.dto.AssenzaDTO;
import com.ferraro.RegistroScolastico.dto.ClasseDTO;
import com.ferraro.RegistroScolastico.dto.DocenteDTO;
import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.dto.VotoDTO;
import com.ferraro.RegistroScolastico.entities.Classe;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.service.AssenzaService;
import com.ferraro.RegistroScolastico.service.ClasseService;
import com.ferraro.RegistroScolastico.service.DocenteService;
import com.ferraro.RegistroScolastico.service.StudenteService;
import com.ferraro.RegistroScolastico.service.VotoService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "rest/api/v0/admin")
public class AdminController {

	@Autowired
	private StudenteService studenteService;

	@Autowired
	private ClasseService classeService;

	@Autowired
	private DocenteService docenteService;
	
	@Autowired
	private VotoService votoService;
	
	@Autowired
	private AssenzaService assenzaService;

	// I GET GENERALI SONO RISERVATI AGLI ADMIN
	@GetMapping(value = "/get-studenti")
	public ResponseEntity<List<StudenteDTO>> getAllStudenti() {
		return ResponseEntity.ok(studenteService.findAll());
	}

	@GetMapping(value = "/get-docenti")
	public ResponseEntity<List<DocenteDTO>> getAllDocenti() {
		return ResponseEntity.ok(docenteService.findAll());
	}

	@GetMapping(value = "/get-classi")
	public ResponseEntity<List<ClasseDTO>> getClasses() {
		return ResponseEntity.ok(classeService.findAll());
	}
	
	@GetMapping(value = "/get-voti")
	public ResponseEntity<List<VotoDTO>> getVoti(){
		return ResponseEntity.ok(votoService.findAll());
	}
	
	@GetMapping(value = "/get-assenze")
	public ResponseEntity<List<AssenzaDTO>> getAssenze(){
		return ResponseEntity.ok(assenzaService.findAll());
	}

	@PostMapping(value = "/save-classe")
	public ResponseEntity<?> saveClasse(@RequestBody @NonNull @Valid ClasseDTO classeDTO) {

		ClasseDTO newClasse;
		try {
			newClasse = classeService.saveClasse(classeDTO); // Controlla prima se esiste già
		} catch (DataAccessException e) {
			log.error("exception Salvataggio classe", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Si è verificato un errore nel server, prova più tardi");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(newClasse);
	}

	// Questo metodo è pensato solo per assegnare la classe a chi non ce l'ha
	// ancora, è da fare un metodo più esplicito in caso di cambio classe
	@PutMapping(value = "/studente-assign-classe")
	public ResponseEntity<?> assignClasseToStudente(@RequestParam(value = "classeId", required = true) Long id,
			@RequestParam(value = "studenteCF", required = true) String cf) {
		Studente studente = studenteService.findByCf(cf); // 404
		Classe classe = classeService.findById(id); // 404
		StudenteDTO studenteAggiornato;
		try { // Controlla se lo studente non ha già una classe, exception gestita
			studenteAggiornato = studenteService.assignClasse(studente, classe);
		} catch (DataAccessException e) {
			log.error("exception assegnazione classe studente", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Si è verificato un errore nel server, riprova più tardi");
		}
		return ResponseEntity.ok().body(studenteAggiornato);
	}

	@PutMapping(value = "/docente-assign-classe")
	public ResponseEntity<?> assignClasseToDocente(@RequestParam(value = "classeId", required = true) Long id,
			@RequestParam(value = "docenteCF", required = true) String cf) {
		Docente docente = docenteService.findByCf(cf);
		Classe classe = classeService.findById(id);
		DocenteDTO docenteAggiornato;
		// Funziona solo se quella materia non è già occupata o se quel docente non è
		// già presente nella classe
		try {
			docenteAggiornato = docenteService.assignClasse(docente, classe);
		}

		catch (DataAccessException e) {
			log.error("Exception assegnazione classe docente", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Si è verificato un errore nel server riprova più tardi");
		}
		return ResponseEntity.ok().body(docenteAggiornato);
	}

}

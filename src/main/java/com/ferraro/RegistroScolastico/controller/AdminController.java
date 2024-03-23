package com.ferraro.RegistroScolastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.ferraro.RegistroScolastico.entities.ConfirmationToken;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.service.AssenzaService;
import com.ferraro.RegistroScolastico.service.ClasseService;
import com.ferraro.RegistroScolastico.service.DocenteService;
import com.ferraro.RegistroScolastico.service.MailService;
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
	
	@Autowired
	private MailService mailService;

	// I GET GENERALI SONO RISERVATI AGLI ADMIN
	@GetMapping(value = "/studenti")
	public ResponseEntity<List<StudenteDTO>> getAllStudenti() {
		return ResponseEntity.ok(studenteService.findAll());
	}

	@GetMapping(value = "/docenti")
	public ResponseEntity<List<DocenteDTO>> getAllDocenti() {
		return ResponseEntity.ok(docenteService.findAll());
	}

	@GetMapping(value = "/classi")
	public ResponseEntity<List<ClasseDTO>> getClasses() {
		return ResponseEntity.ok(classeService.findAll());
	}

	@GetMapping(value = "/voti")
	public ResponseEntity<List<VotoDTO>> getVoti() {
		return ResponseEntity.ok(votoService.findAll());
	}

	@GetMapping(value = "/assenze")
	public ResponseEntity<List<AssenzaDTO>> getAssenze() {
		return ResponseEntity.ok(assenzaService.findAll());
	}
	
	@GetMapping(value = "/token")
	public ResponseEntity<List<ConfirmationToken>> getToken() {
		return ResponseEntity.ok(mailService.allToken());
	}

	@PostMapping(value = "/classe")
	public ResponseEntity<?> saveClasse(@RequestBody @NonNull @Valid ClasseDTO classeDTO) {
		ClasseDTO newClasse = classeService.saveClasse(classeDTO); // Controlla prima se esiste già
		return ResponseEntity.status(HttpStatus.CREATED).body(newClasse);
	}

	// Questo metodo è pensato solo per assegnare la classe a chi non ce l'ha
	// ancora, è da fare un metodo più esplicito in caso di cambio classe
	@PutMapping(value = "/studente/{studentId}/classe/{classeId}")
	public ResponseEntity<?> assignClasseToStudente(@PathVariable("classeId") Long classeId,
			@PathVariable("studentId") Long studentId) {
		Studente studente = studenteService.findById(studentId); 
		Classe classe = classeService.findById(classeId); 
		StudenteDTO studenteAggiornato = studenteService.assignClasse(studente, classe);
		return ResponseEntity.ok().body(studenteAggiornato);
	}

	@PutMapping(value = "/docente/{docenteId}/classe/{classeId}")
	public ResponseEntity<DocenteDTO> assignClasseToDocente(@PathVariable("classeId") Long classeId,
			@PathVariable("docenteId") Long docenteId) {
		Docente docente = docenteService.findById(docenteId);
		Classe classe = classeService.findById(classeId);
		DocenteDTO docenteAggiornato = docenteService.assignClasse(docente, classe);
		return ResponseEntity.ok().body(docenteAggiornato);
	}

	@DeleteMapping(value = "/docente/{docenteId}/classe/{classeId}")
	public ResponseEntity<DocenteDTO> removeClasseFromDocente(@PathVariable("classeId") Long classeId,
			@PathVariable("docenteId") Long docenteId) {
		Docente docente = docenteService.findById(docenteId);
		Classe classe = classeService.findById(classeId);
		DocenteDTO docenteAggiornato = docenteService.removeClasse(docente, classe);
		return ResponseEntity.ok().body(docenteAggiornato);
	}
	
	@DeleteMapping(value = "/studente/{studentId}/classe/{classeId}")
	public ResponseEntity<StudenteDTO> removeClasseFromStudente(@PathVariable("classeId") Long classeId,
			@PathVariable("studentId") Long studentId) {
		Studente studente = studenteService.findById(studentId); 
		Classe classe = classeService.findById(classeId);
		StudenteDTO studenteAggiornato = studenteService.removeClasse(studente, classe);
		return ResponseEntity.ok().body(studenteAggiornato);
	}
	

}

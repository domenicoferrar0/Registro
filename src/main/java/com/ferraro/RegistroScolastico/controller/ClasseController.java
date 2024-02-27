package com.ferraro.RegistroScolastico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ferraro.RegistroScolastico.dto.ClasseDTO;
import com.ferraro.RegistroScolastico.service.ClasseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "rest/api/v0")
public class ClasseController {

	@Autowired
	ClasseService classeService;

	@PostMapping(value = "/save-classe")
	public ResponseEntity<Object> saveClasse(@RequestBody @Valid ClasseDTO classeDTO) {
		if (classeDTO == null) {
			return ResponseEntity.unprocessableEntity()
					.body("Si è verificato un problema, esegui una richiesta in modo appropriato");
		}
		ClasseDTO newClasse;
		try {
			newClasse = classeService.saveClasse(classeDTO);
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Si è verificato un errore nel server, prova più tardi");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(newClasse);
	}

	@GetMapping(value = "/get-classi")
	public ResponseEntity<List<ClasseDTO>> getClasses() {
		return ResponseEntity.ok(classeService.findAll());
	}

	@GetMapping(value = "/get-classe")
	public ResponseEntity<Object> getClasse(@RequestParam(value = "anno") Integer anno,
			@RequestParam(value = "sezione") String sezione) {
		if (anno == null || sezione == null || anno < 1 || anno > 3 || sezione.isBlank()) {
			return ResponseEntity.unprocessableEntity().body("Inserisci i parametri in forma appropriata");
		}
		ClasseDTO classe = classeService.findClasse(anno, sezione);
		if (classe == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe non trovata: " + anno + sezione);
		}
		return ResponseEntity.ok(classe);
	}
}

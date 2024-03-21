package com.ferraro.RegistroScolastico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ferraro.RegistroScolastico.dto.ClasseDTO;
import com.ferraro.RegistroScolastico.service.ClasseService;

@RestController
@RequestMapping(value = "rest/api/v0")
public class ClasseController {

	@Autowired
	ClasseService classeService;


/*
	@GetMapping(value = "/classe")
	public ResponseEntity<Object> getClasse(@RequestParam(value = "anno", required = true) Integer anno,
			@RequestParam(value = "sezione", required = true) String sezione) {
		if (anno < 1 || anno > 3 || sezione.isBlank()) {
			return ResponseEntity.unprocessableEntity().body("Inserisci i parametri in forma appropriata");
		}
		ClasseDTO classe = classeService.findClasseDTO(anno, sezione);
		if (classe == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Classe non trovata: " + anno + sezione);
		}
		return ResponseEntity.ok(classe);
	} */
}

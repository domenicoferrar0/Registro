package com.ferraro.RegistroScolastico.controller;

import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ferraro.RegistroScolastico.dto.ApiResponse;
import com.ferraro.RegistroScolastico.dto.AssenzaDTO;
import com.ferraro.RegistroScolastico.dto.AssenzaRequest;
import com.ferraro.RegistroScolastico.dto.ClasseDTO;
import com.ferraro.RegistroScolastico.dto.DocenteDTO;
import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.dto.VotoDTO;
import com.ferraro.RegistroScolastico.dto.VotoRequest;
import com.ferraro.RegistroScolastico.entities.Assenza;
import com.ferraro.RegistroScolastico.entities.Classe;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Periodo;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.entities.Voto;
import com.ferraro.RegistroScolastico.enums.Materia;
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

	/*NOTA:
	 *In buona parte dei metodi risalgo al docente attraverso il JWT, per capire se ha effettivamente l'autorizzazione 
	 *per l'azione che sta compiendo ES: Solo un docente che appartiene alla classe di cui fa parte lo studente può
	 *inserire dei voti per quello studente, così come solo chi ha inserito il voto può eliminarlo o modificarlo.
	 *Buona parte delle situazioni sono gestite tramite il lancio di eccezioni custom gestite attraverso ExceptionHandler.  */
	
	@GetMapping("/summary")
	public ResponseEntity<DocenteDTO> docenteSummary(@NonNull @RequestHeader("Authorization") String authorization) {
		String token = authorization.substring(7);
		String email = jwtService.extractUsername(token);
		DocenteDTO docente = docenteService.findDtoByEmail(email); // 404
		return ResponseEntity.ok(docente);
	}

	@GetMapping("/classe/{classeId}")
	public ResponseEntity<?> docenteGetClasse(@NonNull @RequestHeader("Authorization") String authorization,
			@PathVariable("classeId") Long classeId) {
		
		Docente docente = docenteService.extractDocente(authorization);

		Classe classe = classeService.findById(classeId);
		if (!docente.getClassi().contains(classe)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.unauthorized(
							"Questo docente non è autorizzato a visualizzare la classe " + classe.getNome(),
							docenteService.docenteToDto(docente)));
		}
		return ResponseEntity.ok(classeService.classeToDtoFull(classe));
	}
	//Per popolare il form
	@GetMapping("/classe/{classeId}/studenti")
	public ResponseEntity<?> docenteGetForm(@NonNull @RequestHeader("Authorization") String authorization,
			@PathVariable("classeId") Long classeId) {
		Docente docente = docenteService.extractDocente(authorization);
		Classe classe = classeService.findById(classeId);
		if (!docente.getClassi().contains(classe)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.unauthorized(
							"Questo docente non è autorizzato a visualizzare la classe " + classe.getNome(),
							docenteService.docenteToDto(docente)));
		}
		return ResponseEntity.ok(classeService.classeToDtoNoDocenti(classe));
	}
	
	@GetMapping("/materie")
	public ResponseEntity<Set<Materia>> docenteGetMaterie(@NonNull @RequestHeader("Authorization") String authorization){
		Docente docente = docenteService.extractDocente(authorization);
		return ResponseEntity.ok(docente.getMaterie());
	}

	@GetMapping("studenti/{studenteId}")
	public ResponseEntity<StudenteDTO> docenteGetStudente(@NonNull @RequestHeader("Authorization") String authorization,
			@PathVariable("studenteId") Long id) {
		Docente docente = docenteService.extractDocente(authorization);
		Studente studente = studenteService.findById(id);
		StudenteDTO studenteDTO = studenteService.getStudenteForDocente(studente, docente);
		return ResponseEntity.ok(studenteDTO);
	}

	@GetMapping("/studenti/{studenteId}/voti")
	public ResponseEntity<List<VotoDTO>> docenteGetVotiStudente(
			@NonNull @RequestHeader("Authorization") String authorization, @PathVariable("studenteId") Long id) {
		Docente docente = docenteService.extractDocente(authorization);
		Studente studente = studenteService.findById(id);
		List<VotoDTO> voti = votoService.findVotiByDocente(studente, docente);
		return ResponseEntity.ok(voti);
	}

	@GetMapping("/classi")		
	public ResponseEntity<List<ClasseDTO>> docenteGetClassi(@NonNull @RequestHeader("Authorization") String authorization,
			@RequestParam(value = "startYear", required = false)Integer startYear) {
		Docente docente = docenteService.extractDocente(authorization);
		List<ClasseDTO> classi = docenteService.getClassi(startYear, docente);
		return ResponseEntity.ok(classi);
	}

	@PostMapping("/voto")
	public ResponseEntity<VotoDTO> inserisciVoto(@NonNull @RequestHeader("Authorization") String authorization,
			@RequestBody @NonNull @Valid VotoRequest request) {
		Docente docente = docenteService.extractDocente(authorization);
		Studente studente = studenteService.findById(request.getStudentId());
		Voto voto = votoService.creaVoto(docente, studente, request);
		VotoDTO newVoto = votoService.salvaVoto(voto);
		return ResponseEntity.status(HttpStatus.CREATED).body(newVoto);
	}

	@PostMapping("/assenza")
	public ResponseEntity<AssenzaDTO> inserisciAssenza(@RequestBody @NonNull @Valid AssenzaRequest request,
			@NonNull @RequestHeader("Authorization") String authorization) {
		log.info("inserimento assenza");
		Docente docente = docenteService.extractDocente(authorization);
		Studente studente = studenteService.findById(request.getStudentId()); // 404 Gestito
		Assenza assenza = assenzaService.creaAssenza(docente, studente, request);
		AssenzaDTO nuovaAssenza = assenzaService.salvaAssenza(assenza);
		return ResponseEntity.status(HttpStatus.CREATED).body(nuovaAssenza);

	}

	@DeleteMapping("/voto/{id}")
	public ResponseEntity<String> deleteVoto(@NonNull @RequestHeader("Authorization") String authorization,
			@PathVariable("id") Long id) {
		Docente docente = docenteService.extractDocente(authorization);
		votoService.deleteVoto(id, docente);
		return ResponseEntity.ok().body("Voto eliminato correttamente");
	}

	@DeleteMapping("/assenza/{id}")
	public ResponseEntity<String> deleteAssenza(@PathVariable("id") Long id,
			@NonNull @RequestHeader("Authorization") String authorization) {
		String token = authorization.substring(7);
		String email = jwtService.extractUsername(token);
		Docente docente = docenteService.findByEmail(email);
		assenzaService.deleteAssenza(id, docente);
		return ResponseEntity.ok().body("Assenza eliminata correttamente");

	}

	@PutMapping("/voto/{id}")
	public ResponseEntity<VotoDTO> updateVoto(@NonNull @RequestHeader("Authorization") String authorization,
			@PathVariable("id") Long id, @NonNull @Valid @RequestBody VotoRequest votoRequest) {
		Docente docente = docenteService.extractDocente(authorization);
		Studente studente = studenteService.findById(votoRequest.getStudentId()); // 404 checked
		VotoDTO votoAggiornato = votoService.aggiornaVoto(docente, id, votoRequest, studente);
		return ResponseEntity.ok(votoAggiornato);
	}

	@PutMapping("/assenza/{id}")
	public ResponseEntity<AssenzaDTO> updateAssenza(@PathVariable("id") Long id,
			@NonNull @Valid @RequestBody AssenzaRequest assenzaRequest,
			@NonNull @RequestHeader("Authorization") String authorization) {
		Studente studente = studenteService.findById(assenzaRequest.getStudentId());
		Docente docente = docenteService.extractDocente(authorization);
		AssenzaDTO assenzaAggiornata = assenzaService.aggiornaAssenza(docente, id, assenzaRequest, studente);

		return ResponseEntity.ok(assenzaAggiornata);
	}
}

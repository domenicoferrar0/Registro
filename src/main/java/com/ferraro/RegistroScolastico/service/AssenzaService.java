package com.ferraro.RegistroScolastico.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.dto.AssenzaDTO;
import com.ferraro.RegistroScolastico.dto.AssenzaRequest;
import com.ferraro.RegistroScolastico.entities.Assenza;
import com.ferraro.RegistroScolastico.entities.Classe;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.enums.Resource;
import com.ferraro.RegistroScolastico.exceptions.AssenzaAlreadyExistsException;
import com.ferraro.RegistroScolastico.exceptions.DocenteUnauthorizedException;
import com.ferraro.RegistroScolastico.exceptions.ResourceNotFoundException;
import com.ferraro.RegistroScolastico.exceptions.StudenteHasNoClassException;
import com.ferraro.RegistroScolastico.mapper.AssenzaMapper;
import com.ferraro.RegistroScolastico.mapper.DocenteMapper;
import com.ferraro.RegistroScolastico.mapper.StudenteMapper;
import com.ferraro.RegistroScolastico.repository.AssenzaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssenzaService {

	@Autowired
	private AssenzaRepository assenzaRepository;

	@Autowired
	private AssenzaMapper assenzaMapper;

	@Autowired
	private DocenteMapper docenteMapper;

	@Autowired
	private StudenteMapper studenteMapper;

	/*
	 * IN ORDINE: Uno studente senza classe non può risultare assente. Se è già
	 * stato segnato assente per quel giorno non può risultare assente. Solo il
	 * docente non è un suo docente non può inserirgli un'assenza
	 */
	public Assenza creaAssenza(Docente docente, Studente studente, AssenzaRequest request) {
		if (studente.getClasse() == null) {
			throw new StudenteHasNoClassException(studenteMapper.studenteToDto(studente));
		}
		if (assenzaRepository.existsByStudenteAndData(studente, request.getData())) {
			throw new AssenzaAlreadyExistsException(studente.getCf(), request.getData());
		}
		Classe classe = studente.getClasse();
		if (!classe.getDocenti().contains(docente)) {
			throw new DocenteUnauthorizedException(docenteMapper.docenteToDtoSimple(docente));
		}
		Assenza assenza = assenzaMapper.requestToVoto(request);
		assenza.setStudente(studente);
		assenza.setPeriodo(classe.getPeriodo());
		return assenza;
	}

	@Transactional // Restituisco l'assenza Mappata ed aggiornata.
	public AssenzaDTO salvaAssenza(Assenza assenza) {
		return assenzaMapper.assenzaToDto(assenzaRepository.save(assenza));
	}

	public Assenza findById(Long id) {
		return assenzaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Resource.ASSENZA, id));
	}

	@Transactional /*
					 * Trova l'assenza a partire dall'Id controlla se il docente ha l'autorizzazione
					 * per farlo ed infine elimina l'assenza se l'esito è positivo.
					 */
	public void deleteAssenza(Long id, Docente docente) {
		Assenza assenza = assenzaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Resource.ASSENZA, id));
		Classe classe = assenza.getStudente().getClasse();
		if (!classe.getDocenti().contains(docente)) {
			throw new DocenteUnauthorizedException(docenteMapper.docenteToDtoSimple(docente));
		}
		assenzaRepository.delete(assenza);
	}

	public List<AssenzaDTO> findAll() {
		Set<Assenza> assenze = new HashSet<>(assenzaRepository.findAll());
		return assenzaMapper.assenzeToDto(assenze);

	}

	public AssenzaDTO aggiornaAssenza(Docente docente, Long id, AssenzaRequest assenzaRequest, Studente studente) {
		Assenza assenza = assenzaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Resource.ASSENZA, id));
		if (assenzaRepository.existsByStudenteAndData(studente, assenzaRequest.getData())) {
			throw new AssenzaAlreadyExistsException(studente.getCf(), assenzaRequest.getData());
		}
		assenza.setData(assenzaRequest.getData());
		assenza.setOre(assenzaRequest.getOre());
		assenza.setStudente(studente);

		Classe classe = studente.getClasse();
		if (!classe.getDocenti().contains(docente)) {
			throw new DocenteUnauthorizedException(docenteMapper.docenteToDtoSimple(docente));
		}
		// salvato e mappato;
		return assenzaMapper.assenzaToDto(assenzaRepository.save(assenza));
	}

	public Page<AssenzaDTO> getAssenzeStudente(Studente studente, int page, int size, LocalDate startRange) {
		List<AssenzaDTO> assenzeTot;
		if (startRange == null) {
			assenzeTot = assenzaMapper.assenzeToDto(studente.getAssenze());
		} else {
			Set<Assenza> assenzaRange = assenzaRepository.searchByStudente(studente, startRange);
			assenzeTot = assenzaMapper.assenzeToDto(assenzaRange);
		}
		log.info("assenze vuoto? {}", assenzeTot.isEmpty());
		assenzeTot.sort(Comparator.comparing(AssenzaDTO::getData).reversed());
		Pageable pageRequest = PageRequest.of(page, size);
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), assenzeTot.size());

		List<AssenzaDTO> sublist = assenzeTot.subList(start, end);
		return new PageImpl<>(sublist, pageRequest, assenzeTot.size());
	}
}

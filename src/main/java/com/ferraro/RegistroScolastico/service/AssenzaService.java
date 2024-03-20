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
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.exceptions.ResourceNotFoundException;
import com.ferraro.RegistroScolastico.mapper.AssenzaMapper;
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
	
	public Assenza creaAssenza(Studente studente, AssenzaRequest request) {
		if (studente.getClasse() == null) {
			throw new IllegalArgumentException("Uno studente non può avere avere assenze se non ha una classe");
		}
		
		if(assenzaRepository.existsByStudenteAndData(studente, request.getData())) {
			throw new IllegalArgumentException("Impossibile inserire l'assenza, questo studente è già stato segnato assente in questa data: "
					.concat(request.getData().toString()));
		}
		Assenza assenza = assenzaMapper.requestToVoto(request);
		assenza.setStudente(studente);
		return assenza;
		
	}
	
	@Transactional
	public AssenzaDTO salvaAssenza(Assenza assenza) {
		assenzaRepository.save(assenza);
		return assenzaMapper.assenzaToDto(assenza);
	}
	
	public Assenza findById(Long id) {
		return assenzaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	@Transactional
	public boolean deleteAssenza(Long id) {
		System.out.println(id);
		try {
			return assenzaRepository.removeById(id)>0;
		}
		catch(Exception e) {
			e.printStackTrace();;
			return false;
		}
	}
	
	public List<AssenzaDTO> findAll(){
		Set<Assenza> assenze = new HashSet<>(assenzaRepository.findAll());
		return assenzaMapper.assenzeToDto(assenze);
		
	}

	public AssenzaDTO aggiornaAssenza(Long id, AssenzaRequest assenzaRequest, Studente studente) {
		Assenza assenza = assenzaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(id));
				assenza.setData(assenzaRequest.getData());
				assenza.setOre(assenzaRequest.getOre());
				assenza.setStudente(studente);
								//salvato e mappato;
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

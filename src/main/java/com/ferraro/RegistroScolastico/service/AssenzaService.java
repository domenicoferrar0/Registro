package com.ferraro.RegistroScolastico.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.dto.AssenzaDTO;
import com.ferraro.RegistroScolastico.dto.AssenzaRequest;
import com.ferraro.RegistroScolastico.entities.Assenza;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.exceptions.ResourceNotFoundException;
import com.ferraro.RegistroScolastico.mapper.AssenzaMapper;
import com.ferraro.RegistroScolastico.repository.AssenzaRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
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
}

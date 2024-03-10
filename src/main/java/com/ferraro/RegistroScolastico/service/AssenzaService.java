package com.ferraro.RegistroScolastico.service;

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

@Service
public class AssenzaService {

	@Autowired
	private AssenzaRepository assenzaRepository;
	
	@Autowired
	private AssenzaMapper assenzaMapper;
	
	public Assenza creaAssenza(Studente studente, AssenzaRequest request) {
		if(assenzaRepository.existsByStudenteAndData(studente, request.getData())) {
			throw new IllegalArgumentException("Uno studente non può avere più assenze nello stesso giorno");
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
	
	public boolean deleteAssenza(Long id) {
		try {
			return assenzaRepository.removeById(id);
		}
		catch(Exception e) {
			return false;
		}
	}
}

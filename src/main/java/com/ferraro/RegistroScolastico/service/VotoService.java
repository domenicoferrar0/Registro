package com.ferraro.RegistroScolastico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.dto.VotoDTO;
import com.ferraro.RegistroScolastico.dto.VotoRequest;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.entities.Voto;
import com.ferraro.RegistroScolastico.exceptions.ResourceNotFoundException;
import com.ferraro.RegistroScolastico.mapper.VotoMapper;
import com.ferraro.RegistroScolastico.repository.VotoRepository;

import jakarta.transaction.Transactional;

@Service
public class VotoService {

	@Autowired
	private VotoMapper votoMapper;

	@Autowired
	private VotoRepository votoRepository;

	public Voto creaVoto(Docente docente, Studente studente, VotoRequest request) {
		Voto voto = votoMapper.requestToVoto(request);
		voto.setDocente(docente);
		voto.setStudente(studente);
		return voto;

	}

	@Transactional
	public VotoDTO salvaVoto(Voto voto) {
		Voto newVoto = votoRepository.save(voto);
		return votoMapper.votoToDto(newVoto);
	}

	public Voto findById(Long id) {
		return votoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
	}

	@Transactional
	public boolean deleteVoto(Long id) {
		try {
			return votoRepository.removeById(id);
		} catch (Exception e) {
			return false;
		}
	}
}

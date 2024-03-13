package com.ferraro.RegistroScolastico.service;

import java.util.Set;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.dto.VotoDTO;
import com.ferraro.RegistroScolastico.dto.VotoRequest;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.entities.Voto;
import com.ferraro.RegistroScolastico.exceptions.DocenteUnauthorizedException;
import com.ferraro.RegistroScolastico.exceptions.ResourceNotFoundException;
import com.ferraro.RegistroScolastico.mapper.DocenteMapper;
import com.ferraro.RegistroScolastico.mapper.VotoMapper;
import com.ferraro.RegistroScolastico.repository.VotoRepository;

import jakarta.transaction.Transactional;

@Service
public class VotoService {

	@Autowired
	private VotoMapper votoMapper;

	@Autowired
	private VotoRepository votoRepository;
	
	@Autowired
	private DocenteMapper docenteMapper;

	public List<VotoDTO> findAll() {
		List<Voto> voti = votoRepository.findAll();
		return votoMapper.votiToDto(voti);
	}

	public Voto creaVoto(Docente docente, Studente studente, VotoRequest request) {
		if (studente.getClasse() == null) {
			
			throw new DocenteUnauthorizedException(docenteMapper.docenteToDtoSimple(docente));
			
		}
		Set<Docente> docentiClasse = studente.getClasse().getDocenti();
		if (!docentiClasse.contains(docente)) {
			
			throw new DocenteUnauthorizedException(docenteMapper.docenteToDtoSimple(docente));
		}
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
			return votoRepository.removeById(id)>0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}

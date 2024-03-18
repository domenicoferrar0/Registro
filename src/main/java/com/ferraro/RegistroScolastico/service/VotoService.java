package com.ferraro.RegistroScolastico.service;

import java.util.Set;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
		Set<Voto> voti = new HashSet<>(votoRepository.findAll());
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
			return votoRepository.removeById(id) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public VotoDTO aggiornaVoto(Voto voto, VotoRequest votoRequest, Studente studente) {
		voto.setStudente(studente);
		voto.setData(votoRequest.getData());
		voto.setVoto(votoRequest.getVoto());
		
		return votoMapper.votoToDto(votoRepository.save(voto));
	}
	
	public Page<VotoDTO> getVotiStudente(Studente studente, int page, int size, String searchTerm) {
		List<VotoDTO> votiTot;
		if (searchTerm == null || searchTerm.isBlank()) {
			votiTot = votoMapper.votiToDto(studente.getVoti());
		} else {
			Set<Voto> votiQuery = votoRepository.searchByStudente(searchTerm, studente);
			votiTot = votoMapper.votiToDto(votiQuery);

		}
		votiTot.sort(Comparator.comparing(VotoDTO::getData).reversed());

		Pageable pageRequest = PageRequest.of(page, size);
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), votiTot.size());

		List<VotoDTO> sublist = votiTot.subList(start, end);

		return new PageImpl<>(sublist, pageRequest, votiTot.size());
	}
}

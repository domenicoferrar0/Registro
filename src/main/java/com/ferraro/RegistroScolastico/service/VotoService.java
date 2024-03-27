package com.ferraro.RegistroScolastico.service;

import java.util.Set;
import java.util.stream.Collectors;
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
import com.ferraro.RegistroScolastico.entities.Classe;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.entities.Voto;
import com.ferraro.RegistroScolastico.exceptions.DocenteUnauthorizedException;
import com.ferraro.RegistroScolastico.exceptions.ResourceNotFoundException;
import com.ferraro.RegistroScolastico.exceptions.StudenteHasNoClassException;
import com.ferraro.RegistroScolastico.mapper.DocenteMapper;
import com.ferraro.RegistroScolastico.mapper.StudenteMapper;
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

	@Autowired
	private StudenteMapper studenteMapper;

	public List<VotoDTO> findAll() {
		Set<Voto> voti = new HashSet<>(votoRepository.findAll());
		return votoMapper.votiToDto(voti);
	}
	
	
	public Voto creaVoto(Docente docente, Studente studente, VotoRequest request) {
		if (studente.getClasse() == null) {

			throw new StudenteHasNoClassException(studenteMapper.studenteToDto(studente));

		}
		Set<Docente> docentiClasse = studente.getClasse().getDocenti();
		//Se il docente non è assegnato alla classe oppure se prova ad inserire un voto per una materia che non è la sua
		if (!docentiClasse.contains(docente) || !docente.getMateria().contains(request.getMateria())){

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

	@Transactional
	public void deleteVoto(Long id, Docente docente) {
		Voto voto = votoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("voto: " + id));
		if (!voto.getDocente().equals(docente)) {
			throw new DocenteUnauthorizedException(docenteMapper.docenteToDtoSimple(docente));
		}
		votoRepository.delete(voto);
	}

	public VotoDTO aggiornaVoto(Docente docente, Long id, VotoRequest votoRequest, Studente studente) {
		Voto voto = votoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("voto: " + id));
		if (!voto.getDocente().equals(docente)) {
			throw new DocenteUnauthorizedException(docenteMapper.docenteToDtoSimple(docente));
		}
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

	public List<VotoDTO> findVotiByDocente(Studente studente, Docente docente) {
		Classe classe = studente.getClasse();
		if (classe == null || !classe.getDocenti().contains(docente)) {
			throw new DocenteUnauthorizedException(docenteMapper.docenteToDtoSimple(docente));
		}
		Set<Voto> voti = studente.getVoti();
		return voti.stream().filter((v) -> v.getDocente().equals(docente)).map(votoMapper::votoToDto)
				.collect(Collectors.toList());
	}
}

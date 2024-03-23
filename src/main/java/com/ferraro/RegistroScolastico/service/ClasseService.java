package com.ferraro.RegistroScolastico.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.dto.ClasseDTO;
import com.ferraro.RegistroScolastico.dto.ClasseDTOFull;
import com.ferraro.RegistroScolastico.entities.Periodo;
import com.ferraro.RegistroScolastico.entities.Classe;
import com.ferraro.RegistroScolastico.exceptions.ClasseAlreadyExistsException;
import com.ferraro.RegistroScolastico.exceptions.ClasseNotFoundException;
import com.ferraro.RegistroScolastico.mapper.ClasseMapper;
import com.ferraro.RegistroScolastico.repository.ClasseRepository;

import jakarta.transaction.Transactional;

@Service
public class ClasseService {

	@Autowired
	private ClasseRepository classeRepository;

	@Autowired
	private ClasseMapper classeMapper;

	@Transactional
	public ClasseDTO saveClasse(ClasseDTO classeDTO) {
		int startYear = LocalDate.now().getYear();
		Periodo periodo = new Periodo(startYear, startYear+1);
		classeDTO.setPeriodo(periodo);
		if (classeRepository.existsByAnnoAndSezioneAndPeriodo(classeDTO.getAnno(), classeDTO.getSezione(), periodo)) {
			throw new ClasseAlreadyExistsException(classeDTO);
		}
		Classe classe = classeMapper.dtoToClasse(classeDTO);
		classeRepository.save(classe);
		return classeDTO;
	}

	public List<ClasseDTO> findAll() {
		Set<Classe> lista = classeRepository.findAllClasse();
		return classeMapper.classesToDto(lista);
	}

	public ClasseDTO findClasseDTO(Integer anno, String sezione) {
		return classeRepository.findByAnnoAndSezione(anno, sezione).map(classeMapper::classeToDto).orElse(null);
	}

	
	/*
	 * public Set<ClasseDTOFull> getAllClasses(Set<Classe> classi){ return
	 * classeMapper.classeToDtosFull(classi); }
	 */

/*	public ClasseDTOFull getClasseFull(Integer anno, String sezione) {
		Classe classe = classeRepository.findByAnnoAndSezione(anno, sezione)
				.orElseThrow(() -> new ClasseNotFoundException(anno + sezione));
		return classeMapper.classeToDtoFull(classe);
	}
	*/
	public ClasseDTOFull classeToDtoFull(Classe classe) {
		return classeMapper.classeToDtoFull(classe);
	}
	
	public ClasseDTOFull classeToDtoNoDocenti(Classe classe) {
		return classeMapper.classeToDtoNoDocenti(classe);
	}
	
	public Classe findById(Long classeId) {
		
		return classeRepository.findById(classeId)
				.orElseThrow(() -> new ClasseNotFoundException(classeId.toString()));
	}
}

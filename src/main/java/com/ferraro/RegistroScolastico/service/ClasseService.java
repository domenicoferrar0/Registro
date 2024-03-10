package com.ferraro.RegistroScolastico.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.dto.ClasseDTO;
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
		
		if (classeRepository.existsByAnnoAndSezione(classeDTO.getAnno(), classeDTO.getSezione())) {
			throw new ClasseAlreadyExistsException(classeDTO.getAnno()+classeDTO.getSezione());
		}
		Classe classe = classeMapper.dtoToClasse(classeDTO);
		classeRepository.save(classe);
		return classeDTO;
	}	
	
	public List<ClasseDTO> findAll(){
		List<Classe> lista = classeRepository.findAll();
		return classeMapper.classesToDto(lista);
	}
	
	public ClasseDTO findClasseDTO(Integer anno, String sezione) {
		return classeRepository.findByAnnoAndSezione(anno, sezione)
				.map(classeMapper::classeToDto)
				.orElse(null);
	}
	
	public Classe findClasse(Integer anno, String sezione) {
		return classeRepository.findByAnnoAndSezione(anno, sezione)				
				.orElseThrow(() -> new ClasseNotFoundException(anno+sezione));
	}
	
}

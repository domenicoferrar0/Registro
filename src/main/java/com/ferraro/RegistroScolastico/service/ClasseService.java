package com.ferraro.RegistroScolastico.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.dto.ClasseDTO;
import com.ferraro.RegistroScolastico.dto.ClasseDTOFull;
import com.ferraro.RegistroScolastico.entities.Periodo;
import com.ferraro.RegistroScolastico.enums.Materia;
import com.ferraro.RegistroScolastico.entities.Classe;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.exceptions.ClasseAlreadyExistsException;
import com.ferraro.RegistroScolastico.exceptions.ClasseNotFoundException;
import com.ferraro.RegistroScolastico.exceptions.DocenteUnauthorizedException;
import com.ferraro.RegistroScolastico.exceptions.MateriaHandlingException;
import com.ferraro.RegistroScolastico.mapper.ClasseMapper;
import com.ferraro.RegistroScolastico.mapper.DocenteMapper;
import com.ferraro.RegistroScolastico.repository.ClasseRepository;

import jakarta.transaction.Transactional;

@Service
public class ClasseService {

	@Autowired
	private ClasseRepository classeRepository;

	@Autowired
	private ClasseMapper classeMapper;
	
	@Autowired
	private DocenteMapper docenteMapper;

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
	
	public ClasseDTO liberaMateria(Materia materia, Classe classe) {
		Map<Materia, Docente> materieAssegnate = classe.getMaterieAssegnate();
		if (!materieAssegnate.containsKey(materia)) {
			throw new MateriaHandlingException("Impossibile eseguire, la materia non è stata ancora assegnata",classeMapper.classeToDto(classe), materia);
		}
		materieAssegnate.remove(materia);
		return classeMapper.classeToDto(classeRepository.save(classe));
	}
	
	public ClasseDTO assegnaMateria(Materia materia, Classe classe, Docente docente) {
		if (!classe.getDocenti().contains(docente) || !docente.getMateria().contains(materia)) {
			throw new DocenteUnauthorizedException(docenteMapper.docenteToDtoSimple(docente));
		}
		Map<Materia, Docente> materieAssegnate = classe.getMaterieAssegnate();
		if (materieAssegnate.containsKey(materia)) {
			throw new MateriaHandlingException("Materia già assegnata provvedere a liberarla prima di reassegnarla",classeMapper.classeToDto(classe), materia);
		}
		
		materieAssegnate.put(materia, docente);
		classe.setMaterieAssegnate(materieAssegnate);
		return classeMapper.classeToDto(classeRepository.save(classe));
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

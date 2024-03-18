package com.ferraro.RegistroScolastico.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.dto.VotoDTO;
import com.ferraro.RegistroScolastico.dto.AssenzaDTO;
import com.ferraro.RegistroScolastico.dto.ClasseDTOFull;
import com.ferraro.RegistroScolastico.dto.RegistrationForm;
import com.ferraro.RegistroScolastico.entities.Assenza;
import com.ferraro.RegistroScolastico.entities.Classe;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.entities.Voto;
import com.ferraro.RegistroScolastico.exceptions.DuplicateRegistrationException;
import com.ferraro.RegistroScolastico.exceptions.PersonNotFoundException;
import com.ferraro.RegistroScolastico.exceptions.ResourceNotFoundException;
import com.ferraro.RegistroScolastico.exceptions.ClassAssignException;
import com.ferraro.RegistroScolastico.exceptions.ClasseNotFoundException;
import com.ferraro.RegistroScolastico.mapper.AssenzaMapper;
import com.ferraro.RegistroScolastico.mapper.ClasseMapper;
import com.ferraro.RegistroScolastico.mapper.StudenteMapper;
import com.ferraro.RegistroScolastico.mapper.VotoMapper;
import com.ferraro.RegistroScolastico.repository.AnagraficaRepository;
import com.ferraro.RegistroScolastico.repository.AssenzaRepository;
import com.ferraro.RegistroScolastico.repository.StudenteRepository;
import com.ferraro.RegistroScolastico.repository.UserRepository;
import com.ferraro.RegistroScolastico.repository.VotoRepository;

import jakarta.transaction.Transactional;

@Service
public class StudenteService {

	@Autowired
	private StudenteMapper studenteMapper;

	@Autowired
	private StudenteRepository studenteRepository;

	@Autowired
	private AnagraficaRepository anagraficaRepository;

	@Autowired
	private UserRepository userRepository;	

	@Autowired
	private ClasseMapper classeMapper;

	

	

	public List<StudenteDTO> findAll() {
		return studenteMapper.studentiToDto(studenteRepository.findAll());
	}

	public Studente formToStudente(RegistrationForm form) {
		if (userRepository.existsByEmail(form.getEmail()) || anagraficaRepository.existsByCf(form.getCf())) {
			throw new DuplicateRegistrationException(form);
		}
		return studenteMapper.formToStudente(form);
	}

	@Transactional
	public StudenteDTO saveStudente(Studente studente) {
		Studente nuovoStudente = studenteRepository.save(studente);
		return studenteMapper.studenteToDto(nuovoStudente);
	}

	public StudenteDTO findDtoByEmail(String email) {
		Studente studente = studenteRepository.findByUser_Email(email)
				.orElseThrow(() -> new UsernameNotFoundException(email));
		return studenteMapper.studenteToDto(studente);
	}

	public Studente findByEmail(String email) {
		Studente studente = studenteRepository.findByUser_Email(email)
				.orElseThrow(() -> new UsernameNotFoundException(email));
		return studente;
	}

	public Studente findByCf(String cf) {
		return studenteRepository.findByAnagrafica_Cf(cf).orElseThrow(() -> new PersonNotFoundException(cf));
	}

	@Transactional
	public StudenteDTO assignClasse(Studente studente, Classe classe) {

		if (studente.getClasse() != null) { // Passo messaggio indicativo con oggetto a cui fa riferimento
			throw new ClassAssignException("Questo studente ha già una classe", studenteMapper.studenteToDto(studente));
		}
		studente.setClasse(classe);

		studenteRepository.save(studente);

		return studenteMapper.studenteToDto(studente);
	}

	public ClasseDTOFull getClasse(Studente studente) {
		Classe classe = studente.getClasse();
		if (classe == null) {
			throw new ClasseNotFoundException("per questo studente ".concat(studente.getAnagrafica().getCf()));
		}
		return classeMapper.classeToDtoFull(classe);
	}

	

	

}

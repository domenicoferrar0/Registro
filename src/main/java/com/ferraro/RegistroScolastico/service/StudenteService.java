package com.ferraro.RegistroScolastico.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.dto.RegistrationForm;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.exceptions.DuplicateRegistrationException;
import com.ferraro.RegistroScolastico.mapper.StudenteMapper;
import com.ferraro.RegistroScolastico.repository.AnagraficaRepository;
import com.ferraro.RegistroScolastico.repository.StudenteRepository;
import com.ferraro.RegistroScolastico.repository.UserRepository;

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
	
	public List<StudenteDTO> findAll(){
		return studenteMapper.studentiToDto(studenteRepository.findAll());
	}
	
	public Studente formToStudente(RegistrationForm form) {
		if(userRepository.existsByEmail(form.getEmail()) || anagraficaRepository.existsByCf(form.getCf())) {
			throw new DuplicateRegistrationException(form.getCf(), form.getEmail());
		}
		return studenteMapper.formToStudente(form);
	}
	
	public StudenteDTO saveStudente(Studente studente) {
		Studente nuovoStudente = studenteRepository.save(studente);
		return studenteMapper.studenteToDto(nuovoStudente);
	}
}

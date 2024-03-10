package com.ferraro.RegistroScolastico.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.mapper.DocenteMapper;
import com.ferraro.RegistroScolastico.mapper.VotoMapper;
import com.ferraro.RegistroScolastico.repository.AnagraficaRepository;
import com.ferraro.RegistroScolastico.repository.DocenteRepository;
import com.ferraro.RegistroScolastico.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.ferraro.RegistroScolastico.dto.DocenteDTO;
import com.ferraro.RegistroScolastico.dto.RegistrationForm;
import com.ferraro.RegistroScolastico.entities.Classe;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.exceptions.DuplicateRegistrationException;
import com.ferraro.RegistroScolastico.exceptions.PersonNotFoundException;

@Service
public class DocenteService {
	
	@Autowired
	private DocenteRepository docenteRepository;
	
	@Autowired
	private DocenteMapper docenteMapper;
	
	@Autowired
	private AnagraficaRepository anagraficaRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	
	
	public List<DocenteDTO> findAll(){
		return docenteMapper.docentiToDto(docenteRepository.findAll());
	}
	
	public Docente formToDocente(RegistrationForm form) {
		if (userRepository.existsByEmail(form.getEmail()) || anagraficaRepository.existsByCf(form.getCf())){
			throw new DuplicateRegistrationException(form.getCf(), form.getEmail());
		}
		return docenteMapper.formToDocente(form);
		
	}
	@Transactional
	public DocenteDTO saveDocente(Docente docente) {
		
		Docente nuovoDocente = docenteRepository.save(docente);
		return docenteMapper.docenteToDto(nuovoDocente);
	}

	public Docente findByCf(String cf) {
		return docenteRepository.findByAnagrafica_Cf(cf)
				.orElseThrow(() -> new PersonNotFoundException(cf));
	}
		
	
	public boolean assignClasse(Docente docente, Classe classe) {
		Set<Docente> docentiAttuali = classe.getDocenti();
		//Controllo se esiste già un docente con quella materia in quella classe
		if( docentiAttuali.stream()
				//d è il docente iterato del set
				.anyMatch(d -> d.getMateria() == docente.getMateria())) {
			return false; //da fare eccezione custom
		}
		return docente.getClassi().add(classe);
		
	}

	public Docente findByEmail(String email) {
		return docenteRepository.findByUser_Email(email)
		.orElseThrow(()-> new UsernameNotFoundException(email));
		
	}	
	
}

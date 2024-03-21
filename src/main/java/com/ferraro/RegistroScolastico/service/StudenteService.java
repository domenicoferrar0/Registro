package com.ferraro.RegistroScolastico.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.dto.ClasseDTOFull;
import com.ferraro.RegistroScolastico.dto.RegistrationForm;
import com.ferraro.RegistroScolastico.entities.Classe;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.exceptions.DuplicateRegistrationException;
import com.ferraro.RegistroScolastico.exceptions.MailNotSentException;
import com.ferraro.RegistroScolastico.exceptions.PersonNotFoundException;
import com.ferraro.RegistroScolastico.exceptions.ResourceNotFoundException;
import com.ferraro.RegistroScolastico.exceptions.ClassAssignException;
import com.ferraro.RegistroScolastico.exceptions.ClasseNotFoundException;
import com.ferraro.RegistroScolastico.mapper.ClasseMapper;
import com.ferraro.RegistroScolastico.mapper.StudenteMapper;
import com.ferraro.RegistroScolastico.repository.AnagraficaRepository;
import com.ferraro.RegistroScolastico.repository.StudenteRepository;
import com.ferraro.RegistroScolastico.repository.UserRepository;
import jakarta.mail.MessagingException;
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

	@Autowired
	private MailService mailService;

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
	public StudenteDTO saveStudente(Studente studente, RegistrationForm form, String plainPw) {
		Studente nuovoStudente = studenteRepository.save(studente);
		String token = mailService.createToken(studente.getUser());
		try {
			mailService.sendRegistrationEmail(form, token, plainPw);
		}catch(MessagingException e) {
			throw new MailNotSentException();
		}
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

	public Studente findById(Long id) {
		return studenteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("studente: "+id));
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

	public StudenteDTO removeClasse(Studente studente, Classe classe) {
		if(!studente.getClasse().equals(classe)) {
			throw new ClassAssignException("Impossibile rimuovere la classe, la classe fornita non corrisponde",
					studenteMapper.studenteToDto(studente));
		}
		studente.setClasse(null);				//SALVATO E MAPPATO
		return studenteMapper.studenteToDto(studenteRepository.save(studente));
	}

}

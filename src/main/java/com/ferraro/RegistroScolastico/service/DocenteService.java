package com.ferraro.RegistroScolastico.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.mapper.ClasseMapper;
import com.ferraro.RegistroScolastico.mapper.DocenteMapper;
import com.ferraro.RegistroScolastico.repository.AnagraficaRepository;
import com.ferraro.RegistroScolastico.repository.ClasseRepository;
import com.ferraro.RegistroScolastico.repository.DocenteRepository;
import com.ferraro.RegistroScolastico.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.ferraro.RegistroScolastico.dto.ClasseDTO;
import com.ferraro.RegistroScolastico.dto.DocenteDTO;
import com.ferraro.RegistroScolastico.dto.RegistrationForm;
import com.ferraro.RegistroScolastico.entities.Classe;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Periodo;
import com.ferraro.RegistroScolastico.exceptions.ClassAssignException;
import com.ferraro.RegistroScolastico.exceptions.DuplicateRegistrationException;
import com.ferraro.RegistroScolastico.exceptions.MailNotSentException;
import com.ferraro.RegistroScolastico.exceptions.PersonNotFoundException;

@Service
@Slf4j
public class DocenteService {

	@Autowired
	private DocenteRepository docenteRepository;

	@Autowired
	private DocenteMapper docenteMapper;

	@Autowired
	private ClasseMapper classeMapper;

	@Autowired
	private ClasseRepository classeRepository;

	@Autowired
	private AnagraficaRepository anagraficaRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MailService mailService;

	public DocenteDTO docenteToDto(Docente docente) {
		return docenteMapper.docenteToDto(docente);
	}

	public List<DocenteDTO> findAll() {
		return docenteMapper.docentiToDto(docenteRepository.findAll());
	}

	public Docente formToDocente(RegistrationForm form) {
		if (userRepository.existsByEmail(form.getEmail()) || anagraficaRepository.existsByCf(form.getCf())) {
			throw new DuplicateRegistrationException(form);
		}
		return docenteMapper.formToDocente(form);

	}

	@Transactional
	public DocenteDTO saveDocente(Docente docente, RegistrationForm form, String plainPw) {
		Docente nuovoDocente = docenteRepository.save(docente);
		String token = mailService.createToken(docente.getUser());
		try {
			mailService.sendRegistrationEmail(form, plainPw, token);
		} catch (MessagingException e) {
			throw new MailNotSentException();
		}
		return docenteMapper.docenteToDto(nuovoDocente);
	}

	public Docente findByCf(String cf) {
		return docenteRepository.findByAnagrafica_Cf(cf).orElseThrow(() -> new PersonNotFoundException(cf));
	}

	public DocenteDTO assignClasse(Docente docente, Classe classe) {
		Set<Docente> docentiAttuali = classe.getDocenti();

		boolean isMateriaOccupata = docentiAttuali.stream().anyMatch(d -> d.getMateria() == docente.getMateria());

		if (isMateriaOccupata || !docente.getClassi().add(classe)) {
			throw new ClassAssignException("Impossibile assegnare classe, materia occupata o docente già presente",
					docenteMapper.docenteToDto(docente));
		} // Salvo e ritorno il docente Mappato
		return docenteMapper.docenteToDto(docenteRepository.save(docente));

	}

	public Docente findByEmail(String email) {
		return docenteRepository.findByUser_Email(email).orElseThrow(() -> new UsernameNotFoundException(email));

	}

	public DocenteDTO findDtoByEmail(String email) {

		return docenteMapper.docenteToDto(findByEmail(email));
	}

	public List<ClasseDTO> getClassiByPeriodo(Periodo periodo, Docente docente) {
		List<Classe> classi = classeRepository.findByDocenteAndPeriodo(docente, periodo);
		// classi.sort(Comparator.comparing(Classe::getSezione));
		// classi.sort(Comparator.comparing(Classe::getAnno));;

		log.info("check classe {}", classi.isEmpty());
		return classeMapper.classesToDto(classi);
	}

}

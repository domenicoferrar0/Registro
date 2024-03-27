package com.ferraro.RegistroScolastico.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.dto.StudenteDTOSimple;
import com.ferraro.RegistroScolastico.dto.VotoDTO;
import com.ferraro.RegistroScolastico.dto.VotoMedia;
import com.ferraro.RegistroScolastico.dto.ClasseDTOFull;
import com.ferraro.RegistroScolastico.dto.RegistrationForm;
import com.ferraro.RegistroScolastico.entities.Classe;
import com.ferraro.RegistroScolastico.entities.Docente;
import com.ferraro.RegistroScolastico.entities.Studente;
import com.ferraro.RegistroScolastico.entities.Voto;
import com.ferraro.RegistroScolastico.enums.Materia;
import com.ferraro.RegistroScolastico.exceptions.DuplicateRegistrationException;
import com.ferraro.RegistroScolastico.exceptions.MailNotSentException;
import com.ferraro.RegistroScolastico.exceptions.PersonNotFoundException;
import com.ferraro.RegistroScolastico.exceptions.ResourceNotFoundException;
import com.ferraro.RegistroScolastico.exceptions.StudenteHasNoClassException;
import com.ferraro.RegistroScolastico.exceptions.ClassAssignException;
import com.ferraro.RegistroScolastico.exceptions.ClasseNotFoundException;
import com.ferraro.RegistroScolastico.exceptions.DocenteUnauthorizedException;
import com.ferraro.RegistroScolastico.mapper.ClasseMapper;
import com.ferraro.RegistroScolastico.mapper.DocenteMapper;
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
	private DocenteMapper docenteMapper;

	@Autowired
	private MailService mailService;
	
	@Autowired 
	private JwtService jwtService;
	
	public Studente extractStudente(String authorization) {
		String token = authorization.substring(7);
		String email = jwtService.extractUsername(token);		
		return findByEmail(email);
	}
	
	public List<VotoMedia> getMediaVoti(Studente studente){
		if (studente.getClasse()== null) {
			throw new StudenteHasNoClassException(studenteMapper.studenteToDto(studente));
		}
		
		Set<Voto> voti = studente.getVoti();
		if(voti.isEmpty()) {
			return Collections.emptyList();
		}
		LocalDate data = LocalDate.now();
		Map<Materia, Double> mappaVoti = voti.stream().collect(Collectors.groupingBy(Voto::getMateria, Collectors.averagingDouble(Voto::getVoto)));
		List<VotoMedia> votoMedia = new ArrayList<VotoMedia>();
		StudenteDTOSimple studenteDto = studenteMapper.studenteToDtoSimple(studente);
		for (Map.Entry<Materia, Double> m : mappaVoti.entrySet()) {
			VotoMedia voto = new VotoMedia(studenteDto, data, m.getValue(), m.getKey());
			votoMedia.add(voto);
		}
		return votoMedia;
	}
	
	public VotoMedia votoMedio(Studente studente, Materia materia) {
		if (studente.getClasse()== null) {
			throw new StudenteHasNoClassException(studenteMapper.studenteToDto(studente));
		}
		List<Voto> votiMateria = studente.getVoti().stream().filter((d) -> d.getMateria() == materia).collect(Collectors.toList());
		if(votiMateria.isEmpty()) {
			throw new ResourceNotFoundException("voti studente "+studente.getAnagrafica().getCf());
		}
		Double votoMedio = votiMateria.stream().collect(Collectors.averagingDouble(Voto::getVoto));
		StudenteDTOSimple studenteDto = studenteMapper.studenteToDtoSimple(studente);
		return new VotoMedia(studenteDto, LocalDate.now(), votoMedio, materia);
	}

	public List<StudenteDTO> findAll() {
		return studenteMapper.studentiToDto(studenteRepository.findAll());
	}
	
	//Metodo che crea entità a partire dal form di registrazione, eccezione gestita in caso di chiavi uniche duplicate
	public Studente formToStudente(RegistrationForm form) {
		if (userRepository.existsByEmail(form.getEmail()) || anagraficaRepository.existsByCf(form.getCf())) {
			throw new DuplicateRegistrationException(form);
		}
		return studenteMapper.formToStudente(form);
	}

	@Transactional/*In concomitanza alla registrazione viene creato un token di conferma e mandato via mail.
	Tutto nella stessa transazione, che viene annullata in caso ci siano problemi con il Mail Sender */
	public StudenteDTO saveStudente(Studente studente, RegistrationForm form, String plainPw) {
		Studente nuovoStudente = studenteRepository.save(studente);
		String token = mailService.createToken(studente.getUser());
		try {
			mailService.sendRegistrationEmail(form, plainPw, token);
		} catch (MessagingException e) {
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
		return studenteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("studente: " + id));
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
		if (!studente.getClasse().equals(classe)) {
			throw new ClassAssignException("Impossibile rimuovere la classe, la classe fornita non corrisponde",
					studenteMapper.studenteToDto(studente));
		}
		studente.setClasse(null); // SALVATO E MAPPATO
		return studenteMapper.studenteToDto(studenteRepository.save(studente));
	}

	public StudenteDTO getStudenteForDocente(Studente studente, Docente docente) {
		Classe classe = studente.getClasse();
		if (classe == null || !classe.getDocenti().contains(docente)) {
			throw new DocenteUnauthorizedException(docenteMapper.docenteToDtoSimple(docente));
		}
		return studenteMapper.studenteToDtoFull(studente);
	}

	

}

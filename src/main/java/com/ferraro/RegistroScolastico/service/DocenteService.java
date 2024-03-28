package com.ferraro.RegistroScolastico.service;

import java.util.List;
import java.util.Map;
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
import com.ferraro.RegistroScolastico.enums.Materia;
import com.ferraro.RegistroScolastico.enums.Resource;
import com.ferraro.RegistroScolastico.exceptions.ClassAssignException;
import com.ferraro.RegistroScolastico.exceptions.DocenteUnauthorizedException;
import com.ferraro.RegistroScolastico.exceptions.DuplicateRegistrationException;
import com.ferraro.RegistroScolastico.exceptions.MailNotSentException;
import com.ferraro.RegistroScolastico.exceptions.ResourceNotFoundException;

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
	
	@Autowired 
	private JwtService jwtService;
	
	
	
	public Docente extractDocente(String authorization) {
		String token = authorization.substring(7);
		String email = jwtService.extractUsername(token);		
		return findByEmail(email);
	}
	
	public DocenteDTO docenteToDto(Docente docente) {
		return docenteMapper.docenteToDto(docente);
	}

	public List<DocenteDTO> findAll() {
		return docenteMapper.docentiToDto(docenteRepository.findAll());
	}
	
	//Metodo che crea entità a partire dal form di registrazione, eccezione gestita in caso di chiavi uniche duplicate
	public Docente formToDocente(RegistrationForm form) {
		if (userRepository.existsByEmail(form.getEmail()) || anagraficaRepository.existsByCf(form.getCf())) {
			throw new DuplicateRegistrationException(form);
		}
		return docenteMapper.formToDocente(form);
	}

	@Transactional /*In concomitanza alla registrazione viene creato un token di conferma e mandato via mail.
	Tutto nella stessa transazione, che viene annullata in caso ci siano problemi con il Mail Sender */
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

	public Docente findById(Long id) {
		return docenteRepository.findById(id).orElseThrow(() -> new  ResourceNotFoundException(Resource.DOCENTE, id));
	}

	@Transactional
	public DocenteDTO assignClasse(Docente docente, Classe classe, Set<Materia> materie) {
		boolean hasDocenteMaterie = materie.stream().allMatch((m) -> docente.getMaterie().contains(m));
		
		if(!hasDocenteMaterie) {
			throw new DocenteUnauthorizedException(docenteMapper.docenteToDtoSimple(docente));
		}
		
		boolean anyMateriaOccupata = materie.stream().anyMatch((m) -> classe.getMaterieAssegnate().containsKey(m));
		//Faccio riferimento al booleano generato dall'add al set per determinare se l'operazione ha avuto successo
		if (anyMateriaOccupata || !docente.getClassi().add(classe)) {
			throw new ClassAssignException("Impossibile assegnare classe, materia occupata o docente già presente",
					docenteMapper.docenteToDto(docente));
		} // Salvo e ritorno il docente Mappato
		for (Materia m : materie) {
			classe.getMaterieAssegnate().put(m, docente);
		}
		classeRepository.save(classe);
		return docenteMapper.docenteToDto(docenteRepository.save(docente));

	} 

	public Docente findByEmail(String email) {
		return docenteRepository.findByUser_Email(email).orElseThrow(() -> new UsernameNotFoundException(email));
	}
	
	public DocenteDTO findDtoByEmail(String email) {
		return docenteMapper.docenteToDto(findByEmail(email));
	}
	
	/*Periodo è una classe embeddata nell'entità Classe, in maniera opzionale il docente può cercare
	 * le sue classi di uno specifico Periodo */
	public List<ClasseDTO> getClassi(Integer startYear, Docente docente) {
		Set<Classe> classi;
		if (startYear == null || startYear < 0) {
			classi = docente.getClassi();
		} else {
			classi = classeRepository.findByDocenteAndPeriodo(docente, new Periodo(startYear, startYear + 1));
		}
		log.info("check classe {}", classi.isEmpty());
		return classeMapper.classesToDto(classi);
	}
	
	public DocenteDTO removeClasse(Docente docente, Classe classe) {
		
		if (!docente.getClassi().contains(classe)) {
			throw new ClassAssignException(
					"Impossibile rimuovere, il docente non è assegnato a questa classe " + classe.getId(),
					docenteMapper.docenteToDto(docente));
		}
		Map<Materia, Docente> materieAssegnate = classe.getMaterieAssegnate();
		if (materieAssegnate.containsValue(docente)) {
			for (Map.Entry<Materia, Docente> m : materieAssegnate.entrySet()) {
				if(m.getValue().equals(docente)) {
					materieAssegnate.remove(m.getKey());
				}
			}
		}
		classe.setMaterieAssegnate(materieAssegnate);
		classeRepository.save(classe);
		docente.getClassi().remove(classe); // SALVATO E MAPPATO
		return docenteMapper.docenteToDto(docenteRepository.save(docente));
	}

}

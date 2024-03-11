package com.ferraro.RegistroScolastico.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.dto.StudenteDTOSimple;
import com.ferraro.RegistroScolastico.dto.RegistrationForm;
import com.ferraro.RegistroScolastico.entities.Studente;

@Mapper(componentModel = "spring")
public interface StudenteMapper {

	StudenteMapper INSTANCE = Mappers.getMapper(StudenteMapper.class);
	
	@Mapping(source = "user.email", target = "email")
	public StudenteDTO studenteToDto(Studente studente);
	
	default List<StudenteDTO> studentiToDto(List<Studente> studenti){
		return studenti.stream().map(this::studenteToDto).collect(Collectors.toList());
	}
	
	@Mapping(source = "studenteDTO.email", target = "user.email")
	public Studente dtoToStudente(StudenteDTO studenteDTO);
	
	
	@Mapping(source = "form.email", target = "user.email")
	@Mapping(source = "form.nome", target = "anagrafica.nome")
	@Mapping(source = "form.cognome", target = "anagrafica.cognome")
	@Mapping(source = "form.nascita", target = "anagrafica.nascita")
	@Mapping(source = "form.cf", target = "anagrafica.cf")
	@Mapping(source = "form.indirizzo", target = "anagrafica.indirizzo")
	@Mapping(source = "form.genere", target = "anagrafica.genere")
	@Mapping(source = "form.luogoDiNascita", target = "anagrafica.luogoDiNascita")
	public Studente formToStudente(RegistrationForm form);
	
	
	@Mapping(source = "anagrafica.nome", target = "nome")
	@Mapping(source = "anagrafica.cognome", target = "cognome")
	@Mapping(source = "anagrafica.cf", target = "cf")
	public StudenteDTOSimple studenteToDtoSimple(Studente studente);
	
		
}

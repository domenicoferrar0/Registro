package com.ferraro.RegistroScolastico.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ferraro.RegistroScolastico.dto.DocenteDTO;
import com.ferraro.RegistroScolastico.dto.DocenteDTOSimple;
import com.ferraro.RegistroScolastico.dto.RegistrationForm;
import com.ferraro.RegistroScolastico.entities.Docente;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DocenteMapper {

	DocenteMapper INSTANCE = Mappers.getMapper(DocenteMapper.class);
	
	
	@Mapping(source = "form.email", target = "user.email")
	@Mapping(source = "form.nome", target = "anagrafica.nome")
	@Mapping(source = "form.cognome", target = "anagrafica.cognome")
	@Mapping(source = "form.nascita", target = "anagrafica.nascita")
	@Mapping(source = "form.cf", target = "anagrafica.cf")
	@Mapping(source = "form.indirizzo", target = "anagrafica.indirizzo")
	@Mapping(source = "form.genere", target = "anagrafica.genere")
	@Mapping(source = "form.luogoDiNascita", target = "anagrafica.luogoDiNascita")
	public Docente formToDocente(RegistrationForm form);
	
	@Mapping(source = "user.email", target = "email")
	public DocenteDTO docenteToDto(Docente docente);
	
	default List<DocenteDTO> docentiToDto(List<Docente> docenti){
		return docenti.stream().map(this::docenteToDto).collect(Collectors.toList());
	}
	
	@Mapping(source = "anagrafica.nome", target = "nome")
	@Mapping(source = "anagrafica.cognome", target = "cognome")
	@Mapping(source = "anagrafica.cf", target = "cf")
	public DocenteDTOSimple docenteToDtoSimple(Docente docente);
}

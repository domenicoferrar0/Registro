package com.ferraro.RegistroScolastico.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.ferraro.RegistroScolastico.dto.RegistrationForm;
import com.ferraro.RegistroScolastico.entities.Docente;

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
	@Mapping(source = "form.materia", target = "materia")
	public Docente formToDocente(RegistrationForm form);
	
	
}

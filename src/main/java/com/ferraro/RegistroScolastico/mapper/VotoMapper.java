package com.ferraro.RegistroScolastico.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ferraro.RegistroScolastico.dto.VotoDTO;
import com.ferraro.RegistroScolastico.dto.VotoRequest;
import com.ferraro.RegistroScolastico.entities.Voto;

@Mapper(componentModel = "spring")
public interface VotoMapper {

	VotoMapper  INSTANCE = Mappers.getMapper(VotoMapper.class);
	
	@Mapping(target = "studente", ignore = true)
	public VotoDTO votoToDtoNoStudent(Voto voto);
		
	public VotoDTO votoToDto(Voto voto);
		
		
	public Voto requestToVoto(VotoRequest request);
}

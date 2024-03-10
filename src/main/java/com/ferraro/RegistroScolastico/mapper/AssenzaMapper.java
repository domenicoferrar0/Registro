package com.ferraro.RegistroScolastico.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ferraro.RegistroScolastico.dto.AssenzaDTO;
import com.ferraro.RegistroScolastico.dto.AssenzaRequest;
import com.ferraro.RegistroScolastico.entities.Assenza;

@Mapper(componentModel = "spring")
public interface AssenzaMapper {

	AssenzaMapper INSTANCE = Mappers.getMapper(AssenzaMapper.class);
	
	
	public Assenza requestToVoto(AssenzaRequest request);
	
	
	public AssenzaDTO assenzaToDto(Assenza assenza);
}

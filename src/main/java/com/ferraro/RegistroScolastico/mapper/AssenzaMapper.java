package com.ferraro.RegistroScolastico.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ferraro.RegistroScolastico.dto.AssenzaDTO;
import com.ferraro.RegistroScolastico.dto.AssenzaRequest;
import com.ferraro.RegistroScolastico.entities.Assenza;

@Mapper(componentModel = "spring", uses = StudenteMapper.class)
public interface AssenzaMapper {

	AssenzaMapper INSTANCE = Mappers.getMapper(AssenzaMapper.class);
	
	
	public Assenza requestToVoto(AssenzaRequest request);
	
	
	public AssenzaDTO assenzaToDto(Assenza assenza);
	
	default List<AssenzaDTO> assenzeToDto(Set<Assenza> assenze){
		return assenze.stream().map(this::assenzaToDto).collect(Collectors.toList());
	}
}

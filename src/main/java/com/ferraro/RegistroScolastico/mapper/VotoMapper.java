package com.ferraro.RegistroScolastico.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ferraro.RegistroScolastico.dto.VotoDTO;
import com.ferraro.RegistroScolastico.dto.VotoRequest;
import com.ferraro.RegistroScolastico.entities.Voto;

@Mapper(componentModel = "spring", uses = { StudenteMapper.class, DocenteMapper.class})
public interface VotoMapper {

	VotoMapper  INSTANCE = Mappers.getMapper(VotoMapper.class);
	
	@Mapping(target = "studente", ignore = true)
	public VotoDTO votoToDtoNoStudent(Voto voto);
	
	
	public VotoDTO votoToDto(Voto voto);
	
	public default List<VotoDTO> votiToDto(Set<Voto> voti){
		return voti.stream().map(this::votoToDto).collect(Collectors.toList());
	}
		
		
	public Voto requestToVoto(VotoRequest request);
}

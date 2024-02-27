package com.ferraro.RegistroScolastico.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ferraro.RegistroScolastico.dto.ClasseDTO;
import com.ferraro.RegistroScolastico.entities.Classe;

@Mapper(componentModel = "spring")
public interface ClasseMapper {
	
	ClasseMapper INSTANCE = Mappers.getMapper(ClasseMapper.class);
	
	public ClasseDTO classeToDto(Classe classe); 
	
	public Classe dtoToClasse(ClasseDTO classeDTO);
	
	default List<ClasseDTO> classesToDto(List<Classe> classi){
		return classi.stream().map(this::classeToDto).collect(Collectors.toList());
	}
	
	default List<Classe> dtosToClasses (List<ClasseDTO> classi){
		return classi.stream().map(this::dtoToClasse).collect(Collectors.toList());
	}
}

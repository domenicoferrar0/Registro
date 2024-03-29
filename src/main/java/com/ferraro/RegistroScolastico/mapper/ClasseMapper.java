package com.ferraro.RegistroScolastico.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ferraro.RegistroScolastico.dto.ClasseDTO;
import com.ferraro.RegistroScolastico.dto.ClasseDTOFull;
import com.ferraro.RegistroScolastico.entities.Classe;

@Mapper(componentModel = "spring", uses = {StudenteMapper.class, DocenteMapper.class})
public interface ClasseMapper {
	
	ClasseMapper INSTANCE = Mappers.getMapper(ClasseMapper.class);
	
	public ClasseDTO classeToDto(Classe classe); 
	
	public Classe dtoToClasse(ClasseDTO classeDTO);
	
	public ClasseDTOFull classeToDtoFull(Classe classe);
	
	@Mapping(target = "docenti", ignore = true)
	public ClasseDTOFull classeToDtoNoDocenti(Classe classe);
	
	default List<ClasseDTO> classesToDto(Set<Classe> classi){
		return classi.stream().map(this::classeToDto).collect(Collectors.toList());
	}
/*	
	default List<ClasseDTO> classesToDto(Set<Classe> classi){
		return classi.stream().map(this::classeToDto).collect(Collectors.toList());
	} */
	
	default List<Classe> dtosToClasses (List<ClasseDTO> classi){
		return classi.stream().map(this::dtoToClasse).collect(Collectors.toList());
	}
	
	/*default Set<ClasseDTOFull> classeToDtosFull(Set<Classe> classi){
		return classi.stream().map(this::classeToDtoFull).collect(Collectors.toSet());
	} */
}

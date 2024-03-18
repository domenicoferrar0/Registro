package com.ferraro.RegistroScolastico.exceptions;

import com.ferraro.RegistroScolastico.dto.ClasseDTO;

public class ClasseAlreadyExistsException extends CustomException{
	
	
	
	public ClasseAlreadyExistsException(ClasseDTO classe) {
		super("Questa classe esiste già, impossible duplicarla", classe);
		
	}
}

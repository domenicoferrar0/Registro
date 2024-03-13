package com.ferraro.RegistroScolastico.exceptions;

import com.ferraro.RegistroScolastico.dto.ClasseDTO;

public class ClasseAlreadyExistsException extends CustomException{
	
	
	
	public ClasseAlreadyExistsException(Object object) {
		super("Questa classe esiste già, impossible duplicarla", object);
		
	}
}

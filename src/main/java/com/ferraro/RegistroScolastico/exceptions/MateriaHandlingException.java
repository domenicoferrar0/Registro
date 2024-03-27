package com.ferraro.RegistroScolastico.exceptions;

import com.ferraro.RegistroScolastico.dto.ClasseDTO;
import com.ferraro.RegistroScolastico.enums.Materia;

public class MateriaHandlingException extends CustomException{
	
	Materia materia;
	
	public MateriaHandlingException(String messaggio, ClasseDTO classe, Materia materia) {
		super(messaggio, classe);
		this.materia = materia;
	}
}

package com.ferraro.RegistroScolastico.exceptions;

import com.ferraro.RegistroScolastico.dto.StudenteDTO;

public class StudenteHasNoClassException extends CustomException{

	public StudenteHasNoClassException(StudenteDTO studente) {
		super("Impossibile completare l'operazione, lo studente non ha una classe", studente);
		
	}

}

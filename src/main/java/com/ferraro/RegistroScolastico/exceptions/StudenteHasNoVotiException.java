package com.ferraro.RegistroScolastico.exceptions;

import com.ferraro.RegistroScolastico.dto.StudenteDTO;

public class StudenteHasNoVotiException extends CustomException{
	public StudenteHasNoVotiException(StudenteDTO studente) {
		super("Spiacente, lo studente attualmente non ha voti", studente);
	}
}

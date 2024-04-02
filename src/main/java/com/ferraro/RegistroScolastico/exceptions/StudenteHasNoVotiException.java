package com.ferraro.RegistroScolastico.exceptions;

import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.enums.Materia;

public class StudenteHasNoVotiException extends CustomException{
	public StudenteHasNoVotiException(StudenteDTO studente) {
		super("Spiacente, lo studente attualmente non ha voti", studente);
	}
	
	public StudenteHasNoVotiException(StudenteDTO studente, Materia materia) {
		super("Spiacente, lo studente attualmente non ha voti per questa materia, ".concat(materia.getNome()), studente);
	}
}

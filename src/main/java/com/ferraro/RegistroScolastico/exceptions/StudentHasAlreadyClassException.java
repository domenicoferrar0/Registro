package com.ferraro.RegistroScolastico.exceptions;

import com.ferraro.RegistroScolastico.dto.StudenteDTO;
import com.ferraro.RegistroScolastico.entities.Studente;

public class StudentHasAlreadyClassException extends RuntimeException{
	
	private StudenteDTO studente;	
		
	
	public StudentHasAlreadyClassException(StudenteDTO studente) {
	    super("Questo studente ha già una classe");
	    this.studente = studente;
	  }
	
	public StudenteDTO getStudente() {
		return this.studente;
	}
}

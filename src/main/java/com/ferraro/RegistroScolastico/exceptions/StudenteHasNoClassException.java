package com.ferraro.RegistroScolastico.exceptions;

public class StudenteHasNoClassException extends CustomException{

	public StudenteHasNoClassException(Object object) {
		super("Impossibile completare l'operazione, lo studente non ha una classe", object);
		
	}

}

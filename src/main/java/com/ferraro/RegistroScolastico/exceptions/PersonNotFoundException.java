package com.ferraro.RegistroScolastico.exceptions;

public class PersonNotFoundException extends RuntimeException{

	public PersonNotFoundException(String cf) {
	    super("Nessuna corrispondenza trovata per questo CF: " + cf);
	  }
}

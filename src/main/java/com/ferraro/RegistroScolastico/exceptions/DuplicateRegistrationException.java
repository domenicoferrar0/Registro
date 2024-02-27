package com.ferraro.RegistroScolastico.exceptions;

public class DuplicateRegistrationException extends RuntimeException{

	public DuplicateRegistrationException(String cf, String email) {
		super("Attenzione, utente già registrato, controlla Codice Fiscale ed email:"+cf+", "+email);
	}
}

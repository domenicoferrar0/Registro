package com.ferraro.RegistroScolastico.exceptions;

public class DuplicateRegistrationException extends CustomException{

	public DuplicateRegistrationException(Object object) {
		super("Attenzione, utente già registrato, controlla Codice Fiscale ed email", object);
	}
}

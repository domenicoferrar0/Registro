package com.ferraro.RegistroScolastico.exceptions;

import com.ferraro.RegistroScolastico.dto.RegistrationForm;

public class DuplicateRegistrationException extends CustomException{

	public DuplicateRegistrationException(RegistrationForm form) {
		super("Attenzione, utente già registrato, controlla Codice Fiscale ed email", form);
	}
}

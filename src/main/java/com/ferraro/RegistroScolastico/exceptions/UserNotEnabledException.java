package com.ferraro.RegistroScolastico.exceptions;

import com.ferraro.RegistroScolastico.entities.User;

public class UserNotEnabledException extends CustomException{
	
	public UserNotEnabledException(String email) {
		super("Questo utente non è abilitato", email);
	}
}

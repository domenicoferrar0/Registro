package com.ferraro.RegistroScolastico.exceptions;

public class RoleNotFoundException extends RuntimeException{

	public RoleNotFoundException(String name) {
		super("Ruolo non trovato, contattare l'admin ".concat(name));
	}
}

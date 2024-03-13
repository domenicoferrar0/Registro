package com.ferraro.RegistroScolastico.exceptions;

public class ClasseNotFoundException extends RuntimeException {

	public ClasseNotFoundException(String id) {
		super("Classe non trovata ".concat(id));
	}
}

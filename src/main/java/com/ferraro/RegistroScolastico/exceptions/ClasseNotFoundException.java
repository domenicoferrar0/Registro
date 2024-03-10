package com.ferraro.RegistroScolastico.exceptions;

public class ClasseNotFoundException extends RuntimeException {

	public ClasseNotFoundException(String classe) {
		super("Classe non trovata "+classe);
	}
}

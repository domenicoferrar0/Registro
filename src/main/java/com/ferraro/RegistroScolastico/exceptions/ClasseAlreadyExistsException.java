package com.ferraro.RegistroScolastico.exceptions;

public class ClasseAlreadyExistsException extends RuntimeException{
	
	public ClasseAlreadyExistsException(String nomeClasse) {
		super("Questa classe esiste già, impossible duplicarla ".concat(nomeClasse));
	}
}

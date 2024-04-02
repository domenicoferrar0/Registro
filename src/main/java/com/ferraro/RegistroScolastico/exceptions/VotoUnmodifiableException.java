package com.ferraro.RegistroScolastico.exceptions;

public class VotoUnmodifiableException extends RuntimeException{
	
	public VotoUnmodifiableException() {
		super("Spiacente, impossibile modificare il voto, sei fuori dal tempo massimo");
	}
}

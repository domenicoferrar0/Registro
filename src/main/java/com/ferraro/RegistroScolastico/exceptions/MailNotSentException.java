package com.ferraro.RegistroScolastico.exceptions;

public class MailNotSentException extends RuntimeException{
	
	public MailNotSentException() {
		super("Registrazione non andata a buon fine, riprova più tardi");
	}
}

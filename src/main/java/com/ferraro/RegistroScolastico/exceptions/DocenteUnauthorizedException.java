package com.ferraro.RegistroScolastico.exceptions;

public class DocenteUnauthorizedException extends RuntimeException {
	
	public DocenteUnauthorizedException(String docenteCF) {
		super("Spiacente, questo docente non è autorizzato per questa operazione ".concat(docenteCF));
	}
}

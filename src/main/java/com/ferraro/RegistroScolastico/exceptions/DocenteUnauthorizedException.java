package com.ferraro.RegistroScolastico.exceptions;

import com.ferraro.RegistroScolastico.dto.DocenteDTOSimple;

public class DocenteUnauthorizedException extends RuntimeException {
	
	private DocenteDTOSimple docente;
	
	public DocenteDTOSimple getDocente() {
		return docente;
	}
	
	public DocenteUnauthorizedException(DocenteDTOSimple docente) {
		super("Spiacente, questo docente non è autorizzato per questa operazione ");
		this.docente = docente;
	}
}

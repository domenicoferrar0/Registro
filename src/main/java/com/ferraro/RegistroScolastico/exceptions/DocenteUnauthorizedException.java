package com.ferraro.RegistroScolastico.exceptions;

import com.ferraro.RegistroScolastico.dto.DocenteDTOSimple;


public class DocenteUnauthorizedException extends CustomException {
	
	
	
	public DocenteUnauthorizedException(DocenteDTOSimple docente) {
		super("Spiacente, questo docente non è autorizzato per questa operazione ", docente);
		
	}
}

package com.ferraro.RegistroScolastico.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ferraro.RegistroScolastico.dto.ApiResponse;
import com.ferraro.RegistroScolastico.dto.StudenteDTO;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({ ClasseAlreadyExistsException.class, DuplicateRegistrationException.class, ClassAssignException.class })
	public ResponseEntity<ApiResponse> handleConflictException(CustomException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.conflict(ex.getMessage(), ex.getObject()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<String, String>();
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		for (FieldError error : fieldErrors) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return ResponseEntity.unprocessableEntity().body(errors);
	}

	@ExceptionHandler({ RoleNotFoundException.class, UsernameNotFoundException.class, PersonNotFoundException.class,
			ClasseNotFoundException.class, ResourceNotFoundException.class, ConfirmationTokenNotFoundException.class })
	public ResponseEntity<String> handleNotFoundException(Exception ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	
	@ExceptionHandler({DocenteUnauthorizedException.class, UserNotEnabledException.class})
	public ResponseEntity<ApiResponse> unauthorizedException(CustomException ex){
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.unauthorized(ex.getMessage(), ex.getObject()));
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<String> handleJwtExpiring(ExpiredJwtException ex){
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("autorizzazione scaduta rieffettua il login");
	}
}

package com.ferraro.RegistroScolastico.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ferraro.RegistroScolastico.dto.ApiResponse;
import com.ferraro.RegistroScolastico.dto.StudenteDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({ ClasseAlreadyExistsException.class, DuplicateRegistrationException.class })
	public ResponseEntity<String> handleConflictException(Exception ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
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
			ClasseNotFoundException.class, ResourceNotFoundException.class })
	public ResponseEntity<String> handleNotFoundException(Exception ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(StudentHasAlreadyClassException.class)
	public ResponseEntity<ApiResponse<?>> handleClassException(StudentHasAlreadyClassException ex) {
		ApiResponse<StudenteDTO> response = new ApiResponse();
		response.setData(ex.getStudente());
		response.setMessage(ex.getMessage());
		response.setStatus(HttpStatus.CONFLICT);

		return ResponseEntity.status(response.getStatus()).body(response);
	}
}

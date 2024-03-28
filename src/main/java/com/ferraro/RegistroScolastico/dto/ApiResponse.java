package com.ferraro.RegistroScolastico.dto;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class ApiResponse {
	private HttpStatus status;
	private String message;
	private Object object;

	public static ApiResponse unauthorized(String message, Object object) {
		return new ApiResponse(HttpStatus.UNAUTHORIZED, message, object);
	}

	public static ApiResponse conflict(String message, Object object) {
		return new ApiResponse(HttpStatus.CONFLICT, message, object);
	}
	
	public static ApiResponse forbidden(String message, Object object) {
		return new ApiResponse(HttpStatus.FORBIDDEN, message, object);
	}
	
	public static ApiResponse notFound(String message, Object object) {
		return new ApiResponse(HttpStatus.NOT_FOUND, message, object);
	}
}

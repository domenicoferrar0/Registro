package com.ferraro.RegistroScolastico.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class ApiResponse<T> {
	private HttpStatus status;
	private String message;
	private T data;
	
	
	
	
}

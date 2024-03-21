package com.ferraro.RegistroScolastico.dto;

import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Setter
public class LoginDTO {
	
	@NotBlank
	private String email;
	
	@NotBlank
	@Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[A-Z]).{8,}$", message = "Formato password non valido, "
			+ "deve contenere almeno 8 caratteri, una maiuscola ed un simbolo speciale")
	private String password;
}

package com.ferraro.RegistroScolastico.dto;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Setter
public class LoginDTO {
	private String email;
	private String password;
}

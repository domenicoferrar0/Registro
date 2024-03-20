package com.ferraro.RegistroScolastico.dto;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

import com.ferraro.RegistroScolastico.exceptions.RoleNotFoundException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class LoginResponse {

	private String jwt;

	private String redirect;

	public static LoginResponse login(String jwt, Collection<? extends GrantedAuthority> auth) {
		String url = "";
		for (GrantedAuthority a : auth) {
			
			switch (a.getAuthority()) {
			
			case "ROLE_ADMIN":
				url = "/admin/home";
				break;

			case "ROLE_DOCENTE":
				url = "/docente/home";
				break;

			case "ROLE_STUDENT":
				url = "/studente/home";
				break;

			default:
				throw new RoleNotFoundException("");

			}
		}
		return new LoginResponse(jwt, url);

	}

}

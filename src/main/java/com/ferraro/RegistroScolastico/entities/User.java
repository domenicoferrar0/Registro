package com.ferraro.RegistroScolastico.entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "utenti")
@Getter
@Setter
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false)
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Formato email non valido")
	private String email;
	
	@Column(nullable = false)
	@Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[A-Z]).{8,}$", message = "Formato password non valido, "
			+ "deve contenere almeno 8 caratteri, una maiuscola ed un simbolo speciale")
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Set<Role> roles;
	
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
	private boolean isEnabled;
	
	
}

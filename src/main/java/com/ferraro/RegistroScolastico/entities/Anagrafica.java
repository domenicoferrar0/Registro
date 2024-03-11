package com.ferraro.RegistroScolastico.entities;

import java.time.LocalDate;
import com.ferraro.RegistroScolastico.enums.Genere;
import com.ferraro.RegistroScolastico.enums.Provincia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "anagrafica")
@Getter
@Setter
@NoArgsConstructor
public class Anagrafica {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Size(min = 2, max = 35, message = "Formato non valid, minimo 2 caratteri, massimo 35")
	@Pattern(regexp="\\S(\\s*[a-zA-Z]+)*\\s*", message = "Formato nome non valido, rimuovi gli spazi in eccesso e i caratteri non autorizzati")
	@Column(nullable = false)
	private String nome;
	
	@Size(min = 2, max = 35, message = "Formato non valido, minimo 2 caratteri, massimo 35")
	@Pattern(regexp="\\S(\\s*[a-zA-Z]+)*\\s*", message = "Formato nome non valido, rimuovi gli spazi in eccesso e i caratteri non autorizzati")
	@Column(nullable = false)
	private String cognome;
	
	
	@Past(message = "La data di nascita non può essere nel futuro")
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private LocalDate nascita;
		
	@Column(nullable = false, unique = true)
	@Pattern(regexp ="^(?:[A-Z][AEIOU][AEIOUX]|[AEIOU]X{2}|"
		    + "[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])B[26NS][9V])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$", message ="Formato CF non valido")
	private String cf;
	
	@Column(nullable = false)
	private String indirizzo;
		
	
	@Enumerated(EnumType.STRING)
 	@Column(nullable = false)
	private Genere genere;
	
	@Enumerated(EnumType.STRING)
 	@Column(nullable = false)
	private Provincia luogoDiNascita;
}

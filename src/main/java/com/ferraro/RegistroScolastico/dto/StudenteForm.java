package com.ferraro.RegistroScolastico.dto;

import java.time.LocalDate;

import com.ferraro.RegistroScolastico.enums.Genere;
import com.ferraro.RegistroScolastico.enums.Provincia;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudenteForm {
	@Size(min = 2, max = 35, message = "Questo campo deve avere un massimo di 35 caratteri ed un minimo di 2")
    @Pattern(regexp="\\S(\\s*[a-zA-Z]+)*\\s*", message = "Formato nome non valido, rimuovi gli spazi in eccesso e i caratteri non autorizzati")
    private String nome;

    @Size(min = 2, max = 35, message = "Questo campo deve avere un massimo di 35 caratteri ed un minimo di 2")
    @Pattern(regexp="\\S(\\s*[a-zA-Z]+)*\\s*", message = "Formato nome non valido, rimuovi gli spazi in eccesso e i caratteri non autorizzati")
    private String cognome;
    
    @Past(message = "La data di nascita non può essere nel futuro")
    @NotNull(message = "La data di nascita è obbligatoria")
    private LocalDate nascita;

    @Pattern(regexp ="^(?:[A-Z][AEIOU][AEIOUX]|[AEIOU]X{2}|"
            + "[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])B[26NS][9V])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$", message ="Formato Codice Fiscale non valido")
    private String cf;

    @NotNull(message = "L'indirizzo è obbligatorio")
    private String indirizzo;

    @NotNull(message = "Il genere è obbligatorio")
    private Genere genere;
    
    @Enumerated(EnumType.STRING)
	 @Column(nullable = false)
    private Provincia luogoDiNascita;
    
    @NotBlank
    @Pattern(regexp ="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Formato email non valido")
    private String email;
    
    @NotBlank
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[A-Z]).{8,}$", message = "Formato password non valido, "
			+ "deve contenere almeno 8 caratteri, una maiuscola ed un simbolo speciale")
    private String password;
}

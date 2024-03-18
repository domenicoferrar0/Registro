package com.ferraro.RegistroScolastico.dto;

import java.time.LocalDate;

import com.ferraro.RegistroScolastico.enums.Genere;
import com.ferraro.RegistroScolastico.enums.Materia;
import com.ferraro.RegistroScolastico.enums.Provincia;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationForm {
	
	@NotNull(message = "questo campo non può essere vuoto")
	@Size(min = 2, max = 35, message = "Questo campo deve avere un massimo di 35 caratteri ed un minimo di 2")
    @Pattern(regexp="\\S(\\s*[a-zA-Z]+)*\\s*", message = "Formato nome non valido")
    private String nome;
	
	@NotNull(message = "questo campo non può essere vuoto")
    @Size(min = 2, max = 35, message = "Questo campo deve avere un massimo di 35 caratteri ed un minimo di 2")
    @Pattern(regexp="\\S(\\s*[a-zA-Z]+)*\\s*", message = "Formato cognome non valido")
    private String cognome;
    
    @Past(message = "La data di nascita non può essere nel futuro")
    @NotNull(message = "La data di nascita è obbligatoria")
    private LocalDate nascita;
    
    @NotNull(message = "questo campo non può essere vuoto")
    @Pattern(regexp ="^(?:[A-Z][AEIOU][AEIOUX]|[AEIOU]X{2}|"
            + "[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])B[26NS][9V])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$", message ="Formato Codice Fiscale non valido")
    private String cf;

    @NotBlank(message = "L'indirizzo è obbligatorio")
    @Size(min = 2, max = 100, message = "L'indirizzo deve essere compreso tra 2 e 100 caratteri")
    private String indirizzo;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Il genere è obbligatorio")
    private Genere genere;
    
    @Enumerated(EnumType.STRING)
	@NotNull(message = "Richiesto il luogo di nascita")
    private Provincia luogoDiNascita;
    
    @NotNull
    @Pattern(regexp ="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Formato email non valido")
    private String email;
    
    
    
    @Enumerated(EnumType.STRING)
    private Materia materia;
}

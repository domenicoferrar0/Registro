package com.ferraro.RegistroScolastico.entities;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Periodo {
	
	@NotNull
	int startYear;
	
	@NotNull
	int finishYear;
	
}

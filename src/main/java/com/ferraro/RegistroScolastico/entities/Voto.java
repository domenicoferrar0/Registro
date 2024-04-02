package com.ferraro.RegistroScolastico.entities;

import java.time.LocalDate;

import com.ferraro.RegistroScolastico.enums.Materia;
import com.ferraro.RegistroScolastico.enums.Quadrimestre;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "voti")
@Getter
@Setter
@NoArgsConstructor
public class Voto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "studente_id", nullable = false)
	private Studente studente;
	
	@PastOrPresent(message = "La data non può essere nel futuro")
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private LocalDate data;
	
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "docente_id", nullable = false)
	private Docente docente;
	
	@DecimalMin(value = "0.0", message = "il voto deve essere compreso tra 0 e 10")
    @DecimalMax(value = "10.0",  message = "il voto deve essere compreso tra 0 e 10")
	@Column(nullable = false)
	private Double voto;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Materia materia;
	
	@Embedded
	@NotNull
	private Periodo periodo;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Quadrimestre quadrimestre;
	
	
}

package com.ferraro.RegistroScolastico.entities;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "assenze")
@Getter
@Setter
@NoArgsConstructor
public class Assenza {
	
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
	
	@Column(nullable = false)
	@Range(min = 1, max = 6, message = "le ore di assenza variano da 1 a 6")
	private Integer ore;
	
	@Embedded
	@Column( nullable = false)
	private Periodo periodo;

}

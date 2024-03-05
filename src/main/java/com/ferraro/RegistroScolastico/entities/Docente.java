package com.ferraro.RegistroScolastico.entities;

import java.util.Set;

import com.ferraro.RegistroScolastico.enums.Materia;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "docenti")
@Getter
@Setter
@NoArgsConstructor
public class Docente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "anagrafica_id", nullable = false, unique = true)
	private Anagrafica anagrafica;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;
	
	@OneToMany(mappedBy = "docente")
	private Set<Voto> voti;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Materia materia;
	
	@ManyToMany
	@JoinTable(name = "docenti_classi",
	joinColumns = @JoinColumn(name = "docente_id"),
	inverseJoinColumns = @JoinColumn(name = "classe_id"))
	private Set<Classe> classi;
	
	
	
	
}

package com.ferraro.RegistroScolastico.entities;

import java.util.Set;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "studenti")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "classe")
public class Studente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "anagrafica_id", nullable = false, unique = true)
	private Anagrafica anagrafica;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;
	
	@OneToMany(mappedBy = "studente", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<Voto> voti;
	
	@OneToMany(mappedBy = "studente", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<Assenza> assenze;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "classe_id", nullable = true)
	private Classe classe;
}

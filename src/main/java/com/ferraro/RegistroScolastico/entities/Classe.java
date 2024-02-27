package com.ferraro.RegistroScolastico.entities;

import java.util.Set;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "classi")
@NoArgsConstructor
@Getter
@Setter
public class Classe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(nullable = false)
	@Range(min = 1, max = 3, message = "Gli anni scolastici vanno da 1 a 3")
	private Integer anno;
	
	@Column(nullable = false)
	@Pattern(regexp = "^[A-Z]$", message = "La sezione è identificata da una singola lettera dell'alfabeto")
	private String sezione;
	
	@OneToMany(mappedBy = "classe")
	private Set<Studente> studenti;
	
	@ManyToMany(mappedBy = "classi")
	private Set<Docente> docenti;
	
	public String getNome() {
		return getAnno()+getSezione();
	}
}
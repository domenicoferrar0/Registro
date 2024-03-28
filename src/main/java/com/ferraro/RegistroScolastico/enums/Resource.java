package com.ferraro.RegistroScolastico.enums;

public enum Resource {
	STUDENTE("Studente"),
	DOCENTE("Docente"),
	VOTO("Voto"), 
	CLASSE("Classe"),
	ASSENZA("Assenza");

	private final String nome;

	Resource(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return this.nome;
	}
}

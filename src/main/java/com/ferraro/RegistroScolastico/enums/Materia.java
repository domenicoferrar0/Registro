package com.ferraro.RegistroScolastico.enums;

public enum Materia {
	ITALIANO("Italiano", 6),
    STORIA_E_GEOGRAFIA("Storia e geografia", 3),
    MATEMATICA("Matematica", 5),
    SCIENZE("Scienze", 4),
    TECNOLOGIA("Tecnologia", 2),
    INGLESE("Inglese", 4),
    ARTE_E_IMMAGINE("Arte e Immagine", 2),
    SCIENZE_MOTORIE_E_SPORTIVE("Scienze motorie e Sportive", 2),
    MUSICA("Musica", 2);

    private final String nome;
    private final int ore;

    Materia(String nome, int ore) {
        this.nome = nome;
        this.ore = ore;
    }

    public String getNome() {
        return nome;
    }

    public int getOre() {
        return ore;
    }
}

package com.ferraro.RegistroScolastico.enums;

public enum Materia {
	ITALIANO("Italiano"),
    STORIA("Storia"),
    GEOGRAFIA("Geografia"),
    MATEMATICA("Matematica"),
    SCIENZE("Scienze"),
    TECNOLOGIA("Tecnologia"),
    INGLESE("Inglese"),
    FRANCESE("Francese"),
    ARTE_E_IMMAGINE("Arte e Immagine"),
    SCIENZE_MOTORIE_E_SPORTIVE("Scienze motorie e Sportive"),
    MUSICA("Musica");

    private final String nome;
    

    Materia(String nome) {
        this.nome = nome;
        
    }

    public String getNome() {
        return nome;
    }

    
}

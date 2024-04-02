package com.ferraro.RegistroScolastico.enums;

public enum Quadrimestre {
	PRIMO, SECONDO;

	public static Quadrimestre determinaQuadrimestre(int month) {
		if (month == 1 || month >= 9 && month <= 12) {
			return Quadrimestre.PRIMO;
		}
		return Quadrimestre.SECONDO;
	}
}
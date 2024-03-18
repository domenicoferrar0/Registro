package com.ferraro.RegistroScolastico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagineController {
	
	@GetMapping("/login")
	public String showLoginPage() {
		return "pagelogin";
	}
	
	@GetMapping("/studente/home")
	public String showHomePage() {
		return "studente-homepage";
	}
	
	@GetMapping("/studente/voti")
	public String showVotiStudente() {
		return "studente-voti";
	}
	
	@GetMapping("/studente/assenze")
	public String showAssenzeStudente() {
		return "studente-assenze";
	}
	@GetMapping("/home/registrazione-studente")
	public String showFormStudente() {
		return "studente-registration";
	}
	
}

package com.ferraro.RegistroScolastico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
	
	@GetMapping("/studente/classe")
	public String showClasseStudente() {
		return "studente-classe";
	}
	
	@GetMapping("/home/registrazione/studente")
	public String showFormStudente() {
		return "studente-registration";
	}
	
	@GetMapping("/home/registrazione/docente")
	public String showFormDocente() {
		return "docente-registration";
	}
	
	
	
	@GetMapping("/home/confirm")
	public String showMailConfirmation() {
		return "mail-confirmation";
	}
	
	@GetMapping("/docente/form-voti/classe/{classeId}")
	public String showVotoForm(@PathVariable("classeId") Long classeId) {
		return "docente-form-voti";
	}
	
}

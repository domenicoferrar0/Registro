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
	
}

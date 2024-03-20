package com.ferraro.RegistroScolastico.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import com.ferraro.RegistroScolastico.service.MyUserDetails;

@EnableWebSecurity
@Configuration
@Slf4j
public class SecurityConfig {
	
	final private String API = "/rest/api/v0";

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private JwtAuthFilter jwtFilter;
	
	@Autowired
	private MyUserDetails userDetails;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
			log.debug("Auth managaer initialized");
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(registry -> { //Per avere sia le API che gli url regolari autorizzati in base al ruolo
			registry.requestMatchers(API.concat("/admin/**"),"/admin/**").hasAuthority("ROLE_ADMIN");
			registry.requestMatchers(API.concat("/studente/**"), "/studente/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_STUDENT");
			registry.requestMatchers(API.concat("/docente/**"), "/docente/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_DOCENTE");
			registry.requestMatchers(API.concat("/home/**"), "/home/**").permitAll();
			registry.anyRequest().authenticated();

		}).formLogin(httpSecurityFormLoginConfigurer -> {
			httpSecurityFormLoginConfigurer.loginPage("/login")			
			.permitAll();
		}).httpBasic(Customizer.withDefaults())
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
	}

	

	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetails);
		log.debug("auth Provider initialized");
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	

}

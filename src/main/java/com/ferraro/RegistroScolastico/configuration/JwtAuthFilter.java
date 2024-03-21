package com.ferraro.RegistroScolastico.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ferraro.RegistroScolastico.exceptions.UserNotEnabledException;
import com.ferraro.RegistroScolastico.service.JwtService;
import com.ferraro.RegistroScolastico.service.UserDetailsImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
	
	@Autowired
	private final JwtService jwtService;
	
	@Autowired
	private final UserDetailsImpl myUserDetails;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		log.info("INSIDE THE FILTER");
		final String authHeader = request.getHeader("Authorization");

		final String userEmail;

		final String jwt;

		log.info("header {}", authHeader);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			log.info("No auth");
			return;
		}
		log.info("JWT present");
		jwt = authHeader.substring(7);
		log.info("prima di estrarre");
		userEmail = jwtService.extractUsername(jwt);
		log.info("post estrazione");
		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails;
			try {
				userDetails = this.myUserDetails.loadUserByUsername(userEmail);
			} catch (UserNotEnabledException e) {
				filterChain.doFilter(request, response);
				return;
			}

			if (jwtService.isTokenValid(jwt, userDetails)) {
				log.info("token valido");
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		log.info("autenticato");
		filterChain.doFilter(request, response);
	}

}

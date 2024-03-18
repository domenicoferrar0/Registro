package com.ferraro.RegistroScolastico.entities;

import java.time.LocalDateTime;
import java.util.UUID;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "confirmation_token")
@Getter
@Setter
@NoArgsConstructor
public class ConfirmationToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String confirmationToken;
	
	
	@Column(nullable = false)
	private LocalDateTime createdDate;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;
	
	public ConfirmationToken(User user) {
		String token = UUID.randomUUID().toString();
		LocalDateTime currentDate = LocalDateTime.now();
		this.confirmationToken = token;
		this.createdDate = currentDate;
		this.user = user;
	}
}

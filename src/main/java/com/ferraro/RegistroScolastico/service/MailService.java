package com.ferraro.RegistroScolastico.service;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.ferraro.RegistroScolastico.dto.RegistrationForm;
import com.ferraro.RegistroScolastico.entities.ConfirmationToken;
import com.ferraro.RegistroScolastico.entities.User;
import com.ferraro.RegistroScolastico.exceptions.ConfirmationTokenNotFoundException;
import com.ferraro.RegistroScolastico.repository.ConfirmationTokenRepository;
import com.ferraro.RegistroScolastico.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Autowired
	private ConfirmationTokenRepository tokenRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Transactional
	public void sendRegistrationEmail(RegistrationForm form, String pw, String token)
			throws MessagingException {
		log.info("INSIDE THE MAIL SENDER");
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_NO,
				StandardCharsets.UTF_8.name());

		Context context = new Context();
		context.setVariable("name", form.getNome() + " " + form.getCognome());
		context.setVariable("password", pw);
		context.setVariable("token", token);
		String html = templateEngine.process("mail-template", context);

		helper.setTo(form.getEmail());
		helper.setSubject("Welcome " + form.getNome());
		helper.setText(html, true);
		javaMailSender.send(message);
		log.info("MAIL SENT"); 

	}
	
	@Transactional
	public String createToken(User user) {
		ConfirmationToken token = new ConfirmationToken(user);
		tokenRepository.save(token);
		return token.getConfirmationToken();
	}
	
	@Transactional
	public boolean confirmEmail(String token) {
		ConfirmationToken confirmationToken = tokenRepository.findByConfirmationToken(token)
				.orElseThrow(() -> new ConfirmationTokenNotFoundException(token));

		User user = confirmationToken.getUser();
		if (user == null || user.isEnabled()) {
			return false;
		}
		user.setEnabled(true);
		tokenRepository.delete(confirmationToken);
		userRepository.save(user);
		return true;
	}
}

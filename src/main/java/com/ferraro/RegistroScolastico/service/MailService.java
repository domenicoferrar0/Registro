package com.ferraro.RegistroScolastico.service;



import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.ferraro.RegistroScolastico.dto.RegistrationForm;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Service
@Slf4j
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	
	public void sendRegistrationEmail(RegistrationForm form, String pw, String token) throws MessagingException, MessagingException {
			log.info("INSIDE THE MAIL SENDER");
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_NO, StandardCharsets.UTF_8.name());
		
			Context context = new Context();
			context.setVariable("name", form.getNome()+" "+form.getCognome());
			context.setVariable("password", pw);
			context.setVariable("token", token);
			String html = templateEngine.process("mail-template", context);
			
			helper.setTo(form.getEmail());
			helper.setSubject("Welcome "+form.getNome());
			helper.setText(html, true);
			javaMailSender.send(message);
			log.info("MAIL SENT");
			
	} 
}

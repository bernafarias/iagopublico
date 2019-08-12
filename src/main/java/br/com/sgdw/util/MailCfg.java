package br.com.sgdw.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailCfg {

	private static String toEmail;
	
	@Autowired
	private MailCfg(@Value("${app.mail}") String email){
		setToEmail(email);
	}

	public static String getToEmail() {
		return toEmail;
	}

	private static void setToEmail(String toEmail) {
		MailCfg.toEmail = toEmail;
	}
	
}

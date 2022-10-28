package com.zezoo.emergency;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends Authenticator {
	public SMTPAuthenticator() {

		super();
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		String username = "youremail@gmail";
		String password = "password";
		if ((username != null) && (username.length() > 0) && (password != null) && (password.length() > 0)) {

			return new PasswordAuthentication(username, password);
		}

		return null;
	}
}

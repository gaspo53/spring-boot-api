package com.example.api.service;

import com.example.api.entities.Mail;

/**
 * Service to handle the mailing process.<br>
 *
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
public interface MailService{

	/**
	 * 
     * Sends the Mail received.
     * 
	 * @param mail
	 */
	void send(Mail mail);

}

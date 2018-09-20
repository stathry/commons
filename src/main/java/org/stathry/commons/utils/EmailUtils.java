/**
 * Copyright 2016-2100 free Co., Ltd.
 */
package org.stathry.commons.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathry.commons.pojo.config.EmailConfig;

/**
 * 
 * @author dongdaiming@free.com
 *
 *         2016年8月16日
 */
public class EmailUtils {

	private static Logger LOGGER = LoggerFactory.getLogger(EmailUtils.class);

	private static final String DEFAULT_CHARTSET = "UTF-8";

	private static  EmailConfig emailConfig;
	
	static {
		emailConfig = ApplicationContextUtils.getBean(EmailConfig.class);
	}

	public static void sendHtmlEmail(String subject, String msg, String to) {
		sendHtmlEmail(subject, msg, null, new String[]{to}, null, null);
	}
	
	public static void sendHtmlEmail(String subject, String msg, String[] to) {
		sendHtmlEmail(subject, msg, null, to, null, null);
	}
	
	public static void sendHtmlEmail(String subject, String msg, String[] to, String[] cc) {
		sendHtmlEmail(subject, msg, null, to, cc, null);
	}
	
	public static void sendHtmlEmail(String subject, String msg, String[] to, String[] cc, String[] bcc) {
		sendHtmlEmail(subject, msg, null, to, cc, bcc);
	}
	
	public static void sendHtmlEmail(String subject, String msg, EmailConfig config, String[] to, String[] cc, String[] bcc) {
		
		final HtmlEmail email = new HtmlEmail();
		
		try {
			init(email,config,subject, to);
		} catch (EmailException e) {
			LOGGER.warn("init email conf error.", e);
			return ;
		}

		try {
			email.setHtmlMsg("<html>" + msg + "</html>");
			email.setTextMsg("Your email client does not support HTML messages");
		} catch (EmailException e) {
			LOGGER.warn("set msg error", e);
			return ;
		}
		
		try {
			setDest(email, to, cc, bcc);
		} catch (UnsupportedEncodingException e) {
			LOGGER.warn("The Character Encoding is not supported", e);
			return ;
		} catch (EmailException e) {
			LOGGER.warn("Indicates an invalid email address", e);
			return ;
		}

		
		sendEmail(email, "t-sendHtmlEmail");
		
	}

	public static void sendSimpleEmail(String subject, String msg, String to) {
		sendSimpleEmail(subject, msg, null, new String[]{to}, null, null);
	}
	
	public static void sendSimpleEmail(String subject, String msg, String[] to) {
		sendSimpleEmail(subject, msg, null, to, null, null);
	}
	
	public static void sendSimpleEmail(String subject, String msg, String[] to, String[] cc) {
		sendSimpleEmail(subject, msg, null, to, cc, null);
	}
	
	public static void sendSimpleEmail(String subject, String msg, String[] to, String[] cc, String[] bcc) {
		sendSimpleEmail(subject, msg, null, to, cc, bcc);
	}
	
	public static void sendSimpleEmail(String subject, String msg, EmailConfig config, String[] to, String[] cc, String[] bcc) {
		final Email email = new SimpleEmail();
		
		try {
			init(email,config,subject, to);
		} catch (EmailException e) {
			LOGGER.warn("Indicates an invalid email address.", e);
			return ;
		}
		
		try {
			email.setMsg(msg);
		} catch (EmailException e) {
			LOGGER.warn("generic msg exception", e);
		}

		try {
			setDest(email, to, cc, bcc);
		} catch (UnsupportedEncodingException e) {
			LOGGER.warn("The Character Encoding is not supported", e);
			return ;
		} catch (EmailException e) {
			LOGGER.warn("Indicates an invalid email address", e);
			return ;
		}

		sendEmail(email, "t-sendSimpleEmail");
	}

	public static void sendWithAttach(String subject, String msg, EmailAttachment attach, String to) {
		sendWithAttach(subject, msg, null, attach, new String[]{to}, null, null);
	}
	
	public static void sendWithAttach(String subject, String msg, EmailAttachment attach, String[] to) {
		sendWithAttach(subject, msg, null, attach, to, null, null);
	}
	
	public static void sendWithAttach(String subject, String msg, EmailAttachment attach, String[] to, String[] cc) {
		sendWithAttach(subject, msg, null, attach, to, cc, null);
	}
	
	public static void sendWithAttach(String subject, String msg, EmailAttachment attach, String[] to, String[] cc, String[] bcc) {
		sendWithAttach(subject, msg, null, attach, to, cc, bcc);
	}
	
	public static void sendWithAttach(String subject, String msg, EmailConfig config, EmailAttachment attach, String[] to, String[] cc, String[] bcc) {
		if (StringUtils.isBlank(subject) || to == null || to.length < 1) {
			LOGGER.warn("subject or toAddress must not be null");
			return ;
		}

		if (config == null) {
			config = emailConfig;
		}

		final MultiPartEmail email = new MultiPartEmail();
		
		try {
			init(email,config,subject, to);
		} catch (EmailException e) {
			LOGGER.warn("Indicates an invalid email address.", e);
			return ;
		}
		
		try {
			email.setMsg(msg);
		} catch (EmailException e) {
			LOGGER.warn("generic msg exception", e);
		}

		try {
			setDest(email, to, cc, bcc);
		} catch (UnsupportedEncodingException e) {
			LOGGER.warn("The Character Encoding is not supported", e);
			return ;
		} catch (EmailException e) {
			LOGGER.warn("Indicates an invalid email address", e);
			return ;
		}

		sendEmail(email, "t-sendWithAttach");
	}

	private static List<InternetAddress> toInternetAddress(String[] emails)
			throws UnsupportedEncodingException, EmailException {
		if (emails == null || emails.length < 1) {
			return null;
		}

		List<InternetAddress> add = new ArrayList<InternetAddress>();

		for (String email : emails) {
			add.add(createInternetAddress(email, null, DEFAULT_CHARTSET));
		}
		return add;
	}

	private static InternetAddress createInternetAddress(final String email,
			final String name, final String charsetName) throws EmailException {
		InternetAddress address = null;

		try {
			address = new InternetAddress(email);

			// check name input
			if (StringUtils.isNotEmpty(name)) {
				// check charset input.
				if (StringUtils.isEmpty(charsetName)) {
					address.setPersonal(name);
				} else {
					// canonicalize the charset name and make sure
					// the current platform supports it.
					final Charset set = Charset.forName(charsetName);
					address.setPersonal(name, set.name());
				}
			}

			// run sanity check on new InternetAddress object; if this fails
			// it will throw AddressException.
			address.validate();
		} catch (final AddressException e) {
			throw new EmailException(e);
		} catch (final UnsupportedEncodingException e) {
			throw new EmailException(e);
		}
		return address;
	}
	
	private static void init(Email email, EmailConfig config, String subject, String[] to) throws EmailException {
		if (StringUtils.isBlank(subject) || to == null || to.length < 1) {
			LOGGER.warn("subject or toAddress must not be null");
			throw new EmailException("subject or toAddress must not be null");
		}

		if (config == null) {
			config = emailConfig;
		}
		
		email.setCharset(DEFAULT_CHARTSET);
		email.setHostName(config.getServerHostName());
		email.setSmtpPort(config.getServerSmtpPort());
		email.setAuthenticator(new DefaultAuthenticator(config.getServerUsername(), config.getServerPassword()));
		email.setSSLOnConnect(config.isServerUseSSL());
		email.setSubject(subject);
		email.setFrom(config.getServerFrom());
	}
	
	private static void setDest(Email email, String[] to, String[] cc, String[] bcc) throws UnsupportedEncodingException, EmailException {
		email.setTo(toInternetAddress(to));
		if (cc != null && cc.length > 1) {
			email.setCc(toInternetAddress(cc));
		}
		if (bcc != null && bcc.length > 1) {
			email.setBcc(toInternetAddress(bcc));
		}
	}
	
	//TODO SendEmailThread
	private static void sendEmail(Email email, String threadName) {
		
	}

}

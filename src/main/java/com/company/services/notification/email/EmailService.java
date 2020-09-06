package com.company.services.notification.email;

import com.company.services.logger.ILogger;
import com.typesafe.config.Config;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailService {
    static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    static Properties props;
    Config config;
    ILogger logger;

    public EmailService(Config config, ILogger logger) {
        this.config = config;
        this.logger = logger;
    }

    public static void initializeProps() {
        if (props == null) {
            props = System.getProperties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.port", "465");
            props.setProperty("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "true");
            props.put("mail.store.protocol", "pop3");
            props.put("mail.transport.protocol", "smtp");
        }
    }
    public void sendEmail(EmailMessage message) {
        try {
            initializeProps();
            Session session = Session.getDefaultInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(config.getString("configuration.username"), config.getString("configuration.password"));
                        }
                    });

            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(config.getString("configuration.from")));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(config.getString("configuration.to"), false));//check if can parse to custom object
            msg.setSubject(message.getSubject());
            msg.setText(message.getText());
            msg.setSentDate(new Date());
            Transport.send(msg);
            logger.log("Message sent.");
        } catch(Throwable t) {
            logger.log("ERROR: sending message");
        }
    }
}

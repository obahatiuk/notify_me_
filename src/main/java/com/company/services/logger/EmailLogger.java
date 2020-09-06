package com.company.services.logger;

import com.company.services.notification.email.EmailMessage;
import com.company.services.notification.email.EmailService;

public class EmailLogger implements ILogger{
    private EmailService emailService;
    public EmailLogger(EmailService emailService) {
        emailService = emailService;
    }
    @Override
    public void log(String message) {
        EmailMessage em = new EmailMessage();
        em.setSubject("ERROR");
        em.setText(message);
        emailService.sendEmail(em);
    }
}

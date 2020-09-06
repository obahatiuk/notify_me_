package com.company.services.notification;

import com.company.services.notification.email.EmailMessage;
import com.company.services.notification.email.EmailService;

public class EmailNotificationService implements IObserver{
    private EmailService emailService;

    public EmailNotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void update() {
        EmailMessage em = new EmailMessage();
        em.setSubject("html changed");
        em.setText("html changed");
        emailService.sendEmail(em);
    }
}

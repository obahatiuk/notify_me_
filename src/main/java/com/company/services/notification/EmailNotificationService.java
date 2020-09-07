package com.company.services.notification;

import com.company.services.notification.email.EmailMessage;
import com.company.services.notification.email.EmailService;

public class EmailNotificationService implements IObserver{
    private EmailService emailService;

    public EmailMessage getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    EmailMessage emailMessage;

    public EmailNotificationService(EmailService emailService, EmailMessage emailMessage) {
        this.emailService = emailService;
        this.emailMessage = emailMessage;
        if ( this.emailMessage == null ) {
            this.emailMessage = new EmailMessage();
            this.emailMessage.setSubject("html changed");
            this.emailMessage.setText("html changed");
        }
    }

    @Override
    public void update() {
        emailService.sendEmail(this.emailMessage);
    }
}

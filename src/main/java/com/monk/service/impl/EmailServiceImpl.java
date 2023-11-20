package com.monk.service.impl;

import com.monk.model.entity.MonkUser;
import com.monk.service.EmailServiceI;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailServiceI {

    @Value("${smtp.hostname}")
    private final String SMTP_HOSTNAME;

    @Value("${smtp.username}")
    private final String SMTP_USERNAME;

    @Value("${smtp.password}")
    private final String SMTP_PASSWORD;

    private final TemplateEngine templateEngine;

    @Override
    public void sendAdminNotificationEmail(String message) throws MessagingException {

        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(SMTP_HOSTNAME);
        mailSender.setUsername(SMTP_USERNAME);
        mailSender.setPassword(SMTP_PASSWORD);
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");


        final MimeMessage adminMimeMessage = mailSender.createMimeMessage();

        adminMimeMessage.setFrom("support@monk-digital.com");
        adminMimeMessage.setSubject(message);
        adminMimeMessage.setText("A contact request has been made check the database");
        adminMimeMessage.addRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse("emre.caniklioglu.00@gmail.com")
        );

        mailSender.send(adminMimeMessage);
    }

    @Override
    public void sendContactEmail(String to, String message) throws MailException, MessagingException {

        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(SMTP_HOSTNAME);
        mailSender.setUsername(SMTP_USERNAME);
        mailSender.setPassword(SMTP_PASSWORD);
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");

        final Context ctx = new Context();
        ctx.setVariable("message", message);

        final MimeMessage mimeMessage = mailSender.createMimeMessage();

        mimeMessage.setFrom("support@monk-digital.com");
        mimeMessage.setSubject("Contact request received");

        final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                "UTF-8"
        );

        mimeMessageHelper.setTo(to);

        final String htmlTemplate = templateEngine.process("html/contact-request.html", ctx);
        mimeMessageHelper.setText(htmlTemplate, true);
        mailSender.send(mimeMessage);

        sendAdminNotificationEmail("Contact request has been made");
    }

    @Override
    public void sendEmailValidationMail(MonkUser monkUser, String token, long expiration) throws MessagingException {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(SMTP_HOSTNAME);
        mailSender.setUsername(SMTP_USERNAME);
        mailSender.setPassword(SMTP_PASSWORD);
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");

        final Context ctx = new Context();
        ctx.setVariable("username", monkUser.getUsername());
        ctx.setVariable("expiration", expiration);
        ctx.setVariable("token", token.replaceAll("\\.", "%"));

        final MimeMessage mimeMessage = mailSender.createMimeMessage();

        mimeMessage.setFrom("support@monk-digital.com");
        mimeMessage.setSubject("Validate your email");

        final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                "UTF-8"
        );

        mimeMessageHelper.setTo(monkUser.getEmail());

        final String htmlTemplate = templateEngine.process("html/email-validation.html", ctx);
        mimeMessageHelper.setText(htmlTemplate, true);
        mailSender.send(mimeMessage);
    }
}

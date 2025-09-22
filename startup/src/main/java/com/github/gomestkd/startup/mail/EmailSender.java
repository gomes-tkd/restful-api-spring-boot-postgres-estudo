package com.github.gomestkd.startup.mail;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
public class EmailSender implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    private final JavaMailSender mailSender;

    private String to;
    private String subject;
    private String body;
    private Set<InternetAddress> recipients = Collections.emptySet();
    private File attachment;


    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = Objects.requireNonNull(mailSender, "JavaMailSender must not be null");
    }

    public EmailSender to(String to) {
        this.to = to;
        this.recipients = parseRecipients(to);
        return this;
    }

    public EmailSender withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailSender withBody(String body) {
        this.body = body;
        return this;
    }

    public EmailSender attachment(String fileDir) {
        if (fileDir != null && !fileDir.isBlank()) {
            this.attachment = new File(fileDir);
        }
        return this;
    }

    public void send(EmailConfig config) {
        Objects.requireNonNull(config, "EmailConfig must not be null");

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(config.getUsername);
            helper.setTo(this.recipients.toArray(new InternetAddress[0]));
            helper.setSubject(this.subject);
            helper.setText(this.body, true);

            if (this.attachment != null && this.attachment.exists()) {
                helper.addAttachment(this.attachment.getName(), this.attachment);
            }

            mailSender.send(message);
            logger.info("Email sent to {} with the subject '{}'", to, subject);

            reset();
        } catch (Exception e) {
            logger.error("Failed to send email to {} with subject '{}'", to, subject, e);
            throw new RuntimeException("Error sending the email", e);
        }
    }

    private void reset() {
        this.to = null;
        this.subject = null;
        this.body = null;
        this.recipients = Collections.emptySet();
        this.attachment = null;
    }

    private Set<InternetAddress> parseRecipients(String to) {
        if (to == null || to.isBlank()) {
            return Collections.emptySet();
        }

        Set<InternetAddress> recipientsSet = new HashSet<>();
        String[] addresses = to.replaceAll("\\s", "").split(",");

        for (String address : addresses) {
            try {
                recipientsSet.add(new InternetAddress(address));
            } catch (Exception e) {
                logger.error("Invalid email address: {}", address, e);
            }
        }
        return Collections.unmodifiableSet(recipientsSet);
    }
}

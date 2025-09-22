package com.github.gomestkd.startup.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gomestkd.startup.config.EmailConfig;
import com.github.gomestkd.startup.data.dto.request.EmailRequestDTO;
import com.github.gomestkd.startup.mail.EmailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    private final EmailSender emailSender;
    private final EmailConfig emailConfig;

    public EmailService(EmailSender emailSender, EmailConfig emailConfig) {
        this.emailSender = emailSender;
        this.emailConfig = emailConfig;
    }

    public void sendSimpleEmail(EmailRequestDTO emailRequestDTO) {
        emailSender.to(emailRequestDTO.getTo())
                .withSubject(emailRequestDTO.getSubject())
                .withBody(emailRequestDTO.getBody())
                .send(emailConfig);
    }

    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment) {
        File tempFile = null;
        try {
            EmailRequestDTO emailRequestDTO = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);
            tempFile = File.createTempFile("attachment-", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);

            emailSender.to(emailRequestDTO.getTo())
                    .withSubject(emailRequestDTO.getSubject())
                    .withBody(emailRequestDTO.getBody())
                    .attachment(tempFile.getAbsolutePath())
                    .send(emailConfig);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing email request JSON", e);
        } catch (IOException e) {
            throw new RuntimeException("Error handling email attachment", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
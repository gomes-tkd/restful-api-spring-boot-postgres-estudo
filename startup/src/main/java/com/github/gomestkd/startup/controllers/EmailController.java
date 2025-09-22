package com.github.gomestkd.startup.controllers;

import com.github.gomestkd.startup.controllers.docs.EmailControllerDocs;
import com.github.gomestkd.startup.data.dto.request.EmailRequestDTO;
import com.github.gomestkd.startup.services.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/email")
@Tag(name = "E-mail")
public class EmailController implements EmailControllerDocs {

    private final EmailService service;

    public EmailController(EmailService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequest) {
        service.sendSimpleEmail(emailRequest);
        return ResponseEntity.ok("e-Mail sent with success!");
    }

    @PostMapping(
            value = "/attachment",
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(
            @RequestParam("emailRequestJson") String emailRequestJson,
            MultipartFile attachment
    ) {
        service.sendEmailWithAttachment(emailRequestJson, attachment);
        return ResponseEntity.ok("e-Mail with attachment sent with success!");
    }
}
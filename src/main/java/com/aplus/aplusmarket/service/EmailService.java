package com.aplus.aplusmarket.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;


    public void sendVerificationEmail(String email, String code) {
        System.out.println("인증 코드 발송: " + email + " -> " + code);

        try{
            ClassPathResource resource = new ClassPathResource("templates/email_verification.html");
            String template = new String(Files.readAllBytes(resource.getFile().toPath()), "UTF-8");
            String htmlContent = template.replace("{{code}}",code);

            sendEmail(email, "APPLUS Market 이메일 인증",htmlContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public void sendEmail(String to, String subject, String htmlContent){

        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent,true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

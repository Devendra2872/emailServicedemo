package com.example.emailServicemwf.consumers;

import com.example.emailServicemwf.dtos.SendEmailEventDto;
import com.example.emailServicemwf.utils.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class SendEmailEventConsumer {
    private ObjectMapper objectMapper;

    public SendEmailEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void handleSendEmailEvent(String message) throws JsonProcessingException {
        SendEmailEventDto event = objectMapper.readValue(message, SendEmailEventDto.class);


        String to = event.getTo();
        String body = event.getBody();
        String from = event.getFrom();
        String subject = event.getSubject();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("infotostalin1234@gmail.com", "ujghmialzjwgymvb");
            }
        };
        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session, to,subject, body);
    }
}

package in.n2w.boot.events.listeners;

import in.n2w.boot.entities.User;
import in.n2w.boot.events.RegoCompleteEvent;
import in.n2w.boot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by Karanbir Singh on 4/18/2019.
 **/
@Component
public class RegoListener implements ApplicationListener<RegoCompleteEvent> {

    @Autowired
    private UserService service;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;


    @Override
    public void onApplicationEvent(final RegoCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final RegoCompleteEvent event) {
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);

        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        javaMailSender.send(email);
    }

    //

    private SimpleMailMessage constructEmailMessage(final RegoCompleteEvent event, final User user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Please open the following URL to verify your account: \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}

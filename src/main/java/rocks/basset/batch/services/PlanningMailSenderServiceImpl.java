package rocks.basset.batch.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
public class PlanningMailSenderServiceImpl implements PlanningMailSenderService {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(String destination, String content) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        message.setContent(content, "text/html");
        helper.setTo(destination);
        helper.setSubject("Votre planning de formations");

        javaMailSender.send(message);
    }
}

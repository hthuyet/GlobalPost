package vn.vnpt.ssdc.common.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * Created by THANHLX on 5/11/2017.
 */
@Service
public class MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
    @Autowired
    private AsyncService asyncService;
    private Session session;
    private String from;

    @Autowired
    public MailService(final Environment environment) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", environment.getRequiredProperty("spring.mail.host"));
            props.put("mail.smtp.port", environment.getRequiredProperty("spring.mail.port"));
            from = environment.getRequiredProperty("spring.mail.username");
            session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(environment.getRequiredProperty("spring.mail.username"),environment.getRequiredProperty("spring.mail.password"));
                        }
                    });
        } catch (Exception ex) {
            LOGGER.error("Could not find mail session. {}", ex.getMessage(), ex);
        }

    }

    public void sendMail(String recipient, String subject, String body, String fileName, DataSource dataSource) {
        LOGGER.debug("Send email to: {}, subject: {}", recipient, subject);
        if (recipient == null || subject == null || body == null) {
            throw new IllegalArgumentException();
        }

        if (dataSource != null) {
            asyncService.execute(new SendMailTask(from, recipient, subject, body, dataSource));
        } else {
            asyncService.execute(new SendMailTask(from, recipient, subject, body, fileName));
        }
    }

    private class SendMailTask implements Runnable {

        private String from;
        private String recipient;
        private String subject;
        private String body;
        private String fileName;
        private DataSource dataSource;

        public SendMailTask(String from, String recipient, String subject, String body, String fileName) {
            this.from = from;
            this.recipient = recipient;
            this.subject = subject;
            this.body = body;
            this.fileName = fileName;
        }

        public SendMailTask(String from, String recipient, String subject, String body, DataSource source) {
            this.from = from;
            this.recipient = recipient;
            this.subject = subject;
            this.body = body;
            this.dataSource = source;
            this.fileName = source.getName();
        }

        @Override
        public void run() {
            try {
                if (fileName == null || fileName.length() == 0) {
                    MimeMessage msg = new MimeMessage(session);
                    msg.setContent(body, "text/html; charset=utf-8");
                    msg.setSubject(subject);
                    msg.setFrom(new InternetAddress(from,"UMP DevTeam"));
                    msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                    Transport.send(msg);
                } else {
                    MimeMessage msg = new MimeMessage(session);
                    msg.setSubject(subject);
                    msg.setFrom(new InternetAddress(from));
                    msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent(body, "text/html; charset=utf-8");
                    Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(messageBodyPart);

                    // Attachment if any
                    messageBodyPart = new MimeBodyPart();
                    if (dataSource != null) {
                        messageBodyPart.setDataHandler(new DataHandler(dataSource));
                        messageBodyPart.setFileName(dataSource.getName());
                    } else {
                        DataSource source = new FileDataSource(fileName);
                        messageBodyPart.setDataHandler(new DataHandler(source));
                        messageBodyPart.setFileName(source.getName());
                    }
                    multipart.addBodyPart(messageBodyPart);
                    msg.setContent(multipart, "text/html; charset=utf-8");
                    Transport.send(msg);
                }
            } catch (Exception ex) {
                LOGGER.error("Failed to send email to {}.", recipient, ex);
            }
        }
    }
}

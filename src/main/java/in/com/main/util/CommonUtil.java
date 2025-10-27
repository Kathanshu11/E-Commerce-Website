package in.com.main.util;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;  // ✅ must NOT be static

    // ✅ also remove static from this method
    public Boolean email(String url, String recipientEmail)
            throws UnsupportedEncodingException, MessagingException, MailException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("kathanshupatil11@gmail.com", "MyApp Support");
        helper.setTo(recipientEmail);

        helper.setSubject("Password Reset");

        String content = "<p>Hello,</p>"
                + "<p>Click the link below to reset your password:</p>"
                + "<p><a href=\"" + url + "\">Reset Password</a></p>"
                + "<p>If you didn’t request this, please ignore this email.</p>";

        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }

    // static is fine here — no dependencies
    public static String genrateUrl(HttpServletRequest req) {
        return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
    }
}

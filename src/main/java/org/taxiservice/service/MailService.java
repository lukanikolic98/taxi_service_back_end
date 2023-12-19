package org.taxiservice.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Service
public class MailService {
  private static final Logger logger = LoggerFactory.getLogger(MailService.class);

  public String sendTextEmail(String reciver, String registrationKey) throws IOException {
    // the sender email should be the same as we used to Create a Single Sender
    // Verification
    Email from = new Email("lukadroid98@gmail.com");
    String subject = "Account activation";
    Email to = new Email(reciver);
    Content content = new Content("text/plain",
        "This is a test email. To activate your account, go to: 'localhost:4200/confirm/?regKey=" + registrationKey
            + "'");
    Mail mail = new Mail(from, subject, to, content);

    SendGrid sg = new SendGrid("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\r\n" + //
        "");
    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      logger.info(response.getBody());
      return response.getBody();
    } catch (IOException ex) {
      throw ex;
    }
  }
}
package in.tayana.service;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import in.tayana.model.EmailRequest;
import java.io.File;

import java.util.Arrays;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServices {

	@Autowired
	private JavaMailSender emailSender;


	  private static final Logger requestLogger = LogManager.getLogger(EmailServices.class);


	public boolean sendEmail(EmailRequest request) throws Exception {
		// Generating a unique request ID
		ThreadContext.put("reportId", request.getReport_id().toString());

		MimeMessage mimeMessage = emailSender.createMimeMessage();
		requestLogger.info("Request started");

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			// Set the 'To' recipients
			requestLogger.info("verifying To list");
			List<String> listForTo = EmailServices.HalperClass.emailValidation(request.getTo(), 1);
			if (listForTo == null || listForTo.isEmpty()) {
				requestLogger.warn("No valid 'To' email addresses provided.");
				requestLogger.info("verifying Bcc list");
				List<String> listForBcc = EmailServices.HalperClass.emailValidation(request.getBcc(), 3);
				if (listForBcc == null || listForBcc.isEmpty()) {
					requestLogger.warn("No valid 'Bcc' email addresses provided.");
					return false;
				} else {
					requestLogger.info("Set all 'Bcc' recipients");
					helper.setBcc(listForBcc.toArray(new String[0]));
					
				}
				
			} else {
				requestLogger.info("Set all 'To' recipients");
				helper.setTo(listForTo.toArray(new String[0]));
				
				// Set the 'Bcc' recipients
				requestLogger.info("verifying Bcc list");
				List<String> listForBcc = EmailServices.HalperClass.emailValidation(request.getBcc(), 3);
				if (listForBcc == null) {
					requestLogger.error("No valid 'Bcc' email addresses provided.");
					
				} else {
					requestLogger.info("Set all 'Bcc' recipients");
					helper.setBcc(listForBcc.toArray(new String[0]));
				}
			}

			// Set the 'Cc' recipients
			requestLogger.info("verifying Cc list");
			List<String> listForCc = EmailServices.HalperClass.emailValidation(request.getCc(), 2);
			if (listForCc == null||listForCc.isEmpty()) {
				requestLogger.warn("No valid 'Cc' email addresses provided.");
			} else {
				requestLogger.info("Set all 'Cc' recipients");
				helper.setCc(listForCc.toArray(new String[0]));
			}

	


			// email subject and body
			helper.setSubject(request.getSubject());
			helper.setText(request.getBody_file(), true);

			// attachments
			if (request.getAttachment() != null) {
				requestLogger.info("Checking all attachments");
				for (String attachmentPath : request.getAttachment()) {
					File file = new File(attachmentPath);
					if (file.exists()) {
						helper.addAttachment(file.getName(), file);
					} else {
						requestLogger.warn("Attachment file does not exist: " + attachmentPath);
					}
				}
			} else {
				requestLogger.info("No attachments provided");
			}

			// Send the email
			emailSender.send(mimeMessage);
			requestLogger.info("Email successfully sent");
		} catch (MessagingException e) {
			requestLogger.error("Error sending email: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			requestLogger.error("Error in email sending process: " +e.getMessage());
			throw e;
		}
		finally {
			ThreadContext.remove("request_id");

		}

		requestLogger.info("Request completed");
		return true;
	}

	
	
	private static class HalperClass {

		private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

		private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

		public static List<String> emailValidation(String emails, Integer code) {
			if (emails == null) {
				return null;
			}
			return Arrays.asList(emails.split(",")).stream().filter(email -> {
				Matcher matcher = pattern.matcher(email);
				boolean flag = matcher.matches();
				if (!flag) {
					System.out.println("EmailServices.HalperClass.emailValidation()");
					requestLogger.info(email+" is invalid");
				}
				return flag;
			}).collect(Collectors.toList());
		}
	}
}

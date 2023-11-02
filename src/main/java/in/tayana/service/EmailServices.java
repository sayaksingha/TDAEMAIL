package in.tayana.service;

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

    public boolean sendEmail(EmailRequest request) throws Exception {
    	MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            //to can be multiple
            List<String> listForTo=emailValidation(request.getTo(),1);
            if(listForTo == null) {
            	System.out.println("EmailServices.sendEmail() listForTo");
            	return false;
                 }else{
            	helper.setTo(listForTo.toArray(new String[0]));
            }
            
            //Cc can be multiple
            List<String> listForCc=emailValidation(request.getCc(),2);
            if(listForCc == null) {
            	
            }else{
            	helper.setCc(listForCc.toArray(new String[0]));
            }
            
            //Bcc can be multiple
            List<String> listForBcc=emailValidation(request.getBcc(),3);
            if(listForBcc == null) {
            	System.out.println(listForBcc);
            	return false;
            }else{
            	helper.setBcc(listForBcc.toArray(new String[0]));
            }
            
            helper.setSubject(request.getSubject());
            helper.setText(request.getBody_file(), true);

            // Add attachments if available
            if (request.getAttachment() != null) {
                for (String attachmentPath : request.getAttachment()) {
                    File file = new File(attachmentPath);
                    if (file.exists()) {
                        helper.addAttachment(file.getName(), file);
                    } else {
                        break;
                    }
                }
            }

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Handle email sending exceptions
            e.printStackTrace();
            throw new Exception("Error sending email", e);
        } catch (Exception e) {
            // Handle attachment file not found exception
            e.printStackTrace();
            throw e;
        }
        return true;
    }
    
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static List<String> emailValidation(String emails,Integer code) {
    	System.err.println(emails);
        if (emails == null) {
            return null;
        }
       return Arrays.asList(emails.split(",")).stream().filter(email->{
        	Matcher matcher = pattern.matcher(email);
        	return matcher.matches();
        }).collect(Collectors.toList());
        
        
    }
    
}

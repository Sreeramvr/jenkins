package com.strand.app.crm;
import java.io.IOException;
import java.util.Properties;
 
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.strand.app.system.Login;
import com.strand.app.system.ReadFromProperyFile;


/**
 * Servlet implementation class SendMail
 */
@RestController
@RequestMapping("/SendMail")
public class SendMail  {
	
	static Logger logger = Logger.getLogger(Login.class);  
	
	@Value("${app.schema:default}")
	String schema;
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	

//	@Value("${MAIL_USERNAME:default}")
//	String MAIL_USERNAME;
//	@Value("${MAIL_PASSWORD:default}")
//	String MAIL_PASSWORD;
//	@Value("${MAIL_SMTP:default}")
//	String MAIL_SMTP;
//	@Value("${MAIL_PORT:default}")
//	String MAIL_PORT;
	
	static String MAIL_USERNAME="AKIAIZO45WSZPJIMKICA";
	static String MAIL_PASSWORD="ArOY/Jujwv8gGAi1diwviWjlTnrlNRu0cSJi3KrEOKal";
	static String MAIL_SMTP="email-smtp.us-east-1.amazonaws.com";
	static String MAIL_PORT="587";
	
//	public static void main(String args[])
//	{
//		HttpServletResponse response=null;
//		
		//SendManualMailNew("","abhishek@tarkasoft.com","Hi","Verification");
		
//		SendMailWithAtatchment("","abhishek@tarkasoft.com",response,"Yes");
		//SendMailWithAtatchment("","abhishekbharadwaj41@gmail.com",response,"No");
		
		//SendMailWithAtatchment("","amits@wonderla.com",response);
		
		//SendMailWithAtatchment("","deepak.n@wonderla.com",response,"No");
		
//	}
	 
	 

	
	public static String SendManualMailNew(String to_id, String msg,String subject)
	{
//		JSONObject propObj = ReadFromProperyFile.GetDataFromPropFile();
//		String MAIL_USERNAME = propObj.get("MAIL_USERNAME").toString();
//		String MAIL_PASSWORD = propObj.get("MAIL_PASSWORD").toString();
//		String MAIL_SMTP = propObj.get("MAIL_SMTP").toString();
//		String MAIL_PORT = propObj.get("MAIL_PORT").toString();
		 
		
		final String username =MAIL_USERNAME;
		final String password = MAIL_PASSWORD;
		final String smtp = MAIL_SMTP;
		final String PORT = MAIL_PORT;

		/*
		 * final String username = "info@wonderla.com"; final String password =
		 * "wonder@info"; final String smtp = "smtp.gmail.com"; final String PORT =
		 * "587";
		 */
		String cc="";
		System.out.println("to_id===="+to_id+"cc="+cc+"username="+username+"\tpassword="+password+"\tsmtp="+smtp+"\tPORT="+PORT);

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtp);
		props.put("mail.smtp.port", PORT);
		System.out.println("Mail Sending Start==================>");
		Session mail_session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {
			//get emp mail id
				 String from_email="info@wonderla.com";
				Message message = new MimeMessage(mail_session);
				message.setFrom(new InternetAddress(from_email));
				
				
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to_id));
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
				message.setSubject(subject);
				
				
				message.setContent(msg,"text/html");
				Transport.send(message);
				
				System.out.println("Done");	
				String html="Mail Sent To-->"+to_id;
				System.out.println(html);
				return html;

		} catch (MessagingException e) {
			
			System.out.println("Error While Sending Mail.."+e.getMessage());
			return "Error While Sending Mail.."+e.getMessage();
		}
	}

	

}

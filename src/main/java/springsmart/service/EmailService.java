package springsmart.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

	  /*msg="Hello Rushikesh";
    subject="Regarding Leave";
    to="rushikeshlondhe3386@gmail.com";*/

//send email
public boolean sendEmail(String msg1, String subject1, String to1) {
boolean f=false;
   System.out.println( "Preparing to send msg" );
   String from1="rushikeshlondhe3385@gmail.com";
   //get the system properties
   Properties properties = System.getProperties();
   System.out.println(properties);

   //setting imp infromation to properties object

   //host set
   properties.put("mail.smtp.auth",true);
   properties.put("mail.smtp.starttls.enable",true);
   properties.put("mail.smtp.port","587");
   properties.put("mail.smtp.host", "smtp.gmail.com");

   String uname="rushikeshlondhe3385@gmail.com";
   String pass="twpfaxabkusgbyuz";


   //step1 : to get the session object


   Session session =	Session.getInstance(properties,new Authenticator() {


       protected PasswordAuthentication getPasswordAuthentication() {

           return new
                   PasswordAuthentication(uname,pass);
       }


   });
   session.setDebug(true);

   //step 2: compose msg
   MimeMessage mm=new MimeMessage(session);


   try {
       //from email
       mm.setFrom(new InternetAddress(from1));

       //adding recipient
       mm.setRecipient(Message.RecipientType.TO, new InternetAddress(to1));

       //subject
       mm.setSubject(subject1);

       //adding text to msg
       mm.setText(msg1);

       //send
       //step3:send the msg using transport class
       Transport.send(mm);

       System.out.println("sent succesfully");
f=true;
   } catch (MessagingException e) {

       e.printStackTrace();
   }
   return f;

}
}

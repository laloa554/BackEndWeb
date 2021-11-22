package Practica1.correo;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Correo {
	
	private String correoEnvia;
	private String contrasena;
	private String destinatario;
	 
	public Correo() {
		super();
	}

	public Correo(String correoEnvia, String contrasena, String destinatario) {
		super();
		this.correoEnvia = correoEnvia;
		this.contrasena = contrasena;
		this.destinatario = destinatario;
	}
	


	public String getCorreoEnvia() {
		return correoEnvia;
	}

	public void setCorreoEnvia(String correoEnvia) {
		this.correoEnvia = correoEnvia;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public void enviar_correo(String Titular, String token) {                                         
        Calendar fecha = Calendar.getInstance();
            int año = fecha.get(Calendar.YEAR);
            int mes = fecha.get(Calendar.MONTH) + 1;
            int dia = fecha.get(Calendar.DAY_OF_MONTH);
            int hora = fecha.get(Calendar.HOUR_OF_DAY);
            int minuto = fecha.get(Calendar.MINUTE);
            int segundo = fecha.get(Calendar.SECOND);
            
        Properties propiedad = new Properties();
        propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
        propiedad.setProperty("mail.smtp.starttls.enable", "true");
        propiedad.setProperty("mail.smtp.port", "587");
        propiedad.setProperty("mail.smtp.auth", "true");
        
        Session sesion = Session.getDefaultInstance(propiedad);
        String asunto = "Reestablecer tu contraseña, "+ dia + "/" + (mes) + "/" + año +" - "+ hora + ":" + minuto ;
        String mensaje = "<html>\r\n"
        		+ "\r\n"
        		+ "<head>\r\n"
        		+ "	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=\" utf-8\">\r\n"
        		+ "	<title>Referencia de Pago</title>\r\n"
        		+ "	<style type=\"text/css\">\r\n"
        		+ "		* {\r\n"
        		+ "			margin: 0;\r\n"
        		+ "			padding: 0;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		body {\r\n"
        		+ "			font-size: 14px;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		h3 {\r\n"
        		+ "			margin-bottom: 10px;\r\n"
        		+ "			font-size: 15px;\r\n"
        		+ "			font-weight: 600;\r\n"
        		+ "			text-transform: uppercase;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		.opps {\r\n"
        		+ "			width: 496px;\r\n"
        		+ "			border-radius: 4px;\r\n"
        		+ "			box-sizing: border-box;\r\n"
        		+ "			padding: 0 45px;\r\n"
        		+ "			margin: 40px auto;\r\n"
        		+ "			overflow: hidden;\r\n"
        		+ "			border: 1px solid #b0afb5;\r\n"
        		+ "			font-family: 'Open Sans', sans-serif;\r\n"
        		+ "			color: #4f5365;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		.opps-reminder {\r\n"
        		+ "			position: relative;\r\n"
        		+ "			top: -1px;\r\n"
        		+ "			padding: 9px 0 10px;\r\n"
        		+ "			font-size: 11px;\r\n"
        		+ "			text-transform: uppercase;\r\n"
        		+ "			text-align: center;\r\n"
        		+ "			color: #ffffff;\r\n"
        		+ "			background: #000000;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		.opps-info {\r\n"
        		+ "			margin-top: 26px;\r\n"
        		+ "			position: relative;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		.opps-info:after {\r\n"
        		+ "			visibility: hidden;\r\n"
        		+ "			display: block;\r\n"
        		+ "			font-size: 0;\r\n"
        		+ "			content: \" \";\r\n"
        		+ "			clear: both;\r\n"
        		+ "			height: 0;\r\n"
        		+ "\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		.opps-brand {\r\n"
        		+ "			width: 50%;\r\n"
        		+ "			float: center;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		.opps-brand img {\r\n"
        		+ "			max-width: 200px;\r\n"
        		+ "			margin-top: 2px;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		.opps-ammount {\r\n"
        		+ "			width: 55%;\r\n"
        		+ "			float: right;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		.opps-ammount h2 {\r\n"
        		+ "			font-size: 36px;\r\n"
        		+ "			color: #000000;\r\n"
        		+ "			line-height: 24px;\r\n"
        		+ "			margin-bottom: 15px;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		.opps-ammount h2 sup {\r\n"
        		+ "			font-size: 16px;\r\n"
        		+ "			position: relative;\r\n"
        		+ "			top: -2px\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		.opps-ammount p {\r\n"
        		+ "			font-size: 10px;\r\n"
        		+ "			line-height: 14px;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		.opps-reference {\r\n"
        		+ "			margin-top: 14px;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		h1 {\r\n"
        		+ "			font-size: 27px;\r\n"
        		+ "			color: #000000;\r\n"
        		+ "			text-align: center;\r\n"
        		+ "			margin-top: -1px;\r\n"
        		+ "			padding: 6px 0 7px;\r\n"
        		+ "			border: 1px solid #b0afb5;\r\n"
        		+ "			border-radius: 4px;\r\n"
        		+ "			background: #f8f9fa;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		.opps-instructions {\r\n"
        		+ "			margin: 32px -45px 0;\r\n"
        		+ "			padding: 32px 45px 45px;\r\n"
        		+ "			border-top: 1px solid #b0afb5;\r\n"
        		+ "			background: #f8f9fa;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		ol {\r\n"
        		+ "			margin: 17px 0 0 16px;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		li+li {\r\n"
        		+ "			margin-top: 10px;\r\n"
        		+ "			color: #000000;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		a {\r\n"
        		+ "			color: #1155cc;\r\n"
        		+ "		}\r\n"
        		+ "\r\n"
        		+ "		.opps-footnote {\r\n"
        		+ "			margin-top: 22px;\r\n"
        		+ "			padding: 22px 20 24px;\r\n"
        		+ "			color: #000000;\r\n"
        		+ "			text-align: center;\r\n"
        		+ "			border: 1px solid #000000;\r\n"
        		+ "			border-radius: 4px;\r\n"
        		+ "			background: #ffffff;\r\n"
        		+ "		}\r\n"
        		+ "	</style>\r\n"
        		+ "	<link href=\"https://fonts.googleapis.com/css?family=Open+Sans:400,600,700\" rel=\"stylesheet\">\r\n"
        		+ "</head>\r\n"
        		+ "\r\n"
        		+ "<body>\r\n"
        		+ "	<div class=\"opps\">\r\n"
        		+ "		<div class=\"opps-header\">\r\n"
        		+ "\r\n"
        		+ "			<div class=\"opps-info\">\r\n"
        		+ "				<div class=\"opps-brand\"><img\r\n"
        		+ "						src=\"D:\\BUAP\\Semestre 11\\Aplicaciones web\\front\\eshopper\\images\\home\\logo.png\" alt=\"OXXOPay\">\r\n"
        		+ "				</div>\r\n"
        		+ "\r\n"
        		+ "			</div>\r\n"
        		+ "\r\n"
        		+ "			<div class=\"opps-reference\">\r\n"
        		+ "				<h1>Recuperar Contraseña</h1>\r\n"
        		+ "			</div>\r\n"
        		+ "		</div>\r\n"
        		+ "		<div class=\"opps-instructions\">\r\n"
        		+ "			<center><h2>Instucciones</h2></center>\r\n"
        		+ "			<li>Copie el siguiente token</li>\r\n"
        		+ "			<li>\""+token+"\"</li>\r\n"
        		+ "			<li>Pegue el token en el formulario de recuperación</li>\r\n"
        		+ "			<li>Escriba su nueva contraseña</li>\r\n"
        		+ "			<li>Confirme su nueva contraseña</li>\r\n"
        		+ "			<li>Click en el boton \"enviar\"</li>\r\n"
        		+ "			<br>\r\n"
        		+ "			Búscanos en\r\n"
        		+ "			<center> <a href=\"\" class=\"nav-link\" target=\"_blank\"><img\r\n"
        		+ "						src=\"https://img.icons8.com/color/48/000000/facebook.png\"></a>\r\n"
        		+ "				<a href=\"\" class=\"nav-link\" target=\"_blank\"><img\r\n"
        		+ "						src=\"https://img.icons8.com/fluent/48/000000/twitter.png\" /></a>\r\n"
        		+ "				<a href=\"\" class=\"nav-link\" target=\"_blank\"><img\r\n"
        		+ "						src=\"https://img.icons8.com/fluent/48/000000/instagram-new.png\" /></a>\r\n"
        		+ "			</center>\r\n"
        		+ "			<div class=\"opps-footnote\"><img\r\n"
        		+ "					src=\"D:\\BUAP\\Semestre 11\\Aplicaciones web\\front\\eshopper\\images\\home\\logo.png\" alt=\"OXXOPay\"></div>\r\n"
        		+ "		</div>\r\n"
        		+ "	</div>\r\n"
        		+ "</body>\r\n"
        		+ "</html>";
        MimeMessage mail = new MimeMessage(sesion);
        
        try {
            mail.setFrom(new InternetAddress (correoEnvia));
            mail.addRecipients(Message.RecipientType.BCC, new InternetAddress[] { new InternetAddress(destinatario) });
            mail.setSubject(asunto);
            mail.setContent(mensaje, "text/html");
            
            Transport transporte = sesion.getTransport("smtp");
            transporte.connect(correoEnvia,contrasena);
            transporte.sendMessage(mail, mail.getRecipients(Message.RecipientType.BCC));
            transporte.close();           
        } catch (AddressException ex) {
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
	
}
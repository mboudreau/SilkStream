package com.silkstream.platform.service;

import com.silkstream.platform.enums.EnvironmentType;
import com.silkstream.platform.models.BeanstalkProperties;
import com.silkstream.platform.models.db.User;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;
import sun.misc.BASE64Encoder;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service("mailService")
public class MailService extends BasicService {

	@Inject
	@Qualifier("amazonMailSender")
	private JavaMailSenderImpl amazonMailSender;
	@Inject
	@Qualifier("internalMailSender")
	private JavaMailSenderImpl internalMailSender;
	@Inject
	private BeanstalkProperties properties;
	private VelocityEngine velocityEngine;

	public MailService() throws Exception {
		VelocityEngineFactoryBean eb = new VelocityEngineFactoryBean();
		Properties pad = new Properties();
		pad.setProperty("resource.loader", "class");
		pad.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		eb.setVelocityProperties(pad);
		this.velocityEngine = eb.createVelocityEngine();
		System.out.println("da");
	}

	public void sendException(final Exception e) {
		//TODO : a template for a user that signed in with social account
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setTo("admin@silkstream.us");
				message.setFrom("TeamTivity <team@silkstream.us>");
				message.setReplyTo("TeamTivity <team@silkstream.us>");
				message.setSubject("Exception occured on " + properties.getEnvironment());
				message.setValidateAddresses(true);
				Map model = new HashMap();
				model.put("exception",ExceptionUtils.getFullStackTrace(e));
				String subdomain = "www";
				String port = "";
				if (properties.getEnvironment() != EnvironmentType.PROD) {
					subdomain = properties.getEnvironment().toString().toLowerCase();
					if (properties.getEnvironment() == EnvironmentType.LOCAL) {
						port = ":8080";
					}
				}
				model.put("subdomain", subdomain);
				model.put("port", port);
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/exception.vm", model);
				message.setText(text, true);
				Resource rr = new ClassPathResource("silkstream.png");
				FileSystemResource res = new FileSystemResource(rr.getFile());
				message.addInline("logo", res, "image/png");
				rr = new ClassPathResource("google.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("gp", res, "image/png");
				rr = new ClassPathResource("facebook.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("fb", res, "image/png");
				rr = new ClassPathResource("twitter.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("tw", res, "image/png");
			}
		};
		this.getSender().send(preparator);
	}



	public void sendNotification(final User user, final String subject, final String content, final String link) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setTo(user.getEmail());
				message.setFrom("TeamTivity <team@silkstream.us>");
				message.setReplyTo("TeamTivity <team@silkstream.us>");
				message.setSubject(subject);
				message.setValidateAddresses(true);
				Map model = new HashMap();
				model.put("content", content);
				String subdomain = "www";
				String port = "";
				if (properties.getEnvironment() != EnvironmentType.PROD) {
					subdomain = properties.getEnvironment().toString().toLowerCase();
					if (properties.getEnvironment() == EnvironmentType.LOCAL) {
						port = ":8080";
					}
				}
				model.put("subdomain", subdomain);
				model.put("port", port);
				model.put("link",link);
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/notificationEmail.vm", model);
				message.setText(text, true);
				Resource rr = new ClassPathResource("silkstream.png");
				FileSystemResource res = new FileSystemResource(rr.getFile());
				message.addInline("logo", res, "image/png");
				rr = new ClassPathResource("google.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("gp", res, "image/png");
				rr = new ClassPathResource("facebook.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("fb", res, "image/png");
				rr = new ClassPathResource("twitter.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("tw", res, "image/png");
			}
		};
		this.getSender().send(preparator);
	}


	public void sendWelcome(final User user) {
		//TODO : a template for a user that signed in with social account
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setTo(user.getEmail());
				message.setFrom("TeamTivity <team@silkstream.us>");
				message.setReplyTo("TeamTivity <team@silkstream.us>");
				message.setSubject("Whoop there it is...You've Joined Tivity - Now Get Active!");
				message.setValidateAddresses(true);
				Map model = new HashMap();
				String subdomain = "www";
				String port = "";
				if (properties.getEnvironment() != EnvironmentType.PROD) {
					subdomain = properties.getEnvironment().toString().toLowerCase();
					if (properties.getEnvironment() == EnvironmentType.LOCAL) {
						port = ":8080";
					}
				}
				model.put("subdomain", subdomain);
				model.put("port", port);
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/welcomeEmail.vm", model);
				message.setText(text, true);
				Resource rr = new ClassPathResource("silkstream.png");
				FileSystemResource res = new FileSystemResource(rr.getFile());
				message.addInline("logo", res, "image/png");
				rr = new ClassPathResource("google.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("gp", res, "image/png");
				rr = new ClassPathResource("facebook.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("fb", res, "image/png");
				rr = new ClassPathResource("twitter.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("tw", res, "image/png");
			}
		};
		this.getSender().send(preparator);
	}

	public void sendSignUp(final User user, final String conf) {
		//TODO : a template for a user that signed in with social account
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setTo(user.getEmail());
				message.setFrom("TeamTivity <team@silkstream.us>");
				message.setReplyTo("TeamTivity <team@silkstream.us>");
				message.setSubject("Email Confirmation");
				message.setValidateAddresses(true);
				Map model = new HashMap();
				model.put("confirmationId", conf);
				String subdomain = "www";
				String port = "";
				if (properties.getEnvironment() != EnvironmentType.PROD) {
					subdomain = properties.getEnvironment().toString().toLowerCase();
					if (properties.getEnvironment() == EnvironmentType.LOCAL) {
						port = ":8080";
					}
				}
				model.put("subdomain", subdomain);
				model.put("port", port);
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/signupEmail.vm", model);
				message.setText(text, true);
				Resource rr = new ClassPathResource("silkstream.png");
				FileSystemResource res = new FileSystemResource(rr.getFile());
				message.addInline("logo", res, "image/png");
				rr = new ClassPathResource("google.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("gp", res, "image/png");
				rr = new ClassPathResource("facebook.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("fb", res, "image/png");
				rr = new ClassPathResource("twitter.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("tw", res, "image/png");
			}
		};
		this.getSender().send(preparator);
	}

	public void sendSignUpFacebook(final User user) {
		//TODO : a template for a user that signed in with social account
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setTo(user.getEmail());
				message.setFrom("TeamTivity <team@silkstream.us>");
				message.setReplyTo("TeamTivity <team@silkstream.us>");
				message.setSubject("Thanks for Registering with Us");
				message.setValidateAddresses(true);
				Map model = new HashMap();
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/signupEmailFacebook.vm", model);
				message.setText(text, true);
				Resource rr = new ClassPathResource("silkstream.png");
				FileSystemResource res = new FileSystemResource(rr.getFile());
				message.addInline("logo", res, "image/png");
				rr = new ClassPathResource("google.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("gp", res, "image/png");
				rr = new ClassPathResource("facebook.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("fb", res, "image/png");
				rr = new ClassPathResource("twitter.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("tw", res, "image/png");
			}
		};
		this.getSender().send(preparator);
	}

	public void sendInvite(final String email, final User user) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setTo(email);
				message.setFrom("TeamTivity <team@silkstream.us>");
				message.setReplyTo("TeamTivity <team@silkstream.us>");
				message.setSubject(user.getFirstName() + " Invited You To Tivity");
				message.setValidateAddresses(true);
				Map model = new HashMap();
				model.put("firstName", user.getFirstName());
				model.put("lastName", user.getLastName());
				model.put("userid", user.getId());
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/inviteEmail.vm", model);
				message.setText(text, true);
				Resource rr = new ClassPathResource("silkstream.png");
				FileSystemResource res = new FileSystemResource(rr.getFile());
				message.addInline("logo", res, "image/png");
				rr = new ClassPathResource("google.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("gp", res, "image/png");
				rr = new ClassPathResource("facebook.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("fb", res, "image/png");
				rr = new ClassPathResource("twitter.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("tw", res, "image/png");
			}
		};
		this.getSender().send(preparator);
	}

	public void sendResetPassword(final String conf, final User user) {
		//TODO : a template for a user that signed in with social account
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setTo(user.getEmail());
				message.setFrom("TeamTivity <team@silkstream.us>");
				message.setReplyTo("TeamTivity <team@silkstream.us>");
				message.setSubject("Email Confirmation");
				message.setValidateAddresses(true);
				Map model = new HashMap();
				model.put("confirmationId", conf);
				String subdomain = "www";
				String port = "";
				if (properties.getEnvironment() != EnvironmentType.PROD) {
					subdomain = properties.getEnvironment().toString().toLowerCase();
					if (properties.getEnvironment() == EnvironmentType.LOCAL) {
						port = ":8080";
					}
				}
				model.put("subdomain", subdomain);
				model.put("port", port);
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/resetPasswordEmail.vm", model);
				message.setText(text, true);
				Resource rr = new ClassPathResource("silkstream.png");
				FileSystemResource res = new FileSystemResource(rr.getFile());
				message.addInline("logo", res, "image/png");
				rr = new ClassPathResource("google.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("gp", res, "image/png");
				rr = new ClassPathResource("facebook.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("fb", res, "image/png");
				rr = new ClassPathResource("twitter.png");
				res = new FileSystemResource(rr.getFile());
				message.addInline("tw", res, "image/png");
			}
		};
		this.getSender().send(preparator);
	}


    public void sendTivityCreation(final String tivityId, final User user) {
        //TODO : a template for a user that signed in with social account
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
                message.setTo(user.getEmail());
                message.setFrom("TeamTivity <team@silkstream.us>");
                message.setReplyTo("TeamTivity <team@silkstream.us>");
                message.setSubject("You just created a Tivity!");
                message.setValidateAddresses(true);
                Map model = new HashMap();
	            model.put("tivityId", tivityId);
                String subdomain = "www";
                String port = "";
                if (properties.getEnvironment() != EnvironmentType.PROD) {
                    subdomain = properties.getEnvironment().toString().toLowerCase();
                    if (properties.getEnvironment() == EnvironmentType.LOCAL) {
                        port = ":8080";
                    }
                }
                model.put("subdomain", subdomain);
                model.put("port", port);
                String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/tivityCreationEmail.vm", model);
                message.setText(text, true);
                Resource rr = new ClassPathResource("silkstream.png");
                FileSystemResource res = new FileSystemResource(rr.getFile());
                message.addInline("logo", res, "image/png");
                rr = new ClassPathResource("google.png");
                res = new FileSystemResource(rr.getFile());
                message.addInline("gp", res, "image/png");
                rr = new ClassPathResource("facebook.png");
                res = new FileSystemResource(rr.getFile());
                message.addInline("fb", res, "image/png");
                rr = new ClassPathResource("twitter.png");
                res = new FileSystemResource(rr.getFile());
                message.addInline("tw", res, "image/png");
            }
        };
        this.getSender().send(preparator);
    }

	public void sendSupportEmail(final String email, final String name, final String subject, final String message) {
		this.sendSupportEmail(email, name, subject, message, "templates/tivityInternalEmail.vm");
	}

	public void sendSupportEmail(final String email, final String name, final String subject, final String message, final String template) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			BASE64Encoder ada = new BASE64Encoder();
			String encoding = ada.encode("teamtivity@silkstream.us:Tivity2012".getBytes());
			HttpPost httppost = new HttpPost("http://silkstream.freshdesk.com/helpdesk/tickets.xml");
			httppost.setHeader("Authorization", "Basic " + encoding);
			String from = "team@silkstream.us";
			if (email != null) {
				from = email;
				if (name != null) {
					from = name + " <" + from + ">";
				}
			}
			StringEntity se = new StringEntity("<helpdesk_ticket><description>" + "<![CDATA[<strong>Name</strong> " + StringEscapeUtils.escapeHtml(from) + "<br /><strong>Comments</strong><br/> " + StringEscapeUtils.escapeHtml(message) + "  ]]></description><email>" + StringEscapeUtils.escapeHtml(from) + "</email><subject>" + StringEscapeUtils.escapeHtml(subject) + "</subject></helpdesk_ticket>");
			httppost.setEntity(se);
			httppost.setHeader("Content-type", "application/xml");
			HttpResponse response = httpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
		} catch (Exception e) {

		}
    }
/*
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper mail = new MimeMessageHelper(mimeMessage, true);
				mail.setTo("team@silkstream.us");
				if (subject != null) {
					mail.setSubject(subject);
				}
				String from = "team@silkstream.us";
				if (email != null) {
					from = email;
					if (name != null) {
						from = name + "<" + from + ">";
					}
				}
				mail.setFrom(from);
				mail.setReplyTo(from);
				Map model = new HashMap();
				model.put("email", email == null ? "" : email);
				model.put("name", name == null ? "" : name);
				model.put("message", message == null ? "" : message);
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, model);
				mail.setText(text, true);
			}
		};
		this.getSender().send(preparator);*/


	protected JavaMailSenderImpl getSender() {
		JavaMailSenderImpl mailSender = internalMailSender;
		if (properties.getEnvironment() == EnvironmentType.DEV || properties.getEnvironment() == EnvironmentType.LOCAL ) {
			mailSender = amazonMailSender;
		}
		if(properties.getEnvironment() == EnvironmentType.TEST){
			mailSender = new TestMailSender();
		}
		return mailSender;
	}
}

class TestMailSender extends JavaMailSenderImpl {

	public void send(SimpleMailMessage[] simpleMessages) throws MailException {
	}

	public void send(MimeMessagePreparator[] mimeMessagePreparators) throws MailException {

	}

}
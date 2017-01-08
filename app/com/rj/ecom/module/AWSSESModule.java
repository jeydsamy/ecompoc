package com.rj.ecom.module;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.google.inject.AbstractModule;

import play.Configuration;
import play.Environment;
import play.Logger;

public class AWSSESModule extends AbstractModule implements AWSModule {
	@SuppressWarnings("unused")
	private Configuration configuration;
	@SuppressWarnings("unused")
	private Environment environment;

	// IMPORTANT: To successfully send an email, you must replace the values of
	// the strings below with your own values.
	private static String EMAIL_FROM = null;
	private static String EMAIL_REPLY_TO = null;
	// IMPORTANT: Ensure that the region selected below is the one in which your
	// identities are verified.
	private static Regions AWS_REGION = Regions.US_EAST_1;
	private static String accessKey = null;
	private static String secretKey = null;

	@Override
	protected void configure() {

	}

	public AWSSESModule(Environment environment, Configuration configuration) {
		Logger.info("AWSSESModule(Environment environment, Configuration configuration) called");
		this.configuration = configuration;
		this.environment = environment;
		accessKey = configuration.getString(AWS_ACCESS_KEY);
		secretKey = configuration.getString(AWS_SECRET_KEY);
		EMAIL_FROM = configuration.getString(AWS_SES_EMAIL_FROM_KEY);
		EMAIL_REPLY_TO = configuration.getString(AWS_SES_EMAIL_REPLY_TO_KEY);
	}

	public static void sendSESEmail(String emailSubject, String emailBody, String emailReceipientTo)
			throws AddressException, MessagingException, IOException {
		Session session = Session.getDefaultInstance(new Properties());
		MimeMessage message = new MimeMessage(session);
		message.setSubject(emailSubject, "UTF-8");

		message.setFrom(new InternetAddress(EMAIL_FROM));
		message.setReplyTo(new Address[] { new InternetAddress(EMAIL_REPLY_TO) });
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailReceipientTo));

		// Cover wrap
		MimeBodyPart wrap = new MimeBodyPart();

		// Alternative TEXT/HTML content
		MimeMultipart cover = new MimeMultipart("alternative");
		MimeBodyPart html = new MimeBodyPart();
		cover.addBodyPart(html);

		wrap.setContent(cover);

		MimeMultipart content = new MimeMultipart("related");
		message.setContent(content);
		content.addBodyPart(wrap);

		String[] attachmentsFiles = null;

		// This is just for testing HTML embedding of different type of
		// attachments.
		StringBuilder sb = new StringBuilder();

		/*
		 * for (String attachmentFileName : attachmentsFiles) { String id =
		 * UUID.randomUUID().toString(); sb.append("<img src=\"cid:");
		 * sb.append(id); sb.append("\" alt=\"ATTACHMENT\"/>\n");
		 * 
		 * MimeBodyPart attachment = new MimeBodyPart();
		 * 
		 * DataSource fds = new FileDataSource(attachmentFileName);
		 * attachment.setDataHandler(new DataHandler(fds));
		 * attachment.setHeader("Content-ID", "<" + id + ">");
		 * attachment.setFileName(fds.getName());
		 * 
		 * content.addBodyPart(attachment); }
		 */

		html.setContent("<html><body>\n" + emailBody + "</body></html>", "text/html");

		try {
			Logger.debug("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

			AmazonSimpleEmailServiceClient client = null;
			// Instantiate an Amazon SES client, which will make the service
			// call with the supplied AWS credentials.

			if ((accessKey != null) && (secretKey != null)) {
				AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
				client = new AmazonSimpleEmailServiceClient(awsCredentials);

			} else {
				String msg = "Missing configuration AWS_ACCESS_KEY AWS_SECRET_KEY";
				Logger.error(msg);
				throw new RuntimeException(msg);
			}

			Region REGION = Region.getRegion(AWS_REGION);
			client.setRegion(REGION);

			// Print the raw email content on the console
			PrintStream out = System.out;
			message.writeTo(out);

			// Send the email.
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			message.writeTo(outputStream);
			RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

			SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
			client.sendRawEmail(rawEmailRequest);
			Logger.debug("Email sent!");

		} catch (Exception ex) {
			Logger.error("Email Failed" + ex.getMessage(), ex);
		}
	}

}

package com.rj.ecom.services;

import java.io.IOException;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.rj.ecom.module.AWSSESModule;
import com.rj.ecom.module.AWSSQSModule;

import play.Configuration;
import play.Logger;

public class AWSService {
	private Configuration configuration;
	private static final String AWS_SQS_QUEUE_URL_KEY = "aws.sqs.url";

	@Inject
	public AWSService(Configuration configuration) {
		this.configuration = configuration;
	}

	public String sendSQSMessage(String message) {
		String queueUrl = configuration.getString(AWS_SQS_QUEUE_URL_KEY);
		Logger.info(this.getClass() + " sendMessage queueUrl = " + queueUrl);
		return AWSSQSModule.sendMessage(queueUrl, message);
	}
	
	public void sendSESEmail(String emailSubject, String emailBody, String emailReceipientTo)
			throws AddressException, MessagingException, IOException {
		AWSSESModule.sendSESEmail(emailSubject, emailBody, emailReceipientTo);
		
	}
}

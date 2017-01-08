package com.rj.ecom.module;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.inject.AbstractModule;

import play.Configuration;
import play.Environment;
import play.inject.ApplicationLifecycle;
import play.Logger;

public class AWSSQSModule extends AbstractModule {

	private Configuration configuration;
	@SuppressWarnings("unused")
	private Environment environment;

	public static final String AWS_ACCESS_KEY = "aws.access.key";
	public static final String AWS_SECRET_KEY = "aws.secret.key";

	private static AmazonSQSClient client;

	public AWSSQSModule(Environment environment, Configuration configuration) {
		Logger.info("AWSSQSModule(Environment environment, Configuration configuration) called");
		this.configuration = configuration;
		this.environment = environment;
		setupClient();
	}

	@Inject
	public AWSSQSModule(ApplicationLifecycle lifecycle) {
		Logger.info("AWSSQSModule(ApplicationLifecycle lifecycle) called");
		// Onstart create a client
		setupClient();

		// onStop
		lifecycle.addStopHook(() -> {
			// previous contents of Plugin.onStop
			if (client != null) {
				Logger.info("AWSSQSModule trying to  shutdown AWS SQS Client");
				client.shutdown();
			}
			return CompletableFuture.completedFuture(null);
		});
	}

	private void setupClient() {
		Logger.info("AWSSQSModule setupClient called");
		String accessKey = configuration.getString(AWS_ACCESS_KEY);
		String secretKey = configuration.getString(AWS_SECRET_KEY);
		if ((accessKey != null) && (secretKey != null)) {
			AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
			client = new AmazonSQSClient(awsCredentials);
		} else {
			String msg = "Missing configuration AWS_ACCESS_KEY AWS_SECRET_KEY";
			Logger.error(msg);
			throw new RuntimeException(msg);
		}
	}

	public static String sendMessage(String queueUrl, String message) {
		SendMessageResult sendMessageResult= null;
		String messageId = null;
		if (queueUrl != null) {
			SendMessageRequest sendMsgReq = new SendMessageRequest(queueUrl, message);
			if(client != null) {
				sendMessageResult =client.sendMessage(sendMsgReq);
				messageId = sendMessageResult.getMessageId();
			}
			else {
				throw new RuntimeException ("Missing AWS SQS Connection client.");
			}
		}
		return messageId;
	}

	public static List<Message> receiveMessages(String queueUrl, int maxNumberOfMessages) {
		ReceiveMessageRequest receiveMsgReq = new ReceiveMessageRequest(queueUrl);
		receiveMsgReq.setMaxNumberOfMessages(maxNumberOfMessages);
		ReceiveMessageResult result = client.receiveMessage(receiveMsgReq);
		return result.getMessages();
	}

	public static void deleteMessage(String queueUrl, Message msg) {
		DeleteMessageRequest delMsgRequest = new DeleteMessageRequest(queueUrl, msg.getReceiptHandle());
		client.deleteMessage(delMsgRequest);
	}

	@Override
	protected void configure() {

	}
}

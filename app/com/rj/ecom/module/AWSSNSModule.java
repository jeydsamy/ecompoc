package com.rj.ecom.module;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.inject.AbstractModule;

import play.Configuration;
import play.Environment;
import play.Logger;
import play.inject.ApplicationLifecycle;

public class AWSSNSModule extends AbstractModule implements AWSModule {
	private Configuration configuration;
	@SuppressWarnings("unused")
	private Environment environment;
	
	
	private static AmazonSNSClient client;
	
	public AWSSNSModule(Environment environment, Configuration configuration) {
		Logger.info("AWSSNSModule(Environment environment, Configuration configuration) called");
		this.configuration = configuration;
		this.environment = environment;
		setupClient();
	}
	
	private void setupClient() {
		Logger.info("AWSSNSModule setupClient called");
		String accessKey = configuration.getString(AWS_ACCESS_KEY);
		String secretKey = configuration.getString(AWS_SECRET_KEY);
		if ((accessKey != null) && (secretKey != null)) {
			AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
			client = new AmazonSNSClient(awsCredentials);
		} else {
			String msg = "Missing configuration AWS_ACCESS_KEY AWS_SECRET_KEY";
			Logger.error(msg);
			throw new RuntimeException(msg);
		}
	}
	
	@Inject
	public AWSSNSModule(ApplicationLifecycle lifecycle) {
		Logger.info("AWSSNSModule(ApplicationLifecycle lifecycle) called");
		// Onstart create a client
		setupClient();

		// onStop
		lifecycle.addStopHook(() -> {
			// previous contents of Plugin.onStop
			if (client != null) {
				Logger.info("AWSSNSModule trying to  shutdown AWS SQS Client");
				client.shutdown();
			}
			return CompletableFuture.completedFuture(null);
		});
	}
	
	public static String sendMessage(String topicArn, String message) {
		PublishRequest publishRequest = new PublishRequest(topicArn, message);
		PublishResult publishResult = client.publish(publishRequest);
		return publishResult.getMessageId();
	}
	
	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		
	}

}

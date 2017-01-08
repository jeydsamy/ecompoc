package com.rj.ecom.controllers;

import java.io.IOException;

import javax.inject.Inject;
import javax.mail.MessagingException;

import com.rj.ecom.services.AWSService;

import play.mvc.Controller;
import play.mvc.Result;

public class TestController extends Controller {
	AWSService awsService;

	@Inject
	public TestController(AWSService awsService) {
		this.awsService = awsService;
	}

	public Result sendSQSMessage(String message) {
		String resultSQSMessage =  awsService.sendSQSMessage(message);
		try {
			awsService.sendSESEmail("Test subject", message, "ramkallam@icloud.com");
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok("It works! sent message ID = " +resultSQSMessage);
	}
}

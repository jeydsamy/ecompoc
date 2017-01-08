package com.rj.ecom.controllers;

import javax.inject.Inject;

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
		return ok("It works! sent message ID = " + awsService.sendSQSMessage(message));
	}
}

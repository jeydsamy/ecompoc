package com.rj.ecom.controllers;

import com.rj.ecom.services.AWSService;

import play.mvc.Controller;
import play.mvc.Result;

public class TestController extends Controller{
	
	public Result sendSQSMessage(String message) {
		AWSService awsService = new AWSService();
		awsService.sendMessage(message);
		return ok("It works! sent message = "+ message);
	}
}

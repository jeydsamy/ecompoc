package com.rj.ecom.controllers;

import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rj.ecom.dao.impl.ProductCatalogDAOImpl;
import com.rj.ecom.data.ProductCatalog;
import com.rj.ecom.services.AWSService;

import play.Configuration;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

	@Inject ProductCatalogDAOImpl productCatalogDAOImpl;
	
	@Inject AWSService awsService;
	
	@Inject Configuration config;
	
	
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
	@Transactional
    public Result index() {
        return ok(index.render("Welcome!"));
    }
	
	@Transactional
    public Result productsList() {
		List<ProductCatalog> products = productCatalogDAOImpl.findAll(new ProductCatalog());
        return ok(Json.toJson(products));
    }

	public Result placeOrder(String productId){
		  System.out.println(productId);
		  
		  ObjectNode toSQS = Json.newObject();
		  toSQS.put("productId", productId);
		  toSQS.put("action", "Add");
		  toSQS.put("qty", 1);
		  String tex = toSQS.toString();
		  awsService.sendMessage(tex);
		  
		ObjectNode result = Json.newObject();
	    result.put("message", "Order Placed Successfully...productId:" + productId);
	    
		return ok(Json.toJson(result));
	}
	
}

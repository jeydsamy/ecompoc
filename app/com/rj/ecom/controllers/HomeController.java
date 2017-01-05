package com.rj.ecom.controllers;

import java.util.List;

import javax.inject.Inject;

import com.rj.ecom.dao.impl.ProductCatalogDAOImpl;
import com.rj.ecom.data.ProductCatalog;

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
	
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
	@Transactional
    public Result index() {
		List<ProductCatalog> products = productCatalogDAOImpl.findAll(new ProductCatalog());
        return ok(index.render("Play Java Ready, within transaction", products));
    }

	
}

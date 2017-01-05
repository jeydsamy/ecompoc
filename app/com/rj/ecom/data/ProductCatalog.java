package com.rj.ecom.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT_CATALOG")
public class ProductCatalog {
	
	@Id
	@SequenceGenerator(name = "PROCUCT_CATALOG_PRODUCTID_GENERATOR", sequenceName = "PRODUCT_ID", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCUCT_CATALOG_PRODUCTID_GENERATOR")
	@Column(name = "PRODUCT_ID")
	private long productId;
	
	@Column(name="PRODUCT_NAME")
	private String productName;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="ORIGINAL_PRICE")
	private double originalPrice;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(double originalPrice) {
		this.originalPrice = originalPrice;
	}
	
}

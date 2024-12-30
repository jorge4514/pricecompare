package com.supermarketapi.supermarketapi.dao;

public class Product {
    private String name;
    private String brand;
    private double price;
    private double previous_unit_price;
    private String share_url;
    private String picture;
    private String market;
    private double price_unit;
    private String unit;


    // Constructor
    public Product(String name, String brand, double price, double previous_unit_price, String share_url, String picture, String market) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.previous_unit_price = previous_unit_price;
        this.share_url = share_url;
        this.picture = picture;
        this.market = market;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

	public double getPreviousUnitPrice() {
		return previous_unit_price;
	}

	public void setPreviousUnitPrice(double previous_unit_price) {
		this.previous_unit_price = previous_unit_price;
	}

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public double getPrevious_unit_price() {
		return previous_unit_price;
	}

	public void setPrevious_unit_price(double previous_unit_price) {
		this.previous_unit_price = previous_unit_price;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public double getPrice_unit() {
		return price_unit;
	}

	public void setPrice_unit(double price_unit) {
		this.price_unit = price_unit;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	
	
	
}


package com.supermarketapi.supermarketapi.bo.interfaces;


public interface SupermarketApi {
    <T> T getProduct(String productId, Class<T> responseType);
    <T> T postProduct(String url, Object requestBody, Class<T> responseType);
}



package com.supermarketapi.supermarketapi.bo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.supermarketapi.supermarketapi.bo.interfaces.SupermarketApi;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;

@Service
public abstract class BaseSupermarketApi implements SupermarketApi {

    protected final RestTemplate restTemplate;

    @Autowired
    public BaseSupermarketApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> T getProduct(String productId, Class<T> responseType) {
        String url = getBaseUrl() + "/product/" + productId;
        return getRequest(url, responseType);
    }

    @Override
    public <T> T postProduct(String url, Object requestBody, Class<T> responseType) {
        return postRequest(url, requestBody, responseType);
    }

    // Método genérico para GET
    protected <T> T getRequest(String url, Class<T> responseType) {
        try {
            HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
            return response.getBody();
        } catch (Exception e) {
            System.out.println("Error en GET: " + e.getMessage());
            return null;
        }
    }

    // Método genérico para POST
    protected <T> T postRequest(String url, Object requestBody, Class<T> responseType) {
        try {
            HttpEntity<Object> entity = new HttpEntity<>(requestBody, new HttpHeaders());
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
            return response.getBody();
        } catch (Exception e) {
            System.out.println("Error en POST: " + e.getMessage());
            return null;
        }
    }

    // Método abstracto para obtener la URL base del supermercado
    protected abstract String getBaseUrl();
}


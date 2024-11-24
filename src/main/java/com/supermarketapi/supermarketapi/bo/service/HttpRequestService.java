package com.supermarketapi.supermarketapi.bo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class HttpRequestService {

    private final RestTemplate restTemplate;

    @Autowired
    public HttpRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Método para realizar una petición GET.
     * 
     * @param url La URL a la que hacer la petición.
     * @param responseType Tipo de la respuesta esperada.
     * @param params Parámetros opcionales para la URL.
     * @param headers Opcionales cabeceras personalizadas.
     * @param <T> Tipo de la respuesta.
     * @return La respuesta de la API.
     */
    public <T> T getRequest(String url, Class<T> responseType, Object params, HttpHeaders headers) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            if (params != null) {
                builder.queryParams((MultiValueMap<String, String>) params);  // Si tienes parámetros, añádelos
            }
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<T> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, responseType);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            // Manejo de errores, puedes personalizarlo
            System.out.println("Error en la petición GET: " + e.getMessage());
            return null; // o lanzar una excepción personalizada
        }
    }

    /**
     * Método para realizar una petición POST.
     * 
     * @param url La URL a la que hacer la petición.
     * @param requestBody El cuerpo de la solicitud.
     * @param responseType Tipo de la respuesta esperada.
     * @param headers Opcionales cabeceras personalizadas.
     * @param <T> Tipo de la respuesta.
     * @return La respuesta de la API.
     */
    public <T> T postRequest(String url, Object requestBody, Class<T> responseType, HttpHeaders headers) {
        try {
            HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            // Manejo de errores, puedes personalizarlo
            System.out.println("Error en la petición POST: " + e.getMessage());
            return null; // o lanzar una excepción personalizada
        }
    }
}


package com.supermarketapi.supermarketapi.markets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.supermarketapi.supermarketapi.bo.service.BaseSupermarketApi;
import com.supermarketapi.supermarketapi.dao.Product;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Service
public class MercadonaApi extends BaseSupermarketApi {

    private static final String BASE_URL = "https://7uzjkl1dj0-dsn.algolia.net/1/indexes/products_prod_vlc1_es/query";

    @Autowired
    public MercadonaApi(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * Método para hacer la búsqueda de productos en Mercadona
     * @param query el término de búsqueda recibido desde el front-end
     * @return Los resultados de la búsqueda
     */
    public List<Product> searchProducts(String query) {
        // Construir el JSON del cuerpo de la solicitud POST
        String params = String.format(
                "{\"params\":\"query=%s&clickAnalytics=true&analyticsTags=%%5B%%22web%%22%%5D&getRankingInfo=true\"}",
                query);

        // Construir la URL completa con los parámetros que serían los headers
        String url = String.format("%s?x-algolia-agent=%s&x-algolia-application-id=%s&x-algolia-api-key=%s",
                BASE_URL,
                "Algolia%20for%20JavaScript%20(3.35.1)%3B%20Browser",
                "7UZJKL1DJ0",
                "9d8f2e39e90df472b4f2e559a116fe17");

        // Configurar el header de contenido para JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        // Crear el RestTemplate y el HttpEntity
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(params, headers);

        try {
            // Realizar la solicitud POST y obtener la respuesta
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            // Extraer la lista de productos del campo "hits"
            List<Map<String, Object>> hits = (List<Map<String, Object>>) response.getBody().get("hits");

            // Crear la lista de productos mapeados
            List<Product> products = new ArrayList<>();

            // Iterar sobre cada producto en "hits" y mapearlo a un objeto Product
            for (Map<String, Object> hit : hits) {
                String name = (String) hit.get("display_name");
                String brand = (String) hit.get("brand");
                double price = Double.parseDouble((String) ((Map<String, Object>) hit.get("price_instructions")).get("unit_price"));
                String previousPriceStr = (String) ((Map<String, Object>) hit.get("price_instructions")).get("previous_unit_price");
                double unit_size = (double) ((Map<String, Object>) hit.get("price_instructions")).get("unit_size");
                double previous_unit_price = 0.0;

                if (previousPriceStr != null) {
                    // Eliminar los espacios en blanco al principio y al final
                    previousPriceStr = previousPriceStr.trim();

                    // Verificar si la cadena no está vacía antes de intentar convertirla a Double
                    if (!previousPriceStr.isEmpty()) {
                        previous_unit_price = Double.parseDouble(previousPriceStr);
                    }
                }
                String shareUrl = (String) hit.get("share_url");
                String picture = (String) hit.get("thumbnail");

                // Crear la instancia de Product y añadirla a la lista
                Product product = new Product(name, brand, price, previous_unit_price, shareUrl, picture, "mercadona");
                product.setUnit_size(unit_size);
                products.add(product);
            }

            return products;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al realizar la solicitud de productos", e);
        }
    }

}

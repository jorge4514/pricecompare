package com.supermarketapi.supermarketapi.markets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.supermarketapi.supermarketapi.bo.service.BaseSupermarketApi;
import com.supermarketapi.supermarketapi.dao.Product;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class ConsumApi extends BaseSupermarketApi {

    private static final String BASE_URL = "https://tienda.consum.es/api/rest/V1.0/catalog/product";

    @Autowired
    public ConsumApi(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * Método para hacer la búsqueda de productos en Consum
     * @param query el término de búsqueda recibido desde el front-end
     * @return Los resultados de la búsqueda
     */
    public List<Product> searchProducts(String query) {
        // Construir la URL completa con los parámetros de consulta
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("q", query)
                .queryParam("limit", 12)
                .queryParam("showRecommendations", false)
                .queryParam("offset", 0)
                .queryParam("page", 1)
                .queryParam("orderById", 13)
                .toUriString();

        // Configurar el encabezado para JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        // Realizar la solicitud GET utilizando RestTemplate
        try {
            // Realizar la solicitud GET y obtener la respuesta
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

            // Extraer la lista de productos del campo "products"
            //Map<String, Object> catalog = (Map<String, Object>) response.getBody().get("catalog");

            // Extraer la lista de productos desde la clave "products" dentro de "catalog"
            List<Map<String, Object>> productsData = (List<Map<String, Object>>) response.getBody().get("products");

            // Crear la lista de productos mapeados
            List<Product> products = new ArrayList<>();

            // Iterar sobre cada producto en "products" y mapearlo a un objeto Product
            for (Map<String, Object> productData : productsData) {
                // Extraer la información del producto
                Map<String, Object> productInfo = (Map<String, Object>) productData.get("productData");

                String name = (String) productInfo.get("name");

	             // Acceder correctamente a la marca, ya que "brand" es un objeto con un campo "name"
	             Map<String, Object> brandData = (Map<String, Object>) productInfo.get("brand");
	             String brand = brandData != null ? (String) brandData.get("name") : "Desconocida"; // Si no hay marca, asignamos "Desconocida"
	
	             // URL del producto
	             String shareUrl = (String) productInfo.get("url");
	
	             String picture = (String) productInfo.get("imageURL");

		          // Verificar si la URL no es nula y contiene una extensión (por ejemplo, ".jpg")
		          if (picture != null && picture.contains(".")) {
		              // Buscar la última aparición del punto (.) que marca el inicio de la extensión del archivo
		              int dotIndex = picture.lastIndexOf(".");
	
		              // Obtener la parte antes de la extensión y agregar "_001"
		              String pictureWithSuffix = picture.substring(0, dotIndex) + "_001" + picture.substring(dotIndex);
	
		              // Asignar la nueva URL de la imagen
		              picture = pictureWithSuffix;
		          }

                // Extraer el precio
                Map<String, Object> priceData = (Map<String, Object>) productData.get("priceData");
                List<Map<String, Object>> prices = (List<Map<String, Object>>) priceData.get("prices");
                double price = 0.0;
                if (prices != null && !prices.isEmpty()) {
                    Map<String, Object> priceValue = prices.get(0); // Suponiendo que hay un solo precio
                    Map<String, Object> priceAmount = (Map<String, Object>) priceValue.get("value");
                    double centAmount = (double) priceAmount.get("centAmount");
                    price = centAmount; // Convertir el precio en centavos a euros
                }

                // Crear la instancia de Product y añadirla a la lista
                Product product = new Product(name, brand, price, 0.0, shareUrl, picture, "consum");
                products.add(product);
            }

            return products;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al realizar la solicitud de productos", e);
        }
    }

}
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
public class AlcampoApi extends BaseSupermarketApi {

    private static final String BASE_URL = "https://www.compraonline.alcampo.es/api/v5/products/search";

    @Autowired
    public AlcampoApi(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * Método para realizar la búsqueda de productos en Alcampo
     * @param query El término de búsqueda recibido desde el front-end
     * @return Lista de productos
     */
    public List<Product> searchProducts(String query) {
    	String url = String.format("%s?offset=%d&term=%s", BASE_URL, 0, query);

        try {
            // Realizar la solicitud GET y obtener la respuesta
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            // Extraer la lista de productos del campo "entities.product"
            Map<String, Map<String, Object>> productMap = (Map<String, Map<String, Object>>) ((Map<String, Object>) response.getBody().get("entities")).get("product");

            List<Product> products = new ArrayList<>();

            // Mapear cada producto al objeto Product
            if(productMap != null) {
	            for (Map.Entry<String, Map<String, Object>> entry : productMap.entrySet()) {
	                Map<String, Object> productData = entry.getValue();
	
	                String name = (String) productData.get("name");
	                String brand = (String) productData.get("brand");
	                boolean available = (boolean) productData.get("available");
	
	                // Precio actual y por unidad
	                Map<String, Object> priceData = (Map<String, Object>) productData.get("price");
	                Map<String, Object> currentPrice = (Map<String, Object>) priceData.get("current");
	                double price = Double.parseDouble((String) currentPrice.get("amount"));
	
	                Map<String, Object> unitPriceData = (Map<String, Object>) priceData.get("unit");
	                double unitPrice = Double.parseDouble((String) ((Map<String, Object>) unitPriceData.get("current")).get("amount"));
	
	                // Extraer el label del precio por unidad
	                String label = (String) unitPriceData.get("label");
	                String unit = label.substring(label.lastIndexOf('.') + 1); // Obtener el texto después del último punto
	
	                String image = (String) ((Map<String, Object>) productData.get("image")).get("src");
	                String shareUrl = (String) productData.get("externalAdvertId");
	
	                // Crear la instancia de Product
	                Product product = new Product(name, brand, price, 0.0, shareUrl, image, "alcampo");
	                product.setPrice_unit(unitPrice);
	                product.setUnit(unit);
	
	                products.add(product);
	            }
            }    

            return products;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al realizar la solicitud de productos", e);
        }
    }
}

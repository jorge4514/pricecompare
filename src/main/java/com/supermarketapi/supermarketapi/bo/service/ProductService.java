package com.supermarketapi.supermarketapi.bo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarketapi.supermarketapi.dao.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private List<Product> products = new ArrayList<>();

    public ProductService() {
        
    }

    public List<Product> searchProductByName(String name) {
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Product> sortProductsByPrice(List<Product> products, boolean ascending) {
        Comparator<Product> priceComparator = Comparator.comparing(product -> {
            Double unitSize = product.getUnit_size(); // Asegúrate de que este método devuelva un Double
            if (unitSize != null && unitSize > 0) {
                return product.getPrice() / unitSize; // Precio por unidad
            } else {
                return product.getPrice(); // Solo el precio si unitSize es null o 0
            }
        });
        
        if (!ascending) {
            priceComparator = priceComparator.reversed();
        }
        
        return products.stream()
                .sorted(priceComparator)
                .collect(Collectors.toList());
    }


}

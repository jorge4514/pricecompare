package com.supermarketapi.supermarketapi.controller;

import com.supermarketapi.supermarketapi.bo.service.ProductService;
import com.supermarketapi.supermarketapi.dao.Product;
import com.supermarketapi.supermarketapi.markets.AlcampoApi;
import com.supermarketapi.supermarketapi.markets.ConsumApi;
import com.supermarketapi.supermarketapi.markets.MercadonaApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;
    private MercadonaApi mercadonaApi;
    private ConsumApi consumApi;
    private AlcampoApi alcampoApi;
    
    @Autowired
    public ProductController(ProductService productService, MercadonaApi mercadonaApi, ConsumApi consumApi, AlcampoApi alcampoApi) {
        this.productService = productService;
        this.mercadonaApi = mercadonaApi;
        this.consumApi = consumApi;
        this.alcampoApi = alcampoApi;
    }
    
    @GetMapping("/search-products")
    public List<Product> searchProducts(@RequestParam String query) throws Exception {
        // Obtener productos de ambos servicios
        List<Product> productsMercadona = mercadonaApi.searchProducts(query);
        List<Product> productsConsum = consumApi.searchProducts(query);
        List<Product> productsAlcampo = alcampoApi.searchProducts(query);

        // Crear una lista para combinar ambos resultados
        List<Product> combinedProducts = new ArrayList<>();
        combinedProducts.addAll(productsMercadona);
        combinedProducts.addAll(productsConsum);
        combinedProducts.addAll(productsAlcampo);
        
        List<Product> res = productService.sortProductsByPrice(combinedProducts, true);

        return res;
    }

}

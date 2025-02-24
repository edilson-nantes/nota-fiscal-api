package com.edilson.service;

import java.util.List;

import com.edilson.entity.ProductEntity;
import com.edilson.exception.ProductNotFoundException;
import com.edilson.repository.ProductRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public ProductEntity createProduct(ProductEntity productEntity) {
        productRepository.persist(productEntity);
        
        return productEntity;
    }

    public List<ProductEntity> findAll(Integer page, Integer pageSize) {
        return productRepository.findAll()
            .page(page, pageSize)
            .list();
    }

    public ProductEntity findById(Long id) {
        return (ProductEntity) productRepository.find("id", id)
            .firstResultOptional()
            .orElseThrow(ProductNotFoundException::new);
    }

    public ProductEntity updateProduct(Long id, ProductEntity productEntity) {
        var product = findById(id);
        
        product.setCode(productEntity.getCode());
        product.setDescription(productEntity.getDescription());
        product.setSituation(productEntity.getSituation());
        
        productRepository.persist(product);
        
        return product;
    }

    public void deleteProduct(Long id) {
        var product = findById(id);
        
        productRepository.delete(product);
    }
    
}

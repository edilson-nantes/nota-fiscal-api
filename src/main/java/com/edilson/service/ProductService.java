package com.edilson.service;

import java.util.List;

import com.edilson.entity.ProductEntity;
import com.edilson.exception.ProductNotFoundException;
import com.edilson.repository.ProductRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepository;
    
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

        if (product.isHasMovement()) {
            if (!product.getSituation().equals(productEntity.getSituation())) {
                product.setSituation(productEntity.getSituation());
                productRepository.persist(product);

                return product;
            } else {
                throw new IllegalStateException("Only the situation can be updated for a product with movement");
            }
        } else {
            product.setCode(productEntity.getCode());
            product.setDescription(productEntity.getDescription());
            product.setSituation(productEntity.getSituation());

            productRepository.persist(product);
            return product;
        }
    }

    public void deleteProduct(Long id) {
        var product = findById(id);

        if (product.isHasMovement()) {
            throw new IllegalStateException("Product has movement and cannot be deleted");
        }
        
        productRepository.delete(product);
    }

    public List<ProductEntity> searchProducts(String code, String description) {
        String searchCode = (code != null) ? "%" + code.toLowerCase() + "%" : "%";
        String searchDescription = (description != null) ? "%" + description.toLowerCase() + "%" : "%";
        
        var query = productRepository.find("LOWER(code) LIKE ?1 OR LOWER(description) LIKE ?2", 
                                           searchCode, 
                                           searchDescription);

        return query.list();
    }
    
}

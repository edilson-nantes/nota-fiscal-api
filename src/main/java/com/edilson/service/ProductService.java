package com.edilson.service;

import java.util.ArrayList;
import java.util.List;

import com.edilson.entity.ProductEntity;
import com.edilson.exception.product.InvalidProductAlterationException;
import com.edilson.exception.product.ProductNotFoundException;
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
                throw new InvalidProductAlterationException("Product has movement and cannot be updated");
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
            throw new InvalidProductAlterationException("Product has movement and cannot be deleted");
        }
        
        productRepository.delete(product);
    }

    public List<ProductEntity> searchProducts(String code, String description) {
        StringBuilder queryBuilder = new StringBuilder("FROM ProductEntity WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (code != null && !code.isEmpty()) {
            queryBuilder.append(" AND LOWER(code) LIKE :code");
            params.add("%" + code.toLowerCase() + "%");
        }
        if (description != null && !description.isEmpty()) {
            queryBuilder.append(" AND LOWER(description) LIKE :description");
            params.add("%" + description.toLowerCase() + "%");
        }

        var query = productRepository.getEntityManager().createQuery(queryBuilder.toString(), ProductEntity.class);

        int paramIndex = 0;

        if (code != null && !code.isEmpty()) {
            query.setParameter("code", params.get(paramIndex++));
        }
        if (description != null && !description.isEmpty()) {
            query.setParameter("description", params.get(paramIndex++));
        }

        return query.getResultList();
    }
    
}

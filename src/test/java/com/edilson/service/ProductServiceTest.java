package com.edilson.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.edilson.entity.ProductEntity;
import com.edilson.enums.SituationProduct;
import com.edilson.exception.product.InvalidProductAlterationException;
import com.edilson.exception.product.ProductNotFoundException;
import com.edilson.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PanacheQuery<ProductEntity> query;

    @InjectMocks
    private ProductService productService;

    @Mock
    private TypedQuery<ProductEntity> typedQuery;

    @Mock
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(productRepository.getEntityManager()).thenReturn(entityManager);
    }

    @Test
    void testCreateProduct() {
        ProductEntity product = new ProductEntity();
        product.setCode("P001");
        product.setDescription("Product 1");

        doNothing().when(productRepository).persist(any(ProductEntity.class));

        ProductEntity createdProduct = productService.createProduct(product);

        assertNotNull(createdProduct);
        assertEquals("P001", createdProduct.getCode());
        assertEquals("Product 1", createdProduct.getDescription());

        verify(productRepository, times(1)).persist(product);
    }

    @Test
    void testFindAll() {
        ProductEntity product1 = new ProductEntity();
        product1.setCode("P001");
        product1.setDescription("Product 1");

        ProductEntity product2 = new ProductEntity();
        product2.setCode("P002");
        product2.setDescription("Product 2");

        when(query.page(anyInt(), anyInt())).thenReturn(query);
        when(query.list()).thenReturn(Arrays.asList(product1, product2));
        when(productRepository.findAll()).thenReturn(query);

        List<ProductEntity> products = productService.findAll(0, 10);

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("P001", products.get(0).getCode());
        assertEquals("P002", products.get(1).getCode());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setCode("P001");
        product.setDescription("Product 1");

        when(query.firstResultOptional()).thenReturn(Optional.of(product));
        when(productRepository.find("id", 1L)).thenReturn(query);

        ProductEntity foundProduct = productService.findById(1L);

        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getId());
        assertEquals("P001", foundProduct.getCode());

        verify(productRepository, times(1)).find("id", 1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(query.firstResultOptional()).thenReturn(Optional.empty());
        when(productRepository.find("id", 1L)).thenReturn(query);

        assertThrows(ProductNotFoundException.class, () -> {
            productService.findById(1L);
        });

        verify(productRepository, times(1)).find("id", 1L);
    }

    @Test
    void testUpdateProduct() {
        ProductEntity existingProduct = new ProductEntity();
        existingProduct.setId(1L);
        existingProduct.setCode("P001");
        existingProduct.setDescription("Product 1");
        existingProduct.setSituation(SituationProduct.Ativo);
        existingProduct.setHasMovement(false);

        ProductEntity updatedProduct = new ProductEntity();
        updatedProduct.setCode("P001-UPDATED");
        updatedProduct.setDescription("Product 1 Updated");
        updatedProduct.setSituation(SituationProduct.Inativo);
        updatedProduct.setHasMovement(false);

        when(query.firstResultOptional()).thenReturn(Optional.of(existingProduct));
        when(productRepository.find("id", 1L)).thenReturn(query);
        doNothing().when(productRepository).persist(existingProduct);

        ProductEntity result = productService.updateProduct(1L, updatedProduct);

        assertNotNull(result);
        assertEquals("P001-UPDATED", result.getCode());
        assertEquals("Product 1 Updated", result.getDescription());
        assertEquals(SituationProduct.Inativo, result.getSituation());

        verify(productRepository, times(1)).find("id", 1L);
        verify(productRepository, times(1)).persist(existingProduct);
    }

    @Test
    void testUpdateProductWithMovement() {
        ProductEntity existingProduct = new ProductEntity();
        existingProduct.setId(1L);
        existingProduct.setCode("P001");
        existingProduct.setDescription("Product 1");
        existingProduct.setSituation(SituationProduct.Ativo);
        existingProduct.setHasMovement(true);

        ProductEntity updatedProduct = new ProductEntity();
        updatedProduct.setSituation(SituationProduct.Ativo);

        when(query.firstResultOptional()).thenReturn(Optional.of(existingProduct));
        when(productRepository.find("id", 1L)).thenReturn(query);

        assertThrows(InvalidProductAlterationException.class, () -> {
            productService.updateProduct(1L, updatedProduct);
        });

        verify(productRepository, times(1)).find("id", 1L);
    }

    @Test
    void testUpdateProductWithMovementAndDifferentSituation() {
        ProductEntity existingProduct = new ProductEntity();
        existingProduct.setId(1L);
        existingProduct.setCode("P001");
        existingProduct.setDescription("Product 1");
        existingProduct.setSituation(SituationProduct.Ativo);
        existingProduct.setHasMovement(true);

        ProductEntity updatedProduct = new ProductEntity();
        updatedProduct.setSituation(SituationProduct.Inativo);

        when(query.firstResultOptional()).thenReturn(Optional.of(existingProduct));
        when(productRepository.find("id", 1L)).thenReturn(query);
        doNothing().when(productRepository).persist(existingProduct);

        ProductEntity result = productService.updateProduct(1L, updatedProduct);

        assertNotNull(result);
        assertEquals(SituationProduct.Inativo, result.getSituation());

        verify(productRepository, times(1)).find("id", 1L);
        verify(productRepository, times(1)).persist(existingProduct);
    }

    @Test
    void testDeleteProduct() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setCode("P001");
        product.setDescription("Product 1");
        product.setHasMovement(false);

        when(query.firstResultOptional()).thenReturn(Optional.of(product));
        when(productRepository.find("id", 1L)).thenReturn(query);
        doNothing().when(productRepository).delete(product);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).find("id", 1L);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testDeleteProductWithMovement() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setCode("P001");
        product.setDescription("Product 1");
        product.setHasMovement(true);

        when(query.firstResultOptional()).thenReturn(Optional.of(product));
        when(productRepository.find("id", 1L)).thenReturn(query);

        assertThrows(InvalidProductAlterationException.class, () -> {
            productService.deleteProduct(1L);
        });

        verify(productRepository, times(1)).find("id", 1L);
    }

    @Test
    void searchProductsByCode() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setCode("P001");
        product.setDescription("Product 1");

        when(typedQuery.getResultList()).thenReturn(Arrays.asList(product));
        when(entityManager.createQuery(anyString(), eq(ProductEntity.class))).thenReturn(typedQuery);

        List<ProductEntity> products = productService.searchProducts("P001", "");

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("P001", products.get(0).getCode());

        verify(entityManager, times(1)).createQuery(anyString(), eq(ProductEntity.class));
        verify(typedQuery, times(1)).setParameter("code", "%p001%");
    }

    @Test
    void searchProductsByDescription() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setCode("P001");
        product.setDescription("Product 1");

        when(typedQuery.getResultList()).thenReturn(Arrays.asList(product));
        when(entityManager.createQuery(anyString(), eq(ProductEntity.class))).thenReturn(typedQuery);

        List<ProductEntity> products = productService.searchProducts("", "Product 1");

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Product 1", products.get(0).getDescription());

        verify(entityManager, times(1)).createQuery(anyString(), eq(ProductEntity.class));
        verify(typedQuery, times(1)).setParameter("description", "%product 1%");
    }

    @Test
    void searchProductsByCodeAndDescription() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setCode("P001");
        product.setDescription("Product 1");

        when(typedQuery.getResultList()).thenReturn(Arrays.asList(product));
        when(entityManager.createQuery(anyString(), eq(ProductEntity.class))).thenReturn(typedQuery);

        List<ProductEntity> products = productService.searchProducts("P001", "Product 1");

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("P001", products.get(0).getCode());
        assertEquals("Product 1", products.get(0).getDescription());

        verify(entityManager, times(1)).createQuery(anyString(), eq(ProductEntity.class));  
        verify(typedQuery, times(1)).setParameter("code", "%p001%");  
        verify(typedQuery, times(1)).setParameter("description", "%product 1%");
    }
}
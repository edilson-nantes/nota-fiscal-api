package com.edilson.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.edilson.entity.ItemNfiscalEntity;
import com.edilson.entity.NotaFiscalEntity;
import com.edilson.entity.ProductEntity;
import com.edilson.entity.SuplierEntity;
import com.edilson.enums.SituationProduct;
import com.edilson.enums.SituationSuplier;
import com.edilson.exception.itemNfiscal.ItemNfiscalNotFoundException;
import com.edilson.repository.ItemNfiscalRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

class ItemNfiscalServiceTest {

    @Mock
    private ItemNfiscalRepository itemNfiscalRepository;

    @Mock
    private NotaFiscalService notaFiscalService;

    @Mock
    private ProductService productService;

    @Mock
    private PanacheQuery<ItemNfiscalEntity> query;

    @InjectMocks
    private ItemNfiscalService itemNfiscalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        ItemNfiscalEntity item1 = new ItemNfiscalEntity();
        item1.setId(1L);

        ItemNfiscalEntity item2 = new ItemNfiscalEntity();
        item2.setId(2L);

        when(query.page(anyInt(), anyInt())).thenReturn(query);
        when(query.list()).thenReturn(Arrays.asList(item1, item2));
        when(itemNfiscalRepository.findAll()).thenReturn(query);

        List<ItemNfiscalEntity> items = itemNfiscalService.findAll(0, 10);

        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(1L, items.get(0).getId());
        assertEquals(2L, items.get(1).getId());

        verify(itemNfiscalRepository, times(1)).findAll();
    }

    @Test
    void testCreateItemNfiscal() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setCode("P001");
        product.setDescription("Product 1");
        product.setSituation(SituationProduct.Ativo);
        product.setHasMovement(false);

        SuplierEntity suplier = new SuplierEntity();
        suplier.setId(1L);
        suplier.setCode("S001");
        suplier.setCnpj("12345678901234");
        suplier.setLegalName("Suplier 1");
        suplier.setEmail("B2u2I@example.com");
        suplier.setPhone("1234567890");
        suplier.setSituation(SituationSuplier.Ativo);
        suplier.setHasMovement(false);

        List<ItemNfiscalEntity> items = new ArrayList<>();

        NotaFiscalEntity notaFiscal = new NotaFiscalEntity();
        notaFiscal.setId(1L);
        notaFiscal.setNumberNota("123");
        notaFiscal.setSuplier(suplier);
        notaFiscal.setItems(
            items
        );

        ItemNfiscalEntity itemNfiscal = new ItemNfiscalEntity();
        itemNfiscal.setProduct(product);
        itemNfiscal.setNotaFiscal(notaFiscal);
        itemNfiscal.setQuantity(10);
        itemNfiscal.setUnitValue((float) 100.00);

        when(productService.findById(anyLong())).thenReturn(product);
        when(notaFiscalService.findById(anyLong())).thenReturn(notaFiscal);
        when(productService.updateProduct(anyLong(), any(ProductEntity.class))).thenReturn(product);
        doNothing().when(itemNfiscalRepository).persist(any(ItemNfiscalEntity.class));
        when(notaFiscalService.updateNotaFiscal(anyLong(), any(NotaFiscalEntity.class))).thenReturn(notaFiscal);
        
        ItemNfiscalEntity createdItemNfiscal = itemNfiscalService.createItemNfiscal(itemNfiscal);

        assertNotNull(createdItemNfiscal);
        assertEquals(product, createdItemNfiscal.getProduct());
        assertEquals(notaFiscal, createdItemNfiscal.getNotaFiscal());
        assertEquals(1000.0, createdItemNfiscal.getTotalItemValue());

        verify(productService, times(1)).findById(anyLong());
        verify(notaFiscalService, times(1)).findById(anyLong());
        verify(productService, times(1)).updateProduct(anyLong(), any(ProductEntity.class));
        verify(itemNfiscalRepository, times(1)).persist(itemNfiscal);
        verify(notaFiscalService, times(1)).updateNotaFiscal(anyLong(), any(NotaFiscalEntity.class));
    }

    @Test
    void testFindById() {
        ItemNfiscalEntity itemNfiscal = new ItemNfiscalEntity();
        itemNfiscal.setId(1L);

        when(query.firstResultOptional()).thenReturn(Optional.of(itemNfiscal));
        when(itemNfiscalRepository.find("id", 1L)).thenReturn(query);

        ItemNfiscalEntity foundItemNfiscal = itemNfiscalService.findById(1L);

        assertNotNull(foundItemNfiscal);
        assertEquals(1L, foundItemNfiscal.getId());

        verify(itemNfiscalRepository, times(1)).find("id", 1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(query.firstResultOptional()).thenReturn(Optional.empty());
        when(itemNfiscalRepository.find("id", 1L)).thenReturn(query);

        assertThrows(ItemNfiscalNotFoundException.class, () -> {
            itemNfiscalService.findById(1L);
        });

        verify(itemNfiscalRepository, times(1)).find("id", 1L);
    }

    @Test
    void testUpdateItemNfiscal() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setCode("P001");
        product.setDescription("Product 1");
        product.setSituation(SituationProduct.Ativo);
        product.setHasMovement(false);

        SuplierEntity suplier = new SuplierEntity();
        suplier.setId(1L);
        suplier.setCode("S001");
        suplier.setCnpj("12345678901234");
        suplier.setLegalName("Suplier 1");
        suplier.setEmail("B2u2I@example.com");
        suplier.setPhone("1234567890");
        suplier.setSituation(SituationSuplier.Ativo);
        suplier.setHasMovement(false);

        List<ItemNfiscalEntity> items = new ArrayList<>();

        NotaFiscalEntity notaFiscal = new NotaFiscalEntity();
        notaFiscal.setId(1L);
        notaFiscal.setNumberNota("123");
        notaFiscal.setSuplier(suplier);
        notaFiscal.setItems(
            items
        );

        ItemNfiscalEntity existingItemNfiscal = new ItemNfiscalEntity();
        existingItemNfiscal.setId(1L);
        existingItemNfiscal.setProduct(product);
        existingItemNfiscal.setNotaFiscal(notaFiscal);

        ItemNfiscalEntity updatedItemNfiscal = new ItemNfiscalEntity();
        updatedItemNfiscal.setProduct(product);
        updatedItemNfiscal.setNotaFiscal(notaFiscal);
        updatedItemNfiscal.setQuantity(20);
        updatedItemNfiscal.setUnitValue((float) 200.00);

        when(query.firstResultOptional()).thenReturn(Optional.of(existingItemNfiscal));
        when(itemNfiscalRepository.find("id", 1L)).thenReturn(query);
        when(productService.findById(anyLong())).thenReturn(product);
        when(notaFiscalService.findById(anyLong())).thenReturn(notaFiscal);
        doNothing().when(itemNfiscalRepository).persist(existingItemNfiscal);
        when(notaFiscalService.updateNotaFiscal(anyLong(), any(NotaFiscalEntity.class))).thenReturn(notaFiscal);

        ItemNfiscalEntity result = itemNfiscalService.updateItemNfiscal(1L, updatedItemNfiscal);

        assertNotNull(result);
        assertEquals(20, result.getQuantity());
        assertEquals(200.0, result.getUnitValue());
        assertEquals(4000.0, result.getTotalItemValue());

        verify(itemNfiscalRepository, times(1)).find("id", 1L);
        verify(itemNfiscalRepository, times(1)).persist(existingItemNfiscal);
        verify(productService, times(1)).findById(anyLong());
        verify(notaFiscalService, times(1)).findById(anyLong());
        verify(notaFiscalService, times(1)).updateNotaFiscal(anyLong(), any(NotaFiscalEntity.class));
    }

    @Test
    void testUpdateItemNfiscalWithProductUpdate() {
        ProductEntity existingProduct = new ProductEntity();
        existingProduct.setId(1L);
        existingProduct.setCode("P001");
        existingProduct.setDescription("Product 1");
        existingProduct.setSituation(SituationProduct.Ativo);
        existingProduct.setHasMovement(false);

        ProductEntity updatedProduct = new ProductEntity();
        updatedProduct.setId(2L);
        updatedProduct.setCode("P002");
        updatedProduct.setDescription("Product 2");
        updatedProduct.setSituation(SituationProduct.Ativo);
        updatedProduct.setHasMovement(false);

        SuplierEntity suplier = new SuplierEntity();
        suplier.setId(1L);
        suplier.setCode("S001");
        suplier.setCnpj("12345678901234");
        suplier.setLegalName("Suplier 1");
        suplier.setEmail("B2u2I@example.com");
        suplier.setPhone("1234567890");
        suplier.setSituation(SituationSuplier.Ativo);
        suplier.setHasMovement(false);

        List<ItemNfiscalEntity> items = new ArrayList<>();

        NotaFiscalEntity notaFiscal = new NotaFiscalEntity();
        notaFiscal.setId(1L);
        notaFiscal.setNumberNota("123");
        notaFiscal.setSuplier(suplier);
        notaFiscal.setItems(items);

        ItemNfiscalEntity existingItemNfiscal = new ItemNfiscalEntity();
        existingItemNfiscal.setId(1L);
        existingItemNfiscal.setProduct(existingProduct);
        existingItemNfiscal.setNotaFiscal(notaFiscal);

        ItemNfiscalEntity updatedItemNfiscal = new ItemNfiscalEntity();
        updatedItemNfiscal.setProduct(updatedProduct);
        updatedItemNfiscal.setNotaFiscal(notaFiscal);
        updatedItemNfiscal.setQuantity(20);
        updatedItemNfiscal.setUnitValue((float) 200.0);

        when(query.firstResultOptional()).thenReturn(Optional.of(existingItemNfiscal));
        when(itemNfiscalRepository.find("id", 1L)).thenReturn(query);
        when(productService.findById(anyLong())).thenReturn(updatedProduct);
        when(notaFiscalService.findById(anyLong())).thenReturn(notaFiscal);
        doNothing().when(itemNfiscalRepository).persist(existingItemNfiscal);
        when(notaFiscalService.updateNotaFiscal(anyLong(), any(NotaFiscalEntity.class))).thenReturn(notaFiscal);

        ItemNfiscalEntity result = itemNfiscalService.updateItemNfiscal(1L, updatedItemNfiscal);

        assertNotNull(result);
        assertEquals(20, result.getQuantity());
        assertEquals(200.0, result.getUnitValue());
        assertEquals(4000.0, result.getTotalItemValue());
        assertEquals("P002", result.getProduct().getCode());
        assertEquals("Product 2", result.getProduct().getDescription());

        verify(itemNfiscalRepository, times(1)).find("id", 1L);
        verify(itemNfiscalRepository, times(1)).persist(existingItemNfiscal);
        verify(productService, times(1)).findById(anyLong());
        verify(notaFiscalService, times(1)).findById(anyLong());
        verify(notaFiscalService, times(1)).updateNotaFiscal(anyLong(), any(NotaFiscalEntity.class));
    }

    @Test
    void testDeleteItemNfiscal() {
        ItemNfiscalEntity itemNfiscal = new ItemNfiscalEntity();
        itemNfiscal.setId(1L);

        when(query.firstResultOptional()).thenReturn(Optional.of(itemNfiscal));
        when(itemNfiscalRepository.find("id", 1L)).thenReturn(query);
        doNothing().when(itemNfiscalRepository).delete(itemNfiscal);

        itemNfiscalService.deleteItemNfiscal(1L);

        verify(itemNfiscalRepository, times(1)).find("id", 1L);
        verify(itemNfiscalRepository, times(1)).delete(itemNfiscal);
    }
}
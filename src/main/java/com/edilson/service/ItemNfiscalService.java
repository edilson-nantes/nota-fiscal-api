package com.edilson.service;

import java.util.List;

import com.edilson.entity.ItemNfiscalEntity;
import com.edilson.exception.ItemNfiscalNotFoundException;
import com.edilson.repository.ItemNfiscalRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ItemNfiscalService {

    private final ItemNfiscalRepository itemNfiscalRepository;
    private final NotaFiscalService notaFiscalService;
    private final ProductService productService;

    public ItemNfiscalService(ItemNfiscalRepository itemNfiscalRepository, NotaFiscalService notaFiscalService, ProductService productService) {
        this.itemNfiscalRepository = itemNfiscalRepository;
        this.notaFiscalService = notaFiscalService;
        this.productService = productService;
    }

    public List<ItemNfiscalEntity> findAll(Integer page, Integer pageSize) {
        return itemNfiscalRepository.findAll()
            .page(page, pageSize)
            .list();
    }
    
    public ItemNfiscalEntity createItemNfiscal(ItemNfiscalEntity itemNfiscal) {
        var notaFiscal = notaFiscalService.findById(itemNfiscal
            .getNotaFiscal()
            .getId());
        var product = productService.findById(itemNfiscal
            .getProduct()
            .getId());
        
        itemNfiscal.setNotaFiscal(notaFiscal);
        itemNfiscal.setProduct(product);
        itemNfiscalRepository.persist(itemNfiscal);

        return itemNfiscal;
    }

    public ItemNfiscalEntity findById(Long id) {
        return (ItemNfiscalEntity) itemNfiscalRepository.find("id", id)
            .firstResultOptional()
            .orElseThrow(ItemNfiscalNotFoundException::new);
    }

    public ItemNfiscalEntity updateItemNfiscal(Long id, ItemNfiscalEntity itemNfiscal) {
        var itemNfiscalEntity = findById(id);
        
        var product = productService.findById(itemNfiscal
            .getProduct()
            .getId());

        var notaFiscal = notaFiscalService.findById(itemNfiscal
            .getNotaFiscal()
            .getId());
        
        itemNfiscalEntity.setNotaFiscal(notaFiscal);
        itemNfiscalEntity.setProduct(product);
        itemNfiscalEntity.setQuantity(itemNfiscal.getQuantity());
        itemNfiscalEntity.setUnitValue(itemNfiscal.getUnitValue());
        itemNfiscalEntity.setTotalItemValue(itemNfiscal.getTotalItemValue());
        
        return itemNfiscalEntity;
    }

    public void deleteItemNfiscal(Long id) {
        var itemNfiscal = findById(id);
        
        itemNfiscalRepository.delete(itemNfiscal);
    }
    
}

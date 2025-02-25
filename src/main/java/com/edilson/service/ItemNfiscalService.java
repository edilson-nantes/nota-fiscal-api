package com.edilson.service;

import java.util.List;

import com.edilson.entity.ItemNfiscalEntity;
import com.edilson.exception.itemNfiscal.ItemNfiscalNotFoundException;
import com.edilson.repository.ItemNfiscalRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ItemNfiscalService {

    @Inject
    ItemNfiscalRepository itemNfiscalRepository;
    
    @Inject
    NotaFiscalService notaFiscalService;
    
    @Inject
    ProductService productService;

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

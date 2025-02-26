package com.edilson.service;

import java.util.List;

import com.edilson.entity.ItemNfiscalEntity;
import com.edilson.entity.ProductEntity;
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
        //Buscando o produto pelo id
        var product = productService.findById(itemNfiscal
            .getProduct()
            .getId());

        // Gerando uma entity auxiliar para atualizar o produto
        ProductEntity productEntity = new ProductEntity();
        productEntity.setCode(product.getCode());
        productEntity.setDescription(product.getDescription());
        productEntity.setSituation(product.getSituation());
        productEntity.setHasMovement(true);

        //Atualizando o produto usando o método updateProduct
        productService.updateProduct(product.getId(), productEntity);
        
        //Buscando a nota fiscal pelo id
        var notaFiscal = notaFiscalService.findById(itemNfiscal
            .getNotaFiscal()
            .getId());
        

        itemNfiscal.setNotaFiscal(notaFiscal);
        itemNfiscal.setProduct(product);
        itemNfiscal.setTotalItemValue(itemNfiscal.calculateTotalItemValue(itemNfiscal.getQuantity(), itemNfiscal.getUnitValue()));

        
        //Persistindo a entity itemNfiscal
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
        
        //Buscando o produto pelo id
        var product = productService.findById(itemNfiscal
            .getProduct()
            .getId());

        // Gerando uma entity auxiliar para atualizar o produto
        ProductEntity productEntity = new ProductEntity();
        productEntity.setCode(product.getCode());
        productEntity.setDescription(product.getDescription());
        productEntity.setSituation(product.getSituation());
        productEntity.setHasMovement(true);

        //Atualizando o produto usando o método updateProduct
        productService.updateProduct(product.getId(), productEntity);
        
        //Buscando a nota fiscal pelo id
        var notaFiscal = notaFiscalService.findById(itemNfiscal
            .getNotaFiscal()
            .getId());
        
        itemNfiscalEntity.setNotaFiscal(notaFiscal);
        itemNfiscalEntity.setProduct(product);
        itemNfiscalEntity.setQuantity(itemNfiscal.getQuantity());
        itemNfiscalEntity.setUnitValue(itemNfiscal.getUnitValue());
        itemNfiscalEntity.setTotalItemValue(itemNfiscal.calculateTotalItemValue(itemNfiscal.getQuantity(), itemNfiscal.getUnitValue()));
        
        return itemNfiscalEntity;
    }

    public void deleteItemNfiscal(Long id) {
        var itemNfiscal = findById(id);
        
        itemNfiscalRepository.delete(itemNfiscal);
    }
    
}

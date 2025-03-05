package com.edilson.service;

import java.util.List;

import com.edilson.entity.ItemNfiscalEntity;
import com.edilson.entity.NotaFiscalEntity;
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
        ProductEntity product = productService.findById(itemNfiscal
            .getProduct()
            .getId());

        //Gerando uma entity auxiliar para atualizar o produto
        ProductEntity productEntity = new ProductEntity();
        productEntity.setCode(product.getCode());
        productEntity.setDescription(product.getDescription());
        productEntity.setSituation(product.getSituation());
        productEntity.setHasMovement(true);

        //Atualizando o produto usando o método updateProduct
        productService.updateProduct(product.getId(), productEntity);
        
        //Buscando a nota fiscal pelo id
        NotaFiscalEntity notaFiscal = notaFiscalService.findById(itemNfiscal
            .getNotaFiscal()
            .getId());
        

        itemNfiscal.setNotaFiscal(notaFiscal);
        itemNfiscal.setProduct(product);
        itemNfiscal.setTotalItemValue(itemNfiscal.calculateTotalItemValue(itemNfiscal.getQuantity(), itemNfiscal.getUnitValue()));

        
        //Persistindo a entity itemNfiscal
        itemNfiscalRepository.persist(itemNfiscal);

        // Adicionando o item à lista de itens da nota fiscal
        notaFiscal.getItems().add(itemNfiscal);

        // Recalculando o valor total da nota fiscal
        notaFiscal.setTotalValue(notaFiscal.calculateTotalValue());

        // Atualizando a nota fiscal
        notaFiscalService.updateNotaFiscal(notaFiscal.getId(), notaFiscal);

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

        if (itemNfiscalEntity.getProduct().getId() != product.getId()) {
            // Gerando uma entity auxiliar para atualizar o produto
            ProductEntity productEntity = new ProductEntity();
            productEntity.setCode(product.getCode());
            productEntity.setDescription(product.getDescription());
            productEntity.setSituation(product.getSituation());
            productEntity.setHasMovement(true);

            //Atualizando o produto usando o método updateProduct
            productService.updateProduct(product.getId(), productEntity);
        }

       
        
        //Buscando a nota fiscal pelo id
        var notaFiscal = notaFiscalService.findById(itemNfiscal
            .getNotaFiscal()
            .getId());
        
        itemNfiscalEntity.setNotaFiscal(notaFiscal);
        itemNfiscalEntity.setProduct(product);
        itemNfiscalEntity.setQuantity(itemNfiscal.getQuantity());
        itemNfiscalEntity.setUnitValue(itemNfiscal.getUnitValue());
        itemNfiscalEntity.setTotalItemValue(itemNfiscal.calculateTotalItemValue(itemNfiscal.getQuantity(), itemNfiscal.getUnitValue()));

        itemNfiscalRepository.persist(itemNfiscalEntity);

        // Recalculando o valor total da nota fiscal
        notaFiscal.setTotalValue(notaFiscal.calculateTotalValue());

        // Atualizando a nota fiscal
        notaFiscalService.updateNotaFiscal(notaFiscal.getId(), notaFiscal);
        
        return itemNfiscalEntity;
    }

    public void deleteItemNfiscal(Long id) {
        var itemNfiscal = findById(id);

        var notaFiscal = itemNfiscal.getNotaFiscal();

        notaFiscal.setTotalValue(notaFiscal.getTotalValue() - itemNfiscal.getTotalItemValue());
        
        itemNfiscalRepository.delete(itemNfiscal);

        // Atualizando a nota fiscal
        notaFiscalService.updateNotaFiscal(notaFiscal.getId(), notaFiscal);
    }
    
}

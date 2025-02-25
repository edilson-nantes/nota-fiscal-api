package com.edilson.repository;

import com.edilson.entity.ItemNfiscalEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ItemNfiscalRepository implements PanacheRepositoryBase<ItemNfiscalEntity, Long> {
    
}

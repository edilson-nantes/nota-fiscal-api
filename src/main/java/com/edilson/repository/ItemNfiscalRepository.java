package com.edilson.repository;

import com.edilson.entity.ItemNfiscalEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ItemNfiscalRepository implements PanacheRepository<ItemNfiscalEntity> {
    
}

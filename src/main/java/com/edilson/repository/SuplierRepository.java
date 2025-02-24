package com.edilson.repository;

import com.edilson.entity.SuplierEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SuplierRepository implements PanacheRepositoryBase<SuplierEntity, Long> {
    
}

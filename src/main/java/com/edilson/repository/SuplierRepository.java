package com.edilson.repository;

import com.edilson.entity.SuplierEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SuplierRepository implements PanacheRepository<SuplierEntity> {
    
}

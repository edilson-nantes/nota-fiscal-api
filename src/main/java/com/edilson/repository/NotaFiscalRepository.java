package com.edilson.repository;

import com.edilson.entity.NotaFiscalEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotaFiscalRepository implements PanacheRepositoryBase<NotaFiscalEntity, Long> {
    
}

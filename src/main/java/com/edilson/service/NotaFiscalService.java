package com.edilson.service;

import java.util.List;

import com.edilson.entity.NotaFiscalEntity;
import com.edilson.exception.NotaFiscalNotFoundException;
import com.edilson.repository.NotaFiscalRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NotaFiscalService {

    @Inject
    NotaFiscalRepository notaFiscalRepository;

    @Inject
    SuplierService suplierService;

    public List<NotaFiscalEntity> findAll(Integer page, Integer pageSize) {
        return notaFiscalRepository.findAll()
            .page(page, pageSize)
            .list();
    }

    public NotaFiscalEntity createNotaFiscal(NotaFiscalEntity notaFiscalEntity) {
        var suplier = suplierService.findById(notaFiscalEntity.getSuplier().getId());
        notaFiscalEntity.setSuplier(suplier);
        notaFiscalRepository.persist(notaFiscalEntity);
        
        return notaFiscalEntity;
    }

    public NotaFiscalEntity findById(Long id) {
        return (NotaFiscalEntity) notaFiscalRepository.find("id", id)
            .firstResultOptional()
            .orElseThrow(NotaFiscalNotFoundException::new);
    }

    public NotaFiscalEntity updateNotaFiscal(Long id, NotaFiscalEntity notaFiscalEntity) {
        var notaFiscal = findById(id);
        var suplier = suplierService.findById(notaFiscalEntity.getSuplier().getId());
        
        notaFiscal.setNumberNota(notaFiscalEntity.getNumberNota());
        notaFiscal.setEmissionDate(notaFiscalEntity.getEmissionDate());
        notaFiscal.setSuplier(suplier);
        notaFiscal.setAddress(notaFiscalEntity.getAddress());
        notaFiscal.setTotalValue(notaFiscalEntity.getTotalValue());
        
        
        return notaFiscal;
    }

    public void deleteNotaFiscal(Long id) {
        var notaFiscal = findById(id);
        
        notaFiscalRepository.delete(notaFiscal);
    }
    
}

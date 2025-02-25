package com.edilson.service;

import java.util.List;

import com.edilson.entity.SuplierEntity;
import com.edilson.exception.SuplierNotFoundException;
import com.edilson.repository.SuplierRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SuplierService {

    @Inject
    SuplierRepository suplierRepository;

    public List<SuplierEntity> findAll(Integer page, Integer pageSize) {
        return suplierRepository.findAll()
            .page(page, pageSize)
            .list();
    }

    public SuplierEntity createSuplier(SuplierEntity suplierEntity) {
        var suplier = suplierRepository.find("cnpj", suplierEntity.getCnpj())
            .firstResultOptional();
        
        if (suplier.isPresent()) {
            throw new IllegalArgumentException("CNPJ já cadastrado");
        }

        var suplierCode = suplierRepository.find("code", suplierEntity.getCode())
            .firstResultOptional();
        
        if (suplierCode.isPresent()) {
            throw new IllegalArgumentException("Código já cadastrado");
        }
        
        suplierRepository.persist(suplierEntity);
        
        return suplierEntity;
    }

    public SuplierEntity findById(Long id) {
        return (SuplierEntity) suplierRepository.find("id", id)
            .firstResultOptional()
            .orElseThrow(SuplierNotFoundException::new);
    }

    public SuplierEntity updateSuplier(Long id, SuplierEntity suplierEntity) {
        var suplier = findById(id);
        
        if (suplier.isHasMovement()) {
            if (!suplier.getSituation().equals(suplierEntity.getSituation())) {
                suplier.setSituation(suplierEntity.getSituation());
                suplierRepository.persist(suplier);
                
                return suplier;
            } else {
                throw new IllegalArgumentException("Suplier has movement and cannot be updated");
                
            }
        } else {
            suplier.setCode(suplierEntity.getCode());
            suplier.setLegalName(suplierEntity.getLegalName());
            suplier.setEmail(suplierEntity.getEmail());
            suplier.setPhone(suplierEntity.getPhone());
            suplier.setCnpj(suplierEntity.getCnpj());
            suplier.setSituation(suplierEntity.getSituation());
            suplier.setDataBaixa(suplierEntity.getDataBaixa());
            
            suplierRepository.persist(suplier);
            
            return suplier;
        }
    }

    public void deleteSuplier(Long id) {
        var suplier = findById(id);

        if (suplier.isHasMovement()) {
            throw new IllegalArgumentException("Suplier has movement and cannot be deleted");
        }
        
        suplierRepository.delete(suplier);
    }
    
}

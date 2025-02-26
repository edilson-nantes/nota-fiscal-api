package com.edilson.service;

import java.util.ArrayList;
import java.util.List;

import com.edilson.entity.SuplierEntity;
import com.edilson.exception.suplier.DuplicatedSuplierException;
import com.edilson.exception.suplier.InvalidSuplierAlterationException;
import com.edilson.exception.suplier.SuplierNotFoundException;
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
            throw new DuplicatedSuplierException("CNPJ já cadastrado");
        }

        var suplierCode = suplierRepository.find("code", suplierEntity.getCode())
            .firstResultOptional();
        
        if (suplierCode.isPresent()) {
            throw new DuplicatedSuplierException("Código já cadastrado");
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

        System.out.println(suplier.isHasMovement());

        System.out.println(suplierEntity.isHasMovement());
        
        if (suplier.isHasMovement()) {
            if (!suplier.getSituation().equals(suplierEntity.getSituation())) {
                suplier.setSituation(suplierEntity.getSituation());
                suplierRepository.persist(suplier);
                
                return suplier;
            } else {
                throw new InvalidSuplierAlterationException("Suplier has movement and cannot be updated");
                
            }
        } else {
            suplier.setCode(suplierEntity.getCode());
            suplier.setLegalName(suplierEntity.getLegalName());
            suplier.setEmail(suplierEntity.getEmail());
            suplier.setPhone(suplierEntity.getPhone());
            suplier.setCnpj(suplierEntity.getCnpj());
            suplier.setSituation(suplierEntity.getSituation());
            suplier.setDataBaixa(suplierEntity.getDataBaixa());
            suplier.setHasMovement(suplierEntity.isHasMovement());
            
            suplierRepository.persist(suplier);
            
            return suplier;
        }
    }

    public void deleteSuplier(Long id) {
        var suplier = findById(id);

        if (suplier.isHasMovement()) {
            throw new InvalidSuplierAlterationException("Suplier has movement and cannot be deleted");
        }
        
        suplierRepository.delete(suplier);
    }

    public List<SuplierEntity> searchSupliers(String code, String legalName, String email, String phone, String cnpj) {
        StringBuilder queryBuilder = new StringBuilder("FROM SuplierEntity WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (code != null && !code.isEmpty()) {
            queryBuilder.append(" AND LOWER(code) LIKE :code");
            params.add("%" + code.toLowerCase() + "%");
        }
        if (legalName != null && !legalName.isEmpty()) {
            queryBuilder.append(" AND LOWER(legalName) LIKE :legalName");
            params.add("%" + legalName.toLowerCase() + "%");
        }
        if (email != null && !email.isEmpty()) {
            queryBuilder.append(" AND LOWER(email) LIKE :email");
            params.add("%" + email.toLowerCase() + "%");
        }
        if (phone != null && !phone.isEmpty()) {
            queryBuilder.append(" AND LOWER(phone) LIKE :phone");
            params.add("%" + phone.toLowerCase() + "%");
        }
        if (cnpj != null && !cnpj.isEmpty()) {
            queryBuilder.append(" AND LOWER(cnpj) LIKE :cnpj");
            params.add("%" + cnpj.toLowerCase() + "%");
        }

        var query = suplierRepository.getEntityManager().createQuery(queryBuilder.toString(), SuplierEntity.class);

        int paramIndex = 0;
        if (code != null && !code.isEmpty()) {
            query.setParameter("code", params.get(paramIndex++));
        }
        if (legalName != null && !legalName.isEmpty()) {
            query.setParameter("legalName", params.get(paramIndex++));
        }
        if (email != null && !email.isEmpty()) {
            query.setParameter("email", params.get(paramIndex++));
        }
        if (phone != null && !phone.isEmpty()) {
            query.setParameter("phone", params.get(paramIndex++));
        }
        if (cnpj != null && !cnpj.isEmpty()) {
            query.setParameter("cnpj", params.get(paramIndex++));
        }

        return query.getResultList();
    }
    
}

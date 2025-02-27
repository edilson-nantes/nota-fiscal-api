package com.edilson.service;

import java.util.ArrayList;
import java.util.List;

import com.edilson.entity.NotaFiscalEntity;
import com.edilson.entity.SuplierEntity;
import com.edilson.exception.notaFiscal.NotaFiscalNotFoundException;
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
        //Buscando o fornecedor pelo id
        var suplier = suplierService.findById(notaFiscalEntity
            .getSuplier()
            .getId());

        //Gerando uma entity auxiliar para atualizar o fornecedor
        SuplierEntity suplierEntity = new SuplierEntity();
        suplierEntity.setCode(suplier.getCode());
        suplierEntity.setLegalName(suplier.getLegalName());
        suplierEntity.setEmail(suplier.getEmail());
        suplierEntity.setPhone(suplier.getPhone());
        suplierEntity.setCnpj(suplier.getCnpj());
        suplierEntity.setSituation(suplier.getSituation());
        suplierEntity.setDataBaixa(suplier.getDataBaixa());
        suplierEntity.setHasMovement(true);

        //Atualizando o fornecedor usando o método updateSuplier
        suplierService.updateSuplier(suplier.getId(), suplierEntity);

        notaFiscalEntity.setSuplier(suplier);

        //Persistindo a entity notaFiscal
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

        //Buscando o fornecedor pelo id
        var suplier = suplierService.findById(notaFiscalEntity
            .getSuplier()
            .getId());

        //Gerando uma entity auxiliar para atualizar o fornecedor
        SuplierEntity suplierEntity = new SuplierEntity();
        suplierEntity.setCode(suplier.getCode());
        suplierEntity.setLegalName(suplier.getLegalName());
        suplierEntity.setEmail(suplier.getEmail());
        suplierEntity.setPhone(suplier.getPhone());
        suplierEntity.setCnpj(suplier.getCnpj());
        suplierEntity.setSituation(suplier.getSituation());
        suplierEntity.setDataBaixa(suplier.getDataBaixa());
        suplierEntity.setHasMovement(true);

        //Atualizando o fornecedor usando o método updateSuplier
        suplierService.updateSuplier(suplier.getId(), suplierEntity);
        
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

    public List<NotaFiscalEntity> searchNotas(String numberNota) {
        StringBuilder queryBuilder = new StringBuilder("FROM NotaFiscalEntity WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (numberNota != null && !numberNota.isEmpty()) {
            queryBuilder.append(" AND LOWER(numberNota) LIKE :numberNota");
            params.add("%" + numberNota.toLowerCase() + "%");
        }

        var query = notaFiscalRepository.getEntityManager().createQuery(queryBuilder.toString(), NotaFiscalEntity.class);

        if (numberNota != null && !numberNota.isEmpty()) {
            query.setParameter("numberNota", params.get(0));
        }

        return query.getResultList();
    }
    
}

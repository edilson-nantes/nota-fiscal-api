package com.edilson.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.edilson.entity.SuplierEntity;
import com.edilson.enums.SituationSuplier;
import com.edilson.exception.suplier.DuplicatedSuplierException;
import com.edilson.exception.suplier.InvalidSuplierAlterationException;
import com.edilson.exception.suplier.SuplierNotFoundException;
import com.edilson.repository.SuplierRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

class SuplierServiceTest {

    @Mock
    private SuplierRepository suplierRepository;

    @Mock
    private PanacheQuery<SuplierEntity> query;

    @Mock
    private PanacheQuery<SuplierEntity> emptyQuery;

    @Mock
    private PanacheQuery<SuplierEntity> codeQuery;

    @Mock
    private TypedQuery<SuplierEntity> typedQuery;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private SuplierService suplierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(suplierRepository.getEntityManager()).thenReturn(entityManager);
    }

    @Test
    void testFindAll() {
        SuplierEntity suplier1 = new SuplierEntity();
        suplier1.setCode("S001");
        suplier1.setLegalName("Suplier 1");

        SuplierEntity suplier2 = new SuplierEntity();
        suplier2.setCode("S002");
        suplier2.setLegalName("Suplier 2");

        when(query.page(anyInt(), anyInt())).thenReturn(query);
        when(query.list()).thenReturn(Arrays.asList(suplier1, suplier2));
        when(suplierRepository.findAll()).thenReturn(query);

        List<SuplierEntity> supliers = suplierService.findAll(0, 10);

        assertNotNull(supliers);
        assertEquals(2, supliers.size());
        assertEquals("S001", supliers.get(0).getCode());
        assertEquals("S002", supliers.get(1).getCode());

        verify(suplierRepository, times(1)).findAll();
    }

    @Test
    void testCreateSuplier() {
        SuplierEntity suplier = new SuplierEntity();
        suplier.setCode("S001");
        suplier.setCnpj("12345678901234");
        suplier.setLegalName("Suplier 1");

        when(suplierRepository.find("cnpj", suplier.getCnpj())).thenReturn(query);
        when(suplierRepository.find("code", suplier.getCode())).thenReturn(query);
        doNothing().when(suplierRepository).persist(any(SuplierEntity.class));

        SuplierEntity createdSuplier = suplierService.createSuplier(suplier);

        assertNotNull(createdSuplier);
        assertEquals("S001", createdSuplier.getCode());
        assertEquals("12345678901234", createdSuplier.getCnpj());
        assertEquals("Suplier 1", createdSuplier.getLegalName());

        verify(suplierRepository, times(1)).find("cnpj", suplier.getCnpj());
        verify(suplierRepository, times(1)).find("code", suplier.getCode());
        verify(suplierRepository, times(1)).persist(suplier);
    }

    @Test
    void testCreateSuplier_DuplicatedCnpj() {
        SuplierEntity suplier = new SuplierEntity();
        suplier.setCode("S001");
        suplier.setCnpj("12345678901234");
        suplier.setLegalName("Suplier 1");

        when(query.firstResultOptional()).thenReturn(Optional.of(suplier));
        when(suplierRepository.find("cnpj", suplier.getCnpj())).thenReturn(query);

        assertThrows(DuplicatedSuplierException.class, () -> {
            suplierService.createSuplier(suplier);
        });

        verify(suplierRepository, times(1)).find("cnpj", suplier.getCnpj());
    }

    @Test
    void testCreateSuplier_DuplicatedCode() {
        SuplierEntity suplier = new SuplierEntity();
        suplier.setCode("S001");
        suplier.setCnpj("12345678901234");
        suplier.setLegalName("Suplier 1");

        when(emptyQuery.firstResultOptional()).thenReturn(Optional.empty());

        when(codeQuery.firstResultOptional()).thenReturn(Optional.of(suplier));

        when(suplierRepository.find("cnpj", suplier.getCnpj())).thenReturn(emptyQuery);
        when(suplierRepository.find("code", suplier.getCode())).thenReturn(codeQuery);

        assertThrows(DuplicatedSuplierException.class, () -> {
            suplierService.createSuplier(suplier);
        });

        verify(suplierRepository, times(1)).find("cnpj", suplier.getCnpj());
        verify(suplierRepository, times(1)).find("code", suplier.getCode());
    }

    @Test
    void testFindById() {
        SuplierEntity suplier = new SuplierEntity();
        suplier.setId(1L);
        suplier.setCode("S001");
        suplier.setLegalName("Suplier 1");

        when(query.firstResultOptional()).thenReturn(Optional.of(suplier));
        when(suplierRepository.find("id", 1L)).thenReturn(query);

        SuplierEntity foundSuplier = suplierService.findById(1L);

        assertNotNull(foundSuplier);
        assertEquals(1L, foundSuplier.getId());
        assertEquals("S001", foundSuplier.getCode());

        verify(suplierRepository, times(1)).find("id", 1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(query.firstResultOptional()).thenReturn(Optional.empty());
        when(suplierRepository.find("id", 1L)).thenReturn(query);

        assertThrows(SuplierNotFoundException.class, () -> {
            suplierService.findById(1L);
        });

        verify(suplierRepository, times(1)).find("id", 1L);
    }

    @Test
    void testUpdateSuplier() {
        SuplierEntity existingSuplier = new SuplierEntity();
        existingSuplier.setId(1L);
        existingSuplier.setCode("S001");
        existingSuplier.setLegalName("Suplier 1");
        existingSuplier.setSituation(SituationSuplier.Ativo);
        existingSuplier.setHasMovement(false);

        SuplierEntity updatedSuplier = new SuplierEntity();
        updatedSuplier.setCode("S001-UPDATED");
        updatedSuplier.setLegalName("Suplier 1 Updated");
        updatedSuplier.setSituation(SituationSuplier.Suspenso);
        updatedSuplier.setHasMovement(false);

        when(query.firstResultOptional()).thenReturn(Optional.of(existingSuplier));
        when(suplierRepository.find("id", 1L)).thenReturn(query);
        doNothing().when(suplierRepository).persist(existingSuplier);

        SuplierEntity result = suplierService.updateSuplier(1L, updatedSuplier);

        assertNotNull(result);
        assertEquals("S001-UPDATED", result.getCode());
        assertEquals("Suplier 1 Updated", result.getLegalName());
        assertEquals(SituationSuplier.Suspenso, result.getSituation());

        verify(suplierRepository, times(1)).find("id", 1L);
        verify(suplierRepository, times(1)).persist(existingSuplier);
    }

    @Test
    void testUpdateSuplierWithMovement() {
        SuplierEntity existingSuplier = new SuplierEntity();
        existingSuplier.setId(1L);
        existingSuplier.setCode("S001");
        existingSuplier.setLegalName("Suplier 1");
        existingSuplier.setSituation(SituationSuplier.Ativo);
        existingSuplier.setHasMovement(true);

        SuplierEntity updatedSuplier = new SuplierEntity();
        updatedSuplier.setSituation(SituationSuplier.Ativo);

        when(query.firstResultOptional()).thenReturn(Optional.of(existingSuplier));
        when(suplierRepository.find("id", 1L)).thenReturn(query);

        assertThrows(InvalidSuplierAlterationException.class, () -> {
            suplierService.updateSuplier(1L, updatedSuplier);
        });

        verify(suplierRepository, times(1)).find("id", 1L);
    }

    @Test
    void testUpdateSuplierWithMovementAndDifferentSituation() {
        SuplierEntity existingSuplier = new SuplierEntity();
        existingSuplier.setId(1L);
        existingSuplier.setCode("S001");
        existingSuplier.setLegalName("Suplier 1");
        existingSuplier.setSituation(SituationSuplier.Ativo);
        existingSuplier.setHasMovement(true);

        SuplierEntity updatedSuplier = new SuplierEntity();
        updatedSuplier.setSituation(SituationSuplier.Suspenso);

        when(query.firstResultOptional()).thenReturn(Optional.of(existingSuplier));
        when(suplierRepository.find("id", 1L)).thenReturn(query);
        doNothing().when(suplierRepository).persist(existingSuplier);

        SuplierEntity result = suplierService.updateSuplier(1L, updatedSuplier);

        assertNotNull(result);
        assertEquals(SituationSuplier.Suspenso, result.getSituation());

        verify(suplierRepository, times(1)).find("id", 1L);
        verify(suplierRepository, times(1)).persist(existingSuplier);
    }

    @Test
    void testDeleteSuplier() {
        SuplierEntity suplier = new SuplierEntity();
        suplier.setId(1L);
        suplier.setCode("S001");
        suplier.setLegalName("Suplier 1");
        suplier.setHasMovement(false);

        when(query.firstResultOptional()).thenReturn(Optional.of(suplier));
        when(suplierRepository.find("id", 1L)).thenReturn(query);
        doNothing().when(suplierRepository).delete(suplier);

        suplierService.deleteSuplier(1L);

        verify(suplierRepository, times(1)).find("id", 1L);
        verify(suplierRepository, times(1)).delete(suplier);
    }

    @Test
    void testDeleteSuplierWithMovement() {
        SuplierEntity suplier = new SuplierEntity();
        suplier.setId(1L);
        suplier.setCode("S001");
        suplier.setLegalName("Suplier 1");
        suplier.setHasMovement(true);

        when(query.firstResultOptional()).thenReturn(Optional.of(suplier));
        when(suplierRepository.find("id", 1L)).thenReturn(query);

        assertThrows(InvalidSuplierAlterationException.class, () -> {
            suplierService.deleteSuplier(1L);
        });

        verify(suplierRepository, times(1)).find("id", 1L);
    }

    @Test
    void testSearchSupliersByCode() {
        SuplierEntity suplier1 = new SuplierEntity();
        suplier1.setId(1L);
        suplier1.setCode("S001");
        suplier1.setLegalName("Suplier 1");

        when(typedQuery.getResultList()).thenReturn(Arrays.asList(suplier1));
        when(entityManager.createQuery(anyString(), eq(SuplierEntity.class))).thenReturn(typedQuery);

        List<SuplierEntity> supliers = suplierService.searchSupliers("S001", "", "", "", "");

        assertNotNull(supliers);
        assertEquals(1, supliers.size());
        assertEquals("S001", supliers.get(0).getCode());

        verify(entityManager, times(1)).createQuery(anyString(), eq(SuplierEntity.class));
        verify(typedQuery, times(1)).setParameter("code", "%s001%");
    }

    @Test
    void testSearchSupliersByLegalName() {
        SuplierEntity suplier1 = new SuplierEntity();
        suplier1.setId(1L);
        suplier1.setCode("S001");
        suplier1.setLegalName("Suplier 1");

        when(typedQuery.getResultList()).thenReturn(Arrays.asList(suplier1));
        when(entityManager.createQuery(anyString(), eq(SuplierEntity.class))).thenReturn(typedQuery);

        List<SuplierEntity> supliers = suplierService.searchSupliers("", "Suplier 1", "", "", "");

        assertNotNull(supliers);
        assertEquals(1, supliers.size());
        assertEquals("Suplier 1", supliers.get(0).getLegalName());

        verify(entityManager, times(1)).createQuery(anyString(), eq(SuplierEntity.class));
        verify(typedQuery, times(1)).setParameter("legalName", "%suplier 1%");
    }

    @Test
    void testSearchSupliersByEmail() {
        SuplierEntity suplier1 = new SuplierEntity();
        suplier1.setId(1L);
        suplier1.setCode("S001");
        suplier1.setLegalName("Suplier 1");
        suplier1.setEmail("suplier1@example.com");

        when(typedQuery.getResultList()).thenReturn(Arrays.asList(suplier1));
        when(entityManager.createQuery(anyString(), eq(SuplierEntity.class))).thenReturn(typedQuery);

        List<SuplierEntity> supliers = suplierService.searchSupliers("", "", "suplier1@example.com", "", "");

        assertNotNull(supliers);
        assertEquals(1, supliers.size());
        assertEquals("suplier1@example.com", supliers.get(0).getEmail());

        verify(entityManager, times(1)).createQuery(anyString(), eq(SuplierEntity.class));
        verify(typedQuery, times(1)).setParameter("email", "%suplier1@example.com%");
    }

    @Test
    void testSearchSupliersByPhone() {
        SuplierEntity suplier1 = new SuplierEntity();
        suplier1.setId(1L);
        suplier1.setCode("S001");
        suplier1.setLegalName("Suplier 1");
        suplier1.setPhone("1234567890");

        when(typedQuery.getResultList()).thenReturn(Arrays.asList(suplier1));
        when(entityManager.createQuery(anyString(), eq(SuplierEntity.class))).thenReturn(typedQuery);

        List<SuplierEntity> supliers = suplierService.searchSupliers("", "", "", "1234567890", "");

        assertNotNull(supliers);
        assertEquals(1, supliers.size());
        assertEquals("1234567890", supliers.get(0).getPhone());

        verify(entityManager, times(1)).createQuery(anyString(), eq(SuplierEntity.class));
        verify(typedQuery, times(1)).setParameter("phone", "%1234567890%");
    }

    @Test
    void testSearchSupliersByCnpj() {
        SuplierEntity suplier1 = new SuplierEntity();
        suplier1.setId(1L);
        suplier1.setCode("S001");
        suplier1.setLegalName("Suplier 1");
        suplier1.setCnpj("12345678901234");

        when(typedQuery.getResultList()).thenReturn(Arrays.asList(suplier1));
        when(entityManager.createQuery(anyString(), eq(SuplierEntity.class))).thenReturn(typedQuery);

        List<SuplierEntity> supliers = suplierService.searchSupliers("", "", "", "", "12345678901234");

        assertNotNull(supliers);
        assertEquals(1, supliers.size());
        assertEquals("12345678901234", supliers.get(0).getCnpj());

        verify(entityManager, times(1)).createQuery(anyString(), eq(SuplierEntity.class));
        verify(typedQuery, times(1)).setParameter("cnpj", "%12345678901234%");
    }

    @Test
    void testSearchSupliersWithAllParameters() {
        SuplierEntity suplier1 = new SuplierEntity();
        suplier1.setId(1L);
        suplier1.setCode("S001");
        suplier1.setLegalName("Suplier 1");
        suplier1.setEmail("suplier1@example.com");
        suplier1.setPhone("1234567890");
        suplier1.setCnpj("12345678901234");

        when(typedQuery.getResultList()).thenReturn(Arrays.asList(suplier1));
        when(entityManager.createQuery(anyString(), eq(SuplierEntity.class))).thenReturn(typedQuery);

        List<SuplierEntity> supliers = suplierService.searchSupliers("S001", "Suplier 1", "suplier1@example.com", "1234567890", "12345678901234");

        assertNotNull(supliers);
        assertEquals(1, supliers.size());
        assertEquals("S001", supliers.get(0).getCode());
        assertEquals("Suplier 1", supliers.get(0).getLegalName());
        assertEquals("suplier1@example.com", supliers.get(0).getEmail());
        assertEquals("1234567890", supliers.get(0).getPhone());
        assertEquals("12345678901234", supliers.get(0).getCnpj());

        verify(entityManager, times(1)).createQuery(anyString(), eq(SuplierEntity.class));
        verify(typedQuery, times(1)).setParameter("code", "%s001%");
        verify(typedQuery, times(1)).setParameter("legalName", "%suplier 1%");
        verify(typedQuery, times(1)).setParameter("email", "%suplier1@example.com%");
        verify(typedQuery, times(1)).setParameter("phone", "%1234567890%");
        verify(typedQuery, times(1)).setParameter("cnpj", "%12345678901234%");
    }
}
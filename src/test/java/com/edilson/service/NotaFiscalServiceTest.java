package com.edilson.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.edilson.entity.NotaFiscalEntity;
import com.edilson.entity.SuplierEntity;
import com.edilson.exception.notaFiscal.NotaFiscalNotFoundException;
import com.edilson.repository.NotaFiscalRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

class NotaFiscalServiceTest {

    @Mock
    private NotaFiscalRepository notaFiscalRepository;

    @Mock
    private SuplierService suplierService;

    @Mock
    private PanacheQuery<NotaFiscalEntity> query;

    @Mock
    private TypedQuery<NotaFiscalEntity> typedQuery;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private NotaFiscalService notaFiscalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(notaFiscalRepository.getEntityManager()).thenReturn(entityManager);
    }

    @Test
    void testFindAll() {
        NotaFiscalEntity notaFiscal1 = new NotaFiscalEntity();
        notaFiscal1.setNumberNota("123");

        NotaFiscalEntity notaFiscal2 = new NotaFiscalEntity();
        notaFiscal2.setNumberNota("456");

        when(query.page(anyInt(), anyInt())).thenReturn(query);
        when(query.list()).thenReturn(Arrays.asList(notaFiscal1, notaFiscal2));
        when(notaFiscalRepository.findAll()).thenReturn(query);

        List<NotaFiscalEntity> notas = notaFiscalService.findAll(0, 10);

        assertNotNull(notas);
        assertEquals(2, notas.size());
        assertEquals("123", notas.get(0).getNumberNota());
        assertEquals("456", notas.get(1).getNumberNota());

        verify(notaFiscalRepository, times(1)).findAll();
    }

    @Test
    void testCreateNotaFiscal() {
        SuplierEntity suplier = new SuplierEntity();
        suplier.setId(1L);
        suplier.setCode("S001");

        NotaFiscalEntity notaFiscal = new NotaFiscalEntity();
        notaFiscal.setNumberNota("123");
        notaFiscal.setSuplier(suplier);

        when(suplierService.findById(anyLong())).thenReturn(suplier);
        when(suplierService.updateSuplier(anyLong(), any(SuplierEntity.class))).thenReturn(suplier);
        doNothing().when(notaFiscalRepository).persist(any(NotaFiscalEntity.class));

        NotaFiscalEntity createdNotaFiscal = notaFiscalService.createNotaFiscal(notaFiscal);

        assertNotNull(createdNotaFiscal);
        assertEquals("123", createdNotaFiscal.getNumberNota());
        assertEquals(suplier, createdNotaFiscal.getSuplier());

        verify(suplierService, times(1)).findById(anyLong());
        verify(suplierService, times(1)).updateSuplier(anyLong(), any(SuplierEntity.class));
        verify(notaFiscalRepository, times(1)).persist(notaFiscal);
    }

    @Test
    void testFindById() {
        NotaFiscalEntity notaFiscal = new NotaFiscalEntity();
        notaFiscal.setId(1L);
        notaFiscal.setNumberNota("123");

        when(query.firstResultOptional()).thenReturn(Optional.of(notaFiscal));
        when(notaFiscalRepository.find("id", 1L)).thenReturn(query);

        NotaFiscalEntity foundNotaFiscal = notaFiscalService.findById(1L);

        assertNotNull(foundNotaFiscal);
        assertEquals(1L, foundNotaFiscal.getId());
        assertEquals("123", foundNotaFiscal.getNumberNota());

        verify(notaFiscalRepository, times(1)).find("id", 1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(query.firstResultOptional()).thenReturn(Optional.empty());
        when(notaFiscalRepository.find("id", 1L)).thenReturn(query);

        assertThrows(NotaFiscalNotFoundException.class, () -> {
            notaFiscalService.findById(1L);
        });

        verify(notaFiscalRepository, times(1)).find("id", 1L);
    }

    @Test
    void testUpdateNotaFiscal() {
        SuplierEntity suplier = new SuplierEntity();
        suplier.setId(1L);
        suplier.setCode("S001");

        NotaFiscalEntity existingNotaFiscal = new NotaFiscalEntity();
        existingNotaFiscal.setId(1L);
        existingNotaFiscal.setNumberNota("123");
        existingNotaFiscal.setSuplier(suplier);

        NotaFiscalEntity updatedNotaFiscal = new NotaFiscalEntity();
        updatedNotaFiscal.setNumberNota("456");
        updatedNotaFiscal.setSuplier(suplier);

        when(query.firstResultOptional()).thenReturn(Optional.of(existingNotaFiscal));
        when(notaFiscalRepository.find("id", 1L)).thenReturn(query);
        doNothing().when(notaFiscalRepository).persist(existingNotaFiscal);

        NotaFiscalEntity result = notaFiscalService.updateNotaFiscal(1L, updatedNotaFiscal);

        assertNotNull(result);
        assertEquals("456", result.getNumberNota());

        verify(notaFiscalRepository, times(1)).find("id", 1L);
        verify(notaFiscalRepository, times(1)).persist(existingNotaFiscal);
    }

    @Test
    void testUpdateNotaFiscalWithSuplierUpdate() {
        SuplierEntity existingSuplier = new SuplierEntity();
        existingSuplier.setId(1L);
        existingSuplier.setCode("S001");

        SuplierEntity updatedSuplier = new SuplierEntity();
        updatedSuplier.setId(2L); // Mudando o ID para garantir que é um suplier diferente
        updatedSuplier.setCode("S002");

        NotaFiscalEntity existingNotaFiscal = new NotaFiscalEntity();
        existingNotaFiscal.setId(1L);
        existingNotaFiscal.setNumberNota("123");
        existingNotaFiscal.setSuplier(existingSuplier);

        NotaFiscalEntity updatedNotaFiscal = new NotaFiscalEntity();
        updatedNotaFiscal.setNumberNota("456");
        updatedNotaFiscal.setSuplier(updatedSuplier);

        when(query.firstResultOptional()).thenReturn(Optional.of(existingNotaFiscal));
        when(notaFiscalRepository.find("id", 1L)).thenReturn(query);
        doNothing().when(notaFiscalRepository).persist(existingNotaFiscal);
        when(suplierService.findById(anyLong())).thenReturn(updatedSuplier);

        NotaFiscalEntity result = notaFiscalService.updateNotaFiscal(1L, updatedNotaFiscal);

        assertNotNull(result);
        assertEquals("456", result.getNumberNota());
        assertEquals("S002", result.getSuplier().getCode());

        verify(notaFiscalRepository, times(1)).find("id", 1L);
        verify(notaFiscalRepository, times(1)).persist(existingNotaFiscal);
        verify(suplierService, times(1)).findById(anyLong());
    }

    @Test
    void testDeleteNotaFiscal() {
        NotaFiscalEntity notaFiscal = new NotaFiscalEntity();
        notaFiscal.setId(1L);
        notaFiscal.setNumberNota("123");

        when(query.firstResultOptional()).thenReturn(Optional.of(notaFiscal));
        when(notaFiscalRepository.find("id", 1L)).thenReturn(query);
        doNothing().when(notaFiscalRepository).delete(notaFiscal);

        notaFiscalService.deleteNotaFiscal(1L);

        verify(notaFiscalRepository, times(1)).find("id", 1L);
        verify(notaFiscalRepository, times(1)).delete(notaFiscal);
    }

    @Test
    void testSearchNotas() {
        NotaFiscalEntity notaFiscal1 = new NotaFiscalEntity();
        notaFiscal1.setNumberNota("123");

        NotaFiscalEntity notaFiscal2 = new NotaFiscalEntity();
        notaFiscal2.setNumberNota("456");

        when(typedQuery.getResultList()).thenReturn(Arrays.asList(notaFiscal1, notaFiscal2));
        when(entityManager.createQuery(anyString(), eq(NotaFiscalEntity.class))).thenReturn(typedQuery);

        List<NotaFiscalEntity> notas = notaFiscalService.searchNotas("123");

        assertNotNull(notas);
        assertEquals(2, notas.size());
        assertEquals("123", notas.get(0).getNumberNota());
        assertEquals("456", notas.get(1).getNumberNota());

        verify(entityManager, times(1)).createQuery(anyString(), eq(NotaFiscalEntity.class));
        verify(typedQuery, times(1)).setParameter("numberNota", "%123%");
    }
}
package com.edilson.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tb_notas_fiscais")
@Schema(description = "Entidade que representa uma Nota Fiscal")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class NotaFiscalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da Nota Fiscal", example = "1")
    private Long id;

    @Column(name = "number_nota", nullable = false, length = 9)
    @Schema(description = "Número da Nota Fiscal", example = "123456")
    private String numberNota;

    @Column(name = "emission_date", nullable = false)
    @Schema(description = "Data de emissão da Nota Fiscal", example = "2025-02-27T10:15:30")
    private Date emissionDate;

    @ManyToOne
    @JoinColumn(name = "suplier_id", nullable = false)
    @Schema(description = "Fornecedor associado à Nota Fiscal")
    private SuplierEntity suplier;

    @Column(nullable = false)
    @Schema(description = "Endereço associado à Nota Fiscal", example = "Rua Exemplo, 123")
    private String address;

    @Column(name = "total_value", nullable = false, columnDefinition = "DECIMAL(10,2)")
    @Schema(description = "Valor total da Nota Fiscal", example = "1000.00")
    private float totalValue = 0.00f;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Data de criação da Nota Fiscal", example = "2025-02-27T10:15:30")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Schema(description = "Data de atualização da Nota Fiscal", example = "2025-02-27T10:15:30")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "notaFiscal", fetch = FetchType.EAGER)
    @Schema(description = "Lista de itens associados à Nota Fiscal")
    private List<ItemNfiscalEntity> items;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public float calculateTotalValue() {
        float totalValue = 0.00f;
        for (ItemNfiscalEntity item : items) {
            totalValue += item.getTotalItemValue();
        }
        return totalValue;
    }
    
}

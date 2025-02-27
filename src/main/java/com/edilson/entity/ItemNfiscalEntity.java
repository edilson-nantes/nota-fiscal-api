package com.edilson.entity;

import java.time.LocalDateTime;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tb_item_nota_fiscal")
@Schema(description = "Entidade que representa um Item de Nota Fiscal")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ItemNfiscalEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do Item de Nota Fiscal", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nota_fiscal_id", nullable = false)
    @Schema(description = "Nota Fiscal associada ao Item")
    private NotaFiscalEntity notaFiscal;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @Schema(description = "Produto associado ao Item de Nota Fiscal")
    private ProductEntity product;

    @Column(name = "unit_value", nullable = false, columnDefinition = "DECIMAL(10,2)")
    @Schema(description = "Valor unitário do Item de Nota Fiscal", example = "100.00")
    private float unitValue;

    @Column(nullable = false)
    @Schema(description = "Quantidade do Item de Nota Fiscal", example = "10")
    private int quantity;

    @Column(name = "total_item_value", nullable = false, columnDefinition = "DECIMAL(10,2)")
    @Schema(description = "Valor total do Item de Nota Fiscal", example = "1000.00")
    private float totalItemValue;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Data de criação do Item de Nota Fiscal", example = "2025-02-27T10:15:30")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Schema(description = "Data de atualização do Item de Nota Fiscal", example = "2025-02-27T10:15:30")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public float calculateTotalItemValue(int quantity, float unitValue) {
        return quantity * unitValue;
    }
}

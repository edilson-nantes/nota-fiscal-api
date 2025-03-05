package com.edilson.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.edilson.enums.SituationProduct;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tb_products")
@Schema(description = "Entidade que representa um Produto")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProductEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do Produto", example = "1")
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "Código do Produto", example = "123456")
    private String code;

    @Column(nullable = false, length = 150)
    @Schema(description = "Descrição do Produto", example = "Produto Exemplo")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Schema(description = "Situação do Produto", example = "Ativo")
    private SituationProduct situation = SituationProduct.Ativo;

    @Column(name = "has_movement", nullable = false)
    @Schema(description = "Indica se o Produto possui movimentação", example = "true")
    private boolean hasMovement = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Data de criação do Produto", example = "2025-02-27T10:15:30")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Schema(description = "Data de atualização do Produto", example = "2025-02-27T10:15:30")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @Schema(description = "Itens de Nota Fiscal associados ao Produto")
    private List<ItemNfiscalEntity> itemNfiscals;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

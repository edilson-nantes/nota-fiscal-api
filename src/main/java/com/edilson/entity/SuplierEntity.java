package com.edilson.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.edilson.enums.SituationSuplier;
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
@Table(name = "tb_supliers")
@Schema(description = "Entidade que representa um Fornecedor")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SuplierEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do Fornecedor", example = "1")
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "Código do Fornecedor", example = "123456")
    private String code;

    @Column(name = "legal_name", nullable = false, length = 150)
    @Schema(description = "Razão Social do Fornecedor", example = "Empresa Exemplo LTDA")
    private String legalName;

    @Column(nullable = false, length = 100)
    @Schema(description = "E-mail do Fornecedor", example = "empresa@example.com")
    private String email;

    @Column(nullable = false, length = 20)
    @Schema(description = "Telefone do Fornecedor", example = "123456789")
    private String phone;

    @Column(unique = true, nullable = false, length = 18)
    @Schema(description = "CNPJ do Fornecedor", example = "12.345.678/0001-90")
    private String cnpj;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Schema(description = "Situação do Fornecedor", example = "Ativo")
    private SituationSuplier situation = SituationSuplier.Ativo;

    @Column(name = "data_baixa", nullable = true)
    @Schema(description = "Data de baixa do Fornecedor", example = "2025-02-27")
    private LocalDate dataBaixa;

    @Column(name = "has_movement", nullable = false)
    @Schema(description = "Indica se o Fornecedor tem movimento", example = "false")
    private boolean hasMovement = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Data de criação do Fornecedor", example = "2025-02-27T10:15:30")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Schema(description = "Data de atualização do Fornecedor", example = "2025-02-27T10:15:30")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "suplier", fetch = FetchType.EAGER)
    @Schema(description = "Lista de notas fiscais associadas ao Fornecedor")
    private List<NotaFiscalEntity> notasFiscais;

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

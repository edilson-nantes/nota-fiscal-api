package com.edilson.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class NotaFiscalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_nota", nullable = false, length = 9)
    private String numberNota;

    @Column(name = "emission_date", nullable = false)
    private Date emissionDate;

    @ManyToOne
    @JoinColumn(name = "suplier_id", nullable = false)
    private SuplierEntity suplier;

    @Column(nullable = false)
    private String address;

    @Column(name = "total_value", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private float totalValue = 0.00f;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "notaFiscal", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

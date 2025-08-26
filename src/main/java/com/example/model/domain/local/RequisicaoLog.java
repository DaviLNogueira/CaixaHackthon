package com.example.model.domain.local;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "RequisicaoLog")
@PersistenceUnit(name = "local")
public class RequisicaoLog extends PanacheEntity {

    public String rota;
    public long duracao;
    public boolean sucesso;
    public LocalDateTime dataReferencia;
}

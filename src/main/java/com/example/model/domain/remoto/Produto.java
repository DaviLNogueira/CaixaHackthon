package com.example.model.domain.remoto;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "PRODUTO", schema = "dbo")
@Getter
@PersistenceUnit(name = "default")
public class Produto extends PanacheEntityBase {

    @Id
    @Column(name = "CO_PRODUTO")
    private int id;

    @Column(name = "NO_PRODUTO")
    private String nome;

    @Column(name = "PC_TAXA_JUROS")
    private double taxaJuros;

    @Column(name = "NU_MINIMO_MESES")
    private int minimoMeses;

    @Column(name = "NU_MAXIMO_MESES")
    private Integer maximoMeses;

    @Column(name = "VR_MINIMO")
    private double valorMinimo;

    @Column(name = "VR_MAXIMO")
    private Integer valorMaximo;
}


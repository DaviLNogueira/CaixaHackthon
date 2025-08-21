package com.example.model.domain.local;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;

@Entity @Getter
@Table(name = "SIMULACAO")
@PersistenceUnit(name = "local")
public class Simulacao extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SIMULACAO")
    private Long idSimulacao;

//    @ManyToOne
//    @JoinColumn(
//            name = "CODIGO_PRODUTO",
//            foreignKey = @ForeignKey(name = "FK_SIMULACAO_PRODUTO")
//    )
//    private Produto produto;

    @Column(name = "CODIGO_PRODUTO")
    private int codigoProduto;

    @Column(name = "TAXA_JUROS")
    private double taxaJuros; // valor pode mudar


    public Simulacao(int codigoProduto, double taxaJuros) {
        this.codigoProduto = codigoProduto;
        this.taxaJuros = taxaJuros;
    }

    public Simulacao() {

    }
}

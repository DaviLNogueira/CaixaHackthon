package com.example.model.domain.local;

import com.example.model.domain.remoto.Produto;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@Table(name = "SIMULACAO")
@PersistenceUnit(name = "local")
public class Simulacao extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SIMULACAO")
    private Long idSimulacao;

    @Column(name = "CODIGO_PRODUTO")
    private int codigoProduto;

    @Column(name = "TAXA_JUROS")
    private double taxaJuros; // valor pode mudar

    @OneToMany(mappedBy = "simulacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TipoEmprestimo> tipoEmprestimos = new ArrayList<>(); ;


    public void addTipoEmprestimo(TipoEmprestimo tipoEmprestimo) {
        tipoEmprestimo.setSimulacao(this);
        this.tipoEmprestimos.add(tipoEmprestimo);
    }

    public Simulacao(int codigoProduto, double taxaJuros) {
        this.codigoProduto = codigoProduto;
        this.taxaJuros = taxaJuros;
    }

    public Simulacao() {

    }
}

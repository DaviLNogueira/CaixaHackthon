package com.example.model.domain.local;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity
@Table(name = "PARCELA")
@PersistenceUnit(name = "local")
public class Parcela extends PanacheEntityBase {

    private int numero;
    private double valorAmortizado;
    private double valorJuros;
    private double valorPrestacao;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tipo_emprestimo_id")
    private TipoEmprestimo tipoEmprestimo;

    public Parcela(int numero, double valorPrestacao, double valorJuros, double valorAmortizado) {
        this.numero = numero;
        this.valorPrestacao = Math.round(valorPrestacao);
        this.valorJuros = Math.round(valorJuros);
        this.valorAmortizado = Math.round(valorAmortizado);
    }

    public Parcela() {

    }

}

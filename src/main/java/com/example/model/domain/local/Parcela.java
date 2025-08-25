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
    @JoinColumn(name = "ID_TIPO_EMPRESTIMO")
    private TipoEmprestimo tipoEmprestimo;

    private double arrendodarValor(double valor){
        return Double.parseDouble(String.format("%.2f",valor ).replace
                (",", "."));
    }

    public Parcela(int numero, double valorPrestacao, double valorJuros, double valorAmortizado) {
        this.numero = numero;
        this.valorPrestacao = arrendodarValor(valorPrestacao);
        this.valorJuros = arrendodarValor(valorJuros);
        this.valorAmortizado = arrendodarValor(valorAmortizado);
    }


    public Parcela() {

    }

}

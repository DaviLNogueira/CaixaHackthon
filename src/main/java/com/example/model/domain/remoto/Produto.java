package com.example.model.domain.remoto;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PRODUTO", schema = "dbo")
@Getter @Setter
@PersistenceUnit(name = "default")
public class Produto extends PanacheEntityBase {

    @Id
    @Column(name = "CO_PRODUTO")
    public Integer id;

    @Column(name = "NO_PRODUTO")
    public String nome;

    @Column(name = "PC_TAXA_JUROS")
    public Double taxaJuros;

    @Column(name = "NU_MINIMO_MESES")
    public Integer minimoMeses;

    @Column(name = "NU_MAXIMO_MESES")
    public Integer maximoMeses;

    @Column(name = "VR_MINIMO")
    public Double valorMinimo;

    @Column(name = "VR_MAXIMO")
    public Double valorMaximo;
}


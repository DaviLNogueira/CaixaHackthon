package com.example.model.domain.local;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "TIPO_EMPRESTIMO")
@PersistenceUnit(name = "local")
public class TipoEmprestimo extends PanacheEntityBase {

    private String tipo;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tipoEmprestimo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parcela> parcelas = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SIMULACAO")
    private Simulacao simulacao;

    public void addParcela(Parcela parcela) {
        parcela.setTipoEmprestimo(this);
        this.parcelas.add(parcela);
    }

    public TipoEmprestimo(String tipo) {
        this.tipo = tipo;
    }


    public TipoEmprestimo() {

    }
}

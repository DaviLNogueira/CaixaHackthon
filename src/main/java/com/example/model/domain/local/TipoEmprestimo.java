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


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "tipo_emprestimo_id")
    private List<Parcela> parcelas = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_SIMULACAO",
            foreignKey = @ForeignKey(name = "FK_TIPO_EMPRESTIMO_SIMULACAO")
    )
    private Simulacao simulacao;

    

    public TipoEmprestimo(String tipo, List<Parcela> parcelas) {
        this.tipo = tipo;
        this.parcelas = parcelas;
    }

    public TipoEmprestimo(String tipo,Simulacao simulacao) {
        this.tipo = tipo;
        this.simulacao = simulacao;
    }

    public TipoEmprestimo() {

    }
}

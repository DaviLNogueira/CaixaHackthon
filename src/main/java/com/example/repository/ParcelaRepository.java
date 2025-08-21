package com.example.repository;

import com.example.model.domain.local.Parcela;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceUnit;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
@PersistenceUnit(name = "local")
public class ParcelaRepository implements PanacheRepository<Parcela> {
}

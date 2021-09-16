package com.tp2.repository;

import com.tp2.models.Genero;
import org.springframework.data.repository.CrudRepository;

public interface GeneroRepository extends CrudRepository<Genero,String> {
        Genero findByNome(String nome);
        }

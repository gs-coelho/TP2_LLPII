package com.tp2.repository;

import com.tp2.models.Autor;
import org.springframework.data.repository.CrudRepository;

public interface AutorRepository extends CrudRepository<Autor, String> {
    Autor findByCodigo(long codigo);
    Autor findByNome(String nome);
}

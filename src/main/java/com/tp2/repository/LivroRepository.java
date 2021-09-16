package com.tp2.repository;

import com.tp2.models.Livro;
import org.springframework.data.repository.CrudRepository;

public interface LivroRepository extends CrudRepository<Livro,String> {
    Livro findByCodigo(long codigo);
}

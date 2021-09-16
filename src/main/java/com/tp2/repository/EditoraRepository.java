package com.tp2.repository;

import com.tp2.models.Editora;
import org.springframework.data.repository.CrudRepository;

public interface EditoraRepository extends CrudRepository<Editora, String> {
    Editora findByCodigo(long codigo);
    Editora findByNome(String nome);
}

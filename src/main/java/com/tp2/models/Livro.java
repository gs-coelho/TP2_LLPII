package com.tp2.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@Entity
public class Livro implements Serializable {

    @Id
    @NotEmpty
    private String isbn;

    @NotEmpty
    private String titulo;

    @NotNull
    private int numPaginas;

    @ManyToOne
    private Autor autor;

    @ManyToOne
    private Editora editora;

    @ManyToOne
    private Genero genero;

    /*public Livro(String isbn, String titulo, int numPaginas, Autor autor, Editora editora, Genero genero) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.numPaginas = numPaginas;
        this.autor = autor;
        this.editora = editora;
        this.genero = genero;
    }*/

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(int numPaginas) {
        this.numPaginas = numPaginas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autores) {
        this.autor = autores;
    }

    public Editora getEditora() {
        return editora;
    }

    public void setEditora(Editora editora) {
        this.editora = editora;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }
}

package com.tp2.controllers;

import com.tp2.models.Autor;
import com.tp2.models.Livro;
import com.tp2.repository.AutorRepository;
import com.tp2.repository.EditoraRepository;
import com.tp2.repository.GeneroRepository;
import com.tp2.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
public class LivroController {

    @Autowired
    private LivroRepository lr;

    @Autowired
    private AutorRepository ar;

    @Autowired
    private EditoraRepository er;

    @Autowired
    private GeneroRepository gr;

    @RequestMapping(value = "/livro", method = RequestMethod.GET)
    public String form(){
        return "livro/formLivro";
    }

    @RequestMapping(value = "/livro", method = RequestMethod.POST)
    public String form(String isbn, String titulo, int numPaginas, String autor, String editora, String genero){
        Livro livro = new Livro(isbn, titulo, numPaginas, ar.findByNome(autor), er.findByNome(editora), gr.findByNome(genero));
        lr.save(livro);

        return "redirect:/livro";
    }

    @RequestMapping("/livros")
    public ModelAndView listaLivros(){
        ModelAndView mv = new ModelAndView("livro/formLivro");
        Iterable<Livro> livros = lr.findAll();
        ArrayList<String> autores = new ArrayList();
        ArrayList<String> editoras = new ArrayList();
        ArrayList<String> generos = new ArrayList();


        for (Livro livro : livros){
            autores.add(livro.getAutor().getNome());
            editoras.add(livro.getEditora().getNome());
            generos.add(livro.getGenero().getNome());
        }

        mv.addObject("livros", livros);
        mv.addObject("autores", autores);
        mv.addObject("editoras", editoras);
        mv.addObject("generos", generos);

        return mv;
    }

    @RequestMapping(value = "/livro/{isbn}", method = RequestMethod.GET)
    public ModelAndView editarLivro(@PathVariable("isbn") String isbn){
        Livro livro = lr.findByIsbn(isbn);
        ModelAndView mv = new ModelAndView("livro/editarLivro");
        mv.addObject("livro", livro);

        return mv;
    }

    @RequestMapping(value = "/livro/{isbn}", method = RequestMethod.POST)
    public String editarLivroPost(@PathVariable("codigo") String isbn, String titulo, int numPaginas, String autor, String editora, String genero){
        Livro livro = new Livro(isbn, titulo, numPaginas, ar.findByNome(autor), er.findByNome(editora), gr.findByNome(genero));
        lr.save(livro);

        return "redirect:/livro/{isbn}";
    }

//    @RequestMapping(value = "/livro/{isbn}", method = RequestMethod.GET)
//    public ModelAndView editarLivro(@PathVariable("isbn") String isbn){
//        Livro livro = lr.findByIsbn(isbn);
//        ModelAndView mv = new ModelAndView("livro/editarLivro");
//        mv.addObject("livro", livro);
//
//        return mv;
//    }
}

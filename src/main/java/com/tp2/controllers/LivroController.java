package com.tp2.controllers;

import com.tp2.models.Livro;
import com.tp2.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LivroController {

    @Autowired
    private LivroRepository lr;

    @RequestMapping(value = "/cadastrarLivro", method = RequestMethod.GET)
    public String form(){
        return "livro/formLivro";
    }

    @RequestMapping(value = "/cadastrarLivro", method = RequestMethod.POST)
    public String form(Livro livro){

        lr.save(livro);

        return "redirect:/cadastrarLivro";
    }

    @RequestMapping("/cadastrarLivros")
    public ModelAndView listaLivros(){
        ModelAndView mv = new ModelAndView("livro/formLivro");
        Iterable<Livro> livros = lr.findAll();
        mv.addObject("livros", livros);

        return mv;
    }

    @RequestMapping(value = "/livro/{isbn}", method = RequestMethod.GET)
    public ModelAndView detalhesLivro(@PathVariable("isbn") String isbn){
        Livro livro = lr.findByIsbn(isbn);
        ModelAndView mv = new ModelAndView("livro/editarLivro");
        mv.addObject("livro", livro);

        return mv;
    }
}

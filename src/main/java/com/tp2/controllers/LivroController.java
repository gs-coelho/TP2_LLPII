package com.tp2.controllers;

import com.tp2.models.Livro;
import com.tp2.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
}

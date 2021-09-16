package com.tp2.controllers;

import com.tp2.models.Editora;
import com.tp2.models.Genero;
import com.tp2.models.Livro;
import com.tp2.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GeneroController {

    @Autowired
    private GeneroRepository gr;

    @RequestMapping(value = "/genero", method = RequestMethod.POST)
    public String cadastraGenero(Genero genero){
        gr.save(genero);
        return "redirect:/genero";
    }

    @RequestMapping(value = "/genero", method = RequestMethod.GET)
    public ModelAndView listaGeneros(){
        ModelAndView mv = new ModelAndView("genero/formGenero");
        Iterable<Genero> generos = gr.findAll();
        mv.addObject("generos", generos);

        return mv;
    }

    @RequestMapping(value = "/genero/{codigo}", method = RequestMethod.GET)
    public ModelAndView editarGenero(@PathVariable("codigo") long codigo){
        Genero genero = gr.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("genero/editarGenero");
        mv.addObject("genero", genero);
        return mv;
    }

    @RequestMapping(value = "/genero/{codigo}", method = RequestMethod.POST)
    public String editarGeneroPost(@PathVariable("codigo") long codigo, Genero genero){
        gr.save(genero);
        return "redirect:/genero/" + codigo;
    }
}

package com.tp2.controllers;

import com.tp2.models.Editora;
import com.tp2.models.Genero;
import com.tp2.models.Livro;
import com.tp2.repository.GeneroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class GeneroController {

    @Autowired
    private GeneroRepository gr;

    @RequestMapping(value = "/genero", method = RequestMethod.POST)
    public String cadastraGenero(@Valid Genero genero, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addAttribute("mensagem", "Verifique os campos!");

            return "redirect:/genero";
        }

        gr.save(genero);
        attributes.addFlashAttribute("mensagem", "Gênero adicionado com sucesso!");

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
    public String editarGeneroPost(@PathVariable("codigo") long codigo, @Valid Genero genero, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/genero/{codigo}";
        }

        gr.save(genero);
        attributes.addFlashAttribute("mensagem", "Gênero editado com sucesso!");

        return "redirect:/genero/" + codigo;
    }

    @RequestMapping(value = "/deletarGenero")
    public String deletarGenero(long codigo){
        Genero genero = gr.findByCodigo(codigo);
        gr.delete(genero);

        return "redirect:/genero";
    }
}

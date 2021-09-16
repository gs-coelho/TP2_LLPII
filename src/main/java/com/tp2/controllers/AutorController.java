package com.tp2.controllers;

import com.tp2.models.Autor;
import com.tp2.repository.AutorRepository;
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
public class AutorController {

    @Autowired
    private AutorRepository ar;

    @RequestMapping(value = "/autor", method = RequestMethod.GET)
    public ModelAndView listaAutores(){
        ModelAndView mv = new ModelAndView("autor/formAutor");
        Iterable<Autor> autores = ar.findAll();
        mv.addObject("autores", autores);
        return mv;
    }

    @RequestMapping(value = "/autor", method = RequestMethod.POST)
    public String cadastraAutor(@Valid Autor autor, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/autor";
        }
        ar.save(autor);
        attributes.addFlashAttribute("mensagem", "Autor adicionado com sucesso!");
        return "redirect:/autor";
    }

    @RequestMapping(value = "/autor/{codigo}", method = RequestMethod.GET)
    public ModelAndView editarAutor(@PathVariable("codigo") long codigo){
        Autor autor = ar.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("autor/editarAutor");
        mv.addObject("autor", autor);
        return mv;
    }

    @RequestMapping(value = "/autor/{codigo}", method = RequestMethod.POST)
    public String editarAutorPost(@PathVariable("codigo") long codigo, @Valid Autor autor, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/autor/{codigo}";
        }
        ar.save(autor);
        attributes.addFlashAttribute("mensagem", "Autor editado com sucesso!");
        return "redirect:/autor/{codigo}";
    }

    @RequestMapping("/autor/deletar")
    public String deletarAutor(long codigo){
        Autor autor = ar.findByCodigo(codigo);
        ar.delete(autor);
        return "redirect:/autor";
    }
}

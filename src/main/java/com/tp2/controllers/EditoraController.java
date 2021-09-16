package com.tp2.controllers;

import com.tp2.models.Editora;
import com.tp2.repository.EditoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EditoraController {

    @Autowired
    private EditoraRepository er;

    @RequestMapping(value = "/editora", method = RequestMethod.GET)
    public ModelAndView listaEditoras(){
        ModelAndView mv = new ModelAndView("editora/formEditora");
        Iterable<Editora> editoras = er.findAll();
        mv.addObject("editoras", editoras);
        return mv;
    }

    @RequestMapping(value = "/editora", method = RequestMethod.POST)
    public String cadastraEditora(Editora editora){
        er.save(editora);
        return "redirect:/editora";
    }

    @RequestMapping(value = "/editora/{codigo}", method = RequestMethod.GET)
    public ModelAndView editarEditora(@PathVariable("codigo") long codigo){
        Editora editora = er.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("editora/editarEditora");
        mv.addObject("editora", editora);
        return mv;
    }

    @RequestMapping(value = "/editora/{codigo}", method = RequestMethod.POST)
    public String editarEditoraPost(@PathVariable("codigo") long codigo, Editora editora){
        er.save(editora);
        return "redirect:/editora/" + codigo;
    }
}

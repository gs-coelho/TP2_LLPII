package com.tp2.controllers;

import com.tp2.models.Autor;
import com.tp2.models.Editora;
import com.tp2.models.Genero;
import com.tp2.models.Livro;
import com.tp2.repository.AutorRepository;
import com.tp2.repository.EditoraRepository;
import com.tp2.repository.GeneroRepository;
import com.tp2.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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

    @RequestMapping(value = "/livro", method = RequestMethod.POST)
    public String cadastraLivro(@Valid String isbn, @Valid String titulo, @Valid String numPaginas, @Valid String autor, @Valid String editora, @Valid String genero, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addAttribute("mensagem", "Verifique os campos!");

            return "redirect:/livro";
        }

        Livro livro = new Livro(isbn, titulo, Integer.parseInt(numPaginas), ar.findByNome(autor), er.findByNome(editora), gr.findByNome(genero));
        lr.save(livro);

        attributes.addFlashAttribute("mensagem", "Livro adicionado com sucesso!");

        return "redirect:/livro";
    }

    @RequestMapping(value = "/livro", method = RequestMethod.GET)
    public ModelAndView listaLivros(){
        ModelAndView mv = new ModelAndView("livro/formLivro");
        Iterable<Livro> livros = lr.findAll();
        Iterable<Genero> generosSelect = gr.findAll();
        Iterable<Autor> autoresSelect = ar.findAll();
        Iterable<Editora> editorasSelect = er.findAll();
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
        mv.addObject("autoresSelect", autoresSelect);
        mv.addObject("editorasSelect", editorasSelect);
        mv.addObject("generosSelect", generosSelect);

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
    public String editarLivroPost(@PathVariable("codigo") String isbn, @Valid String newIsbn, @Valid String titulo, @Valid int numPaginas, @Valid String autor, @Valid String editora, @Valid String genero, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            attributes.addAttribute("mensagem", "Verifique os campos");

            return "redirect:/livro/{isbn}";
        }
        Livro livro = new Livro(newIsbn, titulo, numPaginas, ar.findByNome(autor), er.findByNome(editora), gr.findByNome(genero));
        lr.save(livro);
        attributes.addFlashAttribute("mensagem", "Livro editado com sucesso!");

        return "redirect:/livro/{isbn}";
    }

    @RequestMapping(value = "/livro/deletar")
    public String deletarLivro(String isbn){
        Livro livro = lr.findByIsbn(isbn);
        lr.delete(livro);

        return "redirect:/livro";
    }


}

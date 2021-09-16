package com.tp2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AutorController {

    @RequestMapping("/cadastrarAutor")
    public String form(){
        return "autor/formAutor";
    }
}

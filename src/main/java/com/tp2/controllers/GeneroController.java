package com.tp2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GeneroController {

    @RequestMapping("/cadastrarGenero")
    public String form(){
        return "genero/formGenero";
    }
}

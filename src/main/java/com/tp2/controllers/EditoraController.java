package com.tp2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EditoraController {

    @RequestMapping("/cadastrarEditora")
    public String form() {
        return "editora/formEditora";
    }
}

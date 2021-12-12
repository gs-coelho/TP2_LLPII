package com.tp2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp2.models.Genero;
import com.tp2.repository.GeneroRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GeneroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GeneroRepository gr;

    @Test
    @Order(1)
    @DisplayName("Cadastro de um gênero")
    void cadastraGenero1() throws Exception{
        String nomeGenero = "Suspense";

        mockMvc.perform(
                post("/genero").param("nome", nomeGenero)
        );

        Genero genero = gr.findByNome(nomeGenero);
        assertNotNull(genero);

        if(gr.existsByCodigo(genero.getCodigo())){
            gr.delete(genero);
        }
    }

    @Test
    @Order(2)
    @DisplayName("Validação do cadastro de gênero - nome vazio")
    void cadastraGenero2() throws Exception{
        String nomeGenero = "";

        mockMvc.perform(
                post("/genero").param("nome", nomeGenero)
        );

        Genero genero = gr.findByNome(nomeGenero);
        assertNull(genero);
    }

    @Test
    @Order(3)
    @DisplayName("Listagem de gêneros")
    void listaGeneros() throws Exception{
        MvcResult retornoMVC = mockMvc.perform(get("/genero")).andReturn();

        // Converte a lista de gêneros obtida de Iterable para ArrayList
        Object atual_obj =  Objects.requireNonNull(retornoMVC.getModelAndView()).getModel().get("generos");
        Iterable<Genero> atual_iter = (Iterable<Genero>) atual_obj;
        ArrayList<Genero> atual = new ArrayList<>();
        atual_iter.forEach(atual::add);

        // Converte a lista de gêneros esperada de Iterable para ArrayList
        Iterable<Genero> esperado_iter = gr.findAll();
        ArrayList<Genero> esperado = new ArrayList<>();
        esperado_iter.forEach(esperado::add);

        assertEquals(esperado.size(), atual.size());
    }

    @Test
    @Order(4)
    @DisplayName("Busca de gênero específico")
    void editarGenero1Get() throws Exception{
        Genero esperado = new Genero();
        esperado.setNome("Gênero de teste");
        gr.save(esperado);

        MvcResult retornoMVC = mockMvc.
                perform(get("/genero/{codigo}", esperado.getCodigo()))
                .andReturn();

        Genero obtido = (Genero) Objects
                .requireNonNull(retornoMVC.getModelAndView())
                .getModel()
                .get("genero");

        assertEquals(esperado.getCodigo(), obtido.getCodigo());

        gr.delete(esperado);
    }

    @Test
    @Order(5)
    @DisplayName("Busca de gênero específico inexistente")
    void editarGeneroGet2(){
        assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(get("/genero/{codigo}", 0));
        });
    }

    @Test
    @Order(6)
    @DisplayName("Alteração de nome do gênero")
    void editarGeneroPost1() throws Exception{
        Genero esperado = new Genero();
        esperado.setNome("Nome inicial");
        gr.save(esperado);

        mockMvc.perform(
                post("/genero/{codigo}", esperado.getCodigo())
                        .param("nome", "Nome alterado")
        );
        Genero obtido = gr.findByCodigo(esperado.getCodigo());

        assertEquals("Nome alterado", obtido.getNome());

        gr.delete(obtido);
    }

    @Test
    @Order(7)
    @DisplayName("Alteração de nome para nome inválido")
    void editarGeneroPost2() throws Exception{
        Genero esperado = new Genero();
        esperado.setNome("Nome inicial");
        gr.save(esperado);

        mockMvc.perform(
                post("/genero/{codigo}", esperado.getCodigo())
                        .param("nome", "")
        );
        Genero obtido = gr.findByCodigo(esperado.getCodigo());

        assertEquals(esperado.getNome(), obtido.getNome());

        gr.delete(obtido);
    }

    @Test
    @Order(8)
    @DisplayName("Remoção de gênero")
    void deletarGenero1() throws Exception{
        Genero esperado = new Genero();
        esperado.setNome("Nome inicial");
        gr.save(esperado);

        mockMvc.perform(
                post("/genero/deletar")
                        .param("codigo", "" + esperado.getCodigo())
        );

        boolean obtido = gr.existsByCodigo(esperado.getCodigo());

        assertFalse(obtido);
    }

    @Test
    @Order(9)
    @DisplayName("Remoção de gênero inexistente")
    void deletarGenero2() throws Exception{
        assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(
                    post("/genero/deletar")
                            .param("codigo", "0")
            );
        });
    }
}
package com.tp2.controllers;

import com.tp2.models.Autor;
import com.tp2.repository.AutorRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AutorRepository ar;

    @Test
    @Order(1)
    @DisplayName("Cadastro de um autor")
    void cadastraAutor1() throws Exception{
        String nomeAutor = "Rick Riordan";

        mockMvc.perform(
                post("/autor").param("nome", nomeAutor)
        );

        Autor autor = ar.findByNome(nomeAutor);
        assertNotNull(autor);

        if(ar.existsByCodigo(autor.getCodigo())){
            ar.delete(autor);
        }
    }

    @Test
    @Order(2)
    @DisplayName("Validação do cadastro de autor - nome vazio")
    void cadastraAutor2() throws Exception{
        String nomeAutor = "";

        mockMvc.perform(
                post("/autor").param("nome", nomeAutor)
        );

        Autor autor = ar.findByNome(nomeAutor);
        assertNull(autor);
    }

    @Test
    @Order(3)
    @DisplayName("Listagem de autores")
    void listaAutores() throws Exception{
        MvcResult retornoMVC = mockMvc.perform(get("/autor")).andReturn();

        // Converte a lista de autores obtida de Iterable para ArrayList
        Object atual_obj =  Objects.requireNonNull(retornoMVC.getModelAndView()).getModel().get("autores");
        Iterable<Autor> atual_iter = (Iterable<Autor>) atual_obj;
        ArrayList<Autor> atual = new ArrayList<>();
        atual_iter.forEach(atual::add);

        // Converte a lista de autores esperada de Iterable para ArrayList
        Iterable<Autor> esperado_iter = ar.findAll();
        ArrayList<Autor> esperado = new ArrayList<>();
        esperado_iter.forEach(esperado::add);

        assertEquals(esperado.size(), atual.size());
    }

    @Test
    @Order(4)
    @DisplayName("Busca de autor específico")
    void editarAutor1Get() throws Exception{
        Autor esperado = new Autor();
        esperado.setNome("Autor de teste");
        ar.save(esperado);

        MvcResult retornoMVC = mockMvc.
                perform(get("/autor/{codigo}", esperado.getCodigo()))
                .andReturn();

        Autor obtido = (Autor) Objects
                .requireNonNull(retornoMVC.getModelAndView())
                .getModel()
                .get("autor");

        assertEquals(esperado.getCodigo(), obtido.getCodigo());

        ar.delete(esperado);
    }

    @Test
    @Order(5)
    @DisplayName("Busca de autor específico inexistente")
    void editarAutorGet2(){
        assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(get("/autor/{codigo}", 0));
        });
    }

    @Test
    @Order(6)
    @DisplayName("Alteração de nome do autor")
    void editarAutorPost1() throws Exception{
        Autor esperado = new Autor();
        esperado.setNome("Nome inicial");
        ar.save(esperado);

        mockMvc.perform(
                post("/autor/{codigo}", esperado.getCodigo())
                        .param("nome", "Nome alterado")
        );
        Autor obtido = ar.findByCodigo(esperado.getCodigo());

        assertEquals("Nome alterado", obtido.getNome());

        ar.delete(obtido);
    }

    @Test
    @Order(7)
    @DisplayName("Alteração de nome para nome inválido")
    void editarAutorPost2() throws Exception{
        Autor esperado = new Autor();
        esperado.setNome("Nome inicial");
        ar.save(esperado);

        mockMvc.perform(
                post("/autor/{codigo}", esperado.getCodigo())
                        .param("nome", "")
        );
        Autor obtido = ar.findByCodigo(esperado.getCodigo());

        assertEquals(esperado.getNome(), obtido.getNome());

        ar.delete(obtido);
    }

    @Test
    @Order(8)
    @DisplayName("Remoção de autor")
    void deletarAutor1() throws Exception{
        Autor esperado = new Autor();
        esperado.setNome("Nome inicial");
        ar.save(esperado);

        mockMvc.perform(
                post("/autor/deletar")
                        .param("codigo", "" + esperado.getCodigo())
        );

        boolean obtido = ar.existsByCodigo(esperado.getCodigo());

        assertFalse(obtido);
    }

    @Test
    @Order(9)
    @DisplayName("Remoção de autor inexistente")
    void deletarAutor2() throws Exception{
        assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(
                    post("/autor/deletar")
                            .param("codigo", "0")
            );
        });
    }
}
package com.tp2.controllers;

import com.tp2.models.Editora;
import com.tp2.repository.EditoraRepository;
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
class EditoraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EditoraRepository er;

    @Test
    @Order(1)
    @DisplayName("Cadastro de uma editora")
    void cadastraEditora1() throws Exception{
        String nomeEditora = "Companhia das Letras";

        mockMvc.perform(
                post("/editora").param("nome", nomeEditora)
        );

        Editora editora = er.findByNome(nomeEditora);
        assertNotNull(editora);

        if(er.existsByCodigo(editora.getCodigo())){
            er.delete(editora);
        }
    }

    @Test
    @Order(2)
    @DisplayName("Validação do cadastro de editora - nome vazio")
    void cadastraEditora2() throws Exception{
        String nomeEditora = "";

        mockMvc.perform(
                post("/editora").param("nome", nomeEditora)
        );

        Editora editora = er.findByNome(nomeEditora);
        assertNull(editora);
    }

    @Test
    @Order(3)
    @DisplayName("Listagem de editoras")
    void listaEditoras() throws Exception{
        MvcResult retornoMVC = mockMvc.perform(get("/editora")).andReturn();

        // Converte a lista de editoras obtida de Iterable para ArrayList
        Object atual_obj =  Objects.requireNonNull(retornoMVC.getModelAndView()).getModel().get("editoras");
        Iterable<Editora> atual_iter = (Iterable<Editora>) atual_obj;
        ArrayList<Editora> atual = new ArrayList<>();
        atual_iter.forEach(atual::add);

        // Converte a lista de editoras esperada de Iterable para ArrayList
        Iterable<Editora> esperado_iter = er.findAll();
        ArrayList<Editora> esperado = new ArrayList<>();
        esperado_iter.forEach(esperado::add);

        assertEquals(esperado.size(), atual.size());
    }

    @Test
    @Order(4)
    @DisplayName("Busca de editora específica")
    void editarEditoraGet1() throws Exception{
        Editora esperado = new Editora();
        esperado.setNome("Editora de teste");
        er.save(esperado);

        MvcResult retornoMVC = mockMvc.
                perform(get("/editora/{codigo}", esperado.getCodigo()))
                .andReturn();

        Editora obtido = (Editora) Objects
                .requireNonNull(retornoMVC.getModelAndView())
                .getModel()
                .get("editora");

        assertEquals(esperado.getCodigo(), obtido.getCodigo());

        er.delete(esperado);
    }

    @Test
    @Order(5)
    @DisplayName("Busca de editora específico inexistente")
    void editarEditoraGet2(){
        assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(get("/editora/{codigo}", 0));
        });
    }

    @Test
    @Order(6)
    @DisplayName("Alteração de nome da editora")
    void editarEditoraPost1() throws Exception{
        Editora esperado = new Editora();
        esperado.setNome("Nome inicial");
        er.save(esperado);

        mockMvc.perform(
                post("/editora/{codigo}", esperado.getCodigo())
                        .param("nome", "Nome alterado")
        );
        Editora obtido = er.findByCodigo(esperado.getCodigo());

        assertEquals("Nome alterado", obtido.getNome());

        er.delete(obtido);
    }

    @Test
    @Order(7)
    @DisplayName("Alteração de nome para nome inválido")
    void editarEditoraPost2() throws Exception{
        Editora esperado = new Editora();
        esperado.setNome("Nome inicial");
        er.save(esperado);

        mockMvc.perform(
                post("/editora/{codigo}", esperado.getCodigo())
                        .param("nome", "")
        );
        Editora obtido = er.findByCodigo(esperado.getCodigo());

        assertEquals(esperado.getNome(), obtido.getNome());

        er.delete(obtido);
    }

    @Test
    @Order(8)
    @DisplayName("Remoção de editora")
    void deletarEditora1() throws Exception{
        Editora esperado = new Editora();
        esperado.setNome("Nome inicial");
        er.save(esperado);

        mockMvc.perform(
                post("/editora/deletar")
                        .param("codigo", "" + esperado.getCodigo())
        );

        boolean obtido = er.existsByCodigo(esperado.getCodigo());

        assertFalse(obtido);
    }

    @Test
    @Order(9)
    @DisplayName("Remoção de editora inexistente")
    void deletarEditora2() throws Exception{
        assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(
                    post("/editora/deletar")
                            .param("codigo", "0")
            );
        });
    }
}
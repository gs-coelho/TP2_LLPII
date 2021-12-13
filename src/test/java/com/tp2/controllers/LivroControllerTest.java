package com.tp2.controllers;

import com.tp2.models.*;
import com.tp2.repository.*;
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
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LivroRepository lr;

    @Autowired
    private AutorRepository ar;

    @Autowired
    private EditoraRepository er;

    @Autowired
    private GeneroRepository gr;

    // Como será necessário criar esses mesmos objeto várias vezes, utilizaremos a lógica do
    // BeforeEach e AfterEach para evitar repetição de código.

    Autor autor;
    Editora editora;
    Genero genero;

    @BeforeEach
    void setUp() {
        // Aqui serão instanciados os objetos que serão compartilhados pelos testes.
        autor = new Autor();
        autor.setNome("J. K. Rowling");
        ar.save(autor);

        editora = new Editora();
        editora.setNome("Rocco");
        er.save(editora);

        genero = new Genero();
        genero.setNome("Fantasia");
        gr.save(genero);
    }

    @AfterEach
    void tearDown() {
        // Aqui serão destruidos os objetos que serão compartilhados pelos testes.
        ar.delete(autor);
        autor = null;

        er.delete(editora);
        editora = null;

        gr.delete(genero);
        genero = null;
    }

    @Test
    @Order(1)
    @DisplayName("Cadastro de um livro")
    void cadastraLivro1() throws Exception{
        String titulo = "Harry Potter e a Ordem da Fênix";
        String isbn = "9788532527882";
        String numPaginas = "703";

        mockMvc.perform(post("/livro")
                .param("titulo", titulo)
                .param("numPaginas", numPaginas)
                .param("isbn", isbn)
                .param("autor.codigo", "" + autor.getCodigo())
                .param("editora.codigo", "" + editora.getCodigo())
                .param("genero.codigo", "" + genero.getCodigo())
        );

        Livro livro = lr.findByIsbn(isbn);
        assertNotNull(livro);

        if(lr.existsByIsbn(livro.getIsbn())){
            lr.delete(livro);
        }
    }

    @Test
    @Order(2)
    @DisplayName("Validação do cadastro de livro - título vazio")
    void cadastraLivro2() throws Exception{
        String titulo = "";
        String isbn = "9788532527882";
        String numPaginas = "703";

        mockMvc.perform(post("/livro")
                .param("titulo", titulo)
                .param("numPaginas", numPaginas)
                .param("isbn", isbn)
                .param("autor.codigo", "" + autor.getCodigo())
                .param("editora.codigo", "" + editora.getCodigo())
                .param("genero.codigo", "" + genero.getCodigo())
        );

        Livro livro = lr.findByIsbn(isbn);
        assertNull(livro);
    }

    @Test
    @Order(3)
    @DisplayName("Validação do cadastro de livro - número de páginas vazio")
    void cadastraLivro3() throws Exception{
        String titulo = "Harry Potter e a Ordem da Fênix";
        String isbn = "9788532527882";
        String numPaginas = "";

        mockMvc.perform(post("/livro")
                .param("titulo", titulo)
                .param("numPaginas", numPaginas)
                .param("isbn", isbn)
                .param("autor.codigo", "" + autor.getCodigo())
                .param("editora.codigo", "" + editora.getCodigo())
                .param("genero.codigo", "" + genero.getCodigo())
        );

        Livro livro = lr.findByIsbn(isbn);
        assertNull(livro);
    }

    @Test
    @Order(4)
    @DisplayName("Validação do cadastro de livro - ISBN vazio")
    void cadastraLivro4() throws Exception{
        String titulo = "Harry Potter e a Ordem da Fênix";
        String isbn = "";
        String numPaginas = "703";

        mockMvc.perform(post("/livro")
                .param("titulo", titulo)
                .param("numPaginas", numPaginas)
                .param("isbn", isbn)
                .param("autor.codigo", "" + autor.getCodigo())
                .param("editora.codigo", "" + editora.getCodigo())
                .param("genero.codigo", "" + genero.getCodigo())
        );

        Livro livro = lr.findByIsbn(isbn);
        assertNull(livro);
    }

    @Test
    @Order(5)
    @DisplayName("Cadastro de um livro com autor inválido")
    void cadastraLivro5() throws Exception{
        String titulo = "Harry Potter e a Ordem da Fênix";
        String isbn = "9788532527882";
        String numPaginas = "703";

        assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(post("/livro")
                    .param("titulo", titulo)
                    .param("numPaginas", numPaginas)
                    .param("isbn", isbn)
                    .param("autor.codigo", "" + 0)
                    .param("editora.codigo", "" + editora.getCodigo())
                    .param("genero.codigo", "" + genero.getCodigo())
            );
        });

    }

    @Test
    @Order(6)
    @DisplayName("Listagem de livros")
    void listaLivros() throws Exception{
        MvcResult retornoMVC = mockMvc.perform(get("/livro")).andReturn();

        // Converte a lista de livros obtida de Iterable para ArrayList
        Object atual_obj =  Objects.requireNonNull(retornoMVC.getModelAndView()).getModel().get("livros");
        Iterable<Livro> atual_iter = (Iterable<Livro>) atual_obj;
        ArrayList<Livro> atual = new ArrayList<>();
        atual_iter.forEach(atual::add);

        // Converte a lista de livros esperada de Iterable para ArrayList
        Iterable<Livro> esperado_iter = lr.findAll();
        ArrayList<Livro> esperado = new ArrayList<>();
        esperado_iter.forEach(esperado::add);

        assertEquals(esperado.size(), atual.size());
    }

    @Test
    @Order(7)
    @DisplayName("Busca de livro específico")
    void editarLivroGet1() throws Exception{
        String titulo = "Harry Potter e a Ordem da Fênix";
        String isbn = "9788532527882";
        int numPaginas = 703;

        Livro esperado = new Livro();
        esperado.setTitulo(titulo);
        esperado.setIsbn(isbn);
        esperado.setNumPaginas(numPaginas);
        esperado.setAutor(autor);
        esperado.setEditora(editora);
        esperado.setGenero(genero);
        lr.save(esperado);

        MvcResult retornoMVC = mockMvc.
                perform(get("/livro/{Isbn}", esperado.getIsbn()))
                .andReturn();

        Livro obtido = (Livro) Objects
                .requireNonNull(retornoMVC.getModelAndView())
                .getModel()
                .get("livro");

        assertEquals(esperado.getIsbn(), obtido.getIsbn());

        lr.delete(esperado);
    }

    @Test
    @Order(8)
    @DisplayName("Alteração de título do livro")
    void editarLivroPost1() throws Exception{
        String titulo = "Harry Potter e a Ordem da Fênix";
        String isbn = "9788532527882";
        int numPaginas = 703;

        Livro esperado = new Livro();
        esperado.setTitulo(titulo);
        esperado.setIsbn(isbn);
        esperado.setNumPaginas(numPaginas);
        esperado.setAutor(autor);
        esperado.setEditora(editora);
        esperado.setGenero(genero);
        lr.save(esperado);

        String tituloAlterado = "Harry Potter e o Prisioneiro de Azkaban";

        mockMvc.perform(
                post("/livro/{isbn}", esperado.getIsbn())
                        .param("titulo", tituloAlterado)
        );

        Livro obtido = lr.findByIsbn(esperado.getIsbn());

        assertEquals(tituloAlterado, obtido.getTitulo());

        lr.delete(obtido);
    }

    @Test
    @Order(9)
    @DisplayName("Alteração de título para título inválido")
    void editarLivroPost2() throws Exception{
        String titulo = "Harry Potter e a Ordem da Fênix";
        String isbn = "9788532527882";
        int numPaginas = 703;

        Livro esperado = new Livro();
        esperado.setTitulo(titulo);
        esperado.setIsbn(isbn);
        esperado.setNumPaginas(numPaginas);
        esperado.setAutor(autor);
        esperado.setEditora(editora);
        esperado.setGenero(genero);
        lr.save(esperado);

        mockMvc.perform(
                post("/livro/{isbn}", esperado.getIsbn())
                        .param("titulo", "")
        );

        Livro obtido = lr.findByIsbn(esperado.getIsbn());

        assertEquals(esperado.getTitulo(), obtido.getTitulo());

        lr.delete(obtido);
    }

    @Test
    @Order(10)
    @DisplayName("Alteração do gênero do livro")
    void editarLivroPost3() throws Exception{
        String titulo = "Harry Potter e a Ordem da Fênix";
        String isbn = "9788532527882";
        int numPaginas = 703;

        Livro esperado = new Livro();
        esperado.setTitulo(titulo);
        esperado.setIsbn(isbn);
        esperado.setNumPaginas(numPaginas);
        esperado.setAutor(autor);
        esperado.setEditora(editora);
        esperado.setGenero(genero);
        lr.save(esperado);

        Genero genero2 = new Genero();
        genero2.setNome("Literatura infantojuvenil");

        mockMvc.perform(
                post("/livro/{isbn}", esperado.getIsbn())
                        .param("genero.codigo", "" + genero2.getCodigo())
        );

        Livro obtido = lr.findByIsbn(esperado.getIsbn());

        assertEquals(esperado.getGenero().getCodigo(), obtido.getGenero().getCodigo());

        lr.delete(obtido);
    }

    @Test
    @Order(11)
    @DisplayName("Alteração do gênero do livro para um gênero inválido")
    void editarLivroPost4() throws Exception{
        String titulo = "Harry Potter e a Ordem da Fênix";
        String isbn = "9788532527882";
        int numPaginas = 703;

        Livro esperado = new Livro();
        esperado.setTitulo(titulo);
        esperado.setIsbn(isbn);
        esperado.setNumPaginas(numPaginas);
        esperado.setAutor(autor);
        esperado.setEditora(editora);
        esperado.setGenero(genero);
        lr.save(esperado);

        mockMvc.perform(
                post("/livro/{isbn}", esperado.getIsbn())
                        .param("genero.codigo", "" + 0)
        );

        Livro obtido = lr.findByIsbn(esperado.getIsbn());

        assertEquals(esperado.getGenero().getCodigo(), obtido.getGenero().getCodigo());

        lr.delete(obtido);
    }


    @Test
    @Order(12)
    @DisplayName("Remoção de livro")
    void deletarLivro1() throws Exception{
        String titulo = "Harry Potter e a Ordem da Fênix";
        String isbn = "9788532527882";
        int numPaginas = 703;

        Livro esperado = new Livro();
        esperado.setTitulo(titulo);
        esperado.setIsbn(isbn);
        esperado.setNumPaginas(numPaginas);
        esperado.setAutor(autor);
        esperado.setEditora(editora);
        esperado.setGenero(genero);
        lr.save(esperado);

        mockMvc.perform(
                post("/livro/deletar")
                        .param("isbn", "" + esperado.getIsbn())
        );

        boolean obtido = lr.existsByIsbn(esperado.getIsbn());

        assertFalse(obtido);
    }

    @Test
    @Order(13)
    @DisplayName("Remoção de livro inexistente")
    void deletarLivro2() throws Exception{
        assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(
                    post("/livro/deletar")
                            .param("isbn", "0")
            );
        });
    }
}
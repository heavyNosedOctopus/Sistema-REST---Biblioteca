/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.lista.andre.pacote.controle;

import biblioteca.lista.andre.pacote.dao.AutorDAO;
import biblioteca.lista.andre.pacote.dao.EditoraDAO;
import biblioteca.lista.andre.pacote.dao.LivroDAO;
import biblioteca.lista.andre.pacote.erros.NaoEncontrado;
import biblioteca.lista.andre.pacote.erros.RequisicaoInvalida;
import biblioteca.lista.andre.pacote.modelo.Autor;
import biblioteca.lista.andre.pacote.modelo.Editora;
import biblioteca.lista.andre.pacote.modelo.Livro;
import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class Livros {

    @Autowired
    LivroDAO livroDAO;

    @Autowired
    AutorDAO autorDAO;

    @Autowired
    EditoraDAO editoraDAO;

    @RequestMapping(path = "/livros/pesquisar/titulo/{contem}", method = RequestMethod.GET)
    public Iterable<Livro> pesquisaPorTitulo(
            @PathVariable(required = false) String contem) {

        if (contem != null) {
            return livroDAO.findByTituloContaining(contem);
        } else {

            throw new RequisicaoInvalida("Indique alguma letra contida no título que procura.");
        }

    }

    @RequestMapping(path = "/livros/pesquisar/intervalo/{inicio}/{fim}", method = RequestMethod.GET)
    public Iterable<Livro> pesquisaPorIntervalo(
            @PathVariable(required = false) int inicio, @PathVariable(required = false) int fim) {

        if (inicio != 0 && fim != 0) {
            return livroDAO.findByAnoPublicacaoBetween(inicio, fim);
        } else {
            throw new RequisicaoInvalida("Insira um ano inicial e um ano final na Url");
        }

    }

    @RequestMapping(path = "/livros/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Livro inserir(@RequestBody Livro livro) {

        if (livro.getTitulo() == " " || livro.getTitulo() == null) {
            throw new RequisicaoInvalida("O título do livro deve estar preenchido.");
        } else if (livro.getAnoPublicacao() == 0) {
            throw new RequisicaoInvalida("O ano de publicação do livro deve estar preenchido.");
        }/* else if (livro.getAutores() == null) {
            throw new RequisicaoInvalida("Por favor, adicione algum dado sobre o autor.");
        } else if (livro.getEditoras() == null) {
            throw new RequisicaoInvalida("Por favor, adicione algum dado sobre a editora");
        }*/

        livroDAO.save(livro);

        return livro;
    }

    @RequestMapping(path = "/livros/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Livro> listar() {
        return livroDAO.findAll();
    }

    @RequestMapping(path = "/livros/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public Livro buscaId(@PathVariable int id
    ) {
        Optional<Livro> optLivro = livroDAO.findById(id);
        if (optLivro.isPresent()) {
            return optLivro.get();
        } else {
            throw new NaoEncontrado("Id não encontrada");
        }
    }

    @RequestMapping(path = "/livros/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.FOUND)
    public void apagar(@PathVariable int id
    ) {
        Optional<Livro> optLivro = livroDAO.findById(id);
        if (optLivro.isPresent()) {
            livroDAO.deleteById(id);
        } else {
            throw new NaoEncontrado("Id não encontrada");
        }
    }

    @RequestMapping(path = "/livros/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Livro livroAtual) {
        Livro livroAntigo = this.buscaId(id);
        if (livroAtual.getTitulo() != null) {
            livroAntigo.setTitulo(livroAtual.getTitulo());
        }
        if (livroAtual.getAnoPublicacao() != 0) {
            livroAntigo.setAnoPublicacao(livroAtual.getAnoPublicacao());
        }
        if (livroAtual.getAutores() != null) {
            livroAntigo.setAutores(livroAtual.getAutores());
        }
        if (livroAtual.getEditoras() != null) {
            livroAntigo.setEditoras(livroAtual.getEditoras());
        }
        livroAntigo.setDoacao(livroAtual.isDoacao());

        livroDAO.save(livroAntigo);
    }

    //Associa Autor
    @RequestMapping(path = "/livros/{id}/autores/",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insereAutor(@PathVariable int id, @RequestBody Autor autor) {

        Livro livro = this.buscaId(id);
        livro.getAutores().add(autor);
        livroDAO.save(livro);
    }

    @RequestMapping(path = "/livros/{id}/autores/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public List<Autor> listaAutor(@PathVariable int id) {
        Livro livro = this.buscaId(id);
        return livro.getAutores();

    }

    @RequestMapping(path = "/livros/{idLivro}/autores/{idAutor}")
    @ResponseStatus(HttpStatus.OK)
    public void ApagaAutor(@PathVariable int idLivro, @PathVariable int idAutor) {
        Livro livro = this.buscaId(idLivro);
        if (autorDAO.existsById(idAutor)) {
            livro.getAutores().remove(idAutor);
        }
    }

    //Associa Editora
    @RequestMapping(path = "/livros/{id}/editoras/",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void insereEditora(@PathVariable int id, @RequestBody Editora editora) {

        Livro livro = this.buscaId(id);
        livro.getEditoras().add(editora);
        livroDAO.save(livro);
    }

    @RequestMapping(path = "/livros/{id}/editoras/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public List<Editora> listaEditora(@PathVariable int id) {
        Livro livro = this.buscaId(id);
        return livro.getEditoras();

    }

    @RequestMapping(path = "/livros/{id}/editoras/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagaEditora(@PathVariable int id) {
        if (editoraDAO.existsById(id)) {
            livroDAO.deleteById(id);
        } else {
            throw new NaoEncontrado("Não encontrado");
        }

    }
    
    @RequestMapping(path = "/autores/{idAutor}/livros", method = RequestMethod.GET)
    public List<Livro> pesquisaLivrosAutores(@PathVariable int idAutor) {
        

        if (this.buscaId(idAutor) != null) {
            return livroDAO.pesquisaLivrosDeAutores(idAutor);
        } else {
            throw new RequisicaoInvalida("Insira um id de autor válido na url");
        }

    }
    
    
    @RequestMapping(path = "/editoras/{idEditora}/livros", method = RequestMethod.GET)
    public List<Livro> pesquisaLivrosEditoras(@PathVariable int idEditora) {
        

        if (this.buscaId(idEditora) != null) {
            return livroDAO.pesquisaLivrosDeEditoras(idEditora);
        } else {
            throw new RequisicaoInvalida("Insira um id de autor válido na url");
        }

    }
    @RequestMapping(path = "/livros/autores/nome", method = RequestMethod.GET)
    public List<Livro> pesquisaLivroPorNomeAutor(@RequestParam String primeiroNome) {
        
        return livroDAO.pesquisaLivroPorNomeAutor(primeiroNome);
    }
    
    @RequestMapping(path = "/livros/autores/sobrenome/{segundoNome}", method = RequestMethod.GET)
    public List<Livro> pesquisaLivroPorSobreNomeAutor(@PathVariable String segundoNome) {
        
        return livroDAO.pesquisaLivroPorSobreNomeAutor(segundoNome);
    }
    
    
    @RequestMapping(path = "/livros/emprestados", method = RequestMethod.GET)
    public List<Livro> pesquisaLivrosEmprestados() {

        return livroDAO.pesquisaLivrosEmprestados();

    }
    
    @RequestMapping(path = "/livros/disponiveis", method = RequestMethod.GET)
    public List<Livro> pesquisaLivrosDisponiveis() {
        
           return livroDAO.pesquisaLivrosDisponiveis();

    }
    

}

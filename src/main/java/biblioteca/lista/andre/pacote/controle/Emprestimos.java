/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.lista.andre.pacote.controle;

import biblioteca.lista.andre.pacote.dao.BibliotecarioDAO;
import biblioteca.lista.andre.pacote.dao.EmprestimoDAO;
import biblioteca.lista.andre.pacote.dao.UsuarioDAO;
import biblioteca.lista.andre.pacote.erros.NaoEncontrado;
import biblioteca.lista.andre.pacote.erros.RequisicaoInvalida;

import biblioteca.lista.andre.pacote.modelo.Emprestimo;
import biblioteca.lista.andre.pacote.modelo.Usuario;

import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.ZoneId;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
public class Emprestimos {

    @Autowired
    EmprestimoDAO emprestimoDAO;

    @Autowired
    UsuarioDAO usuarioDAO;

    @Autowired
    BibliotecarioDAO bibliotecarioDAO;

    

    @RequestMapping(path = "/emprestimos/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Emprestimo inserir(@RequestBody Emprestimo emprestimo) {
        emprestimo.setId(0);
        LocalDate inicio = LocalDate.now();
        LocalDate fim = inicio.plusDays(7);
        Date in = Date.from(inicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date f = Date.from(fim.atStartOfDay(ZoneId.systemDefault()).toInstant());
        emprestimo.setRetirada(in);
        emprestimo.setPrevisaoDevolucao(f);
        
        int idDoLivro = emprestimo.getLivro().getId();
        if (emprestimo.getBibliotecario() == null) {
            throw new RequisicaoInvalida("Por favor, insira um Bibliotecário.");
        } else if (emprestimo.getUsuario() == null) {
            throw new RequisicaoInvalida("Por favor, insira um Usuário.");
        } else if (emprestimo.getLivro() == null) {
            throw new RequisicaoInvalida("Por favor, insira um Livro.");
        } else if (emprestimo.getDevolucao() != null && emprestimo.getDevolucao().compareTo(in) > 0) {
            throw new RequisicaoInvalida("A devolução apenas pode ter ocorrido hoje ou anteriormente.");
        } else if (emprestimoDAO.pesquisaSELivroEmprestado(idDoLivro) != null){
            throw new RequisicaoInvalida("Este livro ainda está emprestado, pedimos que cadastre outro.");
        }

        emprestimoDAO.save(emprestimo);

        return emprestimo;
    }

    @RequestMapping(path = "/emprestimos/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Emprestimo> listar() {
        return emprestimoDAO.findAll();
    }

    @RequestMapping(path = "/emprestimos/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public Emprestimo buscaId(@PathVariable int id
    ) {
        Optional<Emprestimo> optEmprestimo = emprestimoDAO.findById(id);
        if (optEmprestimo.isPresent()) {
            return optEmprestimo.get();
        } else {
            throw new NaoEncontrado("Id não encontrada");
        }
    }

    @RequestMapping(path = "/emprestimos/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.FOUND)
    public void apagar(@PathVariable int id
    ) {
        Optional<Emprestimo> optEmprestimo = emprestimoDAO.findById(id);
        if (optEmprestimo.isPresent()) {
            emprestimoDAO.deleteById(id);
        } else {
            throw new NaoEncontrado("Id não encontrada");
        }
    }

    @RequestMapping(path = "/emprestimos/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Emprestimo emprestimoAtual) {

        Emprestimo emprestimoAntigo = this.buscaId(id);
        LocalDate inicio = LocalDate.now();
        Date in = Date.from(inicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
        if (emprestimoAtual.getDevolucao().compareTo(in) > 0) {
            throw new RequisicaoInvalida("A devolução apenas pode ter ocorrido hoje ou anteriormente.");
        }

        if (emprestimoAtual.getRetirada() != null) {
            emprestimoAntigo.setRetirada(emprestimoAtual.getRetirada());
        }
        if (emprestimoAtual.getPrevisaoDevolucao() != null) {
            emprestimoAntigo.setPrevisaoDevolucao(emprestimoAtual.getPrevisaoDevolucao());
        }

        emprestimoAntigo.setDevolucao(emprestimoAtual.getDevolucao());

        if (emprestimoAtual.getBibliotecario() != null) {
            emprestimoAntigo.setBibliotecario(emprestimoAtual.getBibliotecario());
        }
        if (emprestimoAtual.getLivro() != null) {
            emprestimoAntigo.setLivro(emprestimoAtual.getLivro());
        }
        if (emprestimoAtual.getUsuario() != null) {
            emprestimoAntigo.setUsuario(emprestimoAtual.getUsuario());
        }

        emprestimoDAO.save(emprestimoAntigo);
    }

    //Pesquisas
    @RequestMapping(path = "/emprestimos/pesquisar/naodevolvidos", method = RequestMethod.GET)
    public List<Emprestimo> pesquisaEmprestimosNulos() {

        return emprestimoDAO.pesquisaEmprestimosNulos();

    }
    @RequestMapping(path = "/emprestimos/pesquisar/atrasados", method = RequestMethod.GET)
    public List<Emprestimo> pesquisaEmprestimosAtrasados() {

        return emprestimoDAO.pesquisaEmprestimosAtrasados();

    }
    
    @RequestMapping(path = "/emprestimos/pesquisar/usuarios/{id}", method = RequestMethod.GET)
    public List<Emprestimo> pesquisaEmprestimoPorUsuario(@PathVariable int id) {

        return emprestimoDAO.pesquisaEmprestimosPorUsuario(id);

    }
    
    
    
    @RequestMapping(path = "/emprestimos/pesquisa/intervalo", method = RequestMethod.GET)
    public Iterable<Emprestimo> pesquisaPorIntervalo(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date inicio, @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date fim) {

        if (inicio != null && fim != null) {
            return emprestimoDAO.findByRetiradaBetween(inicio, fim);
        } else {
            throw new RequisicaoInvalida("Insira uma data inicial e uma data final na Url");
        }

    }

}

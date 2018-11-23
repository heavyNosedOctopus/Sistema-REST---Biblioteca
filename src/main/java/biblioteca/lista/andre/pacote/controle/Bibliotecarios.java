/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.lista.andre.pacote.controle;

import biblioteca.lista.andre.pacote.dao.BibliotecarioDAO;
import biblioteca.lista.andre.pacote.erros.NaoEncontrado;
import biblioteca.lista.andre.pacote.erros.RequisicaoInvalida;
import biblioteca.lista.andre.pacote.modelo.Bibliotecario;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class Bibliotecarios {

    @Autowired
    BibliotecarioDAO bibliotecarioDAO;

    @RequestMapping(path = "/bibliotecarios/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Bibliotecario inserir(@RequestBody Bibliotecario bibliotecario) {

        

        if (bibliotecario.getEmail() == " " || bibliotecario.getEmail() == null) {
            throw new RequisicaoInvalida("O email do bibliotecario deve estar preenchido.");
        } else if (bibliotecario.getNome() == " " || bibliotecario.getNome() == null) {
            throw new RequisicaoInvalida("O nome do bibliotecario deve estar preenchido.");
        } else if (bibliotecario.getSenha() == " " || bibliotecario.getSenha() == null) {
            throw new RequisicaoInvalida("A senha não pode estar vazia.");
        } else if (bibliotecario.getSenha().length() < 8){
            throw new RequisicaoInvalida("A senha não pode ser menor que 8 caracteres.");
        }
        if (this.pesquisaEmail(bibliotecario.getEmail()) == true) {
            throw new RequisicaoInvalida("Em nosso sistema, este email não está disponível");
        } else if (this.pesquisaEmail(bibliotecario.getEmail()) == false) {
            bibliotecarioDAO.save(bibliotecario);
        }

        return bibliotecario;
    }

    @RequestMapping(path = "/bibliotecarios/{email}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean pesquisaEmail(@PathVariable String email) {
        Optional<Bibliotecario> optBibliotecario = bibliotecarioDAO.findByEmail(email);
        if (optBibliotecario.isPresent()) {
            return true;
        } else {
            return false;
        }

    }

    @RequestMapping(path = "/bibliotecarios/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Bibliotecario> listar() {
        return bibliotecarioDAO.findAll();
    }

    @RequestMapping(path = "/bibliotecarios/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public Bibliotecario buscaId(@PathVariable int id) {
        Optional<Bibliotecario> optBibliotecario = bibliotecarioDAO.findById(id);
        if (optBibliotecario.isPresent()) {
            return optBibliotecario.get();
        } else {
            throw new NaoEncontrado("Id não encontrada");
        }
    }

    @RequestMapping(path = "/bibliotecarios/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.FOUND)
    public void apagar(@PathVariable int id) {
        Optional<Bibliotecario> optBibliotecario = bibliotecarioDAO.findById(id);
        if (optBibliotecario.isPresent()) {
            bibliotecarioDAO.deleteById(id);
        } else {
            throw new NaoEncontrado("Id não encontrada");
        }
    }

    @RequestMapping(path = "/bibliotecarios/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Bibliotecario bibliotecarioAtual) {
        Bibliotecario bibliotecarioAntigo = this.buscaId(id);
        bibliotecarioAntigo.setId(bibliotecarioAtual.getId());
        bibliotecarioAntigo.setEmail(bibliotecarioAtual.getEmail());
        bibliotecarioAntigo.setNome(bibliotecarioAtual.getNome());
        bibliotecarioAntigo.setSenha(bibliotecarioAtual.getSenha());
        
        bibliotecarioDAO.save(bibliotecarioAntigo);
    }

}

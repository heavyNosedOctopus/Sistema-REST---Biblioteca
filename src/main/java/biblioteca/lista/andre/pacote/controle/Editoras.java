/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.lista.andre.pacote.controle;

import biblioteca.lista.andre.pacote.dao.EditoraDAO;
import biblioteca.lista.andre.pacote.erros.NaoEncontrado;
import biblioteca.lista.andre.pacote.erros.RequisicaoInvalida;
import biblioteca.lista.andre.pacote.modelo.Editora;
import java.util.List;
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
public class Editoras {

    @Autowired
    EditoraDAO editoraDAO;

    @RequestMapping(path = "/editoras/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Editora inserir(@RequestBody Editora editora) {

        if (editora.getCnpj()== " " || editora.getCnpj()== null) {
            throw new RequisicaoInvalida("O cnpj da editora deve estar preenchido.");
        } else if (editora.getNome() == " " || editora.getNome() == null) {
            throw new RequisicaoInvalida("O nome da editora deve estar preenchido.");
        } 
        if (this.pesquisaCnpj(editora.getCnpj()) == true) {
            throw new RequisicaoInvalida("Em nosso sistema, este cnpj já está sendo utilizado.");
        } else if (this.pesquisaCnpj(editora.getCnpj()) == false) {
            editoraDAO.save(editora);
        }

        return editora;
    }

    @RequestMapping(path = "/editoras/{cnpj}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean pesquisaCnpj(@PathVariable String cnpj) {
        Optional<Editora> optEditora = editoraDAO.findByCnpj(cnpj);
        if (optEditora.isPresent()) {
            return true;
        } else {
            return false;
        }

    }

    @RequestMapping(path = "/editoras/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Editora> listar() {
        return editoraDAO.findAll();
    }

    @RequestMapping(path = "/editoras/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public Editora buscaId(@PathVariable int id) {
        Optional<Editora> optEditora = editoraDAO.findById(id);
        if (optEditora.isPresent()) {
            return optEditora.get();
        } else {
            throw new NaoEncontrado("Id não encontrada");
        }
    }

    @RequestMapping(path = "/editoras/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.FOUND)
    public void apagar(@PathVariable int id) {
        Optional<Editora> optEditora = editoraDAO.findById(id);
        if (optEditora.isPresent()) {
            editoraDAO.deleteById(id);
        } else {
            throw new NaoEncontrado("Id não encontrada");
        }
    }

    @RequestMapping(path = "/editoras/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Editora editoraAtual) {
       
        Editora editoraAntigo = this.buscaId(id);
        
        editoraAntigo.setCnpj(editoraAtual.getCnpj());
        editoraAntigo.setNome(editoraAtual.getNome());
        
        editoraDAO.save(editoraAntigo);
    }
    
    //Pesquisas
    
    

}
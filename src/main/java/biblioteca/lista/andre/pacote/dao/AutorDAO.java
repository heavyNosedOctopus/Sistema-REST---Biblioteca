/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.lista.andre.pacote.dao;

import biblioteca.lista.andre.pacote.modelo.Autor;
import biblioteca.lista.andre.pacote.modelo.Livro;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author arfsm
 */
@Repository
public interface AutorDAO extends CrudRepository<Autor, Integer> {
    
    Optional<Autor> findByPrimeiroNome(String primeiroNome);
    
    Optional<Autor> findBySegundoNome(String segundoNome);
    
    Optional<Autor> findById(int id);
    
    
}

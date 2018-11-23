/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.lista.andre.pacote.dao;

import biblioteca.lista.andre.pacote.modelo.Bibliotecario;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author arfsm
 */
@Repository
public interface BibliotecarioDAO extends CrudRepository<Bibliotecario, Object> {
    
    Optional<Bibliotecario> findByEmail(String email);
    
}

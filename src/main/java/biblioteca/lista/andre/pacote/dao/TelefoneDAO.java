/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.lista.andre.pacote.dao;

import biblioteca.lista.andre.pacote.modelo.Telefone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author arfsm
 */
@Repository
public interface TelefoneDAO extends CrudRepository<Telefone, Object> {
    
}

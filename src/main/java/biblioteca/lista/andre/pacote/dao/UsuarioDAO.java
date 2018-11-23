/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.lista.andre.pacote.dao;

import biblioteca.lista.andre.pacote.modelo.Usuario;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author arfsm
 */
@Repository
public interface UsuarioDAO extends CrudRepository<Usuario, Object> {
    
    Optional<Usuario> findByCpf(String cpf);
    
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByNome(String nome);

    

    public Iterable<Usuario> findByNomeContaining(String contem);

    

    public Iterable<Usuario> findByCpfContaining(String contem);
    

    public Iterable<Usuario> findByEmailContaining(String contem);
    
}

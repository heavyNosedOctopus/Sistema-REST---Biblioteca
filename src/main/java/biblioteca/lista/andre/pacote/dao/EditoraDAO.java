/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.lista.andre.pacote.dao;

import biblioteca.lista.andre.pacote.modelo.Editora;
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
public interface EditoraDAO extends CrudRepository<Editora, Object> {
    
    Optional<Editora> findByCnpj(String cnpj);
    
    @Query(nativeQuery = true, value= "SELECT * FROM livro_autores WHERE editoras_id =:identificacao")
    List<Editora> pesquisaLivrosDeEditoras(@Param("identificacao") int editoras_id);
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.lista.andre.pacote.dao;

import biblioteca.lista.andre.pacote.modelo.Emprestimo;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author arfsm
 */
@Repository
public interface EmprestimoDAO extends CrudRepository<Emprestimo, Object> {

    public Iterable<Emprestimo> findByRetiradaBetween(Date inicio, Date fim);
    
    @Query(value= "SELECT * FROM emprestimo WHERE devolucao IS NULL", nativeQuery = true)
    List<Emprestimo> pesquisaEmprestimosNulos();
    
    @Query(value= "SELECT * FROM emprestimo WHERE previsao_devolucao<NOW() and devolucao is null", nativeQuery = true)
    List<Emprestimo> pesquisaEmprestimosAtrasados();
    
    
    
    @Query(value= "select id from emprestimo where emprestimo.devolucao is null and exists(select * from livro where emprestimo.livro_id = :identificacao)", nativeQuery = true)
    String pesquisaSELivroEmprestado(@Param("identificacao") int identificacao);
    
    
    @Query(value= "select * from emprestimo where emprestimo.usuario_id = :id", nativeQuery = true)
    List<Emprestimo> pesquisaEmprestimosPorUsuario(@Param("id") int id);
    
}

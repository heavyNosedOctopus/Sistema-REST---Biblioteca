/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.lista.andre.pacote.dao;


import biblioteca.lista.andre.pacote.modelo.Autor;
import biblioteca.lista.andre.pacote.modelo.Livro;
import java.util.Collection;
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
public interface LivroDAO extends CrudRepository<Livro, Object> {
    
    Optional<Autor> findByIdIn(Collection<Livro> livros);

    public Iterable<Livro> findByTituloContaining(String contem);

    public Iterable<Livro> findByAnoPublicacaoBetween(int inicio, int fim);
    
    @Query(value= "select distinct * from livro join livro_autores on livro.id = livro_autores.livro_id join autor on livro_autores.autores_id = autor.id where autor.id = :idAutor", nativeQuery = true)
    List<Livro> pesquisaLivrosDeAutores(@Param("idAutor") int idAutor);
    
    @Query(value= "select distinct * from livro join livro_editoras on livro.id = livro_editoras.livro_id join editora on livro_editoras.editoras_id = editora.id where editora.id = :idEditora", nativeQuery = true)
    List<Livro> pesquisaLivrosDeEditoras(@Param("idEditora") int idEditora);
    
    
    @Query(value= "select * from livro where not exists (select * from emprestimo where emprestimo.livro_id = livro.id) union select * from livro where exists (select * from emprestimo where emprestimo.livro_id = livro.id and emprestimo.devolucao is not null)", nativeQuery = true)
    List<Livro> pesquisaLivrosDisponiveis();
    
    
    @Query(value= "select * from livro where  exists (select * from emprestimo where emprestimo.livro_id = livro.id and emprestimo.devolucao is null)", nativeQuery = true)
    List<Livro> pesquisaLivrosEmprestados();
    
    @Query(value= "select * from livro join livro_autores on livro_autores.livro_id = livro.id join autor on livro_autores.autores_id = autor.id where autor.primeiro_nome like '%:primeiroNome%'", nativeQuery = true)
    List<Livro> pesquisaLivroPorNomeAutor(@Param("primeiroNome") String primeiroNome);
    
    @Query(value= "select * from livro join livro_autores on livro_autores.livro_id = livro.id join autor on livro_autores.autores_id = autor.id where autor.segundo_nome like '%:segundoNome%'", nativeQuery = true)
    List<Livro> pesquisaLivroPorSobreNomeAutor(@Param("segundoNome") String segundoNome);
}

package biblioteca.lista.andre.pacote.controle;

import biblioteca.lista.andre.pacote.dao.AutorDAO;
import biblioteca.lista.andre.pacote.dao.LivroDAO;
import biblioteca.lista.andre.pacote.erros.NaoEncontrado;
import biblioteca.lista.andre.pacote.erros.RequisicaoInvalida;
import biblioteca.lista.andre.pacote.modelo.Autor;
import biblioteca.lista.andre.pacote.modelo.Livro;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
public class Autores {

    @Autowired
    AutorDAO autorDAO;
    
    @Autowired
    LivroDAO livroDAO;

    @RequestMapping(path = "/autores/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Autor inserir(@RequestBody Autor autor) {

        if (autor.getPrimeiroNome() == " " || autor.getPrimeiroNome() == null) {
            throw new RequisicaoInvalida("O primeiro nome do autor deve estar preenchido.");
        } else if (autor.getSegundoNome() == " " || autor.getSegundoNome() == null) {
            throw new RequisicaoInvalida("O segundo nome do autor deve estar preenchido.");
        }
        if (this.pesquisaPrimeiroNome(autor.getPrimeiroNome()) == true && this.pesquisaSegundoNome(autor.getSegundoNome()) == true) {
            throw new RequisicaoInvalida("Já existe um autor com este primeiro e segundo nome em nosso sistema.");
        } else if (this.pesquisaPrimeiroNome(autor.getPrimeiroNome()) == false || this.pesquisaSegundoNome(autor.getSegundoNome()) == false) {
            autorDAO.save(autor);
        }

        return autor;
    }

    @RequestMapping(path = "/autores/{primeiroNome}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean pesquisaPrimeiroNome(@PathVariable String primeiroNome
    ) {
        Optional<Autor> optAutor = autorDAO.findByPrimeiroNome(primeiroNome);
        if (optAutor.isPresent()) {
            return true;
        } else {
            return false;
        }

    }

    @RequestMapping(path = "/autores/{segundoNome}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean pesquisaSegundoNome(@PathVariable String segundoNome
    ) {
        Optional<Autor> optAutor = autorDAO.findBySegundoNome(segundoNome);
        if (optAutor.isPresent()) {
            return true;
        } else {
            return false;
        }

    }

    @RequestMapping(path = "/autores/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Autor> listar() {
        return autorDAO.findAll();
    }

    @RequestMapping(path = "/autores/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public Autor buscaId(@PathVariable int id
    ) {
        Optional<Autor> optAutor = autorDAO.findById(id);
        if (optAutor.isPresent()) {
            return optAutor.get();
        } else {
            throw new NaoEncontrado("Id não encontrada");
        }
    }

    @RequestMapping(path = "/autores/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.FOUND)
    public void apagar(@PathVariable int id
    ) {
        Optional<Autor> optAutor = autorDAO.findById(id);
        if (optAutor.isPresent()) {
            autorDAO.deleteById(id);
        } else {
            throw new NaoEncontrado("Id não encontrada");
        }
    }

    @RequestMapping(path = "/autores/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Autor autorAtual) {
        Autor autorAntigo = this.buscaId(id);

        autorAntigo.setPrimeiroNome(autorAtual.getPrimeiroNome());
        autorAntigo.setSegundoNome(autorAtual.getSegundoNome());

        autorDAO.save(autorAntigo);
    }
    
    //Pesquisas
    
    /*@RequestMapping(path = "/autores/{id}/livros",method = RequestMethod.GET)
    public Optional <Autor> listaLivros(@PathVariable int id){
        
        if (autorDAO.findById(id).isPresent()){
            return livroDAO.findByIdIn(id);
        } else{
            throw new NaoEncontrado("Autor não encontrado");
        }
        
    }*/
    
    

}

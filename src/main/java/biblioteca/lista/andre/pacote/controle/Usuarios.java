/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.lista.andre.pacote.controle;

import biblioteca.lista.andre.pacote.dao.TelefoneDAO;
import biblioteca.lista.andre.pacote.dao.UsuarioDAO;
import biblioteca.lista.andre.pacote.erros.NaoEncontrado;
import biblioteca.lista.andre.pacote.erros.RequisicaoInvalida;
import biblioteca.lista.andre.pacote.modelo.Telefone;
import biblioteca.lista.andre.pacote.modelo.Usuario;
import java.util.Optional;
import java.util.List;
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
public class Usuarios {

    @Autowired
    UsuarioDAO usuarioDAO;

    @Autowired
    TelefoneDAO telefoneDAO;

    @RequestMapping(path = "/usuarios/pesquisar/nome/{contem}", method = RequestMethod.GET)
    public Iterable<Usuario> pesquisaPorNome(
            @PathVariable(required = false) String contem) {

        if (contem != null) {
            return usuarioDAO.findByNomeContaining(contem);
        } else {

            throw new RequisicaoInvalida("Indique alguma letra contida no nome que procura.");
        }

    }

    @RequestMapping(path = "/usuarios/pesquisar/cpf/{contem}", method = RequestMethod.GET)
    public Iterable<Usuario> pesquisaPorCpf(
            @PathVariable int contem) {
        String contido = Integer.toString(contem);
        if (contem != 0) {
            return usuarioDAO.findByCpfContaining(contido);
        } else {

            throw new RequisicaoInvalida("Indique algum numero contido no cpf que procura.");
        }

    }

    @RequestMapping(path = "/usuarios/pesquisar/email/{contem}", method = RequestMethod.GET)
    public Iterable<Usuario> pesquisaPorEmail(
            @PathVariable(required = false) String contem) {
        if (contem != null) {
            return usuarioDAO.findByEmailContaining(contem);
        } else {

            throw new RequisicaoInvalida("Indique alguma letra contida no email que procura.");
        }

    }

    @RequestMapping(path = "/usuarios/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario inserir(@RequestBody Usuario usuario) {
        usuario.setId(0);
        if (usuario.getNome() == " " || usuario.getNome() == null) {
            throw new RequisicaoInvalida("O nome do usuario deve estar preenchido.");
        } else if (usuario.getCpf() == " " || usuario.getCpf() == null) {
            throw new RequisicaoInvalida("O cpf do usuario deve estar preenchido.");
        } else if (usuario.getEmail() == " " || usuario.getEmail() == null) {
            throw new RequisicaoInvalida("O email do usuario deve estar preenchido.");
        }
        if (this.pesquisaEmail(usuario.getEmail()) == true) {
            throw new RequisicaoInvalida("Já existe um usuario com este email em nosso sistema.");
        } else if (this.pesquisaCpf(usuario.getCpf()) == true) {
            throw new RequisicaoInvalida("Já existe um usuario com este cpf em nosso sistema.");
        }
        usuarioDAO.save(usuario);

        return usuario;
    }

    public boolean pesquisaEmail(String email) {
        Optional<Usuario> optUsuario = usuarioDAO.findByEmail(email);
        if (optUsuario.isPresent()) {
            return true;
        } else {
            return false;
        }

    }

    public boolean pesquisaCpf(String cpf) {
        Optional<Usuario> optUsuario = usuarioDAO.findByCpf(cpf);
        if (optUsuario.isPresent()) {
            return true;
        } else {
            return false;
        }

    }

    @RequestMapping(path = "/usuarios/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Usuario> listar() {
        return usuarioDAO.findAll();
    }

    @RequestMapping(path = "/usuarios/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public Usuario buscaId(@PathVariable int id
    ) {
        Optional<Usuario> optUsuario = usuarioDAO.findById(id);
        if (optUsuario.isPresent()) {
            return optUsuario.get();
        } else {
            throw new NaoEncontrado("Id não encontrada");
        }
    }

    @RequestMapping(path = "/usuarios/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.FOUND)
    public void apagar(@PathVariable int id
    ) {
        Optional<Usuario> optUsuario = usuarioDAO.findById(id);
        if (optUsuario.isPresent()) {
            usuarioDAO.deleteById(id);
        } else {
            throw new NaoEncontrado("Id não encontrada");
        }
    }

    @RequestMapping(path = "/usuarios/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable int id, @RequestBody Usuario usuarioAtual) {
        Usuario usuarioAntigo = this.buscaId(id);

        usuarioAntigo.setNome(usuarioAtual.getNome());
        usuarioAntigo.setEmail(usuarioAtual.getEmail());
        usuarioAntigo.setCpf(usuarioAtual.getCpf());
        usuarioAntigo.setTelefones(usuarioAtual.getTelefones());

        usuarioDAO.save(usuarioAntigo);
    }

    //TELEFONES
    @RequestMapping(path = "/usuarios/{idUsuario}/telefones/", method = RequestMethod.GET)
    public Iterable<Telefone> listarTelefone(@PathVariable int idUsuario) {
        return this.buscaId(idUsuario).getTelefones();
    }

    @RequestMapping(path = "/usuarios/{idUsuario}/telefones/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Telefone inserirTelefone(@PathVariable int idUsuario, @RequestBody Telefone telefone) {
        telefone.setId(0);
        if (telefone.getTipo() == " " || telefone.getTipo() == null) {
            throw new RequisicaoInvalida("O tipo de telefone deve estar preenchido.");
        } else if (telefone.getArea() == 0) {
            throw new RequisicaoInvalida("A area do telefone deve ser informada.");
        } else if (telefone.getNumero() == 0) {
            throw new RequisicaoInvalida("O numero deve ser informado.");
        }
        Telefone telefoneSalvo = telefoneDAO.save(telefone);
        Usuario usuario = this.buscaId(idUsuario);
        usuario.getTelefones().add(telefone);
        usuarioDAO.save(usuario);
        return telefoneSalvo;
    }

    @RequestMapping(path = "/usuarios/{idUsuario}/telefones/{id}", method = RequestMethod.GET)
    public Telefone buscaTelefone(@PathVariable int idUsuario, @PathVariable int id) {
        Optional<Telefone> optTelefone = telefoneDAO.findById(id);
        if (optTelefone.isPresent()) {
            return optTelefone.get();
        } else {
            throw new NaoEncontrado("Id de telefone não encontrado");
        }
    }

    @RequestMapping(path = "/usuarios/{idUsuario}/telefones/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void atualizarTelefone(@PathVariable int idUsuario, @PathVariable int id, @RequestBody Telefone telefone) {
        if (telefoneDAO.existsById(id)) {
            telefone.setId(id);
            telefoneDAO.save(telefone);
        } else {
            throw new NaoEncontrado("Id de telefone não encontrado");
        }
    }

    @RequestMapping(path = "/usuarios/{idUsuario}/telefones/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void apagaTelefone(@PathVariable int idUsuario, @PathVariable int id) {
        Telefone telefoneEnc = null;
        Usuario usuario = this.buscaId(idUsuario);
        List<Telefone> telefones = usuario.getTelefones();
        for (Telefone telefoneLista : telefones) {
            if (id == telefoneLista.getId()) {
                telefoneEnc = telefoneLista;
            }
        }
        if (telefoneEnc != null) {
            usuario.getTelefones().remove(telefoneEnc);
            usuarioDAO.save(usuario);
        } else {
            throw new NaoEncontrado("Id de Telefone não encontrada");
        }
    }
}

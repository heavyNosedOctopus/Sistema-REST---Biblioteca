package biblioteca.lista.andre.pacote.erros;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequisicaoInvalida extends RuntimeException {

    public RequisicaoInvalida(String mensagem) {
        super(mensagem);
    }

}

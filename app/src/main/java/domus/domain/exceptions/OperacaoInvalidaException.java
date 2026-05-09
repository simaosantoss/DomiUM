package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando uma operação não é válida no contexto indicado.
 */
public class OperacaoInvalidaException extends OperacaoDominioException {

    /**
     * Cria uma exceção para uma operação inválida.
     *
     * @param message mensagem descritiva do erro
     */
    public OperacaoInvalidaException(String message) {
        super(message);
    }
}

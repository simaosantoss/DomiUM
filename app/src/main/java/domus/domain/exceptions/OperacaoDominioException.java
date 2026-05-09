package domus.domain.exceptions;

/**
 * Representa uma exceção associada a uma operação inválida no domínio.
 */
public class OperacaoDominioException extends DomusException {

    /**
     * Cria uma exceção de operação de domínio.
     *
     * @param message mensagem descritiva do erro
     */
    public OperacaoDominioException(String message) {
        super(message);
    }
}

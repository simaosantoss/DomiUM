package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando uma entidade necessária não é encontrada.
 */
public class EntidadeNaoEncontradaException extends DomusException {

    /**
     * Cria uma exceção para uma entidade inexistente.
     *
     * @param message mensagem descritiva do erro
     */
    public EntidadeNaoEncontradaException(String message) {
        super(message);
    }
}

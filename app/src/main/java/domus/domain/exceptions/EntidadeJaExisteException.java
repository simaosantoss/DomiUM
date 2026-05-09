package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando se tenta criar uma entidade já existente.
 */
public class EntidadeJaExisteException extends DomusException {

    /**
     * Cria uma exceção para uma entidade duplicada.
     *
     * @param message mensagem descritiva do erro
     */
    public EntidadeJaExisteException(String message) {
        super(message);
    }
}

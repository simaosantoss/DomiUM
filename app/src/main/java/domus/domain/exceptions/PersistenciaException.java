package domus.domain.exceptions;

/**
 * Representa uma exceção associada a operações de persistência do domínio.
 */
public class PersistenciaException extends DomusException {

    /**
     * Cria uma exceção de persistência.
     *
     * @param message mensagem descritiva do erro
     */
    public PersistenciaException(String message) {
        super(message);
    }

}

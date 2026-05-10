package domus.domain.exceptions;

/**
 * Representa a exceção base do domínio DomusControl.
 */
public class DomusException extends Exception {

    /**
     * Cria uma exceção de domínio com a mensagem indicada.
     *
     * @param message mensagem descritiva do erro
     */
    public DomusException(String message) {
        super(message);
    }

}

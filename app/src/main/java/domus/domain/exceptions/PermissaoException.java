package domus.domain.exceptions;

/**
 * Representa uma exceção associada a permissões de acesso no domínio.
 */
public class PermissaoException extends DomusException {

    /**
     * Cria uma exceção de permissão.
     *
     * @param message mensagem descritiva do erro
     */
    public PermissaoException(String message) {
        super(message);
    }
}

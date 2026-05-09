package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando um utilizador não tem permissão sobre uma casa.
 */
public class SemPermissaoException extends PermissaoException {

    /**
     * Identificador do utilizador.
     */
    private final String utilizadorId;

    /**
     * Identificador da casa.
     */
    private final String casaId;

    /**
     * Cria uma exceção para uma operação sem permissão suficiente.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     */
    public SemPermissaoException(String utilizadorId, String casaId) {
        super("Sem permissão: utilizador=" + utilizadorId + ", casa=" + casaId);
        this.utilizadorId = utilizadorId;
        this.casaId = casaId;
    }

    /**
     * Devolve o identificador do utilizador.
     *
     * @return identificador do utilizador
     */
    public String getUtilizadorId() {
        return utilizadorId;
    }

    /**
     * Devolve o identificador da casa.
     *
     * @return identificador da casa
     */
    public String getCasaId() {
        return casaId;
    }
}

package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando uma casa já existe.
 */
public class CasaJaExisteException extends EntidadeJaExisteException {

    /**
     * Identificador da casa.
     */
    private final String casaId;

    /**
     * Cria uma exceção para uma casa duplicada.
     *
     * @param casaId identificador da casa
     */
    public CasaJaExisteException(String casaId) {
        super("Casa já existe: " + casaId);
        this.casaId = casaId;
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

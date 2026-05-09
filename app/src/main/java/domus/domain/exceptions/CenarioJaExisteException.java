package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando um cenário já existe numa casa.
 */
public class CenarioJaExisteException extends EntidadeJaExisteException {

    /**
     * Identificador da casa.
     */
    private final String casaId;

    /**
     * Identificador do cenário.
     */
    private final String cenarioId;

    /**
     * Cria uma exceção para um cenário duplicado numa casa.
     *
     * @param casaId identificador da casa
     * @param cenarioId identificador do cenário
     */
    public CenarioJaExisteException(String casaId, String cenarioId) {
        super("Cenário já existe: " + cenarioId + " (casa: " + casaId + ")");
        this.casaId = casaId;
        this.cenarioId = cenarioId;
    }

    /**
     * Devolve o identificador da casa.
     *
     * @return identificador da casa
     */
    public String getCasaId() {
        return casaId;
    }

    /**
     * Devolve o identificador do cenário.
     *
     * @return identificador do cenário
     */
    public String getCenarioId() {
        return cenarioId;
    }
}

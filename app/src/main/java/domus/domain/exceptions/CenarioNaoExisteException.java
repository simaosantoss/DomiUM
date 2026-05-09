package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando um cenário não existe numa casa.
 */
public class CenarioNaoExisteException extends EntidadeNaoEncontradaException {

    /**
     * Identificador da casa.
     */
    private final String casaId;

    /**
     * Identificador do cenário.
     */
    private final String cenarioId;

    /**
     * Cria uma exceção para um cenário inexistente numa casa.
     *
     * @param casaId identificador da casa
     * @param cenarioId identificador do cenário
     */
    public CenarioNaoExisteException(String casaId, String cenarioId) {
        super("Cenário não existe: " + cenarioId + " (casa: " + casaId + ")");
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

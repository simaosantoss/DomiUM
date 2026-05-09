package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando uma automação não existe numa casa.
 */
public class AutomacaoNaoExisteException extends EntidadeNaoEncontradaException {

    /**
     * Identificador da casa.
     */
    private final String casaId;

    /**
     * Identificador da automação.
     */
    private final String automacaoId;

    /**
     * Cria uma exceção para uma automação inexistente numa casa.
     *
     * @param casaId identificador da casa
     * @param automacaoId identificador da automação
     */
    public AutomacaoNaoExisteException(String casaId, String automacaoId) {
        super("Automação não existe: " + automacaoId + " (casa: " + casaId + ")");
        this.casaId = casaId;
        this.automacaoId = automacaoId;
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
     * Devolve o identificador da automação.
     *
     * @return identificador da automação
     */
    public String getAutomacaoId() {
        return automacaoId;
    }
}

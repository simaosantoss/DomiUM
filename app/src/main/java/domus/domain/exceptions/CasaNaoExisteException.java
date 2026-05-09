package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando uma casa não existe.
 */
public class CasaNaoExisteException extends EntidadeNaoEncontradaException {

    /**
     * Identificador da casa.
     */
    private final String casaId;

    /**
     * Cria uma exceção para uma casa inexistente.
     *
     * @param casaId identificador da casa
     */
    public CasaNaoExisteException(String casaId) {
        super("Casa não existe: " + casaId);
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

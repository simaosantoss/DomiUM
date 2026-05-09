package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando um escalonamento não existe numa casa.
 */
public class EscalonamentoNaoExisteException extends EntidadeNaoEncontradaException {

    /**
     * Identificador da casa.
     */
    private final String casaId;

    /**
     * Identificador do escalonamento.
     */
    private final String escalonamentoId;

    /**
     * Cria uma exceção para um escalonamento inexistente numa casa.
     *
     * @param casaId identificador da casa
     * @param escalonamentoId identificador do escalonamento
     */
    public EscalonamentoNaoExisteException(String casaId, String escalonamentoId) {
        super("Escalonamento não existe: " + escalonamentoId + " (casa: " + casaId + ")");
        this.casaId = casaId;
        this.escalonamentoId = escalonamentoId;
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
     * Devolve o identificador do escalonamento.
     *
     * @return identificador do escalonamento
     */
    public String getEscalonamentoId() {
        return escalonamentoId;
    }
}

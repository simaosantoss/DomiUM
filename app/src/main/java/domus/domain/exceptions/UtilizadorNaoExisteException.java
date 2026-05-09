package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando um utilizador não existe.
 */
public class UtilizadorNaoExisteException extends EntidadeNaoEncontradaException {

    /**
     * Identificador do utilizador.
     */
    private final String utilizadorId;

    /**
     * Cria uma exceção para um utilizador inexistente.
     *
     * @param utilizadorId identificador do utilizador
     */
    public UtilizadorNaoExisteException(String utilizadorId) {
        super("Utilizador não existe: " + utilizadorId);
        this.utilizadorId = utilizadorId;
    }

    /**
     * Devolve o identificador do utilizador.
     *
     * @return identificador do utilizador
     */
    public String getUtilizadorId() {
        return utilizadorId;
    }
}

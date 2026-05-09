package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando um utilizador já existe.
 */
public class UtilizadorJaExisteException extends EntidadeJaExisteException {

    /**
     * Identificador do utilizador.
     */
    private final String utilizadorId;

    /**
     * Cria uma exceção para um utilizador duplicado.
     *
     * @param utilizadorId identificador do utilizador
     */
    public UtilizadorJaExisteException(String utilizadorId) {
        super("Utilizador já existe: " + utilizadorId);
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

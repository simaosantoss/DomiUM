package domus.domain.exceptions;

public class UtilizadorNaoExisteException extends EntidadeNaoEncontradaException {
    private final String utilizadorId;

    public UtilizadorNaoExisteException(String utilizadorId) {
        super("Utilizador não existe: " + utilizadorId);
        this.utilizadorId = utilizadorId;
    }

    public String getUtilizadorId() {
        return utilizadorId;
    }
}

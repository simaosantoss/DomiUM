package domus.domain.exceptions;

public class UtilizadorJaExisteException extends EntidadeJaExisteException {
    private final String utilizadorId;

    public UtilizadorJaExisteException(String utilizadorId) {
        super("Utilizador já existe: " + utilizadorId);
        this.utilizadorId = utilizadorId;
    }

    public String getUtilizadorId() {
        return utilizadorId;
    }
}

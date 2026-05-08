package domus.domain.exceptions;

public class CasaNaoExisteException extends EntidadeNaoEncontradaException {
    private final String casaId;

    public CasaNaoExisteException(String casaId) {
        super("Casa não existe: " + casaId);
        this.casaId = casaId;
    }

    public String getCasaId() {
        return casaId;
    }
}

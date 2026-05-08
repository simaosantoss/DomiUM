package domus.domain.exceptions;

public class CasaJaExisteException extends EntidadeJaExisteException {
    private final String casaId;

    public CasaJaExisteException(String casaId) {
        super("Casa já existe: " + casaId);
        this.casaId = casaId;
    }

    public String getCasaId() {
        return casaId;
    }
}

package domus.domain.exceptions;

public class CenarioJaExisteException extends EntidadeJaExisteException {
    private final String casaId;
    private final String cenarioId;

    public CenarioJaExisteException(String casaId, String cenarioId) {
        super("Cenário já existe: " + cenarioId + " (casa: " + casaId + ")");
        this.casaId = casaId;
        this.cenarioId = cenarioId;
    }

    public String getCasaId() {
        return casaId;
    }

    public String getCenarioId() {
        return cenarioId;
    }
}

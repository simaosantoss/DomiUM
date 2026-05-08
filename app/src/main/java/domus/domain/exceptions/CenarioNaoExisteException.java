package domus.domain.exceptions;

public class CenarioNaoExisteException extends EntidadeNaoEncontradaException {
    private final String casaId;
    private final String cenarioId;

    public CenarioNaoExisteException(String casaId, String cenarioId) {
        super("Cenário não existe: " + cenarioId + " (casa: " + casaId + ")");
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

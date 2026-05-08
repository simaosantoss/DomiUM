package domus.domain.exceptions;

public class AutomacaoJaExisteException extends EntidadeJaExisteException {
    private final String casaId;
    private final String automacaoId;

    public AutomacaoJaExisteException(String casaId, String automacaoId) {
        super("Automação já existe: " + automacaoId + " (casa: " + casaId + ")");
        this.casaId = casaId;
        this.automacaoId = automacaoId;
    }

    public String getCasaId() {
        return casaId;
    }

    public String getAutomacaoId() {
        return automacaoId;
    }
}

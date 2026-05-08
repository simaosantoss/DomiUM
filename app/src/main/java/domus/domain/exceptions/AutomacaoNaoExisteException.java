package domus.domain.exceptions;

public class AutomacaoNaoExisteException extends EntidadeNaoEncontradaException {
    private final String casaId;
    private final String automacaoId;

    public AutomacaoNaoExisteException(String casaId, String automacaoId) {
        super("Automação não existe: " + automacaoId + " (casa: " + casaId + ")");
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

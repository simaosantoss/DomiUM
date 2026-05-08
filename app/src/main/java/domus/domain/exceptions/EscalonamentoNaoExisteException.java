package domus.domain.exceptions;

public class EscalonamentoNaoExisteException extends EntidadeNaoEncontradaException {
    private final String casaId;
    private final String escalonamentoId;

    public EscalonamentoNaoExisteException(String casaId, String escalonamentoId) {
        super("Escalonamento não existe: " + escalonamentoId + " (casa: " + casaId + ")");
        this.casaId = casaId;
        this.escalonamentoId = escalonamentoId;
    }

    public String getCasaId() {
        return casaId;
    }

    public String getEscalonamentoId() {
        return escalonamentoId;
    }
}

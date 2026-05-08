package domus.domain.exceptions;

public class EscalonamentoJaExisteException extends EntidadeJaExisteException {
    private final String casaId;
    private final String escalonamentoId;

    public EscalonamentoJaExisteException(String casaId, String escalonamentoId) {
        super("Escalonamento já existe: " + escalonamentoId + " (casa: " + casaId + ")");
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

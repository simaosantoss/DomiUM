package domus.domain.exceptions;

public class DivisaoJaExisteException extends EntidadeJaExisteException {
    private final String casaId;
    private final String divisaoNome;

    public DivisaoJaExisteException(String casaId, String divisaoNome) {
        super("Divisão já existe: " + divisaoNome + " (casa: " + casaId + ")");
        this.casaId = casaId;
        this.divisaoNome = divisaoNome;
    }

    public String getCasaId() {
        return casaId;
    }

    public String getDivisaoNome() {
        return divisaoNome;
    }
}

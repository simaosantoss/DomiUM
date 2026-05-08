package domus.domain.exceptions;

public class DivisaoNaoExisteException extends EntidadeNaoEncontradaException {
    private final String casaId;
    private final String divisaoNome;

    public DivisaoNaoExisteException(String casaId, String divisaoNome) {
        super("Divisão não existe: " + divisaoNome + " (casa: " + casaId + ")");
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

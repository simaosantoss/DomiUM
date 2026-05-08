package domus.domain.exceptions;

public class DispositivoJaExisteException extends EntidadeJaExisteException {
    private final String casaId;
    private final String divisaoNome;
    private final String dispositivoId;

    public DispositivoJaExisteException(String casaId, String divisaoNome, String dispositivoId) {
        super("Dispositivo já existe: " + dispositivoId + " (divisão: " + divisaoNome + ", casa: " + casaId + ")");
        this.casaId = casaId;
        this.divisaoNome = divisaoNome;
        this.dispositivoId = dispositivoId;
    }

    public String getCasaId() {
        return casaId;
    }

    public String getDivisaoNome() {
        return divisaoNome;
    }

    public String getDispositivoId() {
        return dispositivoId;
    }
}

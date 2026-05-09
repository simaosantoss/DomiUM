package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando um dispositivo já existe numa casa.
 */
public class DispositivoJaExisteException extends EntidadeJaExisteException {

    /**
     * Identificador da casa.
     */
    private final String casaId;

    /**
     * Nome da divisão.
     */
    private final String divisaoNome;

    /**
     * Identificador do dispositivo.
     */
    private final String dispositivoId;

    /**
     * Cria uma exceção para um dispositivo duplicado numa divisão.
     *
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     * @param dispositivoId identificador do dispositivo
     */
    public DispositivoJaExisteException(String casaId, String divisaoNome, String dispositivoId) {
        super("Dispositivo já existe: " + dispositivoId + " (divisão: " + divisaoNome + ", casa: " + casaId + ")");
        this.casaId = casaId;
        this.divisaoNome = divisaoNome;
        this.dispositivoId = dispositivoId;
    }

    /**
     * Devolve o identificador da casa.
     *
     * @return identificador da casa
     */
    public String getCasaId() {
        return casaId;
    }

    /**
     * Devolve o nome da divisão.
     *
     * @return nome da divisão
     */
    public String getDivisaoNome() {
        return divisaoNome;
    }

    /**
     * Devolve o identificador do dispositivo.
     *
     * @return identificador do dispositivo
     */
    public String getDispositivoId() {
        return dispositivoId;
    }
}

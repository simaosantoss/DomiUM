package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando um dispositivo não existe numa casa.
 */
public class DispositivoNaoExisteException extends EntidadeNaoEncontradaException {

    /**
     * Identificador da casa.
     */
    private final String casaId;

    /**
     * Nome da divisão, quando conhecido.
     */
    private final String divisaoNome;

    /**
     * Identificador do dispositivo.
     */
    private final String dispositivoId;

    /**
     * Cria uma exceção para um dispositivo inexistente numa casa.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public DispositivoNaoExisteException(String casaId, String dispositivoId) {
        this(casaId, null, dispositivoId);
    }

    /**
     * Cria uma exceção para um dispositivo inexistente numa divisão de uma casa.
     *
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     * @param dispositivoId identificador do dispositivo
     */
    public DispositivoNaoExisteException(String casaId, String divisaoNome, String dispositivoId) {
        super(criarMensagem(casaId, divisaoNome, dispositivoId));
        this.casaId = casaId;
        this.divisaoNome = divisaoNome;
        this.dispositivoId = dispositivoId;
    }

    private static String criarMensagem(String casaId, String divisaoNome, String dispositivoId) {
        if (divisaoNome == null) {
            return "Dispositivo não existe: " + dispositivoId + " (casa: " + casaId + ")";
        }
        return "Dispositivo não existe: " + dispositivoId
                + " (divisão: " + divisaoNome + ", casa: " + casaId + ")";
    }

    /**
     * Dá acesso ao identificador da casa.
     *
     * @return identificador da casa
     */
    public String getCasaId() {
        return casaId;
    }

    /**
     * Dá acesso ao nome da divisão, quando conhecido.
     *
     * @return nome da divisão, ou {@code null} se não tiver sido indicado
     */
    public String getDivisaoNome() {
        return divisaoNome;
    }

    /**
     * Dá acesso ao identificador do dispositivo.
     *
     * @return identificador do dispositivo
     */
    public String getDispositivoId() {
        return dispositivoId;
    }
}

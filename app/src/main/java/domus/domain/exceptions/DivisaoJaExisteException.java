package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando uma divisão já existe numa casa.
 */
public class DivisaoJaExisteException extends EntidadeJaExisteException {

    /**
     * Identificador da casa.
     */
    private final String casaId;

    /**
     * Nome da divisão.
     */
    private final String divisaoNome;

    /**
     * Cria uma exceção para uma divisão duplicada numa casa.
     *
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     */
    public DivisaoJaExisteException(String casaId, String divisaoNome) {
        super("Divisão já existe: " + divisaoNome + " (casa: " + casaId + ")");
        this.casaId = casaId;
        this.divisaoNome = divisaoNome;
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
}

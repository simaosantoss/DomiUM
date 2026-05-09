package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando uma divisão não existe numa casa.
 */
public class DivisaoNaoExisteException extends EntidadeNaoEncontradaException {

    /**
     * Identificador da casa.
     */
    private final String casaId;

    /**
     * Nome da divisão.
     */
    private final String divisaoNome;

    /**
     * Cria uma exceção para uma divisão inexistente numa casa.
     *
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     */
    public DivisaoNaoExisteException(String casaId, String divisaoNome) {
        super("Divisão não existe: " + divisaoNome + " (casa: " + casaId + ")");
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

package domus.domain.exceptions;

/**
 * Representa uma exceção lançada quando o tipo textual de dispositivo não é suportado.
 */
public class TipoDispositivoInvalidoException extends OperacaoDominioException {

    /**
     * Tipo textual de dispositivo.
     */
    private final String tipo;

    /**
     * Cria uma exceção para um tipo de dispositivo inválido.
     *
     * @param tipo tipo textual de dispositivo
     */
    public TipoDispositivoInvalidoException(String tipo) {
        super("Tipo de dispositivo inválido: " + tipo);
        this.tipo = tipo;
    }

    /**
     * Devolve o tipo textual de dispositivo.
     *
     * @return tipo textual de dispositivo
     */
    public String getTipo() {
        return tipo;
    }
}

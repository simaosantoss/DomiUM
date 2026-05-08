package domus.domain.exceptions;

public class TipoDispositivoInvalidoException extends OperacaoDominioException {
    private final String tipo;

    public TipoDispositivoInvalidoException(String tipo) {
        super("Tipo de dispositivo inválido: " + tipo);
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}

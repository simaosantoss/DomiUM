package domus.domain.exceptions;

public class SemPermissaoException extends PermissaoException {
    private final String utilizadorId;
    private final String casaId;

    public SemPermissaoException(String utilizadorId, String casaId) {
        super("Sem permissão: utilizador=" + utilizadorId + ", casa=" + casaId);
        this.utilizadorId = utilizadorId;
        this.casaId = casaId;
    }

    public String getUtilizadorId() {
        return utilizadorId;
    }

    public String getCasaId() {
        return casaId;
    }
}

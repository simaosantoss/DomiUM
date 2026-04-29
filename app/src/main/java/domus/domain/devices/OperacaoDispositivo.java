package domus.domain.devices;

/**
 * Representa uma operação a aplicar sobre um dispositivo do domínio.
 *
 * A operação recebe o dispositivo encontrado internamente pela estrutura que o
 * contém e indica se a alteração foi realmente efetuada.
 */
public interface OperacaoDispositivo {

    /**
     * Aplica a operação ao dispositivo indicado.
     *
     * @param dispositivo dispositivo sobre o qual a operação deve atuar
     * @return {@code true} se a operação tiver sido aplicada com sucesso
     */
    boolean aplicar(Dispositivo dispositivo);
}

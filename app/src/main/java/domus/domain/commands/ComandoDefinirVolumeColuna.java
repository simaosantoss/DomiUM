package domus.domain.commands;

import domus.domain.DomiUM;
import domus.domain.devices.ColunaInteligente;
import java.util.Objects;

/**
 * Comando que define o volume de uma coluna inteligente.
 */
public class ComandoDefinirVolumeColuna extends ComandoDispositivo {

    /**
     * Volume a aplicar.
     */
    private final int volume;

    /**
     * Cria um comando para definir o volume de uma coluna.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param volume volume a aplicar
     */
    public ComandoDefinirVolumeColuna(String utilizadorId, String casaId,
                                      String dispositivoId, int volume) {
        super(utilizadorId, casaId, dispositivoId);
        this.volume = volume;
    }

    /**
     * Cria um comando por cópia.
     *
     * @param outro comando de origem
     */
    private ComandoDefinirVolumeColuna(ComandoDefinirVolumeColuna outro) {
        super(outro);
        this.volume = outro.volume;
    }

    /**
     * Executa o comando sobre a fachada indicada.
     *
     * @param domium fachada do domínio
     */
    @Override
    public void execute(DomiUM domium) {
        if (domium != null) {
            domium.executarOperacaoDispositivoComDescricao(getUtilizadorId(), getCasaId(), getDispositivoId(), dispositivo -> {
                if (!(dispositivo instanceof ColunaInteligente)) {
                    return false;
                }

                ((ColunaInteligente) dispositivo).setVolume(this.volume);
                return true;
            }, "Definiu volume da coluna para " + this.volume);
        }
    }

    /**
     * Compara este comando com outro objeto com base no seu estado.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo comando lógico
     */
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        ComandoDefinirVolumeColuna that = (ComandoDefinirVolumeColuna) o;
        return this.volume == that.volume;
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do comando
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.volume);
    }

    /**
     * Produz uma representação textual legível do comando.
     *
     * @return texto descritivo do comando
     */
    @Override
    public String toString() {
        return "ComandoDefinirVolumeColuna{"
                + "utilizadorId='" + getUtilizadorId() + '\''
                + ", casaId='" + getCasaId() + '\''
                + ", dispositivoId='" + getDispositivoId() + '\''
                + ", volume=" + this.volume
                + '}';
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando equivalente
     */
    @Override
    public ComandoDefinirVolumeColuna clone() {
        return new ComandoDefinirVolumeColuna(this);
    }
}

package domus.domain.commands;

import domus.domain.DomiUM;
import domus.domain.devices.LampadaInteligente;
import java.util.Objects;

/**
 * Comando que define a temperatura de cor de uma lâmpada.
 */
public class ComandoDefinirCorLampada extends ComandoDispositivo {

    /**
     * Temperatura de cor a aplicar, em Kelvin.
     */
    private final int corK;

    /**
     * Cria um comando para definir a temperatura de cor de uma lâmpada.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param corK temperatura de cor a aplicar, em Kelvin
     */
    public ComandoDefinirCorLampada(String utilizadorId, String casaId,
                                    String dispositivoId, int corK) {
        super(utilizadorId, casaId, dispositivoId);
        this.corK = corK;
    }

    /**
     * Cria um comando por cópia.
     *
     * @param outro comando de origem
     */
    private ComandoDefinirCorLampada(ComandoDefinirCorLampada outro) {
        super(outro);
        this.corK = outro.corK;
    }

    /**
     * Executa o comando sobre a fachada indicada.
     *
     * @param domium fachada do domínio
     */
    @Override
    public void execute(DomiUM domium) {
        if (domium != null) {
            domium.executarOperacaoDispositivo(getUtilizadorId(), getCasaId(), getDispositivoId(), dispositivo -> {
                if (!(dispositivo instanceof LampadaInteligente)) {
                    return false;
                }

                ((LampadaInteligente) dispositivo).setCorK(this.corK);
                return true;
            });
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
        ComandoDefinirCorLampada that = (ComandoDefinirCorLampada) o;
        return this.corK == that.corK;
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do comando
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.corK);
    }

    /**
     * Produz uma representação textual legível do comando.
     *
     * @return texto descritivo do comando
     */
    @Override
    public String toString() {
        return "ComandoDefinirCorLampada{"
                + "utilizadorId='" + getUtilizadorId() + '\''
                + ", casaId='" + getCasaId() + '\''
                + ", dispositivoId='" + getDispositivoId() + '\''
                + ", corK=" + this.corK
                + '}';
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando equivalente
     */
    @Override
    public ComandoDefinirCorLampada clone() {
        return new ComandoDefinirCorLampada(this);
    }
}

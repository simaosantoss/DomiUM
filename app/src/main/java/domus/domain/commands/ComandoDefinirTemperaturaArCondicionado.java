package domus.domain.commands;

import domus.domain.DomiUM;
import domus.domain.devices.ArCondicionadoInteligente;
import java.util.Objects;

/**
 * Comando que define a temperatura alvo de um ar condicionado.
 */
public class ComandoDefinirTemperaturaArCondicionado extends ComandoDispositivo {

    /**
     * Temperatura alvo a aplicar.
     */
    private final double temperatura;

    /**
     * Cria um comando para definir a temperatura alvo de um ar condicionado.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param temperatura temperatura alvo a aplicar
     */
    public ComandoDefinirTemperaturaArCondicionado(String utilizadorId, String casaId,
                                                   String dispositivoId, double temperatura) {
        super(utilizadorId, casaId, dispositivoId);
        this.temperatura = temperatura;
    }

    /**
     * Cria um comando por cópia.
     *
     * @param outro comando de origem
     */
    private ComandoDefinirTemperaturaArCondicionado(ComandoDefinirTemperaturaArCondicionado outro) {
        super(outro);
        this.temperatura = outro.temperatura;
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
                if (!(dispositivo instanceof ArCondicionadoInteligente)) {
                    return false;
                }

                ((ArCondicionadoInteligente) dispositivo).setTemperaturaAlvo(this.temperatura);
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
        ComandoDefinirTemperaturaArCondicionado that = (ComandoDefinirTemperaturaArCondicionado) o;
        return Double.compare(this.temperatura, that.temperatura) == 0;
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do comando
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.temperatura);
    }

    /**
     * Produz uma representação textual legível do comando.
     *
     * @return texto descritivo do comando
     */
    @Override
    public String toString() {
        return "ComandoDefinirTemperaturaArCondicionado{"
                + "utilizadorId='" + getUtilizadorId() + '\''
                + ", casaId='" + getCasaId() + '\''
                + ", dispositivoId='" + getDispositivoId() + '\''
                + ", temperatura=" + this.temperatura
                + '}';
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando equivalente
     */
    @Override
    public ComandoDefinirTemperaturaArCondicionado clone() {
        return new ComandoDefinirTemperaturaArCondicionado(this);
    }
}

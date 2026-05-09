package domus.domain.commands;

import domus.domain.DomiUM;
import domus.domain.devices.Dispositivo;
import domus.domain.devices.LampadaInteligente;
import java.util.Objects;

/**
 * Comando que define a intensidade luminosa de uma lâmpada.
 */
public class ComandoDefinirIntensidadeLampada extends ComandoDispositivo {

    /**
     * Intensidade luminosa a aplicar.
     */
    private final int intensidade;

    /**
     * Cria um comando para definir a intensidade de uma lâmpada.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param intensidade intensidade luminosa a aplicar
     */
    public ComandoDefinirIntensidadeLampada(String utilizadorId, String casaId,
                                            String dispositivoId, int intensidade) {
        super(utilizadorId, casaId, dispositivoId);
        this.intensidade = intensidade;
    }

    /**
     * Cria um comando por cópia.
     *
     * @param outro comando de origem
     */
    private ComandoDefinirIntensidadeLampada(ComandoDefinirIntensidadeLampada outro) {
        super(outro);
        this.intensidade = outro.intensidade;
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
                if (!(dispositivo instanceof LampadaInteligente)) {
                    return false;
                }

                ((LampadaInteligente) dispositivo).setIntensidade(this.intensidade);
                return true;
            }, "Definiu intensidade da lâmpada para " + this.intensidade);
        }
    }

    /**
     * Verifica se o comando pode ser aplicado ao dispositivo indicado.
     *
     * @param dispositivo dispositivo a validar
     * @return {@code true} se o dispositivo for uma lâmpada inteligente
     */
    @Override
    public boolean suportaDispositivo(Dispositivo dispositivo) {
        return dispositivo instanceof LampadaInteligente;
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
        ComandoDefinirIntensidadeLampada that = (ComandoDefinirIntensidadeLampada) o;
        return this.intensidade == that.intensidade;
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do comando
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.intensidade);
    }

    /**
     * Produz uma representação textual legível do comando.
     *
     * @return texto descritivo do comando
     */
    @Override
    public String toString() {
        return "ComandoDefinirIntensidadeLampada{"
                + "utilizadorId='" + getUtilizadorId() + '\''
                + ", casaId='" + getCasaId() + '\''
                + ", dispositivoId='" + getDispositivoId() + '\''
                + ", intensidade=" + this.intensidade
                + '}';
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando equivalente
     */
    @Override
    public ComandoDefinirIntensidadeLampada clone() {
        return new ComandoDefinirIntensidadeLampada(this);
    }
}

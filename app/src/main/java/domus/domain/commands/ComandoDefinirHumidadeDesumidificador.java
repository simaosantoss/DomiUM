package domus.domain.commands;

import domus.domain.DomiUM;
import domus.domain.devices.DesumidificadorInteligente;
import domus.domain.devices.Dispositivo;
import java.util.Objects;

/**
 * Comando que define a humidade alvo de um desumidificador.
 */
public class ComandoDefinirHumidadeDesumidificador extends ComandoDispositivo {

    /**
     * Humidade alvo a aplicar.
     */
    private final double humidade;

    /**
     * Cria um comando para definir a humidade alvo de um desumidificador.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param humidade humidade alvo a aplicar
     */
    public ComandoDefinirHumidadeDesumidificador(String utilizadorId, String casaId,
                                                 String dispositivoId, double humidade) {
        super(utilizadorId, casaId, dispositivoId);
        this.humidade = humidade;
    }

    /**
     * Cria um comando por cópia.
     *
     * @param outro comando de origem
     */
    private ComandoDefinirHumidadeDesumidificador(ComandoDefinirHumidadeDesumidificador outro) {
        super(outro);
        this.humidade = outro.humidade;
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
                if (!(dispositivo instanceof DesumidificadorInteligente)) {
                    return false;
                }

                ((DesumidificadorInteligente) dispositivo).setHumidadeAlvo(this.humidade);
                return true;
            }, "Definiu humidade alvo do desumidificador para " + this.humidade);
        }
    }

    /**
     * Verifica se o comando pode ser aplicado ao dispositivo indicado.
     *
     * @param dispositivo dispositivo a validar
     * @return {@code true} se o dispositivo for um desumidificador inteligente
     */
    @Override
    public boolean suportaDispositivo(Dispositivo dispositivo) {
        return dispositivo instanceof DesumidificadorInteligente;
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
        ComandoDefinirHumidadeDesumidificador that = (ComandoDefinirHumidadeDesumidificador) o;
        return Double.compare(this.humidade, that.humidade) == 0;
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do comando
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.humidade);
    }

    /**
     * Produz uma representação textual legível do comando.
     *
     * @return texto descritivo do comando
     */
    @Override
    public String toString() {
        return "ComandoDefinirHumidadeDesumidificador{"
                + "utilizadorId='" + getUtilizadorId() + '\''
                + ", casaId='" + getCasaId() + '\''
                + ", dispositivoId='" + getDispositivoId() + '\''
                + ", humidade=" + this.humidade
                + '}';
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando equivalente
     */
    @Override
    public ComandoDefinirHumidadeDesumidificador clone() {
        return new ComandoDefinirHumidadeDesumidificador(this);
    }
}

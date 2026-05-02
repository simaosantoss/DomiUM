package domus.domain.commands;

import domus.domain.DomiUM;
import domus.domain.devices.ArCondicionadoInteligente;
import java.util.Objects;

/**
 * Comando que define o modo de funcionamento de um ar condicionado.
 */
public class ComandoDefinirModoArCondicionado extends ComandoDispositivo {

    /**
     * Modo de funcionamento a aplicar.
     */
    private final String modo;

    /**
     * Cria um comando para definir o modo de um ar condicionado.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param modo modo de funcionamento a aplicar
     */
    public ComandoDefinirModoArCondicionado(String utilizadorId, String casaId,
                                            String dispositivoId, String modo) {
        super(utilizadorId, casaId, dispositivoId);
        this.modo = modo;
    }

    /**
     * Cria um comando por cópia.
     *
     * @param outro comando de origem
     */
    private ComandoDefinirModoArCondicionado(ComandoDefinirModoArCondicionado outro) {
        super(outro);
        this.modo = outro.modo;
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
                if (!(dispositivo instanceof ArCondicionadoInteligente)) {
                    return false;
                }

                ((ArCondicionadoInteligente) dispositivo).setModo(this.modo);
                return true;
            }, "Definiu modo do ar condicionado para " + this.modo);
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
        ComandoDefinirModoArCondicionado that = (ComandoDefinirModoArCondicionado) o;
        return Objects.equals(this.modo, that.modo);
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do comando
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.modo);
    }

    /**
     * Produz uma representação textual legível do comando.
     *
     * @return texto descritivo do comando
     */
    @Override
    public String toString() {
        return "ComandoDefinirModoArCondicionado{"
                + "utilizadorId='" + getUtilizadorId() + '\''
                + ", casaId='" + getCasaId() + '\''
                + ", dispositivoId='" + getDispositivoId() + '\''
                + ", modo='" + this.modo + '\''
                + '}';
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando equivalente
     */
    @Override
    public ComandoDefinirModoArCondicionado clone() {
        return new ComandoDefinirModoArCondicionado(this);
    }
}

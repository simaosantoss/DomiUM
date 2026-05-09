package domus.domain.commands;

import domus.domain.DomiUM;
import domus.domain.devices.CortinaInteligente;
import domus.domain.devices.Dispositivo;
import java.util.Objects;

/**
 * Comando que define a percentagem de abertura de uma cortina.
 */
public class ComandoDefinirAberturaCortina extends ComandoDispositivo {

    /**
     * Percentagem de abertura a aplicar.
     */
    private final int percentagemAbertura;

    /**
     * Cria um comando para definir a abertura de uma cortina.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param percentagemAbertura percentagem de abertura a aplicar
     */
    public ComandoDefinirAberturaCortina(String utilizadorId, String casaId,
                                         String dispositivoId, int percentagemAbertura) {
        super(utilizadorId, casaId, dispositivoId);
        this.percentagemAbertura = percentagemAbertura;
    }

    /**
     * Cria um comando por cópia.
     *
     * @param outro comando de origem
     */
    private ComandoDefinirAberturaCortina(ComandoDefinirAberturaCortina outro) {
        super(outro);
        this.percentagemAbertura = outro.percentagemAbertura;
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
                if (!(dispositivo instanceof CortinaInteligente)) {
                    return false;
                }

                ((CortinaInteligente) dispositivo).setPercentagemAbertura(this.percentagemAbertura);
                return true;
            }, "Definiu abertura da cortina para " + this.percentagemAbertura + "%");
        }
    }

    /**
     * Verifica se o comando pode ser aplicado ao dispositivo indicado.
     *
     * @param dispositivo dispositivo a validar
     * @return {@code true} se o dispositivo for uma cortina inteligente
     */
    @Override
    public boolean suportaDispositivo(Dispositivo dispositivo) {
        return dispositivo instanceof CortinaInteligente;
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
        ComandoDefinirAberturaCortina that = (ComandoDefinirAberturaCortina) o;
        return this.percentagemAbertura == that.percentagemAbertura;
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do comando
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.percentagemAbertura);
    }

    /**
     * Produz uma representação textual legível do comando.
     *
     * @return texto descritivo do comando
     */
    @Override
    public String toString() {
        return "ComandoDefinirAberturaCortina{"
                + "utilizadorId='" + getUtilizadorId() + '\''
                + ", casaId='" + getCasaId() + '\''
                + ", dispositivoId='" + getDispositivoId() + '\''
                + ", percentagemAbertura=" + this.percentagemAbertura
                + '}';
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando equivalente
     */
    @Override
    public ComandoDefinirAberturaCortina clone() {
        return new ComandoDefinirAberturaCortina(this);
    }
}

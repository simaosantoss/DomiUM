package domus.domain.commands;

import domus.domain.DomiUM;
import domus.domain.devices.ColunaInteligente;
import java.util.Objects;

/**
 * Comando que define a playlist atual de uma coluna inteligente.
 */
public class ComandoDefinirPlaylistColuna extends ComandoDispositivo {

    /**
     * Playlist a aplicar.
     */
    private final String playlist;

    /**
     * Cria um comando para definir a playlist de uma coluna.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param playlist playlist a aplicar
     */
    public ComandoDefinirPlaylistColuna(String utilizadorId, String casaId,
                                        String dispositivoId, String playlist) {
        super(utilizadorId, casaId, dispositivoId);
        this.playlist = playlist;
    }

    /**
     * Cria um comando por cópia.
     *
     * @param outro comando de origem
     */
    private ComandoDefinirPlaylistColuna(ComandoDefinirPlaylistColuna outro) {
        super(outro);
        this.playlist = outro.playlist;
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

                ((ColunaInteligente) dispositivo).setPlaylistAtual(this.playlist);
                return true;
            }, "Definiu playlist da coluna para " + this.playlist);
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
        ComandoDefinirPlaylistColuna that = (ComandoDefinirPlaylistColuna) o;
        return Objects.equals(this.playlist, that.playlist);
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do comando
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.playlist);
    }

    /**
     * Produz uma representação textual legível do comando.
     *
     * @return texto descritivo do comando
     */
    @Override
    public String toString() {
        return "ComandoDefinirPlaylistColuna{"
                + "utilizadorId='" + getUtilizadorId() + '\''
                + ", casaId='" + getCasaId() + '\''
                + ", dispositivoId='" + getDispositivoId() + '\''
                + ", playlist='" + this.playlist + '\''
                + '}';
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando equivalente
     */
    @Override
    public ComandoDefinirPlaylistColuna clone() {
        return new ComandoDefinirPlaylistColuna(this);
    }
}

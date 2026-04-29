package domus.domain.commands;

import java.util.Objects;

/**
 * Representa a base comum dos comandos que atuam sobre um dispositivo.
 *
 * O comando guarda apenas identificadores, evitando manter referências diretas
 * para objetos internos do domínio.
 */
public abstract class ComandoDispositivo implements Command {

    /**
     * Identificador do utilizador que executa o comando.
     */
    private final String utilizadorId;

    /**
     * Identificador da casa onde o dispositivo se encontra.
     */
    private final String casaId;

    /**
     * Identificador do dispositivo alvo do comando.
     */
    private final String dispositivoId;

    /**
     * Cria um comando associado a um dispositivo.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public ComandoDispositivo(String utilizadorId, String casaId, String dispositivoId) {
        this.utilizadorId = utilizadorId;
        this.casaId = casaId;
        this.dispositivoId = dispositivoId;
    }

    /**
     * Cria um comando por cópia de outro comando de dispositivo.
     *
     * @param outro comando de origem
     */
    protected ComandoDispositivo(ComandoDispositivo outro) {
        this(outro.utilizadorId, outro.casaId, outro.dispositivoId);
    }

    /**
     * Dá acesso ao identificador do utilizador.
     *
     * @return identificador do utilizador
     */
    public String getUtilizadorId() {
        return this.utilizadorId;
    }

    /**
     * Dá acesso ao identificador da casa.
     *
     * @return identificador da casa
     */
    public String getCasaId() {
        return this.casaId;
    }

    /**
     * Dá acesso ao identificador do dispositivo.
     *
     * @return identificador do dispositivo
     */
    public String getDispositivoId() {
        return this.dispositivoId;
    }

    /**
     * Compara este comando com outro objeto com base no seu estado comum.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo comando lógico
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ComandoDispositivo that = (ComandoDispositivo) o;
        return Objects.equals(this.utilizadorId, that.utilizadorId)
                && Objects.equals(this.casaId, that.casaId)
                && Objects.equals(this.dispositivoId, that.dispositivoId);
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do comando
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.utilizadorId, this.casaId, this.dispositivoId);
    }

    /**
     * Produz uma representação textual do estado comum do comando.
     *
     * @return texto descritivo do comando
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{"
                + "utilizadorId='" + this.utilizadorId + '\''
                + ", casaId='" + this.casaId + '\''
                + ", dispositivoId='" + this.dispositivoId + '\''
                + '}';
    }

    /**
     * Cria uma cópia lógica deste comando.
     *
     * @return novo comando com o mesmo estado lógico
     */
    @Override
    public abstract ComandoDispositivo clone();
}

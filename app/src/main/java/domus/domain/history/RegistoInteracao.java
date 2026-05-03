package domus.domain.history;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa um evento histórico associado à interação de um utilizador com um
 * dispositivo.
 *
 * Depois de criado, o registo não pode ser alterado, preservando assim a
 * consistência do histórico.
 */
public final class RegistoInteracao implements Serializable {

    /**
     * Momento exato em que a interação ocorreu.
     */
    private final LocalDateTime dataHora;

    /**
     * Identificador da casa onde a interação ocorreu.
     */
    private final String casaId;

    /**
     * Identificador do dispositivo envolvido na interação.
     */
    private final String dispositivoId;

    /**
     * Descrição textual da ação efetuada.
     */
    private final String acao;

    /**
     * Cria um novo registo de interação com indicação da casa.
     *
     * @param dataHora instante em que a interação aconteceu
     * @param casaId identificador da casa onde ocorreu a interação
     * @param dispositivoId identificador do dispositivo envolvido
     * @param acao descrição da ação realizada
     */
    public RegistoInteracao(LocalDateTime dataHora, String casaId, String dispositivoId, String acao) {
        this.dataHora = dataHora;
        this.casaId = casaId;
        this.dispositivoId = dispositivoId;
        this.acao = acao;
    }

    /**
     * Dá acesso ao instante associado ao registo.
     *
     * @return data e hora da interação
     */
    public LocalDateTime getDataHora() {
        return this.dataHora;
    }

    /**
     * Dá acesso ao identificador da casa associada ao registo.
     *
     * @return identificador da casa
     */
    public String getCasaId() {
        return this.casaId;
    }

    /**
     * Dá acesso ao identificador do dispositivo afetado.
     *
     * @return identificador do dispositivo
     */
    public String getDispositivoId() {
        return this.dispositivoId;
    }

    /**
     * Dá acesso à descrição da ação registada.
     *
     * @return ação realizada
     */
    public String getAcao() {
        return this.acao;
    }

    /**
     * Compara este registo com outro objeto com base no seu estado relevante.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo registo
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegistoInteracao)) {
            return false;
        }
        RegistoInteracao that = (RegistoInteracao) o;
        return Objects.equals(this.dataHora, that.dataHora)
                && Objects.equals(this.casaId, that.casaId)
                && Objects.equals(this.dispositivoId, that.dispositivoId)
                && Objects.equals(this.acao, that.acao);
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão do registo
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.dataHora, this.casaId, this.dispositivoId, this.acao);
    }

    /**
     * Produz uma representação textual útil para inspeção do histórico.
     *
     * @return texto com os dados principais do registo
     */
    @Override
    public String toString() {
        return "RegistoInteracao{"
                + "dataHora=" + this.dataHora
                + ", casaId='" + this.casaId + '\''
                + ", dispositivoId='" + this.dispositivoId + '\''
                + ", acao='" + this.acao + '\''
                + '}';
    }

    /**
     * Cria uma cópia independente deste registo.
     *
     * @return novo registo com o mesmo conteúdo
     */
    public RegistoInteracao clone() {
        return new RegistoInteracao(this.dataHora, this.casaId, this.dispositivoId, this.acao);
    }
}

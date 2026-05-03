package domus.domain.suggestions;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Representa uma sugestão textual de escalonamento inferida do histórico.
 */
public class SugestaoEscalonamento implements Serializable {

    /**
     * Identificador do utilizador associado à sugestão.
     */
    private final String utilizadorId;

    /**
     * Identificador da casa associada à sugestão.
     */
    private final String casaId;

    /**
     * Identificador do dispositivo associado à sugestão.
     */
    private final String dispositivoId;

    /**
     * Ação repetida que originou a sugestão.
     */
    private final String acao;

    /**
     * Hora sugerida para o escalonamento.
     */
    private final LocalTime horaSugerida;

    /**
     * Número de ocorrências que suportam a sugestão.
     */
    private final int ocorrencias;

    /**
     * Mensagem textual da sugestão.
     */
    private final String mensagem;

    /**
     * Cria uma sugestão de escalonamento.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param acao ação repetida
     * @param horaSugerida hora sugerida
     * @param ocorrencias número de ocorrências
     * @param mensagem mensagem textual da sugestão
     */
    public SugestaoEscalonamento(String utilizadorId, String casaId, String dispositivoId,
                                 String acao, LocalTime horaSugerida, int ocorrencias,
                                 String mensagem) {
        this.utilizadorId = utilizadorId;
        this.casaId = casaId;
        this.dispositivoId = dispositivoId;
        this.acao = acao;
        this.horaSugerida = horaSugerida;
        this.ocorrencias = ocorrencias;
        this.mensagem = mensagem;
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
     * Dá acesso à ação associada à sugestão.
     *
     * @return ação repetida
     */
    public String getAcao() {
        return this.acao;
    }

    /**
     * Dá acesso à hora sugerida.
     *
     * @return hora sugerida
     */
    public LocalTime getHoraSugerida() {
        return this.horaSugerida;
    }

    /**
     * Dá acesso ao número de ocorrências.
     *
     * @return número de ocorrências
     */
    public int getOcorrencias() {
        return this.ocorrencias;
    }

    /**
     * Dá acesso à mensagem textual da sugestão.
     *
     * @return mensagem da sugestão
     */
    public String getMensagem() {
        return this.mensagem;
    }

    /**
     * Compara esta sugestão com outro objeto com base no seu estado.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem a mesma sugestão
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        SugestaoEscalonamento that = (SugestaoEscalonamento) o;
        return this.ocorrencias == that.ocorrencias
                && Objects.equals(this.utilizadorId, that.utilizadorId)
                && Objects.equals(this.casaId, that.casaId)
                && Objects.equals(this.dispositivoId, that.dispositivoId)
                && Objects.equals(this.acao, that.acao)
                && Objects.equals(this.horaSugerida, that.horaSugerida)
                && Objects.equals(this.mensagem, that.mensagem);
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão da sugestão
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.utilizadorId, this.casaId, this.dispositivoId,
                this.acao, this.horaSugerida, this.ocorrencias, this.mensagem);
    }

    /**
     * Produz uma representação textual legível da sugestão.
     *
     * @return texto descritivo da sugestão
     */
    @Override
    public String toString() {
        return "SugestaoEscalonamento{"
                + "utilizadorId='" + this.utilizadorId + '\''
                + ", casaId='" + this.casaId + '\''
                + ", dispositivoId='" + this.dispositivoId + '\''
                + ", acao='" + this.acao + '\''
                + ", horaSugerida=" + this.horaSugerida
                + ", ocorrencias=" + this.ocorrencias
                + ", mensagem='" + this.mensagem + '\''
                + '}';
    }

    /**
     * Cria uma cópia desta sugestão.
     *
     * @return nova sugestão com os mesmos valores
     */
    public SugestaoEscalonamento clone() {
        return new SugestaoEscalonamento(this.utilizadorId, this.casaId,
                this.dispositivoId, this.acao, this.horaSugerida,
                this.ocorrencias, this.mensagem);
    }
}

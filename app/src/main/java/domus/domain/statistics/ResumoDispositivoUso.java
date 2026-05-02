package domus.domain.statistics;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa um resumo de utilização de um dispositivo.
 */
public class ResumoDispositivoUso implements Serializable {

    /**
     * Identificador do dispositivo.
     */
    private final String dispositivoId;

    /**
     * Marca do dispositivo.
     */
    private final String marca;

    /**
     * Modelo do dispositivo.
     */
    private final String modelo;

    /**
     * Consumo atual calculado pelo dispositivo.
     */
    private final double consumoAtual;

    /**
     * Tempo total ligado, em minutos.
     */
    private final long tempoTotalLigado;

    /**
     * Número de ativações do dispositivo.
     */
    private final int numeroAtivacoes;

    /**
     * Cria um resumo de utilização de dispositivo.
     *
     * @param dispositivoId identificador do dispositivo
     * @param marca marca do dispositivo
     * @param modelo modelo do dispositivo
     * @param consumoAtual consumo atual do dispositivo
     * @param tempoTotalLigado tempo total ligado, em minutos
     * @param numeroAtivacoes número de ativações
     */
    public ResumoDispositivoUso(String dispositivoId, String marca, String modelo,
                                double consumoAtual, long tempoTotalLigado,
                                int numeroAtivacoes) {
        this.dispositivoId = dispositivoId;
        this.marca = marca;
        this.modelo = modelo;
        this.consumoAtual = consumoAtual;
        this.tempoTotalLigado = tempoTotalLigado;
        this.numeroAtivacoes = numeroAtivacoes;
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
     * Dá acesso à marca do dispositivo.
     *
     * @return marca do dispositivo
     */
    public String getMarca() {
        return this.marca;
    }

    /**
     * Dá acesso ao modelo do dispositivo.
     *
     * @return modelo do dispositivo
     */
    public String getModelo() {
        return this.modelo;
    }

    /**
     * Dá acesso ao consumo atual do dispositivo.
     *
     * @return consumo atual do dispositivo
     */
    public double getConsumoAtual() {
        return this.consumoAtual;
    }

    /**
     * Dá acesso ao tempo total ligado.
     *
     * @return tempo total ligado, em minutos
     */
    public long getTempoTotalLigado() {
        return this.tempoTotalLigado;
    }

    /**
     * Dá acesso ao número de ativações.
     *
     * @return número de ativações
     */
    public int getNumeroAtivacoes() {
        return this.numeroAtivacoes;
    }

    /**
     * Compara este resumo com outro objeto com base no seu estado.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo resumo
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ResumoDispositivoUso that = (ResumoDispositivoUso) o;
        return Double.compare(this.consumoAtual, that.consumoAtual) == 0
                && this.tempoTotalLigado == that.tempoTotalLigado
                && this.numeroAtivacoes == that.numeroAtivacoes
                && Objects.equals(this.dispositivoId, that.dispositivoId)
                && Objects.equals(this.marca, that.marca)
                && Objects.equals(this.modelo, that.modelo);
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do resumo
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.dispositivoId, this.marca, this.modelo,
                this.consumoAtual, this.tempoTotalLigado, this.numeroAtivacoes);
    }

    /**
     * Produz uma representação textual legível do resumo.
     *
     * @return texto descritivo do resumo
     */
    @Override
    public String toString() {
        return "ResumoDispositivoUso{"
                + "dispositivoId='" + this.dispositivoId + '\''
                + ", marca='" + this.marca + '\''
                + ", modelo='" + this.modelo + '\''
                + ", consumoAtual=" + this.consumoAtual
                + ", tempoTotalLigado=" + this.tempoTotalLigado
                + ", numeroAtivacoes=" + this.numeroAtivacoes
                + '}';
    }

    /**
     * Cria uma cópia deste resumo.
     *
     * @return novo resumo com os mesmos valores
     */
    public ResumoDispositivoUso clone() {
        return new ResumoDispositivoUso(this.dispositivoId, this.marca, this.modelo,
                this.consumoAtual, this.tempoTotalLigado, this.numeroAtivacoes);
    }
}

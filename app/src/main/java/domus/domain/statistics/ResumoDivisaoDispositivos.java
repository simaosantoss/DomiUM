package domus.domain.statistics;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa um resumo da quantidade de dispositivos numa divisão.
 */
public class ResumoDivisaoDispositivos implements Serializable {

    /**
     * Identificador da casa.
     */
    private final String casaId;

    /**
     * Nome da casa.
     */
    private final String nomeCasa;

    /**
     * Nome da divisão.
     */
    private final String nomeDivisao;

    /**
     * Número de dispositivos existentes na divisão.
     */
    private final int numeroDispositivos;

    /**
     * Cria um resumo de dispositivos por divisão.
     *
     * @param casaId identificador da casa
     * @param nomeCasa nome da casa
     * @param nomeDivisao nome da divisão
     * @param numeroDispositivos número de dispositivos da divisão
     */
    public ResumoDivisaoDispositivos(String casaId, String nomeCasa,
                                     String nomeDivisao, int numeroDispositivos) {
        this.casaId = casaId;
        this.nomeCasa = nomeCasa;
        this.nomeDivisao = nomeDivisao;
        this.numeroDispositivos = numeroDispositivos;
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
     * Dá acesso ao nome da casa.
     *
     * @return nome da casa
     */
    public String getNomeCasa() {
        return this.nomeCasa;
    }

    /**
     * Dá acesso ao nome da divisão.
     *
     * @return nome da divisão
     */
    public String getNomeDivisao() {
        return this.nomeDivisao;
    }

    /**
     * Dá acesso ao número de dispositivos.
     *
     * @return número de dispositivos da divisão
     */
    public int getNumeroDispositivos() {
        return this.numeroDispositivos;
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
        ResumoDivisaoDispositivos that = (ResumoDivisaoDispositivos) o;
        return this.numeroDispositivos == that.numeroDispositivos
                && Objects.equals(this.casaId, that.casaId)
                && Objects.equals(this.nomeCasa, that.nomeCasa)
                && Objects.equals(this.nomeDivisao, that.nomeDivisao);
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do resumo
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.casaId, this.nomeCasa, this.nomeDivisao,
                this.numeroDispositivos);
    }

    /**
     * Produz uma representação textual legível do resumo.
     *
     * @return texto descritivo do resumo
     */
    @Override
    public String toString() {
        return "ResumoDivisaoDispositivos{"
                + "casaId='" + this.casaId + '\''
                + ", nomeCasa='" + this.nomeCasa + '\''
                + ", nomeDivisao='" + this.nomeDivisao + '\''
                + ", numeroDispositivos=" + this.numeroDispositivos
                + '}';
    }

    /**
     * Cria uma cópia deste resumo.
     *
     * @return novo resumo com os mesmos valores
     */
    public ResumoDivisaoDispositivos clone() {
        return new ResumoDivisaoDispositivos(this.casaId, this.nomeCasa,
                this.nomeDivisao, this.numeroDispositivos);
    }
}

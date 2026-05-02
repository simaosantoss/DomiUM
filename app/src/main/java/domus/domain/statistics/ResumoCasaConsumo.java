package domus.domain.statistics;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa um resumo de consumo total de uma casa.
 */
public class ResumoCasaConsumo implements Serializable {

    /**
     * Identificador da casa.
     */
    private final String casaId;

    /**
     * Nome da casa.
     */
    private final String nomeCasa;

    /**
     * Consumo total calculado para a casa.
     */
    private final double consumoTotal;

    /**
     * Cria um resumo de consumo de uma casa.
     *
     * @param casaId identificador da casa
     * @param nomeCasa nome da casa
     * @param consumoTotal consumo total da casa
     */
    public ResumoCasaConsumo(String casaId, String nomeCasa, double consumoTotal) {
        this.casaId = casaId;
        this.nomeCasa = nomeCasa;
        this.consumoTotal = consumoTotal;
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
     * Dá acesso ao consumo total da casa.
     *
     * @return consumo total da casa
     */
    public double getConsumoTotal() {
        return this.consumoTotal;
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
        ResumoCasaConsumo that = (ResumoCasaConsumo) o;
        return Double.compare(this.consumoTotal, that.consumoTotal) == 0
                && Objects.equals(this.casaId, that.casaId)
                && Objects.equals(this.nomeCasa, that.nomeCasa);
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do resumo
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.casaId, this.nomeCasa, this.consumoTotal);
    }

    /**
     * Produz uma representação textual legível do resumo.
     *
     * @return texto descritivo do resumo
     */
    @Override
    public String toString() {
        return "ResumoCasaConsumo{"
                + "casaId='" + this.casaId + '\''
                + ", nomeCasa='" + this.nomeCasa + '\''
                + ", consumoTotal=" + this.consumoTotal
                + '}';
    }

    /**
     * Cria uma cópia deste resumo.
     *
     * @return novo resumo com os mesmos valores
     */
    public ResumoCasaConsumo clone() {
        return new ResumoCasaConsumo(this.casaId, this.nomeCasa, this.consumoTotal);
    }
}

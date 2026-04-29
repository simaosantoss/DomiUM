package domus.domain.conditions;

import domus.domain.environment.AmbienteInterior;
import java.util.Objects;

/**
 * Condição baseada na luminosidade interior de uma divisão.
 */
public class CondicaoLuminosidade implements Condicao {

    /**
     * Limite usado na avaliação.
     */
    private final double limite;

    /**
     * Indica se a condição testa valores superiores ao limite.
     */
    private final boolean maiorQue;

    /**
     * Cria uma condição de luminosidade.
     *
     * @param limite limite de comparação
     * @param maiorQue {@code true} para testar se a luminosidade é superior ao
     *                 limite; {@code false} para testar se é inferior
     */
    public CondicaoLuminosidade(double limite, boolean maiorQue) {
        this.limite = limite;
        this.maiorQue = maiorQue;
    }

    /**
     * Dá acesso ao limite da condição.
     *
     * @return limite de comparação
     */
    public double getLimite() {
        return this.limite;
    }

    /**
     * Indica o sentido da comparação.
     *
     * @return {@code true} se a condição testar valores superiores ao limite
     */
    public boolean isMaiorQue() {
        return this.maiorQue;
    }

    /**
     * Avalia a luminosidade do ambiente indicado.
     *
     * @param ambiente ambiente interior a avaliar
     * @return {@code true} se a condição se verificar
     */
    @Override
    public boolean avaliar(AmbienteInterior ambiente) {
        if (ambiente == null) {
            return false;
        }

        if (this.maiorQue) {
            return ambiente.getLuminosidade() > this.limite;
        }
        return ambiente.getLuminosidade() < this.limite;
    }

    /**
     * Compara esta condição com outro objeto com base no seu estado.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem a mesma condição lógica
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        CondicaoLuminosidade that = (CondicaoLuminosidade) o;
        return Double.compare(this.limite, that.limite) == 0
                && this.maiorQue == that.maiorQue;
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão da condição
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.limite, this.maiorQue);
    }

    /**
     * Produz uma representação textual legível da condição.
     *
     * @return texto descritivo da condição
     */
    @Override
    public String toString() {
        return "CondicaoLuminosidade{"
                + "limite=" + this.limite
                + ", maiorQue=" + this.maiorQue
                + '}';
    }

    /**
     * Cria uma cópia lógica desta condição.
     *
     * @return nova condição equivalente
     */
    @Override
    public CondicaoLuminosidade clone() {
        return new CondicaoLuminosidade(this.limite, this.maiorQue);
    }
}

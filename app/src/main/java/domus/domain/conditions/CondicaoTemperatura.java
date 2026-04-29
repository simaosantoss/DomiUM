package domus.domain.conditions;

import domus.domain.environment.AmbienteInterior;
import java.util.Objects;

/**
 * Condição baseada na temperatura interior de uma divisão.
 */
public class CondicaoTemperatura implements Condicao {

    /**
     * Limite usado na avaliação.
     */
    private final double limite;

    /**
     * Indica se a condição testa valores superiores ao limite.
     */
    private final boolean maiorQue;

    /**
     * Cria uma condição de temperatura.
     *
     * @param limite limite de comparação
     * @param maiorQue {@code true} para testar se a temperatura é superior ao
     *                 limite; {@code false} para testar se é inferior
     */
    public CondicaoTemperatura(double limite, boolean maiorQue) {
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
     * Avalia a temperatura do ambiente indicado.
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
            return ambiente.getTemperatura() > this.limite;
        }
        return ambiente.getTemperatura() < this.limite;
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
        CondicaoTemperatura that = (CondicaoTemperatura) o;
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
        return "CondicaoTemperatura{"
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
    public CondicaoTemperatura clone() {
        return new CondicaoTemperatura(this.limite, this.maiorQue);
    }
}

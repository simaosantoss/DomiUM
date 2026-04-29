package domus.domain.environment;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa as condições ambientais interiores de uma divisão.
 *
 * O ambiente interior agrega apenas grandezas medidas dentro da divisão,
 * mantendo fora deste conceito quaisquer propriedades exteriores à casa.
 */
public class AmbienteInterior implements Serializable {

    /**
     * Temperatura interior.
     */
    private double temperatura;

    /**
     * Humidade interior.
     */
    private double humidade;

    /**
     * Luminosidade interior.
     */
    private double luminosidade;

    /**
     * Cria um ambiente interior com todos os valores a zero.
     */
    public AmbienteInterior() {
        this(0.0, 0.0, 0.0);
    }

    /**
     * Cria um ambiente interior com os valores indicados.
     *
     * @param temperatura temperatura interior
     * @param humidade humidade interior
     * @param luminosidade luminosidade interior
     */
    public AmbienteInterior(double temperatura, double humidade, double luminosidade) {
        this.temperatura = temperatura;
        this.humidade = humidade;
        this.luminosidade = luminosidade;
    }

    /**
     * Dá acesso à temperatura interior.
     *
     * @return temperatura interior
     */
    public double getTemperatura() {
        return this.temperatura;
    }

    /**
     * Dá acesso à humidade interior.
     *
     * @return humidade interior
     */
    public double getHumidade() {
        return this.humidade;
    }

    /**
     * Dá acesso à luminosidade interior.
     *
     * @return luminosidade interior
     */
    public double getLuminosidade() {
        return this.luminosidade;
    }

    /**
     * Atualiza as condições interiores registadas.
     *
     * @param temperatura nova temperatura interior
     * @param humidade nova humidade interior
     * @param luminosidade nova luminosidade interior
     */
    public void setCondicoes(double temperatura, double humidade, double luminosidade) {
        this.temperatura = temperatura;
        this.humidade = humidade;
        this.luminosidade = luminosidade;
    }

    /**
     * Compara este ambiente com outro objeto com base nos seus valores.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo ambiente lógico
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AmbienteInterior that = (AmbienteInterior) o;
        return Double.compare(this.temperatura, that.temperatura) == 0
                && Double.compare(this.humidade, that.humidade) == 0
                && Double.compare(this.luminosidade, that.luminosidade) == 0;
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do ambiente interior
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.temperatura, this.humidade, this.luminosidade);
    }

    /**
     * Produz uma representação textual legível do ambiente interior.
     *
     * @return texto descritivo do ambiente interior
     */
    @Override
    public String toString() {
        return "AmbienteInterior{"
                + "temperatura=" + this.temperatura
                + ", humidade=" + this.humidade
                + ", luminosidade=" + this.luminosidade
                + '}';
    }

    /**
     * Cria uma cópia independente deste ambiente interior.
     *
     * @return novo ambiente interior com os mesmos valores
     */
    public AmbienteInterior clone() {
        return new AmbienteInterior(this.temperatura, this.humidade, this.luminosidade);
    }
}

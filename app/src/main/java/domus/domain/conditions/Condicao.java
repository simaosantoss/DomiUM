package domus.domain.conditions;

import domus.domain.environment.AmbienteInterior;
import java.io.Serializable;

/**
 * Define uma condição avaliável sobre o ambiente interior de uma divisão.
 */
public interface Condicao extends Serializable {

    /**
     * Avalia a condição sobre o ambiente indicado.
     *
     * @param ambiente ambiente interior a avaliar
     * @return {@code true} se a condição se verificar
     */
    boolean avaliar(AmbienteInterior ambiente);

    /**
     * Cria uma cópia lógica da condição.
     *
     * @return nova condição logicamente equivalente
     */
    Condicao clone();
}

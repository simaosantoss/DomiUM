package domus.domain.commands;

import domus.domain.DomiUM;
import java.io.Serializable;

/**
 * Define uma ação executável sobre a fachada do domínio.
 *
 * Um comando guarda apenas os identificadores necessários para localizar os
 * elementos do domínio e delega a execução na {@link DomiUM}.
 */
public interface Command extends Serializable {

    /**
     * Executa o comando sobre a fachada indicada.
     *
     * @param domium fachada do domínio sobre a qual a ação deve ser executada
     */
    void execute(DomiUM domium);

    /**
     * Cria uma cópia lógica do comando.
     *
     * @return novo comando com o mesmo estado lógico
     */
    Command clone();
}

package domus;

import domus.controller.DomiUMController;
import domus.domain.DomiUM;
import domus.ui.ConsoleView;

/**
 * Ponto de entrada da aplicação DomusControl em modo consola.
 */
public final class Main {

    /**
     * Construtor privado para impedir instanciação da classe principal.
     */
    private Main() {
    }

    /**
     * Inicia a aplicação, criando o model, a view e o controller principal.
     *
     * @param args argumentos da linha de comandos
     */
    public static void main(String[] args) {
        DomiUM model = new DomiUM();
        ConsoleView view = new ConsoleView();
        DomiUMController controller = new DomiUMController(model, view);
        controller.iniciar();
    }
}

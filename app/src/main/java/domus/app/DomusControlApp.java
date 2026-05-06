package domus.app;

import domus.controller.DomiUMController;
import domus.domain.DomiUM;
import domus.ui.ConsoleView;

/**
 * Classe principal da aplicação DomusControl em modo consola.
 */
public final class DomusControlApp {

    /**
     * Construtor privado para impedir instanciação da classe principal.
     */
    private DomusControlApp() {
    }

    /**
     * Ponto de entrada da aplicação.
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

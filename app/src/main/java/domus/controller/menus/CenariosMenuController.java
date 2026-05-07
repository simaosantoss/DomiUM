package domus.controller.menus;

import domus.domain.DomiUM;
import domus.domain.commands.ComandoDesligar;
import domus.domain.commands.ComandoLigar;
import domus.ui.ConsoleView;

/**
 * Controller do submenu de cenários da aplicação de consola.
 */
public class CenariosMenuController {

    /**
     * Fachada pública do domínio.
     */
    private final DomiUM model;

    /**
     * View de consola.
     */
    private final ConsoleView view;

    /**
     * Cria um controller para o submenu de cenários.
     *
     * @param model fachada do domínio
     * @param view view de consola
     */
    public CenariosMenuController(DomiUM model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Executa o ciclo do submenu de cenários.
     */
    public void executar() {
        boolean voltar = false;
        while (!voltar) {
            this.view.mostrarMenuCenarios();
            int opcao = this.view.lerOpcao();
            switch (opcao) {
                case 1:
                    criarCenario();
                    break;
                case 2:
                    adicionarComandoLigarACenario();
                    break;
                case 3:
                    adicionarComandoDesligarACenario();
                    break;
                case 4:
                    executarCenario();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    this.view.mostrarErro("Opção desconhecida.");
                    break;
            }
        }
    }

    /**
     * Cria um cenário numa casa.
     */
    private void criarCenario() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String cenarioId = this.view.lerTexto("Identificador do cenário: ");
        String nome = this.view.lerTexto("Nome do cenário: ");

        this.model.criarCenario(utilizadorId, casaId, cenarioId, nome);
        this.view.mostrarMensagem("Cenário criado.");
    }

    /**
     * Adiciona um comando de ligar a um cenário.
     */
    private void adicionarComandoLigarACenario() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String cenarioId = this.view.lerTexto("Identificador do cenário: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        this.model.adicionarComandoACenario(
                utilizadorId, casaId, cenarioId,
                new ComandoLigar(utilizadorId, casaId, dispositivoId)
        );
        this.view.mostrarMensagem("Comando adicionado ao cenário.");
    }

    /**
     * Adiciona um comando de desligar a um cenário.
     */
    private void adicionarComandoDesligarACenario() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String cenarioId = this.view.lerTexto("Identificador do cenário: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        this.model.adicionarComandoACenario(
                utilizadorId, casaId, cenarioId,
                new ComandoDesligar(utilizadorId, casaId, dispositivoId)
        );
        this.view.mostrarMensagem("Comando adicionado ao cenário.");
    }

    /**
     * Executa um cenário.
     */
    private void executarCenario() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String cenarioId = this.view.lerTexto("Identificador do cenário: ");

        this.model.executarCenario(utilizadorId, casaId, cenarioId);
        this.view.mostrarMensagem("Cenário executado.");
    }
}

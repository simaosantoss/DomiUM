package domus.controller.menus;

import domus.domain.DomiUM;
import domus.domain.commands.Command;
import domus.domain.scenarios.Cenario;
import domus.ui.ConsoleView;
import java.util.Iterator;

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
                    adicionarComandoACenario();
                    break;
                case 3:
                    executarCenario();
                    break;
                case 4:
                    listarCenarios();
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

        try {
            this.model.criarCenario(utilizadorId, casaId, cenarioId, nome);
            this.view.mostrarMensagem("Cenário criado.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.CenarioJaExisteException e) {
            this.view.mostrarErro("Já existe um cenário com o identificador \"" + e.getCenarioId() + "\".");
        }
    }

    /**
     * Adiciona um comando de dispositivo a um cenário.
     */
    private void adicionarComandoACenario() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String cenarioId = this.view.lerTexto("Identificador do cenário: ");
        Command comando = new ComandoDispositivoMenuHelper(this.view)
                .lerComandoDispositivo(utilizadorId, casaId);
        if (comando == null) {
            this.view.mostrarMensagem("Operação cancelada.");
            return;
        }

        try {
            this.model.adicionarComandoACenario(utilizadorId, casaId, cenarioId, comando);
            this.view.mostrarMensagem("Comando adicionado ao cenário.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.CenarioNaoExisteException e) {
            this.view.mostrarErro("Cenário \"" + e.getCenarioId() + "\" não existe.");
        } catch (domus.domain.exceptions.DispositivoNaoExisteException e) {
            this.view.mostrarErro("Dispositivo \"" + e.getDispositivoId()
                    + "\" não existe na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.OperacaoInvalidaException e) {
            this.view.mostrarErro(e.getMessage());
        } catch (domus.domain.exceptions.DomusException e) {
            this.view.mostrarErro(e.getMessage());
        }
    }

    /**
     * Executa um cenário.
     */
    private void executarCenario() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String cenarioId = this.view.lerTexto("Identificador do cenário: ");

        try {
            this.model.executarCenario(utilizadorId, casaId, cenarioId);
            this.view.mostrarMensagem("Cenário executado.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.CenarioNaoExisteException e) {
            this.view.mostrarErro("Cenário \"" + e.getCenarioId() + "\" não existe.");
        }
    }

    /**
     * Lista os cenários registados numa casa.
     */
    private void listarCenarios() {
        String casaId = this.view.lerTexto("Identificador da casa: ");
        Iterator<Cenario> iterador = this.model.getIteradorCenarios(casaId);
        boolean encontrou = false;

        while (iterador.hasNext()) {
            encontrou = true;
            this.view.mostrarMensagem(iterador.next().toString());
        }

        if (!encontrou) {
            this.view.mostrarMensagem("Não existem cenários registados na casa indicada.");
        }
    }
}

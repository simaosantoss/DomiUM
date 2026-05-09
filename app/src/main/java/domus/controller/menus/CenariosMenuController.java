package domus.controller.menus;

import domus.domain.DomiUM;
import domus.domain.commands.ComandoDesligar;
import domus.domain.commands.ComandoLigar;
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
                    adicionarComandoLigarACenario();
                    break;
                case 3:
                    adicionarComandoDesligarACenario();
                    break;
                case 4:
                    executarCenario();
                    break;
                case 5:
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
     * Adiciona um comando de ligar a um cenário.
     */
    private void adicionarComandoLigarACenario() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String cenarioId = this.view.lerTexto("Identificador do cenário: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        try {
            this.model.adicionarComandoACenario(
                    utilizadorId, casaId, cenarioId,
                    new ComandoLigar(utilizadorId, casaId, dispositivoId)
            );
            this.view.mostrarMensagem("Comando adicionado ao cenário.");
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
     * Adiciona um comando de desligar a um cenário.
     */
    private void adicionarComandoDesligarACenario() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String cenarioId = this.view.lerTexto("Identificador do cenário: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        try {
            this.model.adicionarComandoACenario(
                    utilizadorId, casaId, cenarioId,
                    new ComandoDesligar(utilizadorId, casaId, dispositivoId)
            );
            this.view.mostrarMensagem("Comando adicionado ao cenário.");
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

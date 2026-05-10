package domus.controller.menus;

import domus.domain.automation.Automacao;
import domus.domain.DomiUM;
import domus.domain.commands.Command;
import domus.domain.conditions.CondicaoHumidade;
import domus.domain.conditions.CondicaoLuminosidade;
import domus.domain.conditions.CondicaoTemperatura;
import domus.ui.ConsoleView;
import java.util.Iterator;

/**
 * Controller do submenu de automações da aplicação de consola.
 */
public class AutomacoesMenuController {

    /**
     * Fachada pública do domínio.
     */
    private final DomiUM model;

    /**
     * View de consola.
     */
    private final ConsoleView view;

    /**
     * Cria um controller para o submenu de automações.
     *
     * @param model fachada do domínio
     * @param view view de consola
     */
    public AutomacoesMenuController(DomiUM model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Executa o ciclo do submenu de automações.
     */
    public void executar() {
        boolean voltar = false;
        while (!voltar) {
            this.view.mostrarMenuAutomacoes();
            int opcao = this.view.lerOpcao();
            switch (opcao) {
                case 1:
                    criarAutomacaoTemperatura();
                    break;
                case 2:
                    criarAutomacaoHumidade();
                    break;
                case 3:
                    criarAutomacaoLuminosidade();
                    break;
                case 4:
                    adicionarAcaoAAutomacao();
                    break;
                case 5:
                    listarAutomacoes();
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
     * Cria uma automação por temperatura.
     */
    private void criarAutomacaoTemperatura() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String automacaoId = this.view.lerTexto("Identificador da automação: ");
        String nome = this.view.lerTexto("Nome da automação: ");
        String divisaoNome = this.view.lerTexto("Nome da divisão: ");
        double limite = this.view.lerDouble("Limite de temperatura: ");
        boolean maiorQue = lerMaiorQue();

        try {
            this.model.criarAutomacao(
                    utilizadorId, casaId, automacaoId, nome, divisaoNome,
                    new CondicaoTemperatura(limite, maiorQue)
            );
            this.view.mostrarMensagem("Automação criada.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.DivisaoNaoExisteException e) {
            this.view.mostrarErro("Divisão \"" + e.getDivisaoNome() + "\" não existe.");
        } catch (domus.domain.exceptions.AutomacaoJaExisteException e) {
            this.view.mostrarErro("Já existe uma automação com o identificador \"" + e.getAutomacaoId() + "\".");
        }
    }

    /**
     * Cria uma automação por humidade.
     */
    private void criarAutomacaoHumidade() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String automacaoId = this.view.lerTexto("Identificador da automação: ");
        String nome = this.view.lerTexto("Nome da automação: ");
        String divisaoNome = this.view.lerTexto("Nome da divisão: ");
        double limite = this.view.lerDouble("Limite de humidade: ");
        boolean maiorQue = lerMaiorQue();

        try {
            this.model.criarAutomacao(
                    utilizadorId, casaId, automacaoId, nome, divisaoNome,
                    new CondicaoHumidade(limite, maiorQue)
            );
            this.view.mostrarMensagem("Automação criada.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.DivisaoNaoExisteException e) {
            this.view.mostrarErro("Divisão \"" + e.getDivisaoNome() + "\" não existe.");
        } catch (domus.domain.exceptions.AutomacaoJaExisteException e) {
            this.view.mostrarErro("Já existe uma automação com o identificador \"" + e.getAutomacaoId() + "\".");
        }
    }

    /**
     * Cria uma automação por luminosidade.
     */
    private void criarAutomacaoLuminosidade() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String automacaoId = this.view.lerTexto("Identificador da automação: ");
        String nome = this.view.lerTexto("Nome da automação: ");
        String divisaoNome = this.view.lerTexto("Nome da divisão: ");
        double limite = this.view.lerDouble("Limite de luminosidade: ");
        boolean maiorQue = lerMaiorQue();

        try {
            this.model.criarAutomacao(
                    utilizadorId, casaId, automacaoId, nome, divisaoNome,
                    new CondicaoLuminosidade(limite, maiorQue)
            );
            this.view.mostrarMensagem("Automação criada.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.DivisaoNaoExisteException e) {
            this.view.mostrarErro("Divisão \"" + e.getDivisaoNome() + "\" não existe.");
        } catch (domus.domain.exceptions.AutomacaoJaExisteException e) {
            this.view.mostrarErro("Já existe uma automação com o identificador \"" + e.getAutomacaoId() + "\".");
        }
    }

    /**
     * Adiciona uma ação de dispositivo a uma automação.
     */
    private void adicionarAcaoAAutomacao() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String automacaoId = this.view.lerTexto("Identificador da automação: ");
        Command comando = new ComandoDispositivoMenuHelper(this.view)
                .lerComandoDispositivo(utilizadorId, casaId);
        if (comando == null) {
            this.view.mostrarMensagem("Operação cancelada.");
            return;
        }

        try {
            this.model.adicionarAcaoAAutomacao(utilizadorId, casaId, automacaoId, comando);
            this.view.mostrarMensagem("Ação adicionada à automação.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.AutomacaoNaoExisteException e) {
            this.view.mostrarErro("Automação \"" + e.getAutomacaoId() + "\" não existe.");
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
     * Lista as automações registadas numa casa.
     */
    private void listarAutomacoes() {
        String casaId = this.view.lerTexto("Identificador da casa: ");
        Iterator<Automacao> iterador = this.model.getIteradorAutomacoes(casaId);
        boolean encontrou = false;

        while (iterador.hasNext()) {
            encontrou = true;
            this.view.mostrarMensagem(iterador.next().toString());
        }

        if (!encontrou) {
            this.view.mostrarMensagem("Não existem automações registadas na casa indicada.");
        }
    }

    /**
     * Lê o sentido de comparação de uma condição.
     *
     * @return {@code true} se a condição for maior que o limite
     */
    private boolean lerMaiorQue() {
        while (true) {
            String texto = this.view.lerTexto("A condição é maior que o limite? (s/n): ");
            if ("s".equalsIgnoreCase(texto) || "sim".equalsIgnoreCase(texto)) {
                return true;
            }
            if ("n".equalsIgnoreCase(texto) || "nao".equalsIgnoreCase(texto)
                    || "não".equalsIgnoreCase(texto)) {
                return false;
            }
            this.view.mostrarErro("Resposta inválida. Introduza s ou n.");
        }
    }
}

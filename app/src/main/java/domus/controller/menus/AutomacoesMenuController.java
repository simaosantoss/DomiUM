package domus.controller.menus;

import domus.domain.DomiUM;
import domus.domain.commands.ComandoDesligar;
import domus.domain.commands.ComandoLigar;
import domus.domain.conditions.CondicaoHumidade;
import domus.domain.conditions.CondicaoLuminosidade;
import domus.domain.conditions.CondicaoTemperatura;
import domus.ui.ConsoleView;

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
                    adicionarAcaoLigarAAutomacao();
                    break;
                case 5:
                    adicionarAcaoDesligarAAutomacao();
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

        this.model.criarAutomacao(
                utilizadorId, casaId, automacaoId, nome, divisaoNome,
                new CondicaoTemperatura(limite, maiorQue)
        );
        this.view.mostrarMensagem("Automação criada.");
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

        this.model.criarAutomacao(
                utilizadorId, casaId, automacaoId, nome, divisaoNome,
                new CondicaoHumidade(limite, maiorQue)
        );
        this.view.mostrarMensagem("Automação criada.");
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

        this.model.criarAutomacao(
                utilizadorId, casaId, automacaoId, nome, divisaoNome,
                new CondicaoLuminosidade(limite, maiorQue)
        );
        this.view.mostrarMensagem("Automação criada.");
    }

    /**
     * Adiciona uma ação de ligar a uma automação.
     */
    private void adicionarAcaoLigarAAutomacao() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String automacaoId = this.view.lerTexto("Identificador da automação: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        this.model.adicionarAcaoAAutomacao(
                utilizadorId, casaId, automacaoId,
                new ComandoLigar(utilizadorId, casaId, dispositivoId)
        );
        this.view.mostrarMensagem("Ação adicionada à automação.");
    }

    /**
     * Adiciona uma ação de desligar a uma automação.
     */
    private void adicionarAcaoDesligarAAutomacao() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String automacaoId = this.view.lerTexto("Identificador da automação: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        this.model.adicionarAcaoAAutomacao(
                utilizadorId, casaId, automacaoId,
                new ComandoDesligar(utilizadorId, casaId, dispositivoId)
        );
        this.view.mostrarMensagem("Ação adicionada à automação.");
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

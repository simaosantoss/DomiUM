package domus.controller.menus;

import domus.domain.DomiUM;
import domus.domain.suggestions.SugestaoEscalonamento;
import domus.ui.ConsoleView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Controller do submenu de sugestões da aplicação de consola.
 */
public class SugestoesMenuController {

    /**
     * Fachada pública do domínio.
     */
    private final DomiUM model;

    /**
     * View de consola.
     */
    private final ConsoleView view;

    /**
     * Cria um controller para o submenu de sugestões.
     *
     * @param model fachada do domínio
     * @param view view de consola
     */
    public SugestoesMenuController(DomiUM model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Executa o ciclo do submenu de sugestões.
     */
    public void executar() {
        boolean voltar = false;
        while (!voltar) {
            this.view.mostrarMenuSugestoes();
            int opcao = this.view.lerOpcao();
            switch (opcao) {
                case 1:
                    listarSugestoesEscalonamento();
                    break;
                case 2:
                    aceitarSugestaoEscalonamento();
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
     * Lista as sugestões de escalonamento de um utilizador.
     */
    private void listarSugestoesEscalonamento() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        Iterator<SugestaoEscalonamento> iterador = this.model.getSugestoesEscalonamento(utilizadorId);
        boolean encontrou = false;
        int numero = 1;

        while (iterador.hasNext()) {
            encontrou = true;
            this.view.mostrarMensagem(numero + ". " + iterador.next().toString());
            numero++;
        }

        if (!encontrou) {
            this.view.mostrarMensagem("Não existem sugestões de escalonamento.");
        }
    }

    /**
     * Aceita uma sugestão de escalonamento de um utilizador.
     */
    private void aceitarSugestaoEscalonamento() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        List<SugestaoEscalonamento> sugestoes = obterSugestoesEscalonamento(utilizadorId);

        if (sugestoes.isEmpty()) {
            this.view.mostrarMensagem("Não existem sugestões de escalonamento.");
            return;
        }

        for (int i = 0; i < sugestoes.size(); i++) {
            this.view.mostrarMensagem((i + 1) + ". " + sugestoes.get(i).toString());
        }

        int escolha = this.view.lerInteiro("Número da sugestão a aceitar: ");
        if (escolha < 1 || escolha > sugestoes.size()) {
            this.view.mostrarErro("Sugestão inválida.");
            return;
        }

        String escalonamentoId = this.view.lerTexto("Identificador do escalonamento: ");
        String nome = this.view.lerTexto("Nome do escalonamento: ");

        try {
            this.model.aceitarSugestaoEscalonamento(
                    utilizadorId, escalonamentoId, nome, sugestoes.get(escolha - 1)
            );
            this.view.mostrarMensagem("Sugestão aceite.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão na casa da sugestão.");
        } catch (domus.domain.exceptions.OperacaoInvalidaException e) {
            this.view.mostrarErro(e.getMessage());
        }
    }

    /**
     * Obtém as sugestões de escalonamento de um utilizador para uma lista local.
     *
     * @param utilizadorId identificador do utilizador
     * @return lista local de sugestões
     */
    private List<SugestaoEscalonamento> obterSugestoesEscalonamento(String utilizadorId) {
        List<SugestaoEscalonamento> sugestoes = new ArrayList<SugestaoEscalonamento>();
        Iterator<SugestaoEscalonamento> iterador = this.model.getSugestoesEscalonamento(utilizadorId);

        while (iterador.hasNext()) {
            sugestoes.add(iterador.next());
        }

        return sugestoes;
    }
}

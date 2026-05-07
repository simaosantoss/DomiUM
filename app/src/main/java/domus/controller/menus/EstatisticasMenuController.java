package domus.controller.menus;

import domus.domain.DomiUM;
import domus.domain.statistics.ResumoCasaConsumo;
import domus.domain.statistics.ResumoDispositivoUso;
import domus.domain.statistics.ResumoDivisaoDispositivos;
import domus.ui.ConsoleView;
import java.util.Iterator;

/**
 * Controller do submenu de estatísticas da aplicação de consola.
 */
public class EstatisticasMenuController {

    /**
     * Fachada pública do domínio.
     */
    private final DomiUM model;

    /**
     * View de consola.
     */
    private final ConsoleView view;

    /**
     * Cria um controller para o submenu de estatísticas.
     *
     * @param model fachada do domínio
     * @param view view de consola
     */
    public EstatisticasMenuController(DomiUM model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Executa o ciclo do submenu de estatísticas.
     */
    public void executar() {
        boolean voltar = false;
        while (!voltar) {
            this.view.mostrarMenuEstatisticas();
            int opcao = this.view.lerOpcao();
            switch (opcao) {
                case 1:
                    mostrarCasaMaiorConsumo();
                    break;
                case 2:
                    mostrarTopDispositivosPorTempo();
                    break;
                case 3:
                    mostrarTopDispositivosPorAtivacoes();
                    break;
                case 4:
                    mostrarTopDivisoesComMaisDispositivos();
                    break;
                case 5:
                    mostrarResumoConsumoCasas();
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
     * Mostra a casa com maior consumo total.
     */
    private void mostrarCasaMaiorConsumo() {
        ResumoCasaConsumo resumo = this.model.getCasaMaiorConsumo();
        if (resumo == null) {
            this.view.mostrarMensagem("Não existem casas registadas.");
            return;
        }

        this.view.mostrarMensagem(resumo.toString());
    }

    /**
     * Mostra os dispositivos mais utilizados por tempo ligado numa casa.
     */
    private void mostrarTopDispositivosPorTempo() {
        String casaId = this.view.lerTexto("Identificador da casa: ");
        Iterator<ResumoDispositivoUso> iterador = this.model.getTop3DispositivosPorTempo(casaId);
        boolean encontrou = false;

        while (iterador.hasNext()) {
            encontrou = true;
            this.view.mostrarMensagem(iterador.next().toString());
        }

        if (!encontrou) {
            this.view.mostrarMensagem("Não existem resultados para a casa indicada.");
        }
    }

    /**
     * Mostra os dispositivos mais utilizados por número de ativações numa casa.
     */
    private void mostrarTopDispositivosPorAtivacoes() {
        String casaId = this.view.lerTexto("Identificador da casa: ");
        Iterator<ResumoDispositivoUso> iterador = this.model.getTop3DispositivosPorAtivacoes(casaId);
        boolean encontrou = false;

        while (iterador.hasNext()) {
            encontrou = true;
            this.view.mostrarMensagem(iterador.next().toString());
        }

        if (!encontrou) {
            this.view.mostrarMensagem("Não existem resultados para a casa indicada.");
        }
    }

    /**
     * Mostra as divisões com mais dispositivos.
     */
    private void mostrarTopDivisoesComMaisDispositivos() {
        Iterator<ResumoDivisaoDispositivos> iterador = this.model.getTop3DivisoesComMaisDispositivos();
        boolean encontrou = false;

        while (iterador.hasNext()) {
            encontrou = true;
            this.view.mostrarMensagem(iterador.next().toString());
        }

        if (!encontrou) {
            this.view.mostrarMensagem("Não existem divisões registadas.");
        }
    }

    /**
     * Mostra o resumo textual de consumo das casas.
     */
    private void mostrarResumoConsumoCasas() {
        this.view.mostrarMensagem(this.model.getResumoConsumoCasas());
    }
}

package domus.controller.menus;

import domus.domain.DomiUM;
import domus.domain.commands.ComandoDesligar;
import domus.domain.commands.ComandoLigar;
import domus.domain.commands.Command;
import domus.domain.exceptions.CasaNaoExisteException;
import domus.domain.exceptions.DispositivoNaoExisteException;
import domus.domain.exceptions.DomusException;
import domus.domain.exceptions.OperacaoInvalidaException;
import domus.domain.exceptions.SemPermissaoException;
import domus.domain.exceptions.UtilizadorNaoExisteException;
import domus.ui.ConsoleView;

/**
 * Controller do submenu de operações sobre dispositivos da aplicação de consola.
 */
public class DispositivosMenuController {

    /**
     * Fachada pública do domínio.
     */
    private final DomiUM model;

    /**
     * View de consola.
     */
    private final ConsoleView view;

    /**
     * Cria um controller para o submenu de dispositivos.
     *
     * @param model fachada do domínio
     * @param view view de consola
     */
    public DispositivosMenuController(DomiUM model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Executa o ciclo do submenu de dispositivos.
     */
    public void executar() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        Command comando = new ComandoDispositivoMenuHelper(this.view)
                .lerComandoDispositivo(utilizadorId, casaId);

        if (comando == null) {
            this.view.mostrarMensagem("Operação cancelada.");
            return;
        }

        executarComando(comando, "Operação executada.");
    }

    /**
     * Liga um dispositivo.
     */
    public void ligarDispositivo() {
        DadosDispositivo dados = lerDadosDispositivo();
        executarComando(
                new ComandoLigar(dados.utilizadorId, dados.casaId, dados.dispositivoId),
                "Dispositivo ligado."
        );
    }

    /**
     * Desliga um dispositivo.
     */
    public void desligarDispositivo() {
        DadosDispositivo dados = lerDadosDispositivo();
        executarComando(
                new ComandoDesligar(dados.utilizadorId, dados.casaId, dados.dispositivoId),
                "Dispositivo desligado."
        );
    }

    /**
     * Lê os identificadores comuns a uma operação sobre dispositivo.
     *
     * @return dados comuns da operação
     */
    private DadosDispositivo lerDadosDispositivo() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");
        return new DadosDispositivo(utilizadorId, casaId, dispositivoId);
    }

    /**
     * Executa um comando validado e apresenta o resultado na view.
     *
     * @param comando comando a executar
     * @param mensagemSucesso mensagem apresentada em caso de sucesso
     */
    private void executarComando(Command comando, String mensagemSucesso) {
        try {
            this.model.executarComandoValidado(comando);
            this.view.mostrarMensagem(mensagemSucesso);
        } catch (UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão na casa \"" + e.getCasaId() + "\".");
        } catch (DispositivoNaoExisteException e) {
            this.view.mostrarErro("Dispositivo \"" + e.getDispositivoId()
                    + "\" não existe na casa \"" + e.getCasaId() + "\".");
        } catch (OperacaoInvalidaException e) {
            this.view.mostrarErro(e.getMessage());
        } catch (DomusException e) {
            this.view.mostrarErro(e.getMessage());
        }
    }

    /**
     * Agrupa os identificadores comuns a comandos sobre dispositivos.
     */
    private static final class DadosDispositivo {

        /**
         * Identificador do utilizador.
         */
        private final String utilizadorId;

        /**
         * Identificador da casa.
         */
        private final String casaId;

        /**
         * Identificador do dispositivo.
         */
        private final String dispositivoId;

        /**
         * Cria um agrupamento de identificadores de dispositivo.
         *
         * @param utilizadorId identificador do utilizador
         * @param casaId identificador da casa
         * @param dispositivoId identificador do dispositivo
         */
        private DadosDispositivo(String utilizadorId, String casaId, String dispositivoId) {
            this.utilizadorId = utilizadorId;
            this.casaId = casaId;
            this.dispositivoId = dispositivoId;
        }
    }
}

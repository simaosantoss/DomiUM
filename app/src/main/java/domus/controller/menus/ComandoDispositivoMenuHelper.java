package domus.controller.menus;

import domus.domain.commands.ComandoAbrirPortao;
import domus.domain.commands.ComandoDefinirAberturaCortina;
import domus.domain.commands.ComandoDefinirCorLampada;
import domus.domain.commands.ComandoDefinirHumidadeDesumidificador;
import domus.domain.commands.ComandoDefinirIntensidadeLampada;
import domus.domain.commands.ComandoDefinirModoArCondicionado;
import domus.domain.commands.ComandoDefinirPlaylistColuna;
import domus.domain.commands.ComandoDefinirTemperaturaArCondicionado;
import domus.domain.commands.ComandoDefinirVolumeColuna;
import domus.domain.commands.ComandoDesligar;
import domus.domain.commands.ComandoDestrancarFechadura;
import domus.domain.commands.ComandoFecharPortao;
import domus.domain.commands.ComandoLigar;
import domus.domain.commands.ComandoTrancarFechadura;
import domus.domain.commands.Command;
import domus.ui.ConsoleView;

/**
 * Helper reutilizável para construir comandos de dispositivo a partir da
 * interação textual com o utilizador.
 *
 * Esta classe apenas recolhe dados e instancia comandos. A validação de
 * permissões, existência de entidades e compatibilidade do comando com o tipo
 * de dispositivo continua a pertencer ao domínio.
 */
public class ComandoDispositivoMenuHelper {

    /**
     * View usada para apresentar mensagens e ler dados.
     */
    private final ConsoleView view;

    /**
     * Cria o helper de construção de comandos.
     *
     * @param view view de consola
     */
    public ComandoDispositivoMenuHelper(ConsoleView view) {
        this.view = view;
    }

    /**
     * Lê a ação pretendida e constrói o comando de dispositivo correspondente.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @return comando construído, ou {@code null} se a operação for cancelada ou
     *         se a opção escolhida for inválida
     */
    public Command lerComandoDispositivo(String utilizadorId, String casaId) {
        mostrarMenuComandos();
        int opcao = this.view.lerOpcao();
        if (opcao == 0) {
            return null;
        }

        if (opcao < 1 || opcao > 14) {
            this.view.mostrarErro("Opção desconhecida.");
            return null;
        }

        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");
        switch (opcao) {
            case 1:
                return new ComandoLigar(utilizadorId, casaId, dispositivoId);
            case 2:
                return new ComandoDesligar(utilizadorId, casaId, dispositivoId);
            case 3:
                return new ComandoDefinirIntensidadeLampada(
                        utilizadorId, casaId, dispositivoId,
                        this.view.lerInteiro("Intensidade: ")
                );
            case 4:
                return new ComandoDefinirCorLampada(
                        utilizadorId, casaId, dispositivoId,
                        this.view.lerInteiro("Cor em Kelvin: ")
                );
            case 5:
                return new ComandoDefinirVolumeColuna(
                        utilizadorId, casaId, dispositivoId,
                        this.view.lerInteiro("Volume: ")
                );
            case 6:
                return new ComandoDefinirPlaylistColuna(
                        utilizadorId, casaId, dispositivoId,
                        this.view.lerTexto("Playlist: ")
                );
            case 7:
                return new ComandoDefinirAberturaCortina(
                        utilizadorId, casaId, dispositivoId,
                        this.view.lerInteiro("Percentagem de abertura: ")
                );
            case 8:
                return new ComandoDefinirTemperaturaArCondicionado(
                        utilizadorId, casaId, dispositivoId,
                        this.view.lerDouble("Temperatura alvo: ")
                );
            case 9:
                return new ComandoDefinirModoArCondicionado(
                        utilizadorId, casaId, dispositivoId,
                        this.view.lerTexto("Modo: ")
                );
            case 10:
                return new ComandoDefinirHumidadeDesumidificador(
                        utilizadorId, casaId, dispositivoId,
                        this.view.lerDouble("Humidade alvo: ")
                );
            case 11:
                return new ComandoAbrirPortao(utilizadorId, casaId, dispositivoId);
            case 12:
                return new ComandoFecharPortao(utilizadorId, casaId, dispositivoId);
            case 13:
                return new ComandoTrancarFechadura(utilizadorId, casaId, dispositivoId);
            case 14:
                return new ComandoDestrancarFechadura(utilizadorId, casaId, dispositivoId);
            default:
                this.view.mostrarErro("Opção desconhecida.");
                return null;
        }
    }

    /**
     * Apresenta o menu de comandos de dispositivo disponíveis.
     */
    private void mostrarMenuComandos() {
        this.view.mostrarMensagem("");
        this.view.mostrarMensagem("=== Comandos de dispositivo ===");
        this.view.mostrarMensagem("1. Ligar dispositivo");
        this.view.mostrarMensagem("2. Desligar dispositivo");
        this.view.mostrarMensagem("3. Definir intensidade da lâmpada");
        this.view.mostrarMensagem("4. Definir cor da lâmpada");
        this.view.mostrarMensagem("5. Definir volume da coluna");
        this.view.mostrarMensagem("6. Definir playlist da coluna");
        this.view.mostrarMensagem("7. Definir abertura da cortina");
        this.view.mostrarMensagem("8. Definir temperatura do ar condicionado");
        this.view.mostrarMensagem("9. Definir modo do ar condicionado");
        this.view.mostrarMensagem("10. Definir humidade alvo do desumidificador");
        this.view.mostrarMensagem("11. Abrir portão");
        this.view.mostrarMensagem("12. Fechar portão");
        this.view.mostrarMensagem("13. Trancar fechadura");
        this.view.mostrarMensagem("14. Destrancar fechadura");
        this.view.mostrarMensagem("0. Cancelar");
    }
}

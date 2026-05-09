package domus.controller.menus;

import domus.domain.DomiUM;
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
import domus.domain.exceptions.DomusException;
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
        boolean voltar = false;
        while (!voltar) {
            this.view.mostrarMenuDispositivos();
            int opcao = this.view.lerOpcao();
            switch (opcao) {
                case 1:
                    ligarDispositivo();
                    break;
                case 2:
                    desligarDispositivo();
                    break;
                case 3:
                    definirIntensidadeLampada();
                    break;
                case 4:
                    definirCorLampada();
                    break;
                case 5:
                    definirVolumeColuna();
                    break;
                case 6:
                    definirPlaylistColuna();
                    break;
                case 7:
                    definirAberturaCortina();
                    break;
                case 8:
                    definirTemperaturaArCondicionado();
                    break;
                case 9:
                    definirModoArCondicionado();
                    break;
                case 10:
                    definirHumidadeDesumidificador();
                    break;
                case 11:
                    abrirPortao();
                    break;
                case 12:
                    fecharPortao();
                    break;
                case 13:
                    trancarFechadura();
                    break;
                case 14:
                    destrancarFechadura();
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
     * Define a intensidade de uma lâmpada.
     */
    private void definirIntensidadeLampada() {
        DadosDispositivo dados = lerDadosDispositivo();
        int intensidade = this.view.lerInteiro("Intensidade: ");
        executarComando(
                new ComandoDefinirIntensidadeLampada(
                        dados.utilizadorId, dados.casaId, dados.dispositivoId, intensidade
                ),
                "Intensidade da lâmpada atualizada."
        );
    }

    /**
     * Define a cor de uma lâmpada.
     */
    private void definirCorLampada() {
        DadosDispositivo dados = lerDadosDispositivo();
        int corK = this.view.lerInteiro("Cor em Kelvin: ");
        executarComando(
                new ComandoDefinirCorLampada(dados.utilizadorId, dados.casaId, dados.dispositivoId, corK),
                "Cor da lâmpada atualizada."
        );
    }

    /**
     * Define o volume de uma coluna.
     */
    private void definirVolumeColuna() {
        DadosDispositivo dados = lerDadosDispositivo();
        int volume = this.view.lerInteiro("Volume: ");
        executarComando(
                new ComandoDefinirVolumeColuna(dados.utilizadorId, dados.casaId, dados.dispositivoId, volume),
                "Volume da coluna atualizado."
        );
    }

    /**
     * Define a playlist de uma coluna.
     */
    private void definirPlaylistColuna() {
        DadosDispositivo dados = lerDadosDispositivo();
        String playlist = this.view.lerTexto("Playlist: ");
        executarComando(
                new ComandoDefinirPlaylistColuna(
                        dados.utilizadorId, dados.casaId, dados.dispositivoId, playlist
                ),
                "Playlist da coluna atualizada."
        );
    }

    /**
     * Define a percentagem de abertura de uma cortina.
     */
    private void definirAberturaCortina() {
        DadosDispositivo dados = lerDadosDispositivo();
        int percentagemAbertura = this.view.lerInteiro("Percentagem de abertura: ");
        executarComando(
                new ComandoDefinirAberturaCortina(
                        dados.utilizadorId, dados.casaId, dados.dispositivoId, percentagemAbertura
                ),
                "Abertura da cortina atualizada."
        );
    }

    /**
     * Define a temperatura alvo de um ar condicionado.
     */
    private void definirTemperaturaArCondicionado() {
        DadosDispositivo dados = lerDadosDispositivo();
        double temperatura = this.view.lerDouble("Temperatura alvo: ");
        executarComando(
                new ComandoDefinirTemperaturaArCondicionado(
                        dados.utilizadorId, dados.casaId, dados.dispositivoId, temperatura
                ),
                "Temperatura do ar condicionado atualizada."
        );
    }

    /**
     * Define o modo de funcionamento de um ar condicionado.
     */
    private void definirModoArCondicionado() {
        DadosDispositivo dados = lerDadosDispositivo();
        String modo = this.view.lerTexto("Modo: ");
        executarComando(
                new ComandoDefinirModoArCondicionado(
                        dados.utilizadorId, dados.casaId, dados.dispositivoId, modo
                ),
                "Modo do ar condicionado atualizado."
        );
    }

    /**
     * Define a humidade alvo de um desumidificador.
     */
    private void definirHumidadeDesumidificador() {
        DadosDispositivo dados = lerDadosDispositivo();
        double humidade = this.view.lerDouble("Humidade alvo: ");
        executarComando(
                new ComandoDefinirHumidadeDesumidificador(
                        dados.utilizadorId, dados.casaId, dados.dispositivoId, humidade
                ),
                "Humidade alvo do desumidificador atualizada."
        );
    }

    /**
     * Abre um portão.
     */
    private void abrirPortao() {
        DadosDispositivo dados = lerDadosDispositivo();
        executarComando(
                new ComandoAbrirPortao(dados.utilizadorId, dados.casaId, dados.dispositivoId),
                "Portão aberto."
        );
    }

    /**
     * Fecha um portão.
     */
    private void fecharPortao() {
        DadosDispositivo dados = lerDadosDispositivo();
        executarComando(
                new ComandoFecharPortao(dados.utilizadorId, dados.casaId, dados.dispositivoId),
                "Portão fechado."
        );
    }

    /**
     * Tranca uma fechadura.
     */
    private void trancarFechadura() {
        DadosDispositivo dados = lerDadosDispositivo();
        executarComando(
                new ComandoTrancarFechadura(dados.utilizadorId, dados.casaId, dados.dispositivoId),
                "Fechadura trancada."
        );
    }

    /**
     * Destranca uma fechadura.
     */
    private void destrancarFechadura() {
        DadosDispositivo dados = lerDadosDispositivo();
        executarComando(
                new ComandoDestrancarFechadura(dados.utilizadorId, dados.casaId, dados.dispositivoId),
                "Fechadura destrancada."
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

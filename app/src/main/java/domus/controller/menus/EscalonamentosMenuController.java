package domus.controller.menus;

import domus.domain.DomiUM;
import domus.domain.commands.ComandoDesligar;
import domus.domain.commands.ComandoLigar;
import domus.ui.ConsoleView;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Controller do submenu de escalonamentos da aplicação de consola.
 */
public class EscalonamentosMenuController {

    /**
     * Fachada pública do domínio.
     */
    private final DomiUM model;

    /**
     * View de consola.
     */
    private final ConsoleView view;

    /**
     * Cria um controller para o submenu de escalonamentos.
     *
     * @param model fachada do domínio
     * @param view view de consola
     */
    public EscalonamentosMenuController(DomiUM model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Executa o ciclo do submenu de escalonamentos.
     */
    public void executar() {
        boolean voltar = false;
        while (!voltar) {
            this.view.mostrarMenuEscalonamentos();
            int opcao = this.view.lerOpcao();
            switch (opcao) {
                case 1:
                    criarEscalonamento();
                    break;
                case 2:
                    adicionarAcaoInicioLigarAEscalonamento();
                    break;
                case 3:
                    adicionarAcaoInicioDesligarAEscalonamento();
                    break;
                case 4:
                    adicionarAcaoFimLigarAEscalonamento();
                    break;
                case 5:
                    adicionarAcaoFimDesligarAEscalonamento();
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
     * Cria um escalonamento numa casa.
     */
    private void criarEscalonamento() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String escalonamentoId = this.view.lerTexto("Identificador do escalonamento: ");
        String nome = this.view.lerTexto("Nome do escalonamento: ");
        LocalTime horaInicio = lerHora("Hora de início (HH:mm): ");
        LocalTime horaFim = lerHora("Hora de fim (HH:mm): ");

        this.model.criarEscalonamento(utilizadorId, casaId, escalonamentoId, nome, horaInicio, horaFim);
        this.view.mostrarMensagem("Escalonamento criado.");
    }

    /**
     * Adiciona uma ação de início para ligar dispositivo.
     */
    private void adicionarAcaoInicioLigarAEscalonamento() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String escalonamentoId = this.view.lerTexto("Identificador do escalonamento: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        this.model.adicionarAcaoInicioAEscalonamento(
                utilizadorId, casaId, escalonamentoId,
                new ComandoLigar(utilizadorId, casaId, dispositivoId)
        );
        this.view.mostrarMensagem("Ação de início adicionada.");
    }

    /**
     * Adiciona uma ação de início para desligar dispositivo.
     */
    private void adicionarAcaoInicioDesligarAEscalonamento() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String escalonamentoId = this.view.lerTexto("Identificador do escalonamento: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        this.model.adicionarAcaoInicioAEscalonamento(
                utilizadorId, casaId, escalonamentoId,
                new ComandoDesligar(utilizadorId, casaId, dispositivoId)
        );
        this.view.mostrarMensagem("Ação de início adicionada.");
    }

    /**
     * Adiciona uma ação de fim para ligar dispositivo.
     */
    private void adicionarAcaoFimLigarAEscalonamento() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String escalonamentoId = this.view.lerTexto("Identificador do escalonamento: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        this.model.adicionarAcaoFimAEscalonamento(
                utilizadorId, casaId, escalonamentoId,
                new ComandoLigar(utilizadorId, casaId, dispositivoId)
        );
        this.view.mostrarMensagem("Ação de fim adicionada.");
    }

    /**
     * Adiciona uma ação de fim para desligar dispositivo.
     */
    private void adicionarAcaoFimDesligarAEscalonamento() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String escalonamentoId = this.view.lerTexto("Identificador do escalonamento: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        this.model.adicionarAcaoFimAEscalonamento(
                utilizadorId, casaId, escalonamentoId,
                new ComandoDesligar(utilizadorId, casaId, dispositivoId)
        );
        this.view.mostrarMensagem("Ação de fim adicionada.");
    }

    /**
     * Lê uma hora no formato HH:mm.
     *
     * @param mensagem mensagem apresentada antes da leitura
     * @return hora lida
     */
    private LocalTime lerHora(String mensagem) {
        while (true) {
            String texto = this.view.lerTexto(mensagem);
            try {
                return LocalTime.parse(texto);
            } catch (DateTimeParseException e) {
                this.view.mostrarErro("Hora inválida. Use o formato HH:mm.");
            }
        }
    }
}

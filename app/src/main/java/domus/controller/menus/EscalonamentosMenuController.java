package domus.controller.menus;

import domus.domain.DomiUM;
import domus.domain.commands.Command;
import domus.domain.scheduling.Escalonamento;
import domus.ui.ConsoleView;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Iterator;

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
                    adicionarAcaoInicioAEscalonamento();
                    break;
                case 3:
                    adicionarAcaoFimAEscalonamento();
                    break;
                case 4:
                    listarEscalonamentos();
                    break;
                case 5:
                    removerEscalonamento();
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

        try {
            this.model.criarEscalonamento(utilizadorId, casaId, escalonamentoId, nome, horaInicio, horaFim);
            this.view.mostrarMensagem("Escalonamento criado.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.EscalonamentoJaExisteException e) {
            this.view.mostrarErro("Já existe um escalonamento com o identificador \"" + e.getEscalonamentoId() + "\".");
        }
    }

    /**
     * Adiciona uma ação de início a um escalonamento.
     */
    private void adicionarAcaoInicioAEscalonamento() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String escalonamentoId = this.view.lerTexto("Identificador do escalonamento: ");
        Command comando = new ComandoDispositivoMenuHelper(this.view)
                .lerComandoDispositivo(utilizadorId, casaId);
        if (comando == null) {
            this.view.mostrarMensagem("Operação cancelada.");
            return;
        }

        try {
            this.model.adicionarAcaoInicioAEscalonamento(
                    utilizadorId, casaId, escalonamentoId, comando
            );
            this.view.mostrarMensagem("Ação de início adicionada.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.EscalonamentoNaoExisteException e) {
            this.view.mostrarErro("Escalonamento \"" + e.getEscalonamentoId() + "\" não existe.");
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
     * Adiciona uma ação de fim a um escalonamento.
     */
    private void adicionarAcaoFimAEscalonamento() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String escalonamentoId = this.view.lerTexto("Identificador do escalonamento: ");
        Command comando = new ComandoDispositivoMenuHelper(this.view)
                .lerComandoDispositivo(utilizadorId, casaId);
        if (comando == null) {
            this.view.mostrarMensagem("Operação cancelada.");
            return;
        }

        try {
            this.model.adicionarAcaoFimAEscalonamento(
                    utilizadorId, casaId, escalonamentoId, comando
            );
            this.view.mostrarMensagem("Ação de fim adicionada.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.EscalonamentoNaoExisteException e) {
            this.view.mostrarErro("Escalonamento \"" + e.getEscalonamentoId() + "\" não existe.");
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
     * Lista os escalonamentos registados numa casa.
     */
    private void listarEscalonamentos() {
        String casaId = this.view.lerTexto("Identificador da casa: ");
        Iterator<Escalonamento> iterador = this.model.getIteradorEscalonamentos(casaId);
        boolean encontrou = false;

        while (iterador.hasNext()) {
            encontrou = true;
            this.view.mostrarMensagem(iterador.next().toString());
        }

        if (!encontrou) {
            this.view.mostrarMensagem("Não existem escalonamentos registados na casa indicada.");
        }
    }

    /**
     * Remove um escalonamento de uma casa.
     */
    private void removerEscalonamento() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String escalonamentoId = this.view.lerTexto("Identificador do escalonamento: ");

        try {
            this.model.removerEscalonamento(utilizadorId, casaId, escalonamentoId);
            this.view.mostrarMensagem("Escalonamento removido.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão de administração na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.EscalonamentoNaoExisteException e) {
            this.view.mostrarErro("Escalonamento \"" + e.getEscalonamentoId() + "\" não existe.");
        } catch (domus.domain.exceptions.DomusException e) {
            this.view.mostrarErro(e.getMessage());
        }
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

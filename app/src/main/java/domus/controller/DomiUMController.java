package domus.controller;

import domus.domain.DomiUM;
import domus.domain.commands.ComandoDesligar;
import domus.domain.commands.ComandoLigar;
import domus.domain.conditions.CondicaoHumidade;
import domus.domain.conditions.CondicaoLuminosidade;
import domus.domain.conditions.CondicaoTemperatura;
import domus.domain.core.Casa;
import domus.domain.core.Utilizador;
import domus.domain.statistics.ResumoCasaConsumo;
import domus.domain.statistics.ResumoDispositivoUso;
import domus.domain.statistics.ResumoDivisaoDispositivos;
import domus.domain.suggestions.SugestaoEscalonamento;
import domus.ui.ConsoleView;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Controller da aplicação de consola DomusControl.
 *
 * Coordena o fluxo textual da aplicação, recebendo dados da view e delegando
 * as operações no model através da fachada {@link DomiUM}.
 */
public class DomiUMController {

    /**
     * Fachada pública do domínio.
     */
    private DomiUM model;

    /**
     * View de consola.
     */
    private final ConsoleView view;

    /**
     * Indica se a aplicação continua em execução.
     */
    private boolean emExecucao;

    /**
     * Cria um controller para a aplicação de consola.
     *
     * @param model fachada do domínio
     * @param view view de consola
     */
    public DomiUMController(DomiUM model, ConsoleView view) {
        this.model = model;
        this.view = view;
        this.emExecucao = true;
    }

    /**
     * Inicia o ciclo principal da aplicação.
     */
    public void iniciar() {
        while (this.emExecucao) {
            this.view.mostrarMenuPrincipal();
            int opcao = this.view.lerOpcao();
            processarOpcao(opcao);
        }
    }

    /**
     * Encaminha a opção escolhida para a operação correspondente.
     *
     * @param opcao opção escolhida
     */
    private void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                criarUtilizador();
                break;
            case 2:
                criarCasa();
                break;
            case 3:
                adicionarDivisao();
                break;
            case 4:
                adicionarDispositivo();
                break;
            case 5:
                ligarDispositivo();
                break;
            case 6:
                desligarDispositivo();
                break;
            case 7:
                listarCasas();
                break;
            case 8:
                listarUtilizadores();
                break;
            case 9:
                guardarEstado();
                break;
            case 10:
                carregarEstado();
                break;
            case 11:
                menuCenarios();
                break;
            case 12:
                menuEscalonamentos();
                break;
            case 13:
                menuAutomacoes();
                break;
            case 14:
                avancarTempo();
                break;
            case 15:
                atualizarAmbienteDivisao();
                break;
            case 16:
                menuEstatisticas();
                break;
            case 17:
                menuSugestoes();
                break;
            case 18:
                criarEstadoDemonstracao();
                break;
            case 0:
                sair();
                break;
            default:
                this.view.mostrarErro("Opção desconhecida.");
                break;
        }
    }

    /**
     * Cria um utilizador através da fachada do domínio.
     */
    private void criarUtilizador() {
        String id = this.view.lerTexto("Identificador do utilizador: ");
        String nome = this.view.lerTexto("Nome do utilizador: ");

        this.model.criarUtilizador(id, nome);
        this.view.mostrarMensagem("Utilizador criado.");
    }

    /**
     * Cria uma casa através da fachada do domínio.
     */
    private void criarCasa() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String nome = this.view.lerTexto("Nome da casa: ");

        this.model.criarCasa(utilizadorId, casaId, nome);
        this.view.mostrarMensagem("Casa criada.");
    }

    /**
     * Adiciona uma divisão a uma casa.
     */
    private void adicionarDivisao() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String divisaoNome = this.view.lerTexto("Nome da divisão: ");

        this.model.adicionarDivisao(utilizadorId, casaId, divisaoNome);
        this.view.mostrarMensagem("Divisão adicionada.");
    }

    /**
     * Adiciona um dispositivo a uma divisão.
     */
    private void adicionarDispositivo() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String divisao = this.view.lerTexto("Nome da divisão: ");
        String tipo = this.view.lerTexto("Tipo do dispositivo: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");
        String marca = this.view.lerTexto("Marca: ");
        String modelo = this.view.lerTexto("Modelo: ");
        double consumoPorHora = this.view.lerDouble("Consumo por hora: ");

        this.model.adicionarDispositivo(
                utilizadorId, casaId, divisao, tipo, dispositivoId, marca,
                modelo, consumoPorHora
        );
        this.view.mostrarMensagem("Dispositivo adicionado.");
    }

    /**
     * Liga um dispositivo através de um comando.
     */
    private void ligarDispositivo() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        this.model.executarComando(new ComandoLigar(utilizadorId, casaId, dispositivoId));
        this.view.mostrarMensagem("Comando de ligar executado.");
    }

    /**
     * Desliga um dispositivo através de um comando.
     */
    private void desligarDispositivo() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        this.model.executarComando(new ComandoDesligar(utilizadorId, casaId, dispositivoId));
        this.view.mostrarMensagem("Comando de desligar executado.");
    }

    /**
     * Lista as casas registadas.
     */
    private void listarCasas() {
        Iterator<Casa> iterador = this.model.getIteradorCasas();
        boolean encontrou = false;

        while (iterador.hasNext()) {
            encontrou = true;
            this.view.mostrarMensagem(iterador.next().toString());
        }

        if (!encontrou) {
            this.view.mostrarMensagem("Não existem casas registadas.");
        }
    }

    /**
     * Lista os utilizadores registados.
     */
    private void listarUtilizadores() {
        Iterator<Utilizador> iterador = this.model.getIteradorUtilizadores();
        boolean encontrou = false;

        while (iterador.hasNext()) {
            encontrou = true;
            this.view.mostrarMensagem(iterador.next().toString());
        }

        if (!encontrou) {
            this.view.mostrarMensagem("Não existem utilizadores registados.");
        }
    }

    /**
     * Guarda o estado atual do domínio.
     */
    private void guardarEstado() {
        String caminho = this.view.lerTexto("Caminho do ficheiro: ");

        this.model.gravarEstado(caminho);
        this.view.mostrarMensagem("Estado guardado.");
    }

    /**
     * Carrega o estado do domínio a partir de um ficheiro.
     */
    private void carregarEstado() {
        String caminho = this.view.lerTexto("Caminho do ficheiro: ");

        this.model = DomiUM.carregarEstado(caminho);
        this.view.mostrarMensagem("Estado carregado.");
    }

    /**
     * Apresenta e processa o submenu de cenários.
     */
    private void menuCenarios() {
        boolean voltar = false;
        while (!voltar && this.emExecucao) {
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

        this.model.criarCenario(utilizadorId, casaId, cenarioId, nome);
        this.view.mostrarMensagem("Cenário criado.");
    }

    /**
     * Adiciona um comando de ligar a um cenário.
     */
    private void adicionarComandoLigarACenario() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String cenarioId = this.view.lerTexto("Identificador do cenário: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        this.model.adicionarComandoACenario(
                utilizadorId, casaId, cenarioId,
                new ComandoLigar(utilizadorId, casaId, dispositivoId)
        );
        this.view.mostrarMensagem("Comando adicionado ao cenário.");
    }

    /**
     * Adiciona um comando de desligar a um cenário.
     */
    private void adicionarComandoDesligarACenario() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String cenarioId = this.view.lerTexto("Identificador do cenário: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        this.model.adicionarComandoACenario(
                utilizadorId, casaId, cenarioId,
                new ComandoDesligar(utilizadorId, casaId, dispositivoId)
        );
        this.view.mostrarMensagem("Comando adicionado ao cenário.");
    }

    /**
     * Executa um cenário.
     */
    private void executarCenario() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String cenarioId = this.view.lerTexto("Identificador do cenário: ");

        this.model.executarCenario(utilizadorId, casaId, cenarioId);
        this.view.mostrarMensagem("Cenário executado.");
    }

    /**
     * Apresenta e processa o submenu de escalonamentos.
     */
    private void menuEscalonamentos() {
        boolean voltar = false;
        while (!voltar && this.emExecucao) {
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
     * Apresenta e processa o submenu de automações.
     */
    private void menuAutomacoes() {
        boolean voltar = false;
        while (!voltar && this.emExecucao) {
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
     * Avança o tempo simulado do domínio.
     */
    private void avancarTempo() {
        int minutos = this.view.lerInteiro("Minutos a avançar: ");

        this.model.avancarTempo(minutos);
        this.view.mostrarMensagem("Tempo avançado.");
    }

    /**
     * Atualiza o ambiente interior de uma divisão.
     */
    private void atualizarAmbienteDivisao() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String divisaoNome = this.view.lerTexto("Nome da divisão: ");
        double temperatura = this.view.lerDouble("Temperatura: ");
        double humidade = this.view.lerDouble("Humidade: ");
        double luminosidade = this.view.lerDouble("Luminosidade: ");

        this.model.atualizarAmbienteDivisao(
                utilizadorId, casaId, divisaoNome, temperatura, humidade, luminosidade
        );
        this.view.mostrarMensagem("Ambiente atualizado.");
    }

    /**
     * Apresenta e processa o submenu de estatísticas.
     */
    private void menuEstatisticas() {
        boolean voltar = false;
        while (!voltar && this.emExecucao) {
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

    /**
     * Apresenta e processa o submenu de sugestões.
     */
    private void menuSugestoes() {
        boolean voltar = false;
        while (!voltar && this.emExecucao) {
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

        this.model.aceitarSugestaoEscalonamento(
                utilizadorId, escalonamentoId, nome, sugestoes.get(escolha - 1)
        );
        this.view.mostrarMensagem("Sugestão aceite.");
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

    /**
     * Cria um pequeno estado de demonstração no model atual.
     */
    private void criarEstadoDemonstracao() {
        String utilizadorId = "demo_u1";
        String casaId = "demo_c1";

        this.model.criarUtilizador(utilizadorId, "Utilizador Demonstração");
        this.model.criarCasa(utilizadorId, casaId, "Casa Demonstração");
        this.model.adicionarDivisao(utilizadorId, casaId, "Sala");
        this.model.adicionarDivisao(utilizadorId, casaId, "Quarto");
        this.model.adicionarDivisao(utilizadorId, casaId, "Garagem");

        this.model.adicionarDispositivo(
                utilizadorId, casaId, "Sala", "lampada", "demo_l1",
                "Philips", "Hue", 10.0
        );
        this.model.adicionarDispositivo(
                utilizadorId, casaId, "Quarto", "lampada", "demo_l2",
                "Philips", "Hue", 8.0
        );
        this.model.adicionarDispositivo(
                utilizadorId, casaId, "Garagem", "lampada", "demo_l3",
                "Philips", "Hue", 6.0
        );

        this.model.criarCenario(utilizadorId, casaId, "demo_cenario_noite", "Cenário Noite");
        this.model.adicionarComandoACenario(
                utilizadorId, casaId, "demo_cenario_noite",
                new ComandoLigar(utilizadorId, casaId, "demo_l1")
        );
        this.model.adicionarComandoACenario(
                utilizadorId, casaId, "demo_cenario_noite",
                new ComandoDesligar(utilizadorId, casaId, "demo_l1")
        );

        this.model.criarEscalonamento(
                utilizadorId, casaId, "demo_esc_manha", "Luz da manhã",
                LocalTime.of(8, 0), LocalTime.of(8, 1)
        );
        this.model.adicionarAcaoInicioAEscalonamento(
                utilizadorId, casaId, "demo_esc_manha",
                new ComandoLigar(utilizadorId, casaId, "demo_l2")
        );

        this.model.criarAutomacao(
                utilizadorId, casaId, "demo_auto_luz", "Ligar luz com pouca luminosidade",
                "Sala", new CondicaoLuminosidade(30.0, false)
        );
        this.model.adicionarAcaoAAutomacao(
                utilizadorId, casaId, "demo_auto_luz",
                new ComandoLigar(utilizadorId, casaId, "demo_l1")
        );

        for (int i = 0; i < 3; i++) {
            this.model.executarComando(new ComandoLigar(utilizadorId, casaId, "demo_l1"));
        }

        this.view.mostrarMensagem(
                "Estado de demonstração criado. Pode agora consultar estatísticas, sugestões, cenários, automações e escalonamentos."
        );
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

    /**
     * Termina a aplicação.
     */
    private void sair() {
        this.emExecucao = false;
        this.view.mostrarMensagem("Aplicação terminada.");
        this.view.fechar();
    }
}

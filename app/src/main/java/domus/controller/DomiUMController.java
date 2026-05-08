package domus.controller;

import domus.controller.menus.AutomacoesMenuController;
import domus.controller.menus.CenariosMenuController;
import domus.controller.menus.EscalonamentosMenuController;
import domus.controller.menus.EstatisticasMenuController;
import domus.controller.menus.SugestoesMenuController;
import domus.demo.EstadoDemonstracao;
import domus.domain.DomiUM;
import domus.domain.commands.ComandoDesligar;
import domus.domain.commands.ComandoLigar;
import domus.domain.core.Casa;
import domus.domain.core.Divisao;
import domus.domain.core.Utilizador;
import domus.domain.devices.Dispositivo;
import domus.ui.ConsoleView;
import java.util.Iterator;

/**
 * Controller principal da aplicação de consola DomusControl.
 *
 * Coordena o menu principal, delegando os submenus especializados em
 * controllers auxiliares compostos com o mesmo model e a mesma view.
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
                new CenariosMenuController(this.model, this.view).executar();
                break;
            case 12:
                new EscalonamentosMenuController(this.model, this.view).executar();
                break;
            case 13:
                new AutomacoesMenuController(this.model, this.view).executar();
                break;
            case 14:
                avancarTempo();
                break;
            case 15:
                atualizarAmbienteDivisao();
                break;
            case 16:
                new EstatisticasMenuController(this.model, this.view).executar();
                break;
            case 17:
                new SugestoesMenuController(this.model, this.view).executar();
                break;
            case 18:
                criarEstadoDemonstracao();
                break;
            case 19:
                consultarCasa();
                break;
            case 20:
                consultarDivisao();
                break;
            case 21:
                consultarDispositivo();
                break;
            case 22:
                listarTodosDispositivosDaCasa();
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
        try {
            this.model.criarUtilizador(id, nome);
            this.view.mostrarMensagem("Utilizador criado.");
        } catch (domus.domain.exceptions.UtilizadorJaExisteException e) {
            this.view.mostrarErro("Já existe um utilizador com o identificador \"" + e.getUtilizadorId() + "\".");
        }
    }

    /**
     * Cria uma casa através da fachada do domínio.
     */
    private void criarCasa() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String nome = this.view.lerTexto("Nome da casa: ");
        try {
            this.model.criarCasa(utilizadorId, casaId, nome);
            this.view.mostrarMensagem("Casa criada.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaJaExisteException e) {
            this.view.mostrarErro("Já existe uma casa com o identificador \"" + e.getCasaId() + "\".");
        }
    }

    /**
     * Adiciona uma divisão a uma casa.
     */
    private void adicionarDivisao() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String divisaoNome = this.view.lerTexto("Nome da divisão: ");
        try {
            this.model.adicionarDivisao(utilizadorId, casaId, divisaoNome);
            this.view.mostrarMensagem("Divisão adicionada.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão de administração na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.DivisaoJaExisteException e) {
            this.view.mostrarErro("Já existe uma divisão \"" + e.getDivisaoNome() + "\" nesta casa.");
        }
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

        try {
            this.model.adicionarDispositivo(
                    utilizadorId, casaId, divisao, tipo, dispositivoId, marca,
                    modelo, consumoPorHora
            );
            this.view.mostrarMensagem("Dispositivo adicionado.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão de administração na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.DivisaoNaoExisteException e) {
            this.view.mostrarErro("Divisão \"" + e.getDivisaoNome() + "\" não existe.");
        } catch (domus.domain.exceptions.DispositivoJaExisteException e) {
            this.view.mostrarErro("Já existe um dispositivo com o identificador \"" + e.getDispositivoId() + "\".");
        } catch (domus.domain.exceptions.TipoDispositivoInvalidoException e) {
            this.view.mostrarErro("Tipo de dispositivo desconhecido: \"" + e.getTipo() + "\".");
        }
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

        try {
            this.model.atualizarAmbienteDivisao(
                    utilizadorId, casaId, divisaoNome, temperatura, humidade, luminosidade
            );
            this.view.mostrarMensagem("Ambiente atualizado.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.DivisaoNaoExisteException e) {
            this.view.mostrarErro("Divisão \"" + e.getDivisaoNome() + "\" não existe.");
        }
    }

    /**
     * Cria um pequeno estado de demonstração no model atual.
     */
    private void criarEstadoDemonstracao() {
        try {
            EstadoDemonstracao.popular(this.model);
            this.view.mostrarMensagem(
                    "Estado de demonstração criado. Pode agora consultar estatísticas, sugestões, cenários, automações e escalonamentos."
            );
        } catch (domus.domain.exceptions.DomusException e) {
            this.view.mostrarErro(e.getMessage());
        }
    }

    /**
     * Consulta as informações de uma casa e lista as suas divisões.
     */
    private void consultarCasa() {
        String casaId = this.view.lerTexto("Identificador da casa: ");
        Casa casa = this.model.getCasa(casaId);
        if (casa == null) {
            this.view.mostrarErro("Casa \"" + casaId + "\" não existe.");
            return;
        }
        this.view.mostrarMensagem("Casa: " + casa.getId() + " — " + casa.getNome());
        Iterator<Divisao> iterador = casa.getIteradorDivisoes();
        if (!iterador.hasNext()) {
            this.view.mostrarMensagem("  (sem divisões)");
        }
        while (iterador.hasNext()) {
            this.view.mostrarMensagem("  • " + iterador.next().getNome());
        }
    }

    /**
     * Consulta as informações de uma divisão e lista os seus dispositivos.
     */
    private void consultarDivisao() {
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String divisaoNome = this.view.lerTexto("Nome da divisão: ");
        Divisao divisao = this.model.getDivisao(casaId, divisaoNome);
        if (divisao == null) {
            this.view.mostrarErro("Divisão \"" + divisaoNome + "\" não existe na casa \"" + casaId + "\".");
            return;
        }
        this.view.mostrarMensagem("Divisão: " + divisao.getNome());
        this.view.mostrarMensagem("Ambiente: " + divisao.getAmbienteInterior());
        Iterator<Dispositivo> iterador = divisao.getIteradorDispositivos();
        if (!iterador.hasNext()) {
            this.view.mostrarMensagem("  (sem dispositivos)");
        }
        while (iterador.hasNext()) {
            this.view.mostrarMensagem("  • " + iterador.next());
        }
    }

    /**
     * Consulta as informações e o estado de um dispositivo.
     */
    private void consultarDispositivo() {
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");
        Dispositivo dispositivo = this.model.getDispositivo(casaId, dispositivoId);
        if (dispositivo == null) {
            this.view.mostrarErro("Dispositivo \"" + dispositivoId + "\" não existe na casa \"" + casaId + "\".");
            return;
        }
        this.view.mostrarMensagem(dispositivo.toString());
    }

    /**
     * Lista todos os dispositivos de uma casa, independentemente da divisão.
     */
    private void listarTodosDispositivosDaCasa() {
        String casaId = this.view.lerTexto("Identificador da casa: ");
        Casa casa = this.model.getCasa(casaId);
        if (casa == null) {
            this.view.mostrarErro("Casa \"" + casaId + "\" não existe.");
            return;
        }
        boolean encontrou = false;
        Iterator<Divisao> iteradorDivisoes = casa.getIteradorDivisoes();
        while (iteradorDivisoes.hasNext()) {
            Divisao divisao = iteradorDivisoes.next();
            Iterator<Dispositivo> iteradorDispositivos = divisao.getIteradorDispositivos();
            while (iteradorDispositivos.hasNext()) {
                encontrou = true;
                this.view.mostrarMensagem("[" + divisao.getNome() + "] " + iteradorDispositivos.next());
            }
        }
        if (!encontrou) {
            this.view.mostrarMensagem("Não existem dispositivos na casa \"" + casaId + "\".");
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

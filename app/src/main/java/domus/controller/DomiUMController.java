package domus.controller;

import domus.controller.menus.AutomacoesMenuController;
import domus.controller.menus.CenariosMenuController;
import domus.controller.menus.DispositivosMenuController;
import domus.controller.menus.EscalonamentosMenuController;
import domus.controller.menus.EstatisticasMenuController;
import domus.controller.menus.SugestoesMenuController;
import domus.domain.DomiUM;
import domus.domain.core.Casa;
import domus.domain.core.Divisao;
import domus.domain.core.TipoPermissao;
import domus.domain.core.Utilizador;
import domus.domain.devices.Dispositivo;
import domus.domain.exceptions.PersistenciaException;
import domus.ui.ConsoleView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

/**
 * Controller principal da aplicação de consola DomusControl.
 *
 * Coordena o menu principal, delegando os submenus especializados em
 * controllers auxiliares compostos com o mesmo model e a mesma view.
 */
public class DomiUMController {

    /**
     * Formato usado para apresentar a data e hora simuladas na consola.
     */
    private static final DateTimeFormatter FORMATO_DATA_HORA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
                listarUtilizadores();
                break;
            case 3:
                atribuirPermissaoCasa();
                break;
            case 4:
                criarCasa();
                break;
            case 5:
                listarCasas();
                break;
            case 6:
                consultarCasa();
                break;
            case 7:
                adicionarDivisao();
                break;
            case 8:
                consultarDivisao();
                break;
            case 9:
                atualizarAmbienteDivisao();
                break;
            case 10:
                adicionarDispositivo();
                break;
            case 11:
                removerDispositivo();
                break;
            case 12:
                new DispositivosMenuController(this.model, this.view).ligarDispositivo();
                break;
            case 13:
                new DispositivosMenuController(this.model, this.view).desligarDispositivo();
                break;
            case 14:
                consultarDispositivo();
                break;
            case 15:
                listarTodosDispositivosDaCasa();
                break;
            case 16:
                new DispositivosMenuController(this.model, this.view).executar();
                break;
            case 17:
                new CenariosMenuController(this.model, this.view).executar();
                break;
            case 18:
                new AutomacoesMenuController(this.model, this.view).executar();
                break;
            case 19:
                new EscalonamentosMenuController(this.model, this.view).executar();
                break;
            case 20:
                new EstatisticasMenuController(this.model, this.view).executar();
                break;
            case 21:
                new SugestoesMenuController(this.model, this.view).executar();
                break;
            case 22:
                guardarEstado();
                break;
            case 23:
                carregarEstado();
                break;
            case 24:
                consultarDataHoraAtual();
                break;
            case 25:
                avancarTempo();
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
     * Atribui uma permissão de acesso a uma casa a um utilizador.
     */
    private void atribuirPermissaoCasa() {
        String administradorId = this.view.lerTexto("Identificador do administrador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String utilizadorAlvoId = this.view.lerTexto("Identificador do utilizador alvo: ");
        TipoPermissao permissao = lerTipoPermissao();

        if (permissao == null) {
            this.view.mostrarErro("Tipo de permissão inválido.");
            return;
        }

        try {
            this.model.atribuirPermissaoCasa(administradorId, casaId, utilizadorAlvoId, permissao);
            this.view.mostrarMensagem("Permissão atribuída.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão de administração na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.DomusException e) {
            this.view.mostrarErro(e.getMessage());
        }
    }

    /**
     * Remove um dispositivo de uma casa.
     */
    private void removerDispositivo() {
        String utilizadorId = this.view.lerTexto("Identificador do utilizador: ");
        String casaId = this.view.lerTexto("Identificador da casa: ");
        String dispositivoId = this.view.lerTexto("Identificador do dispositivo: ");

        try {
            this.model.removerDispositivo(utilizadorId, casaId, dispositivoId);
            this.view.mostrarMensagem("Dispositivo removido.");
        } catch (domus.domain.exceptions.UtilizadorNaoExisteException e) {
            this.view.mostrarErro("Utilizador \"" + e.getUtilizadorId() + "\" não existe.");
        } catch (domus.domain.exceptions.CasaNaoExisteException e) {
            this.view.mostrarErro("Casa \"" + e.getCasaId() + "\" não existe.");
        } catch (domus.domain.exceptions.SemPermissaoException e) {
            this.view.mostrarErro("Sem permissão de administração na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.DispositivoNaoExisteException e) {
            this.view.mostrarErro("Dispositivo \"" + e.getDispositivoId()
                    + "\" não existe na casa \"" + e.getCasaId() + "\".");
        } catch (domus.domain.exceptions.DomusException e) {
            this.view.mostrarErro(e.getMessage());
        }
    }

    /**
     * Lê e interpreta o tipo de permissão a atribuir.
     *
     * @return tipo de permissão escolhido, ou {@code null} se o valor for inválido
     */
    private TipoPermissao lerTipoPermissao() {
        String texto = this.view.lerTexto("Tipo de permissão (1/ADMIN, 2/NORMAL): ");
        if ("1".equals(texto) || "admin".equalsIgnoreCase(texto)) {
            return TipoPermissao.ADMIN;
        }
        if ("2".equals(texto) || "normal".equalsIgnoreCase(texto)) {
            return TipoPermissao.NORMAL;
        }
        return null;
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

        try {
            this.model.gravarEstado(caminho);
            this.view.mostrarMensagem("Estado guardado.");
        } catch (PersistenciaException e) {
            this.view.mostrarErro("Não foi possível guardar o estado. " + e.getMessage());
        }
    }

    /**
     * Carrega o estado do domínio a partir de um ficheiro.
     */
    private void carregarEstado() {
        String caminho = this.view.lerTexto("Caminho do ficheiro: ");

        try {
            DomiUM estadoCarregado = DomiUM.carregarEstado(caminho);
            this.model = estadoCarregado;
            this.view.mostrarMensagem("Estado carregado.");
        } catch (PersistenciaException e) {
            this.view.mostrarErro("Não foi possível carregar o estado. " + e.getMessage());
        }
    }

    /**
     * Consulta a data e hora atuais do relógio simulado.
     */
    private void consultarDataHoraAtual() {
        this.view.mostrarMensagem("Data/hora atual: " + formatarDataHoraAtual());
    }

    /**
     * Avança o tempo simulado do domínio.
     */
    private void avancarTempo() {
        int minutos = this.view.lerInteiro("Minutos a avançar: ");

        this.model.avancarTempo(minutos);
        this.view.mostrarMensagem("Tempo avançado. Data/hora atual: " + formatarDataHoraAtual());
    }

    /**
     * Formata a data e hora atuais do relógio simulado para apresentação.
     *
     * @return data e hora formatadas
     */
    private String formatarDataHoraAtual() {
        return formatarDataHora(this.model.getDataHoraAtual());
    }

    /**
     * Formata uma data e hora para apresentação na consola.
     *
     * @param dataHora data e hora a formatar
     * @return texto formatado
     */
    private String formatarDataHora(LocalDateTime dataHora) {
        if (dataHora == null) {
            return "";
        }
        return dataHora.format(FORMATO_DATA_HORA);
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

package domus.domain;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Objects;

import domus.domain.automation.Automacao;
import domus.domain.commands.ComandoDispositivo;
import domus.domain.commands.Command;
import domus.domain.conditions.Condicao;
import domus.domain.core.Casa;
import domus.domain.core.TipoPermissao;
import domus.domain.core.Utilizador;
import domus.domain.core.Divisao;
import domus.domain.devices.Dispositivo;
import domus.domain.devices.OperacaoDispositivo;
import domus.domain.environment.AmbienteInterior;
import domus.domain.history.RegistoInteracao;
import domus.domain.exceptions.AutomacaoJaExisteException;
import domus.domain.exceptions.AutomacaoNaoExisteException;
import domus.domain.exceptions.CasaJaExisteException;
import domus.domain.exceptions.CasaNaoExisteException;
import domus.domain.exceptions.CenarioJaExisteException;
import domus.domain.exceptions.CenarioNaoExisteException;
import domus.domain.exceptions.DivisaoJaExisteException;
import domus.domain.exceptions.DivisaoNaoExisteException;
import domus.domain.exceptions.DispositivoJaExisteException;
import domus.domain.exceptions.DispositivoNaoExisteException;
import domus.domain.exceptions.DomusException;
import domus.domain.exceptions.EscalonamentoJaExisteException;
import domus.domain.exceptions.EscalonamentoNaoExisteException;
import domus.domain.exceptions.OperacaoInvalidaException;
import domus.domain.exceptions.SemPermissaoException;
import domus.domain.exceptions.TipoDispositivoInvalidoException;
import domus.domain.exceptions.UtilizadorJaExisteException;
import domus.domain.exceptions.UtilizadorNaoExisteException;
import domus.domain.managers.GestorCasas;
import domus.domain.managers.GestorUtilizadores;
import domus.domain.scenarios.Cenario;
import domus.domain.scheduling.Escalonamento;
import domus.domain.statistics.ResumoCasaConsumo;
import domus.domain.statistics.ResumoDispositivoUso;
import domus.domain.statistics.ResumoDivisaoDispositivos;
import domus.domain.suggestions.SugestaoCommandRegistry;
import domus.domain.suggestions.SugestaoEscalonamento;
import domus.domain.time.RelogioSistema;

/**
 * Representa a fachada central do domínio DomusControl.
 *
 * A DomiUM mantém a API pública do domínio e coordena os gestores internos
 * responsáveis por utilizadores e casas, preservando um ponto de entrada único
 * para as operações principais do sistema.
 */
public class DomiUM implements Serializable {

    /**
     * Gestor responsável pelos utilizadores e permissões.
     */
    private final GestorUtilizadores gestorUtilizadores;

    /**
     * Gestor responsável pelas casas e respetivas estruturas internas.
     */
    private final GestorCasas gestorCasas;

    /**
     * Relógio simulado do sistema.
     */
    private final RelogioSistema relogio;

    /**
     * Cria uma instância vazia da DomiUM.
     */
    public DomiUM() {
        this.gestorUtilizadores = new GestorUtilizadores();
        this.gestorCasas = new GestorCasas();
        this.relogio = new RelogioSistema();
    }

    /**
     * Cria e regista um novo utilizador no sistema.
     *
     * @param id identificador do utilizador
     * @param nome nome do utilizador
     * @throws UtilizadorJaExisteException se já existir um utilizador com o
     *         identificador indicado
     */
    public void criarUtilizador(String id, String nome) throws UtilizadorJaExisteException {
        this.gestorUtilizadores.criarUtilizador(id, nome);
    }

    /**
     * Obtém um utilizador a partir do seu identificador.
     *
     * @param id identificador do utilizador
     * @return cópia do utilizador encontrado, ou {@code null} se não existir
     */
    public Utilizador getUtilizador(String id) {
        return this.gestorUtilizadores.getUtilizador(id);
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida dos utilizadores do
     * sistema.
     *
     * @return iterador sobre uma cópia dos utilizadores registados
     */
    public Iterator<Utilizador> getIteradorUtilizadores() {
        return this.gestorUtilizadores.getIteradorUtilizadores();
    }

    /**
     * Obtém sugestões de escalonamento para um utilizador, usando os valores
     * padrão do domínio.
     *
     * @param utilizadorId identificador do utilizador
     * @return iterador sobre as sugestões encontradas
     */
    public Iterator<SugestaoEscalonamento> getSugestoesEscalonamento(String utilizadorId) {
        return this.gestorUtilizadores.getSugestoesEscalonamento(utilizadorId, 3, 5);
    }

    /**
     * Obtém sugestões de escalonamento para um utilizador.
     *
     * @param utilizadorId identificador do utilizador
     * @param minimoOcorrencias número mínimo de ocorrências para gerar sugestão
     * @param limite número máximo de sugestões a devolver
     * @return iterador sobre as sugestões encontradas
     */
    public Iterator<SugestaoEscalonamento> getSugestoesEscalonamento(String utilizadorId, int minimoOcorrencias, int limite) {
        return this.gestorUtilizadores.getSugestoesEscalonamento(utilizadorId, minimoOcorrencias, limite);
    }

    /**
     * Aceita uma sugestão de escalonamento, criando um escalonamento real na
     * casa indicada pela sugestão e adicionando a ação sugerida como ação de
     * início.
     *
     * Nesta fase apenas são aceites sugestões de ações simples, sem parâmetros
     * adicionais na descrição textual.
     *
     * @param utilizadorId identificador do utilizador que aceita a sugestão
     * @param escalonamentoId identificador do escalonamento a criar
     * @param nome nome do escalonamento
     * @param sugestao sugestão a aceitar
     * @throws DomusException se a sugestão não puder ser aceite
     */
    public void aceitarSugestaoEscalonamento(String utilizadorId, String escalonamentoId,
                                             String nome, SugestaoEscalonamento sugestao) throws DomusException {
        if (utilizadorId == null || escalonamentoId == null || nome == null || sugestao == null) {
            return;
        }

        if (!Objects.equals(utilizadorId, sugestao.getUtilizadorId())) {
            throw new OperacaoInvalidaException("Sugestão não pertence ao utilizador: " + utilizadorId);
        }

        String casaId = sugestao.getCasaId();
        LocalTime horaInicio = sugestao.getHoraSugerida();
        if (casaId == null || horaInicio == null) {
            return;
        }
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            throw new SemPermissaoException(utilizadorId, casaId);
        }

        SugestaoCommandRegistry registry = new SugestaoCommandRegistry();
        Command comando = registry.criarComando(utilizadorId, sugestao);
        if (comando == null) {
            throw new OperacaoInvalidaException("A sugestão não corresponde a uma ação suportada.");
        }

        this.gestorCasas.criarEscalonamento(
                casaId, escalonamentoId, nome, horaInicio, horaInicio.plusMinutes(1));
        this.gestorCasas.adicionarAcaoInicioAEscalonamento(casaId, escalonamentoId, comando);
    }

    /**
     * Cria uma nova casa no sistema e atribui ao utilizador criador a permissão
     * de administração sobre essa casa.
     *
     * @param utilizadorId identificador do utilizador criador
     * @param casaId identificador da casa
     * @param nome nome da casa
     * @throws UtilizadorNaoExisteException se o utilizador não existir
     * @throws CasaJaExisteException se já existir uma casa com o identificador
     *         indicado
     */
    public void criarCasa(String utilizadorId, String casaId, String nome) throws UtilizadorNaoExisteException, CasaJaExisteException {
        if (utilizadorId == null || casaId == null || nome == null) {
            return;
        }

        if (!this.gestorUtilizadores.existeUtilizador(utilizadorId)) {
            throw new UtilizadorNaoExisteException(utilizadorId);
        }
        if (this.gestorCasas.existeCasa(casaId)) {
            throw new CasaJaExisteException(casaId);
        }

        this.gestorCasas.criarCasa(casaId, nome);
        this.gestorUtilizadores.adicionarPermissao(utilizadorId, casaId, TipoPermissao.ADMIN);
    }

    /**
     * Obtém uma casa a partir do seu identificador.
     *
     * @param id identificador da casa
     * @return cópia da casa encontrada, ou {@code null} se não existir
     */
    public Casa getCasa(String id) {
        return this.gestorCasas.getCasa(id);
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida das casas do sistema.
     *
     * @return iterador sobre uma cópia das casas registadas
     */
    public Iterator<Casa> getIteradorCasas() {
        return this.gestorCasas.getIteradorCasas();
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida dos cenários de uma
     * casa.
     *
     * @param casaId identificador da casa
     * @return iterador sobre uma cópia dos cenários da casa
     */
    public Iterator<Cenario> getIteradorCenarios(String casaId) {
        return this.gestorCasas.getIteradorCenarios(casaId);
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida dos escalonamentos de
     * uma casa.
     *
     * @param casaId identificador da casa
     * @return iterador sobre uma cópia dos escalonamentos da casa
     */
    public Iterator<Escalonamento> getIteradorEscalonamentos(String casaId) {
        return this.gestorCasas.getIteradorEscalonamentos(casaId);
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida das automações de uma
     * casa.
     *
     * @param casaId identificador da casa
     * @return iterador sobre uma cópia das automações da casa
     */
    public Iterator<Automacao> getIteradorAutomacoes(String casaId) {
        return this.gestorCasas.getIteradorAutomacoes(casaId);
    }

    /**
     * Obtém uma divisão de uma casa a partir do seu nome.
     *
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     * @return cópia da divisão encontrada, ou {@code null} se a casa ou a
     *         divisão não existirem
     */
    public Divisao getDivisao(String casaId, String divisaoNome) {
        return this.gestorCasas.getDivisao(casaId, divisaoNome);
    }

    /**
     * Obtém um dispositivo de uma casa a partir do seu identificador,
     * independentemente da divisão em que se encontra.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @return cópia do dispositivo encontrado, ou {@code null} se a casa ou o
     *         dispositivo não existirem
     */
    public Dispositivo getDispositivo(String casaId, String dispositivoId) {
        return this.gestorCasas.getDispositivo(casaId, dispositivoId);
    }

    /**
     * Obtém a casa com maior consumo total.
     *
     * @return resumo da casa com maior consumo, ou {@code null} se não houver
     *         casas registadas
     */
    public ResumoCasaConsumo getCasaMaiorConsumo() {
        return this.gestorCasas.getCasaMaiorConsumo();
    }

    /**
     * Obtém os três dispositivos mais utilizados por tempo total ligado numa
     * casa.
     *
     * @param casaId identificador da casa
     * @return iterador sobre os resumos dos dispositivos encontrados
     */
    public Iterator<ResumoDispositivoUso> getTop3DispositivosPorTempo(String casaId) {
        return this.gestorCasas.getTopDispositivosPorTempo(casaId, 3);
    }

    /**
     * Obtém os três dispositivos mais utilizados por número de ativações numa
     * casa.
     *
     * @param casaId identificador da casa
     * @return iterador sobre os resumos dos dispositivos encontrados
     */
    public Iterator<ResumoDispositivoUso> getTop3DispositivosPorAtivacoes(String casaId) {
        return this.gestorCasas.getTopDispositivosPorAtivacoes(casaId, 3);
    }

    /**
     * Obtém as três divisões com mais dispositivos, indicando a casa respetiva.
     *
     * @return iterador sobre os resumos das divisões encontradas
     */
    public Iterator<ResumoDivisaoDispositivos> getTop3DivisoesComMaisDispositivos() {
        return this.gestorCasas.getTopDivisoesComMaisDispositivos(3);
    }

    /**
     * Produz um resumo textual do consumo total das casas registadas.
     *
     * @return texto com o consumo total por casa
     */
    public String getResumoConsumoCasas() {
        return this.gestorCasas.getResumoConsumoCasas();
    }

    /**
     * Dá acesso à data e hora atuais do relógio simulado.
     *
     * @return data e hora atuais
     */
    public LocalDateTime getDataHoraAtual() {
        return this.relogio.getDataHoraAtual();
    }

    /**
     * Grava o estado completo desta fachada num ficheiro binário.
     *
     * Se o caminho for inválido ou ocorrer uma falha de escrita, a operação é
     * ignorada, mantendo o comportamento silencioso do domínio.
     *
     * @param filepath caminho do ficheiro onde gravar o estado
     */
    public void gravarEstado(String filepath) {
        if (filepath == null) {
            return;
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filepath))) {
            out.writeObject(this);
        } catch (IOException e) {
            return;
        }
    }

    /**
     * Carrega o estado completo da fachada a partir de um ficheiro binário.
     *
     * Se o caminho for inválido ou se o ficheiro não contiver uma instância
     * válida de {@code DomiUM}, é devolvida uma nova fachada vazia.
     *
     * @param filepath caminho do ficheiro de onde carregar o estado
     * @return DomiUM carregada, ou uma nova instância vazia se o carregamento
     *         falhar
     */
    public static DomiUM carregarEstado(String filepath) {
        if (filepath == null) {
            return new DomiUM();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filepath))) {
            Object objeto = in.readObject();
            if (objeto instanceof DomiUM) {
                return (DomiUM) objeto;
            }
        } catch (IOException e) {
            return new DomiUM();
        } catch (ClassNotFoundException e) {
            return new DomiUM();
        } catch (ClassCastException e) {
            return new DomiUM();
        }

        return new DomiUM();
    }

    /**
     * Executa uma operação genérica sobre um dispositivo de uma casa.
     *
     * A operação só é delegada se os dados forem válidos e se o utilizador tiver
     * permissão de utilização sobre a casa indicada.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param operacao operação a aplicar ao dispositivo
     */
    public void executarOperacaoDispositivo(String utilizadorId, String casaId,
                                            String dispositivoId, OperacaoDispositivo operacao) {
        executarOperacaoDispositivoComDescricao(
                utilizadorId, casaId, dispositivoId, operacao, "Operação sobre dispositivo"
        );
    }

    /**
     * Executa uma operação genérica sobre um dispositivo de uma casa e regista
     * a interação no histórico do utilizador quando a operação é bem-sucedida.
     *
     * A operação só é delegada se os dados forem válidos e se o utilizador tiver
     * permissão de utilização sobre a casa indicada.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param operacao operação a aplicar ao dispositivo
     * @param descricaoAcao descrição da ação realizada
     */
    public void executarOperacaoDispositivoComDescricao(String utilizadorId, String casaId,
                                                        String dispositivoId,
                                                        OperacaoDispositivo operacao,
                                                        String descricaoAcao) {
        if (utilizadorId == null || casaId == null || dispositivoId == null || operacao == null) {
            return;
        }

        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        if (this.gestorCasas.executarOperacaoDispositivo(casaId, dispositivoId, operacao)) {
            String acao = descricaoAcao;
            if (acao == null || acao.isEmpty()) {
                acao = "Operação sobre dispositivo";
            }

            RegistoInteracao registo = new RegistoInteracao(
                    this.relogio.getDataHoraAtual(), casaId, dispositivoId, acao
            );
            this.gestorUtilizadores.registarInteracao(utilizadorId, registo);
        }
    }

    /**
     * Executa um comando sobre esta fachada do domínio.
     *
     * Se o comando for inválido, a operação é ignorada.
     *
     * @param cmd comando a executar
     */
    public void executarComando(Command cmd) {
        if (cmd != null) {
            cmd.execute(this);
        }
    }

    /**
     * Executa um comando depois de validar o seu contexto de domínio.
     *
     * Este método deve ser usado por fluxos interativos em que seja necessário
     * reportar erros ao utilizador. O método {@link #executarComando(Command)}
     * mantém o comportamento silencioso usado por cenários, automações e
     * escalonamentos.
     *
     * @param cmd comando a executar
     * @throws DomusException se o comando for inválido ou não puder ser
     *         executado no contexto indicado
     */
    public void executarComandoValidado(Command cmd) throws DomusException {
        if (cmd == null) {
            throw new OperacaoInvalidaException("Comando inválido.");
        }

        if (cmd instanceof ComandoDispositivo) {
            ComandoDispositivo comando = (ComandoDispositivo) cmd;
            String utilizadorId = comando.getUtilizadorId();
            String casaId = comando.getCasaId();
            String dispositivoId = comando.getDispositivoId();

            if (utilizadorId == null || casaId == null || dispositivoId == null) {
                throw new OperacaoInvalidaException("Comando de dispositivo inválido.");
            }
            if (!this.gestorUtilizadores.existeUtilizador(utilizadorId)) {
                throw new UtilizadorNaoExisteException(utilizadorId);
            }
            if (!this.gestorCasas.existeCasa(casaId)) {
                throw new CasaNaoExisteException(casaId);
            }
            if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
                throw new SemPermissaoException(utilizadorId, casaId);
            }
            Dispositivo dispositivo = this.gestorCasas.getDispositivo(casaId, dispositivoId);
            if (dispositivo == null) {
                throw new DispositivoNaoExisteException(casaId, dispositivoId);
            }
            if (!comando.suportaDispositivo(dispositivo)) {
                throw new OperacaoInvalidaException(
                        "Comando incompatível com o tipo do dispositivo: " + dispositivoId
                );
            }
        }

        cmd.execute(this);
    }

    /**
     * Adiciona uma nova divisão a uma casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param divisaoNome nome da nova divisão
     * @throws UtilizadorNaoExisteException se o utilizador não existir
     * @throws CasaNaoExisteException se a casa não existir
     * @throws SemPermissaoException se o utilizador não tiver permissão de
     *         administração sobre a casa
     * @throws DivisaoJaExisteException se já existir uma divisão com o nome
     *         indicado
     */
    public void adicionarDivisao(String utilizadorId, String casaId, String divisaoNome) throws UtilizadorNaoExisteException, CasaNaoExisteException, SemPermissaoException, DivisaoJaExisteException {
        if (!this.gestorUtilizadores.existeUtilizador(utilizadorId)) {
            throw new UtilizadorNaoExisteException(utilizadorId);
        }
        if (!this.gestorCasas.existeCasa(casaId)) {
            throw new CasaNaoExisteException(casaId);
        }
        if (!temPermissaoAdmin(utilizadorId, casaId)) {
            throw new SemPermissaoException(utilizadorId, casaId);
        }

        this.gestorCasas.adicionarDivisao(casaId, divisaoNome);
    }

    /**
     * Adiciona um novo dispositivo a uma divisão de uma casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param divisao nome da divisão de destino
     * @param tipo tipo textual do dispositivo
     * @param dispositivoId identificador do dispositivo
     * @param marca marca do dispositivo
     * @param modelo modelo do dispositivo
     * @param consumoPorHora consumo base por hora
     * @throws UtilizadorNaoExisteException se o utilizador não existir
     * @throws CasaNaoExisteException se a casa não existir
     * @throws SemPermissaoException se o utilizador não tiver permissão de
     *         administração sobre a casa
     * @throws DivisaoNaoExisteException se a divisão não existir na casa
     * @throws DispositivoJaExisteException se já existir um dispositivo com o
     *         identificador indicado
     * @throws TipoDispositivoInvalidoException se o tipo textual de dispositivo
     *         não for suportado
     */
    public void adicionarDispositivo(String utilizadorId, String casaId, String divisao,
                                     String tipo, String dispositivoId, String marca,
                                     String modelo, double consumoPorHora) throws UtilizadorNaoExisteException, CasaNaoExisteException, SemPermissaoException, DivisaoNaoExisteException, DispositivoJaExisteException, TipoDispositivoInvalidoException {
        if (!this.gestorUtilizadores.existeUtilizador(utilizadorId)) {
            throw new UtilizadorNaoExisteException(utilizadorId);
        }
        if (!this.gestorCasas.existeCasa(casaId)) {
            throw new CasaNaoExisteException(casaId);
        }
        if (!temPermissaoAdmin(utilizadorId, casaId)) {
            throw new SemPermissaoException(utilizadorId, casaId);
        }

        this.gestorCasas.adicionarDispositivo(
                casaId, divisao, tipo, dispositivoId, marca, modelo, consumoPorHora
        );
    }

    /**
     * Avança o relógio simulado e verifica os escalonamentos das casas.
     *
     * O avanço é processado internamente em passos de um minuto para que as
     * ações temporais intermédias possam afetar a acumulação de tempo ligado.
     *
     * @param minutos minutos a avançar
     */
    public void avancarTempo(int minutos) {
        if (minutos <= 0) {
            return;
        }

        for (int i = 0; i < minutos; i++) {
            LocalTime horaAnterior = this.relogio.getHoraAtual();
            this.gestorCasas.acumularTempoDispositivosLigados(1);
            this.relogio.avancarTempo(1);
            LocalTime horaAtual = this.relogio.getHoraAtual();
            verificarEscalonamentos(horaAnterior, horaAtual);
        }
    }

    /**
     * Verifica os escalonamentos das casas para o intervalo temporal indicado.
     *
     * @param horaAnterior hora antes do avanço
     * @param horaAtual hora depois do avanço
     */
    private void verificarEscalonamentos(LocalTime horaAnterior, LocalTime horaAtual) {
        Iterator<Casa> iteradorCasas = this.gestorCasas.getIteradorCasas();
        while (iteradorCasas.hasNext()) {
            Casa casa = iteradorCasas.next();
            Iterator<Escalonamento> iteradorEscalonamentos = casa.getIteradorEscalonamentos();
            while (iteradorEscalonamentos.hasNext()) {
                iteradorEscalonamentos.next().verificarEExecutar(horaAnterior, horaAtual, this);
            }
        }
    }

    /**
     * Cria um novo cenário numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param cenarioId identificador do cenário
     * @param nome nome do cenário
     * @throws UtilizadorNaoExisteException se o utilizador não existir
     * @throws CasaNaoExisteException se a casa não existir
     * @throws SemPermissaoException se o utilizador não tiver permissão de
     *         utilização sobre a casa
     * @throws CenarioJaExisteException se já existir um cenário com o
     *         identificador indicado
     */
    public void criarCenario(String utilizadorId, String casaId, String cenarioId, String nome) throws UtilizadorNaoExisteException, CasaNaoExisteException, SemPermissaoException, CenarioJaExisteException {
        if (!this.gestorUtilizadores.existeUtilizador(utilizadorId)) {
            throw new UtilizadorNaoExisteException(utilizadorId);
        }
        if (!this.gestorCasas.existeCasa(casaId)) {
            throw new CasaNaoExisteException(casaId);
        }
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            throw new SemPermissaoException(utilizadorId, casaId);
        }

        this.gestorCasas.criarCenario(casaId, cenarioId, nome);
    }

    /**
     * Acrescenta um comando a um cenário existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param cenarioId identificador do cenário
     * @param cmd comando a acrescentar
     * @throws UtilizadorNaoExisteException se o utilizador não existir
     * @throws CasaNaoExisteException se a casa não existir
     * @throws SemPermissaoException se o utilizador não tiver permissão de
     *         utilização sobre a casa
     * @throws CenarioNaoExisteException se o cenário não existir na casa
     */
    public void adicionarComandoACenario(String utilizadorId, String casaId, String cenarioId, Command cmd) throws UtilizadorNaoExisteException, CasaNaoExisteException, SemPermissaoException, CenarioNaoExisteException {
        if (cenarioId == null || cmd == null) {
            return;
        }
        if (!this.gestorUtilizadores.existeUtilizador(utilizadorId)) {
            throw new UtilizadorNaoExisteException(utilizadorId);
        }
        if (!this.gestorCasas.existeCasa(casaId)) {
            throw new CasaNaoExisteException(casaId);
        }
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            throw new SemPermissaoException(utilizadorId, casaId);
        }
        if (!comandoPertenceAoContexto(utilizadorId, casaId, cmd)) {
            return;
        }

        this.gestorCasas.adicionarComandoACenario(casaId, cenarioId, cmd);
    }

    /**
     * Executa um cenário existente numa casa.
     *
     * O cenário é obtido por cópia e executado sobre esta fachada, permitindo
     * que os comandos atualizem diretamente o estado interno da DomiUM.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param cenarioId identificador do cenário
     * @throws UtilizadorNaoExisteException se o utilizador não existir
     * @throws CasaNaoExisteException se a casa não existir
     * @throws SemPermissaoException se o utilizador não tiver permissão de
     *         utilização sobre a casa
     * @throws CenarioNaoExisteException se o cenário não existir na casa
     */
    public void executarCenario(String utilizadorId, String casaId, String cenarioId) throws UtilizadorNaoExisteException, CasaNaoExisteException, SemPermissaoException, CenarioNaoExisteException {
        if (cenarioId == null) {
            return;
        }
        if (!this.gestorUtilizadores.existeUtilizador(utilizadorId)) {
            throw new UtilizadorNaoExisteException(utilizadorId);
        }
        if (!this.gestorCasas.existeCasa(casaId)) {
            throw new CasaNaoExisteException(casaId);
        }
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            throw new SemPermissaoException(utilizadorId, casaId);
        }

        Cenario cenario = this.gestorCasas.getCenario(casaId, cenarioId);
        if (cenario == null) {
            throw new CenarioNaoExisteException(casaId, cenarioId);
        }
        cenario.executar(this);
    }

    /**
     * Atualiza o ambiente interior de uma divisão e verifica as automações
     * associadas a essa divisão.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     * @param temperatura nova temperatura interior
     * @param humidade nova humidade interior
     * @param luminosidade nova luminosidade interior
     * @throws UtilizadorNaoExisteException se o utilizador não existir
     * @throws CasaNaoExisteException se a casa não existir
     * @throws SemPermissaoException se o utilizador não tiver permissão de
     *         utilização sobre a casa
     * @throws DivisaoNaoExisteException se a divisão não existir na casa
     */
    public void atualizarAmbienteDivisao(String utilizadorId, String casaId, String divisaoNome,
                                         double temperatura, double humidade, double luminosidade) throws UtilizadorNaoExisteException, CasaNaoExisteException, SemPermissaoException, DivisaoNaoExisteException {
        if (divisaoNome == null) {
            return;
        }
        if (!this.gestorUtilizadores.existeUtilizador(utilizadorId)) {
            throw new UtilizadorNaoExisteException(utilizadorId);
        }
        if (!this.gestorCasas.existeCasa(casaId)) {
            throw new CasaNaoExisteException(casaId);
        }
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            throw new SemPermissaoException(utilizadorId, casaId);
        }

        this.gestorCasas.atualizarAmbienteDivisao(casaId, divisaoNome, temperatura, humidade, luminosidade);

        AmbienteInterior ambiente = this.gestorCasas.getAmbienteInteriorDivisao(casaId, divisaoNome);
        if (ambiente == null) {
            return;
        }

        Iterator<Automacao> iteradorAutomacoes = this.gestorCasas.getIteradorAutomacoes(casaId);
        while (iteradorAutomacoes.hasNext()) {
            Automacao automacao = iteradorAutomacoes.next();
            if (automacao.aplicaA(divisaoNome)) {
                automacao.verificarEExecutar(ambiente, this);
            }
        }
    }

    /**
     * Cria uma nova automação numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param automacaoId identificador da automação
     * @param nome nome da automação
     * @param divisaoNome nome da divisão associada
     * @param condicao condição que ativa a automação
     * @throws UtilizadorNaoExisteException se o utilizador não existir
     * @throws CasaNaoExisteException se a casa não existir
     * @throws SemPermissaoException se o utilizador não tiver permissão de
     *         utilização sobre a casa
     * @throws DivisaoNaoExisteException se a divisão associada não existir na
     *         casa
     * @throws AutomacaoJaExisteException se já existir uma automação com o
     *         identificador indicado
     */
    public void criarAutomacao(String utilizadorId, String casaId, String automacaoId,
                               String nome, String divisaoNome, Condicao condicao) throws UtilizadorNaoExisteException, CasaNaoExisteException, SemPermissaoException, DivisaoNaoExisteException, AutomacaoJaExisteException {
        if (!this.gestorUtilizadores.existeUtilizador(utilizadorId)) {
            throw new UtilizadorNaoExisteException(utilizadorId);
        }
        if (!this.gestorCasas.existeCasa(casaId)) {
            throw new CasaNaoExisteException(casaId);
        }
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            throw new SemPermissaoException(utilizadorId, casaId);
        }

        this.gestorCasas.criarAutomacao(casaId, automacaoId, nome, divisaoNome, condicao);
    }

    /**
     * Acrescenta uma ação a uma automação existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param automacaoId identificador da automação
     * @param cmd comando a acrescentar como ação
     * @throws UtilizadorNaoExisteException se o utilizador não existir
     * @throws CasaNaoExisteException se a casa não existir
     * @throws SemPermissaoException se o utilizador não tiver permissão de
     *         utilização sobre a casa
     * @throws AutomacaoNaoExisteException se a automação não existir na casa
     */
    public void adicionarAcaoAAutomacao(String utilizadorId, String casaId,
                                        String automacaoId, Command cmd) throws UtilizadorNaoExisteException, CasaNaoExisteException, SemPermissaoException, AutomacaoNaoExisteException {
        if (automacaoId == null || cmd == null) {
            return;
        }
        if (!this.gestorUtilizadores.existeUtilizador(utilizadorId)) {
            throw new UtilizadorNaoExisteException(utilizadorId);
        }
        if (!this.gestorCasas.existeCasa(casaId)) {
            throw new CasaNaoExisteException(casaId);
        }
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            throw new SemPermissaoException(utilizadorId, casaId);
        }
        if (!comandoPertenceAoContexto(utilizadorId, casaId, cmd)) {
            return;
        }

        this.gestorCasas.adicionarAcaoAAutomacao(casaId, automacaoId, cmd);
    }

    /**
     * Cria um novo escalonamento numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param escalonamentoId identificador do escalonamento
     * @param nome nome do escalonamento
     * @param horaInicio hora de início
     * @param horaFim hora de fim
     * @throws UtilizadorNaoExisteException se o utilizador não existir
     * @throws CasaNaoExisteException se a casa não existir
     * @throws SemPermissaoException se o utilizador não tiver permissão de
     *         utilização sobre a casa
     * @throws EscalonamentoJaExisteException se já existir um escalonamento com
     *         o identificador indicado
     */
    public void criarEscalonamento(String utilizadorId, String casaId, String escalonamentoId,
                                   String nome, LocalTime horaInicio, LocalTime horaFim) throws UtilizadorNaoExisteException, CasaNaoExisteException, SemPermissaoException, EscalonamentoJaExisteException {
        if (!this.gestorUtilizadores.existeUtilizador(utilizadorId)) {
            throw new UtilizadorNaoExisteException(utilizadorId);
        }
        if (!this.gestorCasas.existeCasa(casaId)) {
            throw new CasaNaoExisteException(casaId);
        }
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            throw new SemPermissaoException(utilizadorId, casaId);
        }

        this.gestorCasas.criarEscalonamento(casaId, escalonamentoId, nome, horaInicio, horaFim);
    }

    /**
     * Acrescenta uma ação de início a um escalonamento existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param escalonamentoId identificador do escalonamento
     * @param cmd comando a acrescentar como ação de início
     * @throws UtilizadorNaoExisteException se o utilizador não existir
     * @throws CasaNaoExisteException se a casa não existir
     * @throws SemPermissaoException se o utilizador não tiver permissão de
     *         utilização sobre a casa
     * @throws EscalonamentoNaoExisteException se o escalonamento não existir na
     *         casa
     */
    public void adicionarAcaoInicioAEscalonamento(String utilizadorId, String casaId,
                                                  String escalonamentoId, Command cmd) throws UtilizadorNaoExisteException, CasaNaoExisteException, SemPermissaoException, EscalonamentoNaoExisteException {
        if (escalonamentoId == null || cmd == null) {
            return;
        }
        if (!this.gestorUtilizadores.existeUtilizador(utilizadorId)) {
            throw new UtilizadorNaoExisteException(utilizadorId);
        }
        if (!this.gestorCasas.existeCasa(casaId)) {
            throw new CasaNaoExisteException(casaId);
        }
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            throw new SemPermissaoException(utilizadorId, casaId);
        }
        if (!comandoPertenceAoContexto(utilizadorId, casaId, cmd)) {
            return;
        }

        this.gestorCasas.adicionarAcaoInicioAEscalonamento(casaId, escalonamentoId, cmd);
    }

    /**
     * Acrescenta uma ação de fim a um escalonamento existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param escalonamentoId identificador do escalonamento
     * @param cmd comando a acrescentar como ação de fim
     * @throws UtilizadorNaoExisteException se o utilizador não existir
     * @throws CasaNaoExisteException se a casa não existir
     * @throws SemPermissaoException se o utilizador não tiver permissão de
     *         utilização sobre a casa
     * @throws EscalonamentoNaoExisteException se o escalonamento não existir na
     *         casa
     */
    public void adicionarAcaoFimAEscalonamento(String utilizadorId, String casaId,
                                               String escalonamentoId, Command cmd) throws UtilizadorNaoExisteException, CasaNaoExisteException, SemPermissaoException, EscalonamentoNaoExisteException {
        if (escalonamentoId == null || cmd == null) {
            return;
        }
        if (!this.gestorUtilizadores.existeUtilizador(utilizadorId)) {
            throw new UtilizadorNaoExisteException(utilizadorId);
        }
        if (!this.gestorCasas.existeCasa(casaId)) {
            throw new CasaNaoExisteException(casaId);
        }
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            throw new SemPermissaoException(utilizadorId, casaId);
        }
        if (!comandoPertenceAoContexto(utilizadorId, casaId, cmd)) {
            return;
        }

        this.gestorCasas.adicionarAcaoFimAEscalonamento(casaId, escalonamentoId, cmd);
    }

    /**
     * Verifica se um utilizador tem permissão de administração sobre uma casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @return {@code true} se existir permissão de administração
     */
    private boolean temPermissaoAdmin(String utilizadorId, String casaId) {
        return this.gestorUtilizadores.temPermissaoAdmin(utilizadorId, casaId);
    }

    /**
     * Verifica se um utilizador tem permissão de utilização sobre uma casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @return {@code true} se existir permissão de utilização
     */
    private boolean temPermissaoUtilizacao(String utilizadorId, String casaId) {
        return this.gestorUtilizadores.temPermissaoUtilizacao(utilizadorId, casaId);
    }

    /**
     * Verifica se um comando pertence ao contexto onde está a ser adicionado.
     *
     * Para comandos de dispositivo, os identificadores guardados no comando
     * devem corresponder à casa e ao utilizador recebidos pela fachada.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param cmd comando a validar
     * @return {@code true} se o comando puder ser associado ao contexto
     */
    private boolean comandoPertenceAoContexto(String utilizadorId, String casaId, Command cmd) {
        if (!(cmd instanceof ComandoDispositivo)) {
            return true;
        }

        ComandoDispositivo comando = (ComandoDispositivo) cmd;
        return Objects.equals(comando.getCasaId(), casaId)
                && Objects.equals(comando.getUtilizadorId(), utilizadorId);
    }

    /**
     * Compara esta fachada com outro objeto com base no seu estado relevante.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem a mesma DomiUM lógica
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DomiUM domiUM = (DomiUM) o;
        return Objects.equals(this.gestorUtilizadores, domiUM.gestorUtilizadores)
                && Objects.equals(this.gestorCasas, domiUM.gestorCasas)
                && Objects.equals(this.relogio, domiUM.relogio);
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão da DomiUM
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.gestorUtilizadores, this.gestorCasas, this.relogio);
    }

    /**
     * Produz uma representação textual legível da fachada do domínio.
     *
     * @return texto descritivo da DomiUM
     */
    @Override
    public String toString() {
        return "DomiUM{"
                + "gestorUtilizadores=" + this.gestorUtilizadores
                + ", gestorCasas=" + this.gestorCasas
                + ", relogio=" + this.relogio
                + '}';
    }

    /**
     * Cria uma cópia profunda desta fachada do domínio.
     *
     * @return nova DomiUM com o mesmo estado lógico
     */
    public DomiUM clone() {
        return new DomiUM(
                this.gestorUtilizadores.clone(),
                this.gestorCasas.clone(),
                this.relogio.clone()
        );
    }

    /**
     * Cria uma nova DomiUM a partir de gestores já existentes.
     *
     * @param gestorUtilizadores gestor de utilizadores a associar
     * @param gestorCasas gestor de casas a associar
     * @param relogio relógio a associar
     */
    private DomiUM(GestorUtilizadores gestorUtilizadores, GestorCasas gestorCasas,
                   RelogioSistema relogio) {
        this.gestorUtilizadores = gestorUtilizadores;
        this.gestorCasas = gestorCasas;
        this.relogio = relogio;
    }
}

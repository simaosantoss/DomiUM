package domus.domain;

import domus.domain.automation.Automacao;
import domus.domain.commands.Command;
import domus.domain.commands.ComandoDispositivo;
import domus.domain.conditions.Condicao;
import domus.domain.core.Casa;
import domus.domain.core.TipoPermissao;
import domus.domain.core.Utilizador;
import domus.domain.environment.AmbienteInterior;
import domus.domain.managers.GestorCasas;
import domus.domain.managers.GestorUtilizadores;
import domus.domain.scheduling.Escalonamento;
import domus.domain.scenarios.Cenario;
import domus.domain.time.RelogioSistema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Objects;

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
     */
    public void criarUtilizador(String id, String nome) {
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
     * Cria uma nova casa no sistema e atribui ao utilizador criador a permissão
     * de administração sobre essa casa.
     *
     * @param utilizadorId identificador do utilizador criador
     * @param casaId identificador da casa
     * @param nome nome da casa
     */
    public void criarCasa(String utilizadorId, String casaId, String nome) {
        if (utilizadorId == null || casaId == null || nome == null) {
            return;
        }

        if (!this.gestorUtilizadores.existeUtilizador(utilizadorId)
                || this.gestorCasas.existeCasa(casaId)) {
            return;
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
     * Dá acesso à data e hora atuais do relógio simulado.
     *
     * @return data e hora atuais
     */
    public LocalDateTime getDataHoraAtual() {
        return this.relogio.getDataHoraAtual();
    }

    /**
     * Adiciona uma nova divisão a uma casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param divisaoNome nome da nova divisão
     */
    public void adicionarDivisao(String utilizadorId, String casaId, String divisaoNome) {
        if (!temPermissaoAdmin(utilizadorId, casaId)) {
            return;
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
     */
    public void adicionarDispositivo(String utilizadorId, String casaId, String divisao,
                                     String tipo, String dispositivoId, String marca,
                                     String modelo, double consumoPorHora) {
        if (!temPermissaoAdmin(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.adicionarDispositivo(
                casaId, divisao, tipo, dispositivoId, marca, modelo, consumoPorHora
        );
    }

    /**
     * Liga um dispositivo existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public void ligarDispositivo(String utilizadorId, String casaId, String dispositivoId) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.ligarDispositivo(casaId, dispositivoId);
    }

    /**
     * Desliga um dispositivo existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public void desligarDispositivo(String utilizadorId, String casaId, String dispositivoId) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.desligarDispositivo(casaId, dispositivoId);
    }

    /**
     * Avança o relógio simulado e verifica os escalonamentos das casas.
     *
     * @param minutos minutos a avançar
     */
    public void avancarTempo(int minutos) {
        if (minutos <= 0) {
            return;
        }

        LocalTime horaAnterior = this.relogio.getHoraAtual();
        this.relogio.avancarTempo(minutos);
        LocalTime horaAtual = this.relogio.getHoraAtual();

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
     * Define a intensidade luminosa de uma lâmpada existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param intensidade nova intensidade luminosa
     */
    public void definirIntensidadeLampada(String utilizadorId, String casaId,
                                          String dispositivoId, int intensidade) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.definirIntensidadeLampada(casaId, dispositivoId, intensidade);
    }

    /**
     * Define a temperatura de cor de uma lâmpada existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param corK nova temperatura de cor, em Kelvin
     */
    public void definirCorLampada(String utilizadorId, String casaId, String dispositivoId, int corK) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.definirCorLampada(casaId, dispositivoId, corK);
    }

    /**
     * Define a percentagem de abertura de uma cortina existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param percentagemAbertura nova percentagem de abertura
     */
    public void definirAberturaCortina(String utilizadorId, String casaId,
                                       String dispositivoId, int percentagemAbertura) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.definirAberturaCortina(casaId, dispositivoId, percentagemAbertura);
    }

    /**
     * Define o volume de uma coluna existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param volume novo volume
     */
    public void definirVolumeColuna(String utilizadorId, String casaId, String dispositivoId, int volume) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.definirVolumeColuna(casaId, dispositivoId, volume);
    }

    /**
     * Define a playlist atual de uma coluna existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param playlist nova playlist atual
     */
    public void definirPlaylistColuna(String utilizadorId, String casaId,
                                      String dispositivoId, String playlist) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.definirPlaylistColuna(casaId, dispositivoId, playlist);
    }

    /**
     * Define a temperatura alvo de um ar condicionado existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param temperatura nova temperatura alvo
     */
    public void definirTemperaturaArCondicionado(String utilizadorId, String casaId,
                                                 String dispositivoId, double temperatura) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.definirTemperaturaArCondicionado(casaId, dispositivoId, temperatura);
    }

    /**
     * Define o modo de funcionamento de um ar condicionado existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param modo novo modo de funcionamento
     */
    public void definirModoArCondicionado(String utilizadorId, String casaId,
                                          String dispositivoId, String modo) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.definirModoArCondicionado(casaId, dispositivoId, modo);
    }

    /**
     * Tranca uma fechadura existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public void trancarFechadura(String utilizadorId, String casaId, String dispositivoId) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.trancarFechadura(casaId, dispositivoId);
    }

    /**
     * Destranca uma fechadura existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public void destrancarFechadura(String utilizadorId, String casaId, String dispositivoId) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.destrancarFechadura(casaId, dispositivoId);
    }

    /**
     * Define a humidade alvo de um desumidificador existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param humidade nova humidade alvo
     */
    public void definirHumidadeDesumidificador(String utilizadorId, String casaId,
                                               String dispositivoId, double humidade) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.definirHumidadeDesumidificador(casaId, dispositivoId, humidade);
    }

    /**
     * Abre um portão de garagem existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public void abrirPortao(String utilizadorId, String casaId, String dispositivoId) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.abrirPortao(casaId, dispositivoId);
    }

    /**
     * Fecha um portão de garagem existente numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public void fecharPortao(String utilizadorId, String casaId, String dispositivoId) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        this.gestorCasas.fecharPortao(casaId, dispositivoId);
    }

    /**
     * Cria um novo cenário numa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param cenarioId identificador do cenário
     * @param nome nome do cenário
     */
    public void criarCenario(String utilizadorId, String casaId, String cenarioId, String nome) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
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
     */
    public void adicionarComandoACenario(String utilizadorId, String casaId, String cenarioId, Command cmd) {
        if (cenarioId == null || cmd == null || !temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
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
     */
    public void executarCenario(String utilizadorId, String casaId, String cenarioId) {
        if (cenarioId == null || !temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        Cenario cenario = this.gestorCasas.getCenario(casaId, cenarioId);
        if (cenario != null) {
            cenario.executar(this);
        }
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
     */
    public void atualizarAmbienteDivisao(String utilizadorId, String casaId, String divisaoNome,
                                         double temperatura, double humidade, double luminosidade) {
        if (divisaoNome == null || !temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        if (!this.gestorCasas.atualizarAmbienteDivisao(
                casaId, divisaoNome, temperatura, humidade, luminosidade)) {
            return;
        }

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
     */
    public void criarAutomacao(String utilizadorId, String casaId, String automacaoId,
                               String nome, String divisaoNome, Condicao condicao) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
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
     */
    public void adicionarAcaoAAutomacao(String utilizadorId, String casaId,
                                        String automacaoId, Command cmd) {
        if (automacaoId == null || cmd == null || !temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
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
     */
    public void criarEscalonamento(String utilizadorId, String casaId, String escalonamentoId,
                                   String nome, LocalTime horaInicio, LocalTime horaFim) {
        if (!temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
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
     */
    public void adicionarAcaoInicioAEscalonamento(String utilizadorId, String casaId,
                                                  String escalonamentoId, Command cmd) {
        if (escalonamentoId == null || cmd == null || !temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
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
     */
    public void adicionarAcaoFimAEscalonamento(String utilizadorId, String casaId,
                                               String escalonamentoId, Command cmd) {
        if (escalonamentoId == null || cmd == null || !temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
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

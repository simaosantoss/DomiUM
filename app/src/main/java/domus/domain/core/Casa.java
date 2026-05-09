package domus.domain.core;

import domus.domain.automation.Automacao;
import domus.domain.commands.Command;
import domus.domain.devices.Dispositivo;
import domus.domain.devices.OperacaoDispositivo;
import domus.domain.scheduling.Escalonamento;
import domus.domain.scenarios.Cenario;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Representa uma casa no sistema DomusControl.
 *
 * Nesta fase do projeto, a casa mantém apenas a sua identidade, o nome e o
 * conjunto de divisões que lhe pertencem.
 */
public class Casa implements Serializable {

    /**
     * Identificador único da casa.
     */
    private final String id;

    /**
     * Nome da casa.
     */
    private final String nome;

    /**
     * Divisões associadas à casa, indexadas pelo respetivo nome.
     */
    private final Map<String, Divisao> divisoes;

    /**
     * Cenários associados à casa, indexados pelo respetivo identificador.
     */
    private final Map<String, Cenario> cenarios;

    /**
     * Automações associadas à casa, indexadas pelo respetivo identificador.
     */
    private final Map<String, Automacao> automacoes;

    /**
     * Escalonamentos associados à casa, indexados pelo respetivo identificador.
     */
    private final Map<String, Escalonamento> escalonamentos;

    /**
     * Cria uma casa com identificador e nome.
     *
     * @param id identificador da casa
     * @param nome nome da casa
     */
    public Casa(String id, String nome) {
        this.id = id;
        this.nome = nome;
        this.divisoes = new HashMap<String, Divisao>();
        this.cenarios = new HashMap<String, Cenario>();
        this.automacoes = new HashMap<String, Automacao>();
        this.escalonamentos = new HashMap<String, Escalonamento>();
    }

    /**
     * Dá acesso ao identificador da casa.
     *
     * @return identificador da casa
     */
    public String getId() {
        return this.id;
    }

    /**
     * Dá acesso ao nome da casa.
     *
     * @return nome da casa
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Obtém uma divisão da casa a partir do seu nome.
     *
     * A divisão é devolvida por cópia, preservando o encapsulamento da
     * coleção interna.
     *
     * @param nome nome da divisão
     * @return cópia da divisão encontrada, ou {@code null} se não existir
     */
    public Divisao getDivisao(String nome) {
        Divisao divisao = this.divisoes.get(nome);
        if (divisao == null) {
            return null;
        }
        return divisao.clone();
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida das divisões da casa.
     *
     * Cada divisão é copiada antes de ser exposta, evitando fugas de referência
     * para a estrutura interna.
     *
     * @return iterador sobre uma cópia das divisões da casa
     */
    public Iterator<Divisao> getIteradorDivisoes() {
        List<Divisao> copia = this.divisoes.values().stream()
                .map(Divisao::clone)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(copia).iterator();
    }

    /**
     * Obtém um cenário da casa a partir do seu identificador.
     *
     * O cenário é devolvido por cópia, preservando o encapsulamento da coleção
     * interna.
     *
     * @param cenarioId identificador do cenário
     * @return cópia do cenário encontrado, ou {@code null} se não existir
     */
    public Cenario getCenario(String cenarioId) {
        Cenario cenario = this.cenarios.get(cenarioId);
        if (cenario == null) {
            return null;
        }
        return cenario.clone();
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida dos cenários da casa.
     *
     * Cada cenário é copiado antes de ser exposto, evitando fugas de referência
     * para a estrutura interna.
     *
     * @return iterador sobre uma cópia dos cenários da casa
     */
    public Iterator<Cenario> getIteradorCenarios() {
        List<Cenario> copia = this.cenarios.values().stream()
                .map(Cenario::clone)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(copia).iterator();
    }

    /**
     * Obtém uma automação da casa a partir do seu identificador.
     *
     * A automação é devolvida por cópia, preservando o encapsulamento da
     * coleção interna.
     *
     * @param automacaoId identificador da automação
     * @return cópia da automação encontrada, ou {@code null} se não existir
     */
    public Automacao getAutomacao(String automacaoId) {
        Automacao automacao = this.automacoes.get(automacaoId);
        if (automacao == null) {
            return null;
        }
        return automacao.clone();
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida das automações da
     * casa.
     *
     * Cada automação é copiada antes de ser exposta.
     *
     * @return iterador sobre uma cópia das automações da casa
     */
    public Iterator<Automacao> getIteradorAutomacoes() {
        List<Automacao> copia = this.automacoes.values().stream()
                .map(Automacao::clone)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(copia).iterator();
    }

    /**
     * Obtém um escalonamento da casa a partir do seu identificador.
     *
     * O escalonamento é devolvido por cópia, preservando o encapsulamento da
     * coleção interna.
     *
     * @param escalonamentoId identificador do escalonamento
     * @return cópia do escalonamento encontrado, ou {@code null} se não existir
     */
    public Escalonamento getEscalonamento(String escalonamentoId) {
        Escalonamento escalonamento = this.escalonamentos.get(escalonamentoId);
        if (escalonamento == null) {
            return null;
        }
        return escalonamento.clone();
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida dos escalonamentos da
     * casa.
     *
     * Cada escalonamento é copiado antes de ser exposto.
     *
     * @return iterador sobre uma cópia dos escalonamentos da casa
     */
    public Iterator<Escalonamento> getIteradorEscalonamentos() {
        List<Escalonamento> copia = this.escalonamentos.values().stream()
                .map(Escalonamento::clone)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(copia).iterator();
    }

    /**
     * Associa uma divisão a esta casa.
     *
     * A divisão é guardada por cópia, respeitando a composição da casa sobre as
     * suas divisões.
     *
     * @param divisao divisão a associar à casa
     */
    public void adicionarDivisao(Divisao divisao) {
        if (divisao != null) {
            this.divisoes.put(divisao.getNome(), divisao.clone());
        }
    }

    /**
     * Associa um cenário a esta casa.
     *
     * O cenário é guardado por cópia, impedindo alterações externas ao estado
     * interno da casa.
     *
     * @param cenarioId identificador do cenário
     * @param cenario cenário a associar
     */
    public void adicionarCenario(String cenarioId, Cenario cenario) {
        if (cenarioId != null && cenario != null) {
            this.cenarios.put(cenarioId, cenario.clone());
        }
    }

    /**
     * Associa uma automação a esta casa.
     *
     * A automação é guardada por cópia, impedindo alterações externas ao estado
     * interno da casa.
     *
     * @param automacaoId identificador da automação
     * @param automacao automação a associar
     */
    public void adicionarAutomacao(String automacaoId, Automacao automacao) {
        if (automacaoId != null && automacao != null) {
            this.automacoes.put(automacaoId, automacao.clone());
        }
    }

    /**
     * Associa um escalonamento a esta casa.
     *
     * O escalonamento é guardado por cópia, impedindo alterações externas ao
     * estado interno da casa.
     *
     * @param escalonamentoId identificador do escalonamento
     * @param escalonamento escalonamento a associar
     */
    public void adicionarEscalonamento(String escalonamentoId, Escalonamento escalonamento) {
        if (escalonamentoId != null && escalonamento != null) {
            this.escalonamentos.put(escalonamentoId, escalonamento.clone());
        }
    }

    /**
     * Verifica se a casa contém uma divisão com o nome indicado.
     *
     * @param nome nome da divisão
     * @return {@code true} se a divisão existir na casa
     */
    public boolean contemDivisao(String nome) {
        return nome != null && this.divisoes.containsKey(nome);
    }

    /**
     * Adiciona um dispositivo a uma divisão da casa.
     *
     * A operação delega na divisão de destino a gestão interna do dispositivo,
     * preservando a organização do domínio por responsabilidades.
     *
     * @param divisaoNome nome da divisão de destino
     * @param dispositivo dispositivo a adicionar
     * @return {@code true} se o dispositivo tiver sido adicionado
     */
    public boolean adicionarDispositivoADivisao(String divisaoNome, Dispositivo dispositivo) {
        Divisao divisao = this.divisoes.get(divisaoNome);
        if (divisao == null || dispositivo == null || divisao.contemDispositivo(dispositivo.getIdentificador())) {
            return false;
        }

        divisao.adicionarDispositivo(dispositivo);
        return true;
    }

    /**
     * Verifica se existe um dispositivo com o identificador indicado em alguma
     * divisão da casa.
     *
     * @param dispositivoId identificador do dispositivo
     * @return {@code true} se o dispositivo existir na casa
     */
    public boolean contemDispositivo(String dispositivoId) {
        if (dispositivoId == null) {
            return false;
        }

        for (Divisao divisao : this.divisoes.values()) {
            if (divisao.contemDispositivo(dispositivoId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Liga um dispositivo existente numa das divisões da casa.
     *
     * A operação é delegada à divisão que contém o dispositivo.
     *
     * @param dispositivoId identificador do dispositivo a ligar
     * @return {@code true} se o dispositivo tiver sido encontrado e processado
     */
    public boolean ligarDispositivo(String dispositivoId) {
        if (dispositivoId == null) {
            return false;
        }

        for (Divisao divisao : this.divisoes.values()) {
            if (divisao.ligarDispositivo(dispositivoId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Desliga um dispositivo existente numa das divisões da casa.
     *
     * A operação é delegada à divisão que contém o dispositivo.
     *
     * @param dispositivoId identificador do dispositivo a desligar
     * @return {@code true} se o dispositivo tiver sido encontrado e processado
     */
    public boolean desligarDispositivo(String dispositivoId) {
        if (dispositivoId == null) {
            return false;
        }

        for (Divisao divisao : this.divisoes.values()) {
            if (divisao.desligarDispositivo(dispositivoId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Aplica uma operação a um dispositivo existente numa das divisões da casa.
     *
     * A casa apenas localiza a divisão que contém o dispositivo e delega nela a
     * aplicação da operação, sem expor as divisões internas.
     *
     * @param dispositivoId identificador do dispositivo
     * @param operacao operação a aplicar ao dispositivo
     * @return {@code true} se alguma divisão tiver aplicado a operação com
     *         sucesso
     */
    public boolean aplicarOperacaoDispositivo(String dispositivoId, OperacaoDispositivo operacao) {
        if (dispositivoId == null || operacao == null) {
            return false;
        }

        for (Divisao divisao : this.divisoes.values()) {
            if (divisao.aplicarOperacaoDispositivo(dispositivoId, operacao)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Acrescenta um comando a um cenário existente.
     *
     * A operação é delegada ao cenário interno, que guarda o comando por cópia.
     *
     * @param cenarioId identificador do cenário
     * @param cmd comando a acrescentar
     * @return {@code true} se o comando tiver sido acrescentado
     */
    public boolean adicionarComandoACenario(String cenarioId, Command cmd) {
        if (cenarioId == null || cmd == null) {
            return false;
        }

        Cenario cenario = this.cenarios.get(cenarioId);
        if (cenario == null) {
            return false;
        }

        cenario.adicionarComando(cmd);
        return true;
    }

    /**
     * Acrescenta uma ação a uma automação existente.
     *
     * A operação é delegada à automação interna, que guarda a ação por cópia.
     *
     * @param automacaoId identificador da automação
     * @param cmd comando a acrescentar como ação
     * @return {@code true} se a ação tiver sido acrescentada
     */
    public boolean adicionarAcaoAAutomacao(String automacaoId, Command cmd) {
        if (automacaoId == null || cmd == null) {
            return false;
        }

        Automacao automacao = this.automacoes.get(automacaoId);
        if (automacao == null) {
            return false;
        }

        automacao.adicionarAcao(cmd);
        return true;
    }

    /**
     * Acrescenta uma ação de início a um escalonamento existente.
     *
     * @param escalonamentoId identificador do escalonamento
     * @param cmd comando a acrescentar
     * @return {@code true} se a ação tiver sido acrescentada
     */
    public boolean adicionarAcaoInicioAEscalonamento(String escalonamentoId, Command cmd) {
        if (escalonamentoId == null || cmd == null) {
            return false;
        }

        Escalonamento escalonamento = this.escalonamentos.get(escalonamentoId);
        if (escalonamento == null) {
            return false;
        }

        escalonamento.adicionarAcaoInicio(cmd);
        return true;
    }

    /**
     * Acrescenta uma ação de fim a um escalonamento existente.
     *
     * @param escalonamentoId identificador do escalonamento
     * @param cmd comando a acrescentar
     * @return {@code true} se a ação tiver sido acrescentada
     */
    public boolean adicionarAcaoFimAEscalonamento(String escalonamentoId, Command cmd) {
        if (escalonamentoId == null || cmd == null) {
            return false;
        }

        Escalonamento escalonamento = this.escalonamentos.get(escalonamentoId);
        if (escalonamento == null) {
            return false;
        }

        escalonamento.adicionarAcaoFim(cmd);
        return true;
    }

    /**
     * Calcula o consumo total dos dispositivos existentes em todas as divisões
     * da casa.
     *
     * @return consumo total da casa
     */
    public double getConsumoTotal() {
        double total = 0.0;

        for (Divisao divisao : this.divisoes.values()) {
            Iterator<Dispositivo> iterador = divisao.getIteradorDispositivos();
            while (iterador.hasNext()) {
                total += iterador.next().getConsumo();
            }
        }

        return total;
    }

    /**
     * Compara esta casa com outro objeto com base no seu estado relevante.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem a mesma casa lógica
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Casa casa = (Casa) o;
        return Objects.equals(this.id, casa.id)
                && Objects.equals(this.nome, casa.nome)
                && Objects.equals(this.divisoes, casa.divisoes)
                && Objects.equals(this.cenarios, casa.cenarios)
                && Objects.equals(this.automacoes, casa.automacoes)
                && Objects.equals(this.escalonamentos, casa.escalonamentos);
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão da casa
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.nome, this.divisoes, this.cenarios,
                this.automacoes, this.escalonamentos);
    }

    /**
     * Produz uma representação textual legível da casa.
     *
     * @return texto descritivo da casa
     */
    @Override
    public String toString() {
        return "Casa{"
                + "id='" + this.id + '\''
                + ", nome='" + this.nome + '\''
                + ", divisoes=" + this.divisoes
                + ", cenarios=" + this.cenarios
                + ", automacoes=" + this.automacoes
                + ", escalonamentos=" + this.escalonamentos
                + '}';
    }

    /**
     * Cria uma cópia profunda desta casa.
     *
     * As divisões são copiadas individualmente para garantir independência
     * entre a cópia e o objeto original.
     *
     * @return nova casa com o mesmo estado lógico
     */
    public Casa clone() {
        Casa copia = new Casa(this.id, this.nome);

        for (Map.Entry<String, Divisao> entry : this.divisoes.entrySet()) {
            copia.divisoes.put(entry.getKey(), entry.getValue().clone());
        }

        for (Map.Entry<String, Cenario> entry : this.cenarios.entrySet()) {
            copia.cenarios.put(entry.getKey(), entry.getValue().clone());
        }

        for (Map.Entry<String, Automacao> entry : this.automacoes.entrySet()) {
            copia.automacoes.put(entry.getKey(), entry.getValue().clone());
        }

        for (Map.Entry<String, Escalonamento> entry : this.escalonamentos.entrySet()) {
            copia.escalonamentos.put(entry.getKey(), entry.getValue().clone());
        }

        return copia;
    }
}

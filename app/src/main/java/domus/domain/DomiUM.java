package domus.domain;

import domus.domain.core.Casa;
import domus.domain.core.Divisao;
import domus.domain.core.TipoPermissao;
import domus.domain.core.Utilizador;
import domus.domain.devices.Dispositivo;
import domus.domain.factories.DispositivoRegistry;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Representa a fachada central do domínio DomusControl.
 *
 * Nesta fase do projeto, a DomiUM coordena a gestão de utilizadores, casas,
 * divisões e dispositivos, servindo como ponto de entrada para as operações
 * principais do sistema.
 */
public class DomiUM implements Serializable {

    /**
     * Utilizadores registados no sistema, indexados pelo respetivo identificador.
     */
    private final Map<String, Utilizador> utilizadores;

    /**
     * Casas registadas no sistema, indexadas pelo respetivo identificador.
     */
    private final Map<String, Casa> casas;

    /**
     * Registry responsável pela criação dos tipos de dispositivos suportados.
     */
    private final DispositivoRegistry dispositivoRegistry;

    /**
     * Cria uma instância vazia da DomiUM.
     *
     * Os mapas internos são inicializados com {@link HashMap} e o registry de
     * dispositivos fica pronto a usar com os tipos base do sistema.
     */
    public DomiUM() {
        this.utilizadores = new HashMap<String, Utilizador>();
        this.casas = new HashMap<String, Casa>();
        this.dispositivoRegistry = new DispositivoRegistry();
    }

    /**
     * Cria e regista um novo utilizador no sistema.
     *
     * Se o identificador ou o nome forem inválidos, ou se já existir um
     * utilizador com o mesmo identificador, a operação é ignorada.
     *
     * @param id identificador do utilizador
     * @param nome nome do utilizador
     */
    public void criarUtilizador(String id, String nome) {
        if (id == null || nome == null || this.utilizadores.containsKey(id)) {
            return;
        }

        this.utilizadores.put(id, new Utilizador(id, nome));
    }

    /**
     * Obtém um utilizador a partir do seu identificador.
     *
     * O utilizador é devolvido por cópia para preservar o encapsulamento do
     * estado interno do sistema.
     *
     * @param id identificador do utilizador
     * @return cópia do utilizador encontrado, ou {@code null} se não existir
     */
    public Utilizador getUtilizador(String id) {
        Utilizador utilizador = this.utilizadores.get(id);
        if (utilizador == null) {
            return null;
        }
        return utilizador.clone();
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida dos utilizadores do
     * sistema.
     *
     * Cada utilizador é copiado antes de ser exposto, evitando fugas de
     * referência para a coleção interna.
     *
     * @return iterador sobre uma cópia dos utilizadores registados
     */
    public Iterator<Utilizador> getIteradorUtilizadores() {
        List<Utilizador> copia = new ArrayList<Utilizador>();
        for (Utilizador utilizador : this.utilizadores.values()) {
            copia.add(utilizador.clone());
        }
        return Collections.unmodifiableList(copia).iterator();
    }

    /**
     * Cria uma nova casa no sistema e atribui ao utilizador criador a permissão
     * de administração sobre essa casa.
     *
     * Se os dados forem inválidos, se o utilizador não existir ou se já existir
     * uma casa com o mesmo identificador, a operação é ignorada.
     *
     * @param utilizadorId identificador do utilizador criador
     * @param casaId identificador da casa
     * @param nome nome da casa
     */
    public void criarCasa(String utilizadorId, String casaId, String nome) {
        if (utilizadorId == null || casaId == null || nome == null) {
            return;
        }

        Utilizador utilizador = this.utilizadores.get(utilizadorId);
        if (utilizador == null || this.casas.containsKey(casaId)) {
            return;
        }

        Casa casa = new Casa(casaId, nome);
        Utilizador utilizadorAtualizado = utilizador.clone();
        utilizadorAtualizado.adicionarPermissao(casaId, TipoPermissao.ADMIN);

        this.casas.put(casaId, casa);
        this.utilizadores.put(utilizadorId, utilizadorAtualizado);
    }

    /**
     * Obtém uma casa a partir do seu identificador.
     *
     * A casa é devolvida por cópia para preservar o encapsulamento do estado
     * interno do sistema.
     *
     * @param id identificador da casa
     * @return cópia da casa encontrada, ou {@code null} se não existir
     */
    public Casa getCasa(String id) {
        Casa casa = this.casas.get(id);
        if (casa == null) {
            return null;
        }
        return casa.clone();
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida das casas do sistema.
     *
     * Cada casa é copiada antes de ser exposta, evitando fugas de referência
     * para a coleção interna.
     *
     * @return iterador sobre uma cópia das casas registadas
     */
    public Iterator<Casa> getIteradorCasas() {
        List<Casa> copia = new ArrayList<Casa>();
        for (Casa casa : this.casas.values()) {
            copia.add(casa.clone());
        }
        return Collections.unmodifiableList(copia).iterator();
    }

    /**
     * Adiciona uma nova divisão a uma casa.
     *
     * A operação só é executada se o utilizador tiver permissão de administração
     * sobre a casa indicada.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param divisaoNome nome da nova divisão
     */
    public void adicionarDivisao(String utilizadorId, String casaId, String divisaoNome) {
        if (divisaoNome == null || !temPermissaoAdmin(utilizadorId, casaId)) {
            return;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return;
        }

        Casa casaAtualizada = casa.clone();
        if (casaAtualizada.contemDivisao(divisaoNome)) {
            return;
        }

        casaAtualizada.adicionarDivisao(new Divisao(divisaoNome));
        this.casas.put(casaId, casaAtualizada);
    }

    /**
     * Adiciona um novo dispositivo a uma divisão de uma casa.
     *
     * A operação só é executada se o utilizador tiver permissão de administração
     * sobre a casa indicada.
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
        if (divisao == null || tipo == null || dispositivoId == null || marca == null
                || modelo == null || !temPermissaoAdmin(utilizadorId, casaId)) {
            return;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return;
        }

        Casa casaAtualizada = casa.clone();
        if (!casaAtualizada.contemDivisao(divisao) || casaAtualizada.contemDispositivo(dispositivoId)) {
            return;
        }

        Dispositivo dispositivo = this.dispositivoRegistry.criar(
                tipo, dispositivoId, marca, modelo, consumoPorHora
        );

        if (dispositivo == null) {
            return;
        }

        if (casaAtualizada.adicionarDispositivoADivisao(divisao, dispositivo)) {
            this.casas.put(casaId, casaAtualizada);
        }
    }

    /**
     * Liga um dispositivo existente numa casa, se o utilizador tiver permissão
     * de utilização nessa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public void ligarDispositivo(String utilizadorId, String casaId, String dispositivoId) {
        if (dispositivoId == null || !temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return;
        }

        Casa casaAtualizada = casa.clone();
        if (casaAtualizada.ligarDispositivo(dispositivoId)) {
            this.casas.put(casaId, casaAtualizada);
        }
    }

    /**
     * Desliga um dispositivo existente numa casa, se o utilizador tiver
     * permissão de utilização nessa casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     */
    public void desligarDispositivo(String utilizadorId, String casaId, String dispositivoId) {
        if (dispositivoId == null || !temPermissaoUtilizacao(utilizadorId, casaId)) {
            return;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return;
        }

        Casa casaAtualizada = casa.clone();
        if (casaAtualizada.desligarDispositivo(dispositivoId)) {
            this.casas.put(casaId, casaAtualizada);
        }
    }

    /**
     * Verifica se um utilizador tem permissão de administração sobre uma casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @return {@code true} se existir permissão de administração
     */
    private boolean temPermissaoAdmin(String utilizadorId, String casaId) {
        Utilizador utilizador = this.utilizadores.get(utilizadorId);
        return utilizador != null && utilizador.temPermissao(casaId, TipoPermissao.ADMIN);
    }

    /**
     * Verifica se um utilizador tem permissão de utilização sobre uma casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @return {@code true} se existir permissão de utilização
     */
    private boolean temPermissaoUtilizacao(String utilizadorId, String casaId) {
        Utilizador utilizador = this.utilizadores.get(utilizadorId);
        return utilizador != null && utilizador.temPermissao(casaId, TipoPermissao.NORMAL);
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
        return Objects.equals(this.utilizadores, domiUM.utilizadores)
                && Objects.equals(this.casas, domiUM.casas)
                && Objects.equals(this.dispositivoRegistry, domiUM.dispositivoRegistry);
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão da DomiUM
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.utilizadores, this.casas, this.dispositivoRegistry);
    }

    /**
     * Produz uma representação textual legível da fachada do domínio.
     *
     * @return texto descritivo da DomiUM
     */
    @Override
    public String toString() {
        return "DomiUM{"
                + "utilizadores=" + this.utilizadores
                + ", casas=" + this.casas
                + ", dispositivoRegistry=" + this.dispositivoRegistry
                + '}';
    }

    /**
     * Cria uma cópia profunda desta fachada do domínio.
     *
     * Utilizadores, casas e registry são copiados de forma independente para
     * preservar o encapsulamento entre a cópia e o objeto original.
     *
     * @return nova DomiUM com o mesmo estado lógico
     */
    public DomiUM clone() {
        return new DomiUM(this.utilizadores, this.casas, this.dispositivoRegistry.clone());
    }

    /**
     * Cria uma nova DomiUM a partir do estado já existente.
     *
     * Este construtor privado apoia a criação de cópias profundas da fachada.
     *
     * @param utilizadores utilizadores a copiar
     * @param casas casas a copiar
     * @param dispositivoRegistry registry a associar
     */
    private DomiUM(Map<String, Utilizador> utilizadores, Map<String, Casa> casas,
                   DispositivoRegistry dispositivoRegistry) {
        this.utilizadores = new HashMap<String, Utilizador>();
        this.casas = new HashMap<String, Casa>();
        this.dispositivoRegistry = dispositivoRegistry;

        for (Map.Entry<String, Utilizador> entry : utilizadores.entrySet()) {
            this.utilizadores.put(entry.getKey(), entry.getValue().clone());
        }

        for (Map.Entry<String, Casa> entry : casas.entrySet()) {
            this.casas.put(entry.getKey(), entry.getValue().clone());
        }
    }
}

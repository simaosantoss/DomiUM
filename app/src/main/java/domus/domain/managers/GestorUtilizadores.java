package domus.domain.managers;

import domus.domain.core.TipoPermissao;
import domus.domain.core.Utilizador;
import domus.domain.exceptions.UtilizadorJaExisteException;
import domus.domain.history.RegistoInteracao;
import domus.domain.suggestions.GeradorSugestoesHistorico;
import domus.domain.suggestions.SugestaoEscalonamento;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Gere os utilizadores registados no domínio DomusControl.
 *
 * Este gestor centraliza o armazenamento dos utilizadores e a validação das
 * respetivas permissões, sem expor diretamente a coleção interna.
 */
public class GestorUtilizadores implements Serializable {

    /**
     * Utilizadores registados, indexados pelo respetivo identificador.
     */
    private final Map<String, Utilizador> utilizadores;

    /**
     * Cria um gestor de utilizadores vazio.
     */
    public GestorUtilizadores() {
        this.utilizadores = new HashMap<String, Utilizador>();
    }

    /**
     * Cria e regista um novo utilizador.
     *
     * Se o identificador ou o nome forem inválidos, a operação é ignorada.
     *
     * @param id identificador do utilizador
     * @param nome nome do utilizador
     * @throws UtilizadorJaExisteException se já existir um utilizador com o
     *         identificador indicado
     */
    public void criarUtilizador(String id, String nome) throws UtilizadorJaExisteException {
        if (id == null || nome == null) {
            return;
        }
        if (this.utilizadores.containsKey(id)) {
            throw new UtilizadorJaExisteException(id);
        }
        this.utilizadores.put(id, new Utilizador(id, nome));
    }

    /**
     * Obtém um utilizador a partir do seu identificador.
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
     * Disponibiliza um iterador sobre uma cópia protegida dos utilizadores.
     *
     * @return iterador sobre uma cópia dos utilizadores registados
     */
    public Iterator<Utilizador> getIteradorUtilizadores() {
        List<Utilizador> copia = this.utilizadores.values().stream()
                .map(Utilizador::clone)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(copia).iterator();
    }

    /**
     * Verifica se existe um utilizador com o identificador indicado.
     *
     * @param id identificador do utilizador
     * @return {@code true} se o utilizador existir
     */
    public boolean existeUtilizador(String id) {
        return id != null && this.utilizadores.containsKey(id);
    }

    /**
     * Verifica se um utilizador tem uma permissão sobre uma casa.
     *
     * A regra de precedência das permissões é delegada ao próprio utilizador.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param permissao permissão pretendida
     * @return {@code true} se o utilizador tiver permissão suficiente
     */
    public boolean temPermissao(String utilizadorId, String casaId, TipoPermissao permissao) {
        Utilizador utilizador = this.utilizadores.get(utilizadorId);
        return utilizador != null && utilizador.temPermissao(casaId, permissao);
    }

    /**
     * Verifica se um utilizador tem permissão de administração sobre uma casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @return {@code true} se existir permissão de administração
     */
    public boolean temPermissaoAdmin(String utilizadorId, String casaId) {
        return temPermissao(utilizadorId, casaId, TipoPermissao.ADMIN);
    }

    /**
     * Verifica se um utilizador tem permissão de utilização sobre uma casa.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @return {@code true} se existir permissão de utilização
     */
    public boolean temPermissaoUtilizacao(String utilizadorId, String casaId) {
        return temPermissao(utilizadorId, casaId, TipoPermissao.NORMAL);
    }

    /**
     * Atribui uma permissão de uma casa a um utilizador.
     *
     * Se algum dado for inválido ou o utilizador não existir, a operação é
     * ignorada.
     *
     * @param utilizadorId identificador do utilizador
     * @param casaId identificador da casa
     * @param permissao permissão a atribuir
     */
    public void adicionarPermissao(String utilizadorId, String casaId, TipoPermissao permissao) {
        if (utilizadorId == null || casaId == null || permissao == null) {
            return;
        }

        Utilizador utilizador = this.utilizadores.get(utilizadorId);
        if (utilizador == null) {
            return;
        }

        Utilizador utilizadorAtualizado = utilizador.clone();
        utilizadorAtualizado.adicionarPermissao(casaId, permissao);
        this.utilizadores.put(utilizadorId, utilizadorAtualizado);
    }

    /**
     * Regista uma interação no histórico de um utilizador.
     *
     * Se algum dado for inválido ou o utilizador não existir, a operação é
     * ignorada. O utilizador é atualizado por cópia para manter a disciplina de
     * encapsulamento do gestor.
     *
     * @param utilizadorId identificador do utilizador
     * @param registo registo de interação a acrescentar
     */
    public void registarInteracao(String utilizadorId, RegistoInteracao registo) {
        if (utilizadorId == null || registo == null) {
            return;
        }

        Utilizador utilizador = this.utilizadores.get(utilizadorId);
        if (utilizador == null) {
            return;
        }

        Utilizador utilizadorAtualizado = utilizador.clone();
        utilizadorAtualizado.registarInteracao(registo);
        this.utilizadores.put(utilizadorId, utilizadorAtualizado);
    }

    /**
     * Gera sugestões de escalonamento a partir do histórico de um utilizador.
     *
     * Se o identificador for inválido ou o utilizador não existir, é devolvido
     * um iterador vazio.
     *
     * @param utilizadorId identificador do utilizador
     * @param minimoOcorrencias número mínimo de ocorrências para sugerir
     * @param limite número máximo de sugestões
     * @return iterador sobre as sugestões geradas
     */
    public Iterator<SugestaoEscalonamento> getSugestoesEscalonamento(String utilizadorId, int minimoOcorrencias, int limite) {
        if (utilizadorId == null) {
            return Collections.<SugestaoEscalonamento>emptyList().iterator();
        }

        Utilizador utilizador = this.utilizadores.get(utilizadorId);
        if (utilizador == null) {
            return Collections.<SugestaoEscalonamento>emptyList().iterator();
        }

        GeradorSugestoesHistorico gerador = new GeradorSugestoesHistorico();
        return gerador.gerarSugestoesEscalonamento(
                utilizadorId, utilizador.getIteradorHistorico(), minimoOcorrencias, limite
        );
    }

    /**
     * Compara este gestor com outro objeto com base no seu estado relevante.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo gestor lógico
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        GestorUtilizadores that = (GestorUtilizadores) o;
        return Objects.equals(this.utilizadores, that.utilizadores);
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão do gestor
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.utilizadores);
    }

    /**
     * Produz uma representação textual legível do gestor.
     *
     * @return texto descritivo do gestor
     */
    @Override
    public String toString() {
        return "GestorUtilizadores{"
                + "utilizadores=" + this.utilizadores
                + '}';
    }

    /**
     * Cria uma cópia profunda deste gestor.
     *
     * @return novo gestor com o mesmo estado lógico
     */
    public GestorUtilizadores clone() {
        return new GestorUtilizadores(this.utilizadores);
    }

    /**
     * Cria um gestor a partir de utilizadores existentes.
     *
     * @param utilizadores utilizadores a copiar
     */
    private GestorUtilizadores(Map<String, Utilizador> utilizadores) {
        this.utilizadores = new HashMap<String, Utilizador>();

        for (Map.Entry<String, Utilizador> entry : utilizadores.entrySet()) {
            this.utilizadores.put(entry.getKey(), entry.getValue().clone());
        }
    }
}

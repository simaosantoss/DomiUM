package domus.domain.managers;

import domus.domain.core.TipoPermissao;
import domus.domain.core.Utilizador;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        List<Utilizador> copia = new ArrayList<Utilizador>();
        for (Utilizador utilizador : this.utilizadores.values()) {
            copia.add(utilizador.clone());
        }
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

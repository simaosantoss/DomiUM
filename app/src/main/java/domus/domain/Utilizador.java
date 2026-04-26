package domus.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Representa um utilizador do sistema DomusControl.
 *
 * Um utilizador possui uma identidade própria, permissões por casa e um
 * histórico das interações realizadas sobre dispositivos.
 */
public class Utilizador implements Serializable {

    /**
     * Identificador único do utilizador no domínio.
     */
    private final String id;

    /**
     * Nome do utilizador.
     */
    private final String nome;

    /**
     * Permissões do utilizador por casa, indexadas pelo identificador da casa.
     */
    private final Map<String, TipoPermissao> permissoes;

    /**
     * Histórico das interações efetuadas pelo utilizador.
     */
    private final List<RegistoInteracao> historico;

    /**
     * Cria um utilizador com identificador e nome.
     *
     * @param id identificador do utilizador
     * @param nome nome do utilizador
     */
    public Utilizador(String id, String nome) {
        this.id = id;
        this.nome = nome;
        this.permissoes = new HashMap<String, TipoPermissao>();
        this.historico = new ArrayList<RegistoInteracao>();
    }

    /**
     * Dá acesso ao identificador do utilizador.
     *
     * @return identificador do utilizador
     */
    public String getId() {
        return this.id;
    }

    /**
     * Dá acesso ao nome do utilizador.
     *
     * @return nome do utilizador
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida do histórico.
     *
     * Os registos são copiados antes de serem expostos, evitando fugas de
     * referência para a coleção interna.
     *
     * @return iterador sobre uma cópia do histórico do utilizador
     */
    public Iterator<RegistoInteracao> getIteradorHistorico() {
        List<RegistoInteracao> copia = new ArrayList<RegistoInteracao>();
        for (RegistoInteracao registo : this.historico) {
            copia.add(registo.clone());
        }
        return Collections.unmodifiableList(copia).iterator();
    }

    /**
     * Verifica se o utilizador possui, para uma dada casa, a permissão pedida
     * ou uma permissão superior.
     *
     * A permissão {@link TipoPermissao#ADMIN} inclui automaticamente a
     * permissão {@link TipoPermissao#NORMAL}.
     *
     * @param casaId identificador da casa
     * @param p permissão a verificar
     * @return {@code true} se a permissão existir e for suficiente
     */
    public boolean temPermissao(String casaId, TipoPermissao p) {
        TipoPermissao permissaoAtual = this.permissoes.get(casaId);

        if (permissaoAtual == null || p == null) {
            return false;
        }

        if (permissaoAtual == TipoPermissao.ADMIN) {
            return true;
        }

        return permissaoAtual == p;
    }

    /**
     * Associa uma permissão a uma casa para este utilizador.
     *
     * @param casaId identificador da casa
     * @param p permissão a atribuir
     */
    public void adicionarPermissao(String casaId, TipoPermissao p) {
        if (casaId != null && p != null) {
            this.permissoes.put(casaId, p);
        }
    }

    /**
     * Regista uma nova interação no histórico do utilizador.
     *
     * O registo é guardado por cópia para impedir alterações externas ao
     * histórico interno.
     *
     * @param registo registo a acrescentar ao histórico
     */
    public void registarInteracao(RegistoInteracao registo) {
        if (registo != null) {
            this.historico.add(registo.clone());
        }
    }

    /**
     * Compara este utilizador com outro objeto com base no estado relevante.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo utilizador
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilizador)) {
            return false;
        }
        Utilizador that = (Utilizador) o;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.nome, that.nome)
                && Objects.equals(this.permissoes, that.permissoes)
                && Objects.equals(this.historico, that.historico);
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão do utilizador
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.nome, this.permissoes, this.historico);
    }

    /**
     * Produz uma representação textual legível do utilizador.
     *
     * @return texto com os dados mais relevantes do utilizador
     */
    @Override
    public String toString() {
        return "Utilizador{"
                + "id='" + this.id + '\''
                + ", nome='" + this.nome + '\''
                + ", permissoes=" + this.permissoes
                + ", historico=" + this.historico
                + '}';
    }

    /**
     * Cria uma cópia profunda deste utilizador.
     *
     * @return novo utilizador com o mesmo estado lógico
     */
    public Utilizador clone() {
        Utilizador copia = new Utilizador(this.id, this.nome);

        for (Map.Entry<String, TipoPermissao> entry : this.permissoes.entrySet()) {
            copia.permissoes.put(entry.getKey(), entry.getValue());
        }

        for (RegistoInteracao registo : this.historico) {
            copia.historico.add(registo.clone());
        }

        return copia;
    }
}

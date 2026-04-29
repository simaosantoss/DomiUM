package domus.domain.automation;

import domus.domain.DomiUM;
import domus.domain.commands.Command;
import domus.domain.conditions.Condicao;
import domus.domain.environment.AmbienteInterior;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Representa uma automação associada ao ambiente interior de uma divisão.
 *
 * A automação possui uma condição e uma sequência de ações, executadas pela
 * ordem em que foram registadas quando a condição se verifica.
 */
public class Automacao implements Serializable {

    /**
     * Nome legível da automação.
     */
    private final String nome;

    /**
     * Nome da divisão à qual a automação está associada.
     */
    private final String divisaoNome;

    /**
     * Condição que determina se a automação deve executar as ações.
     */
    private final Condicao condicao;

    /**
     * Ações executadas quando a condição se verifica.
     */
    private final List<Command> acoes;

    /**
     * Cria uma automação associada a uma divisão.
     *
     * @param nome nome da automação
     * @param divisaoNome nome da divisão associada
     * @param condicao condição a avaliar
     */
    public Automacao(String nome, String divisaoNome, Condicao condicao) {
        this.nome = nome;
        this.divisaoNome = divisaoNome;
        this.condicao = condicao == null ? null : condicao.clone();
        this.acoes = new ArrayList<Command>();
    }

    /**
     * Dá acesso ao nome da automação.
     *
     * @return nome da automação
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Dá acesso ao nome da divisão associada.
     *
     * @return nome da divisão
     */
    public String getDivisaoNome() {
        return this.divisaoNome;
    }

    /**
     * Dá acesso à condição da automação.
     *
     * A condição é devolvida por cópia para preservar o encapsulamento.
     *
     * @return cópia da condição, ou {@code null} se não existir
     */
    public Condicao getCondicao() {
        if (this.condicao == null) {
            return null;
        }
        return this.condicao.clone();
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida das ações.
     *
     * Cada comando é copiado antes de ser exposto.
     *
     * @return iterador sobre uma cópia das ações da automação
     */
    public Iterator<Command> getIteradorAcoes() {
        List<Command> copia = new ArrayList<Command>();
        for (Command comando : this.acoes) {
            copia.add(comando.clone());
        }
        return Collections.unmodifiableList(copia).iterator();
    }

    /**
     * Acrescenta uma ação à automação.
     *
     * A ação é guardada por cópia para impedir alterações externas.
     *
     * @param cmd comando a acrescentar
     */
    public void adicionarAcao(Command cmd) {
        if (cmd != null) {
            this.acoes.add(cmd.clone());
        }
    }

    /**
     * Verifica se a automação está associada à divisão indicada.
     *
     * @param divisaoNome nome da divisão
     * @return {@code true} se a automação se aplicar à divisão indicada
     */
    public boolean aplicaA(String divisaoNome) {
        return Objects.equals(this.divisaoNome, divisaoNome);
    }

    /**
     * Avalia a condição e executa as ações quando esta se verifica.
     *
     * @param ambiente ambiente interior a avaliar
     * @param domium fachada do domínio sobre a qual as ações são executadas
     */
    public void verificarEExecutar(AmbienteInterior ambiente, DomiUM domium) {
        if (ambiente == null || domium == null || this.condicao == null) {
            return;
        }

        if (this.condicao.avaliar(ambiente)) {
            for (Command comando : this.acoes) {
                comando.execute(domium);
            }
        }
    }

    /**
     * Compara esta automação com outro objeto com base no seu estado.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem a mesma automação lógica
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Automacao automacao = (Automacao) o;
        return Objects.equals(this.nome, automacao.nome)
                && Objects.equals(this.divisaoNome, automacao.divisaoNome)
                && Objects.equals(this.condicao, automacao.condicao)
                && Objects.equals(this.acoes, automacao.acoes);
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão da automação
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.nome, this.divisaoNome, this.condicao, this.acoes);
    }

    /**
     * Produz uma representação textual legível da automação.
     *
     * @return texto descritivo da automação
     */
    @Override
    public String toString() {
        return "Automacao{"
                + "nome='" + this.nome + '\''
                + ", divisaoNome='" + this.divisaoNome + '\''
                + ", condicao=" + this.condicao
                + ", acoes=" + this.acoes
                + '}';
    }

    /**
     * Cria uma cópia profunda desta automação.
     *
     * A condição e as ações são copiadas para garantir independência entre a
     * cópia e a automação original.
     *
     * @return nova automação com o mesmo estado lógico
     */
    public Automacao clone() {
        Automacao copia = new Automacao(this.nome, this.divisaoNome, this.condicao);

        for (Command comando : this.acoes) {
            copia.acoes.add(comando.clone());
        }

        return copia;
    }
}

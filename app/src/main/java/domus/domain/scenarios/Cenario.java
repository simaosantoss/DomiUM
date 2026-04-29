package domus.domain.scenarios;

import domus.domain.DomiUM;
import domus.domain.commands.Command;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Representa uma sequência ordenada de comandos do domínio.
 *
 * Um cenário permite guardar um conjunto de ações e executá-las posteriormente
 * pela mesma ordem em que foram registadas.
 */
public class Cenario implements Serializable {

    /**
     * Nome legível do cenário.
     */
    private final String nome;

    /**
     * Comandos que compõem o cenário, guardados pela ordem de execução.
     */
    private final List<Command> comandos;

    /**
     * Cria um cenário com o nome indicado.
     *
     * @param nome nome do cenário
     */
    public Cenario(String nome) {
        this.nome = nome;
        this.comandos = new ArrayList<Command>();
    }

    /**
     * Dá acesso ao nome do cenário.
     *
     * @return nome do cenário
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida dos comandos.
     *
     * Cada comando é copiado antes de ser exposto, preservando o encapsulamento
     * da lista interna.
     *
     * @return iterador sobre uma cópia dos comandos do cenário
     */
    public Iterator<Command> getIteradorComandos() {
        List<Command> copia = new ArrayList<Command>();
        for (Command comando : this.comandos) {
            copia.add(comando.clone());
        }
        return Collections.unmodifiableList(copia).iterator();
    }

    /**
     * Acrescenta um comando ao cenário.
     *
     * O comando é guardado por cópia para impedir alterações externas ao estado
     * interno do cenário.
     *
     * @param cmd comando a acrescentar
     */
    public void adicionarComando(Command cmd) {
        if (cmd != null) {
            this.comandos.add(cmd.clone());
        }
    }

    /**
     * Executa todos os comandos do cenário pela ordem definida.
     *
     * @param domium fachada do domínio sobre a qual os comandos são executados
     */
    public void executar(DomiUM domium) {
        if (domium == null) {
            return;
        }

        for (Command comando : this.comandos) {
            comando.execute(domium);
        }
    }

    /**
     * Compara este cenário com outro objeto com base no seu estado.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo cenário lógico
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Cenario cenario = (Cenario) o;
        return Objects.equals(this.nome, cenario.nome)
                && Objects.equals(this.comandos, cenario.comandos);
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do cenário
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.nome, this.comandos);
    }

    /**
     * Produz uma representação textual legível do cenário.
     *
     * @return texto descritivo do cenário
     */
    @Override
    public String toString() {
        return "Cenario{"
                + "nome='" + this.nome + '\''
                + ", comandos=" + this.comandos
                + '}';
    }

    /**
     * Cria uma cópia profunda deste cenário.
     *
     * Os comandos são copiados individualmente para garantir independência entre
     * a cópia e o cenário original.
     *
     * @return novo cenário com o mesmo estado lógico
     */
    public Cenario clone() {
        Cenario copia = new Cenario(this.nome);

        for (Command comando : this.comandos) {
            copia.comandos.add(comando.clone());
        }

        return copia;
    }
}

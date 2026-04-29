package domus.domain.scheduling;

import domus.domain.DomiUM;
import domus.domain.commands.Command;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Representa um escalonamento temporal de ações.
 *
 * O escalonamento executa ações quando a passagem de tempo atinge a hora de
 * início e a hora de fim configuradas.
 */
public class Escalonamento implements Serializable {

    /**
     * Nome legível do escalonamento.
     */
    private final String nome;

    /**
     * Hora em que as ações de início devem ser executadas.
     */
    private final LocalTime horaInicio;

    /**
     * Hora em que as ações de fim devem ser executadas.
     */
    private final LocalTime horaFim;

    /**
     * Ações executadas quando a hora de início é atingida.
     */
    private final List<Command> acoesInicio;

    /**
     * Ações executadas quando a hora de fim é atingida.
     */
    private final List<Command> acoesFim;

    /**
     * Cria um escalonamento com horas de início e fim.
     *
     * @param nome nome do escalonamento
     * @param horaInicio hora de início
     * @param horaFim hora de fim
     */
    public Escalonamento(String nome, LocalTime horaInicio, LocalTime horaFim) {
        this.nome = nome;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.acoesInicio = new ArrayList<Command>();
        this.acoesFim = new ArrayList<Command>();
    }

    /**
     * Dá acesso ao nome do escalonamento.
     *
     * @return nome do escalonamento
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Dá acesso à hora de início.
     *
     * @return hora de início
     */
    public LocalTime getHoraInicio() {
        return this.horaInicio;
    }

    /**
     * Dá acesso à hora de fim.
     *
     * @return hora de fim
     */
    public LocalTime getHoraFim() {
        return this.horaFim;
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida das ações de início.
     *
     * @return iterador sobre uma cópia das ações de início
     */
    public Iterator<Command> getIteradorAcoesInicio() {
        List<Command> copia = new ArrayList<Command>();
        for (Command comando : this.acoesInicio) {
            copia.add(comando.clone());
        }
        return Collections.unmodifiableList(copia).iterator();
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida das ações de fim.
     *
     * @return iterador sobre uma cópia das ações de fim
     */
    public Iterator<Command> getIteradorAcoesFim() {
        List<Command> copia = new ArrayList<Command>();
        for (Command comando : this.acoesFim) {
            copia.add(comando.clone());
        }
        return Collections.unmodifiableList(copia).iterator();
    }

    /**
     * Acrescenta uma ação de início ao escalonamento.
     *
     * @param cmd comando a acrescentar
     */
    public void adicionarAcaoInicio(Command cmd) {
        if (cmd != null) {
            this.acoesInicio.add(cmd.clone());
        }
    }

    /**
     * Acrescenta uma ação de fim ao escalonamento.
     *
     * @param cmd comando a acrescentar
     */
    public void adicionarAcaoFim(Command cmd) {
        if (cmd != null) {
            this.acoesFim.add(cmd.clone());
        }
    }

    /**
     * Verifica se as horas configuradas foram atingidas e executa as ações
     * correspondentes.
     *
     * @param horaAnterior hora antes do avanço temporal
     * @param horaAtual hora depois do avanço temporal
     * @param domium fachada do domínio sobre a qual os comandos são executados
     */
    public void verificarEExecutar(LocalTime horaAnterior, LocalTime horaAtual, DomiUM domium) {
        if (horaAnterior == null || horaAtual == null || domium == null) {
            return;
        }

        if (horaFoiAtingida(horaAnterior, horaAtual, this.horaInicio)) {
            executarComandos(this.acoesInicio, domium);
        }

        if (horaFoiAtingida(horaAnterior, horaAtual, this.horaFim)) {
            executarComandos(this.acoesFim, domium);
        }
    }

    /**
     * Verifica se uma hora alvo foi atingida durante a passagem de tempo.
     *
     * O intervalo suporta passagens simples e passagens pela meia-noite.
     *
     * @param horaAnterior hora antes do avanço
     * @param horaAtual hora depois do avanço
     * @param horaAlvo hora a verificar
     * @return {@code true} se a hora alvo tiver sido atingida
     */
    private boolean horaFoiAtingida(LocalTime horaAnterior, LocalTime horaAtual, LocalTime horaAlvo) {
        if (horaAlvo == null || horaAnterior.equals(horaAtual)) {
            return false;
        }

        if (horaAnterior.isBefore(horaAtual)) {
            return horaAlvo.isAfter(horaAnterior) && !horaAlvo.isAfter(horaAtual);
        }

        return horaAlvo.isAfter(horaAnterior) || !horaAlvo.isAfter(horaAtual);
    }

    /**
     * Executa uma lista de comandos pela ordem definida.
     *
     * @param comandos comandos a executar
     * @param domium fachada do domínio
     */
    private void executarComandos(List<Command> comandos, DomiUM domium) {
        for (Command comando : comandos) {
            comando.execute(domium);
        }
    }

    /**
     * Compara este escalonamento com outro objeto com base no seu estado.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo escalonamento lógico
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Escalonamento that = (Escalonamento) o;
        return Objects.equals(this.nome, that.nome)
                && Objects.equals(this.horaInicio, that.horaInicio)
                && Objects.equals(this.horaFim, that.horaFim)
                && Objects.equals(this.acoesInicio, that.acoesInicio)
                && Objects.equals(this.acoesFim, that.acoesFim);
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do escalonamento
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.nome, this.horaInicio, this.horaFim, this.acoesInicio, this.acoesFim);
    }

    /**
     * Produz uma representação textual legível do escalonamento.
     *
     * @return texto descritivo do escalonamento
     */
    @Override
    public String toString() {
        return "Escalonamento{"
                + "nome='" + this.nome + '\''
                + ", horaInicio=" + this.horaInicio
                + ", horaFim=" + this.horaFim
                + ", acoesInicio=" + this.acoesInicio
                + ", acoesFim=" + this.acoesFim
                + '}';
    }

    /**
     * Cria uma cópia profunda deste escalonamento.
     *
     * As listas de comandos são copiadas comando a comando.
     *
     * @return novo escalonamento com o mesmo estado lógico
     */
    public Escalonamento clone() {
        Escalonamento copia = new Escalonamento(this.nome, this.horaInicio, this.horaFim);

        for (Command comando : this.acoesInicio) {
            copia.acoesInicio.add(comando.clone());
        }

        for (Command comando : this.acoesFim) {
            copia.acoesFim.add(comando.clone());
        }

        return copia;
    }
}

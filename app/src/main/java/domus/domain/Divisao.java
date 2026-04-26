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
 * Representa uma divisão de uma casa no sistema DomusControl.
 *
 * Cada divisão agrega os dispositivos que lhe pertencem, mantendo uma relação
 * de composição sobre esses elementos.
 */
public class Divisao implements Serializable {

    /**
     * Nome da divisão.
     */
    private final String nome;

    /**
     * Dispositivos associados à divisão, indexados pelo respetivo identificador.
     */
    private final Map<String, Dispositivo> dispositivos;

    /**
     * Cria uma divisão com o nome indicado.
     *
     * @param nome nome da divisão
     */
    public Divisao(String nome) {
        this.nome = nome;
        this.dispositivos = new HashMap<String, Dispositivo>();
    }

    /**
     * Dá acesso ao nome da divisão.
     *
     * @return nome da divisão
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Obtém um dispositivo da divisão a partir do seu identificador.
     *
     * O dispositivo é devolvido por cópia, preservando o encapsulamento da
     * coleção interna.
     *
     * @param id identificador do dispositivo
     * @return cópia do dispositivo encontrado, ou {@code null} se não existir
     */
    public Dispositivo getDispositivo(String id) {
        Dispositivo dispositivo = this.dispositivos.get(id);
        if (dispositivo == null) {
            return null;
        }
        return dispositivo.clone();
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida dos dispositivos da
     * divisão.
     *
     * Cada dispositivo é copiado antes de ser exposto, evitando fugas de
     * referência para a estrutura interna.
     *
     * @return iterador sobre uma cópia dos dispositivos da divisão
     */
    public Iterator<Dispositivo> getIteradorDispositivos() {
        List<Dispositivo> copia = new ArrayList<Dispositivo>();
        for (Dispositivo dispositivo : this.dispositivos.values()) {
            copia.add(dispositivo.clone());
        }
        return Collections.unmodifiableList(copia).iterator();
    }

    /**
     * Associa um dispositivo a esta divisão.
     *
     * O dispositivo é guardado por cópia, respeitando a composição da divisão
     * sobre os seus dispositivos.
     *
     * @param disp dispositivo a associar à divisão
     */
    public void adicionarDispositivo(Dispositivo disp) {
        if (disp != null) {
            this.dispositivos.put(disp.getIdentificador(), disp.clone());
        }
    }

    /**
     * Compara esta divisão com outro objeto com base no seu estado relevante.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem a mesma divisão lógica
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Divisao divisao = (Divisao) o;
        return Objects.equals(this.nome, divisao.nome)
                && Objects.equals(this.dispositivos, divisao.dispositivos);
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão da divisão
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.nome, this.dispositivos);
    }

    /**
     * Produz uma representação textual legível da divisão.
     *
     * @return texto descritivo da divisão
     */
    @Override
    public String toString() {
        return "Divisao{"
                + "nome='" + this.nome + '\''
                + ", dispositivos=" + this.dispositivos
                + '}';
    }

    /**
     * Cria uma cópia profunda desta divisão.
     *
     * Os dispositivos são copiados individualmente para garantir independência
     * entre a cópia e o objeto original.
     *
     * @return nova divisão com o mesmo estado lógico
     */
    public Divisao clone() {
        Divisao copia = new Divisao(this.nome);

        for (Map.Entry<String, Dispositivo> entry : this.dispositivos.entrySet()) {
            copia.dispositivos.put(entry.getKey(), entry.getValue().clone());
        }

        return copia;
    }
}

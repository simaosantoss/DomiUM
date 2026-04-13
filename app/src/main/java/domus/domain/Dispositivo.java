package domus.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa a base comum dos dispositivos controlados pelo sistema.
 *
 * Esta abstração concentra os dados partilhados por qualquer dispositivo,
 * como identificação, fabricante, estado de funcionamento e indicadores de
 * utilização.
 */
public abstract class Dispositivo implements Serializable {

    /**
     * Identificador único do dispositivo.
     */
    private final String identificador;

    /**
     * Marca comercial do dispositivo.
     */
    private final String marca;

    /**
     * Modelo concreto do dispositivo.
     */
    private final String modelo;

    /**
     * Consumo base por hora de funcionamento.
     */
    private final double consumoPorHora;

    /**
     * Indica se o dispositivo se encontra ligado.
     */
    private boolean ligado;

    /**
     * Tempo total acumulado em que o dispositivo esteve ligado, em minutos.
     */
    private long tempoTotalLigado;

    /**
     * Número de ativações do dispositivo.
     */
    private int numeroAtivacoes;

    /**
     * Cria um dispositivo com os seus dados base.
     *
     * @param identificador identificador único do dispositivo
     * @param marca marca do equipamento
     * @param modelo modelo do equipamento
     * @param consumoPorHora consumo energético por hora
     */
    public Dispositivo(String identificador, String marca, String modelo, double consumoPorHora) {
        this(identificador, marca, modelo, consumoPorHora, false, 0L, 0);
    }

    /**
     * Cria um dispositivo com o estado herdado totalmente definido.
     *
     * Este construtor existe para apoiar subclasses que precisem de reconstruir
     * explicitamente uma instância com todo o estado já conhecido, como acontece
     * em operações de cópia manual.
     *
     * @param identificador identificador único do dispositivo
     * @param marca marca do equipamento
     * @param modelo modelo do equipamento
     * @param consumoPorHora consumo energético por hora
     * @param ligado estado atual do dispositivo
     * @param tempoTotalLigado tempo total acumulado em que o dispositivo esteve
     *                         ligado, em minutos
     * @param numeroAtivacoes número de ativações registadas
     */
    protected Dispositivo(String identificador, String marca, String modelo, double consumoPorHora,
                          boolean ligado, long tempoTotalLigado, int numeroAtivacoes) {
        this.identificador = identificador;
        this.marca = marca;
        this.modelo = modelo;
        this.consumoPorHora = consumoPorHora;
        this.ligado = ligado;
        this.tempoTotalLigado = tempoTotalLigado;
        this.numeroAtivacoes = numeroAtivacoes;
    }

    /**
     * Cria uma cópia do estado herdado de outro dispositivo.
     *
     * Este construtor simplifica a implementação de cópias manuais nas
     * subclasses, garantindo que o estado comum é transferido de forma direta e
     * sem reconstruções artificiais.
     *
     * @param outro dispositivo de origem
     */
    protected Dispositivo(Dispositivo outro) {
        this(
                outro.identificador,
                outro.marca,
                outro.modelo,
                outro.consumoPorHora,
                outro.ligado,
                outro.tempoTotalLigado,
                outro.numeroAtivacoes
        );
    }

    /**
     * Dá acesso ao identificador do dispositivo.
     *
     * @return identificador único do dispositivo
     */
    public String getIdentificador() {
        return this.identificador;
    }

    /**
     * Dá acesso à marca do dispositivo.
     *
     * @return marca do dispositivo
     */
    public String getMarca() {
        return this.marca;
    }

    /**
     * Dá acesso ao modelo do dispositivo.
     *
     * @return modelo do dispositivo
     */
    public String getModelo() {
        return this.modelo;
    }

    /**
     * Dá acesso ao consumo base por hora.
     *
     * @return consumo energético por hora
     */
    public double getConsumoPorHora() {
        return this.consumoPorHora;
    }

    /**
     * Indica se o dispositivo está neste momento ligado.
     *
     * @return {@code true} se o dispositivo estiver ligado
     */
    public boolean isLigado() {
        return this.ligado;
    }

    /**
     * Dá acesso ao tempo total acumulado em que o dispositivo esteve ligado.
     *
     * @return tempo total ligado, em minutos
     */
    public long getTempoTotalLigado() {
        return this.tempoTotalLigado;
    }

    /**
     * Dá acesso ao número de ativações do dispositivo.
     *
     * @return número de vezes que o dispositivo transitou para ligado
     */
    public int getNumeroAtivacoes() {
        return this.numeroAtivacoes;
    }

    /**
     * Liga o dispositivo.
     *
     * O contador de ativações só é incrementado quando existe uma transição
     * real do estado desligado para ligado.
     */
    public void ligar() {
        if (!this.ligado) {
            this.ligado = true;
            this.numeroAtivacoes++;
        }
    }

    /**
     * Desliga o dispositivo.
     *
     * Nesta fase, o tempo total ligado ainda não é atualizado no momento do
     * desligar.
     */
    public void desligar() {
        this.ligado = false;
    }

    /**
     * Calcula o consumo do dispositivo segundo a lógica da subclasse concreta.
     *
     * @return consumo calculado
     */
    public abstract double getConsumo();

    /**
     * Cria uma cópia manual do dispositivo.
     *
     * @return novo dispositivo com o mesmo estado lógico
     */
    public abstract Dispositivo clone();

    /**
     * Constrói a parte textual correspondente ao estado herdado do dispositivo.
     *
     * Este método permite que subclasses reutilizem a descrição base no seu
     * próprio {@code toString()} sem depender de manipulação frágil de texto.
     *
     * @return descrição textual do estado comum do dispositivo
     */
    protected String getDescricaoEstadoBase() {
        return "identificador='" + this.identificador + '\''
                + ", marca='" + this.marca + '\''
                + ", modelo='" + this.modelo + '\''
                + ", consumoPorHora=" + this.consumoPorHora
                + ", ligado=" + this.ligado
                + ", tempoTotalLigado=" + this.tempoTotalLigado
                + ", numeroAtivacoes=" + this.numeroAtivacoes;
    }

    /**
     * Compara este dispositivo com outro objeto com base no seu estado
     * relevante.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo dispositivo lógico
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Dispositivo that = (Dispositivo) o;
        return Double.compare(this.consumoPorHora, that.consumoPorHora) == 0
                && this.ligado == that.ligado
                && this.tempoTotalLigado == that.tempoTotalLigado
                && this.numeroAtivacoes == that.numeroAtivacoes
                && Objects.equals(this.identificador, that.identificador)
                && Objects.equals(this.marca, that.marca)
                && Objects.equals(this.modelo, that.modelo);
    }

    /**
     * Produz uma representação textual legível do dispositivo.
     *
     * @return texto com os dados principais do dispositivo
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{"
                + getDescricaoEstadoBase()
                + '}';
    }
}

package domus.domain.devices;

import java.util.Objects;

/**
 * Representa um ar condicionado inteligente no sistema DomusControl.
 *
 * Esta classe especializa um dispositivo genérico com a temperatura alvo e o
 * modo de funcionamento atualmente definido.
 */
public class ArCondicionadoInteligente extends Dispositivo {

    /**
     * Temperatura alvo definida para o equipamento.
     */
    private double temperaturaAlvo;

    /**
     * Modo de funcionamento atualmente selecionado.
     */
    private String modo;

    /**
     * Cria um novo ar condicionado inteligente com os dados base do dispositivo
     * e a temperatura alvo inicial.
     *
     * @param identificador identificador único do ar condicionado
     * @param marca marca do ar condicionado
     * @param modelo modelo do ar condicionado
     * @param consumoPorHora consumo energético por hora
     * @param temperaturaAlvo temperatura alvo inicial
     */
    public ArCondicionadoInteligente(String identificador, String marca, String modelo,
                                     double consumoPorHora, double temperaturaAlvo) {
        super(identificador, marca, modelo, consumoPorHora);
        this.temperaturaAlvo = temperaturaAlvo;
        this.modo = "AUTO";
    }

    /**
     * Cria um novo ar condicionado inteligente por cópia de outro já existente.
     *
     * O estado comum do dispositivo é copiado diretamente pela superclasse e os
     * atributos próprios de climatização são preservados nesta classe.
     *
     * @param outro ar condicionado a copiar
     */
    private ArCondicionadoInteligente(ArCondicionadoInteligente outro) {
        super(outro);
        this.temperaturaAlvo = outro.temperaturaAlvo;
        this.modo = outro.modo;
    }

    /**
     * Dá acesso à temperatura alvo atualmente definida.
     *
     * @return temperatura alvo do equipamento
     */
    public double getTemperaturaAlvo() {
        return this.temperaturaAlvo;
    }

    /**
     * Dá acesso ao modo de funcionamento atual.
     *
     * @return modo atualmente definido
     */
    public String getModo() {
        return this.modo;
    }

    /**
     * Altera a temperatura alvo do equipamento.
     *
     * @param valor nova temperatura alvo
     */
    public void setTemperaturaAlvo(double valor) {
        this.temperaturaAlvo = valor;
    }

    /**
     * Altera o modo de funcionamento do equipamento.
     *
     * @param modo novo modo a guardar
     */
    public void setModo(String modo) {
        this.modo = modo;
    }

    /**
     * Calcula o consumo atual do ar condicionado.
     *
     * Nesta fase do projeto, o consumo corresponde diretamente ao consumo base
     * por hora herdado da superclasse.
     *
     * @return consumo atual do ar condicionado
     */
    @Override
    public double getConsumo() {
        return getConsumoPorHora();
    }

    /**
     * Cria uma cópia manual deste ar condicionado inteligente.
     *
     * A nova instância é criada através do construtor de cópia da própria classe,
     * apoiado no construtor de cópia da superclasse para preservar o estado
     * herdado e os atributos próprios de climatização.
     *
     * @return novo ar condicionado com o mesmo estado lógico
     */
    @Override
    public ArCondicionadoInteligente clone() {
        return new ArCondicionadoInteligente(this);
    }

    /**
     * Compara este ar condicionado com outro objeto com base no estado herdado
     * e no estado específico da classe.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo ar condicionado lógico
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ArCondicionadoInteligente that = (ArCondicionadoInteligente) o;
        return Double.compare(this.temperaturaAlvo, that.temperaturaAlvo) == 0
                && Objects.equals(this.modo, that.modo);
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão do ar condicionado
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.temperaturaAlvo, this.modo);
    }

    /**
     * Produz uma representação textual legível do ar condicionado, reutilizando
     * a informação textual da superclasse e acrescentando os atributos próprios.
     *
     * @return texto descritivo do ar condicionado
     */
    @Override
    public String toString() {
        return "ArCondicionadoInteligente{"
                + getDescricaoEstadoBase()
                + ", temperaturaAlvo=" + this.temperaturaAlvo
                + ", modo='" + this.modo + '\''
                + '}';
    }
}

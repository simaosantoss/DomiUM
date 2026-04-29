package domus.domain.devices;

import java.util.Objects;

/**
 * Representa um desumidificador inteligente no sistema DomusControl.
 *
 * Esta classe especializa um dispositivo genérico com a humidade alvo a manter
 * no ambiente.
 */
public class DesumidificadorInteligente extends Dispositivo {

    /**
     * Humidade alvo definida para o desumidificador.
     */
    private double humidadeAlvo;

    /**
     * Cria um novo desumidificador inteligente com os dados base do dispositivo
     * e a humidade alvo inicial.
     *
     * @param identificador identificador único do desumidificador
     * @param marca marca do desumidificador
     * @param modelo modelo do desumidificador
     * @param consumoPorHora consumo energético por hora
     * @param humidadeAlvo humidade alvo inicial
     */
    public DesumidificadorInteligente(String identificador, String marca, String modelo,
                                      double consumoPorHora, double humidadeAlvo) {
        super(identificador, marca, modelo, consumoPorHora);
        this.humidadeAlvo = humidadeAlvo;
    }

    /**
     * Cria um novo desumidificador inteligente por cópia de outro já existente.
     *
     * O estado comum do dispositivo é copiado diretamente pela superclasse e o
     * estado próprio de humidade é preservado nesta classe.
     *
     * @param outro desumidificador a copiar
     */
    private DesumidificadorInteligente(DesumidificadorInteligente outro) {
        super(outro);
        this.humidadeAlvo = outro.humidadeAlvo;
    }

    /**
     * Dá acesso à humidade alvo atualmente definida.
     *
     * @return humidade alvo do desumidificador
     */
    public double getHumidadeAlvo() {
        return this.humidadeAlvo;
    }

    /**
     * Altera a humidade alvo do desumidificador.
     *
     * @param h nova humidade alvo
     */
    public void setHumidadeAlvo(double h) {
        this.humidadeAlvo = h;
    }

    /**
     * Calcula o consumo atual do desumidificador.
     *
     * Nesta fase do projeto, o consumo corresponde diretamente ao consumo base
     * por hora herdado da superclasse.
     *
     * @return consumo atual do desumidificador
     */
    @Override
    public double getConsumo() {
        return getConsumoPorHora();
    }

    /**
     * Cria uma cópia manual deste desumidificador inteligente.
     *
     * A nova instância é criada através do construtor de cópia da própria classe,
     * apoiado no construtor de cópia da superclasse para preservar o estado
     * herdado e o estado de humidade alvo.
     *
     * @return novo desumidificador com o mesmo estado lógico
     */
    @Override
    public DesumidificadorInteligente clone() {
        return new DesumidificadorInteligente(this);
    }

    /**
     * Compara este desumidificador com outro objeto com base no estado herdado
     * e no estado específico da classe.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo desumidificador lógico
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
        DesumidificadorInteligente that = (DesumidificadorInteligente) o;
        return Double.compare(this.humidadeAlvo, that.humidadeAlvo) == 0;
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão do desumidificador
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.humidadeAlvo);
    }

    /**
     * Produz uma representação textual legível do desumidificador, reutilizando
     * a informação textual da superclasse e acrescentando o estado próprio.
     *
     * @return texto descritivo do desumidificador
     */
    @Override
    public String toString() {
        return "DesumidificadorInteligente{"
                + getDescricaoEstadoBase()
                + ", humidadeAlvo=" + this.humidadeAlvo
                + '}';
    }
}

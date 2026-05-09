package domus.domain.devices;

import java.util.Objects;

/**
 * Representa uma cortina inteligente no sistema DomusControl.
 *
 * Esta classe especializa um dispositivo genérico com o grau de abertura da
 * cortina, permitindo controlar a sua posição de forma simples.
 */
public class CortinaInteligente extends Dispositivo {

    /**
     * Percentagem atual de abertura da cortina.
     */
    private int percentagemAbertura;

    /**
     * Cria uma nova cortina inteligente com os dados base do dispositivo e o
     * estado próprio da cortina.
     *
     * @param identificador identificador único da cortina
     * @param marca marca da cortina
     * @param modelo modelo da cortina
     * @param consumoPorHora consumo energético por hora
     * @param percentagemAbertura percentagem de abertura da cortina
     */
    public CortinaInteligente(String identificador, String marca, String modelo,
                              double consumoPorHora, int percentagemAbertura) {
        super(identificador, marca, modelo, consumoPorHora);
        this.percentagemAbertura = percentagemAbertura;
    }

    /**
     * Cria uma nova cortina inteligente por cópia de outra já existente.
     *
     * O estado comum do dispositivo é copiado diretamente pela superclasse e o
     * estado de abertura é preservado nesta classe.
     *
     * @param outra cortina a copiar
     */
    private CortinaInteligente(CortinaInteligente outra) {
        super(outra);
        this.percentagemAbertura = outra.percentagemAbertura;
    }

    /**
     * Dá acesso à percentagem atual de abertura da cortina.
     *
     * @return percentagem de abertura
     */
    public int getPercentagemAbertura() {
        return this.percentagemAbertura;
    }

    /**
     * Altera a percentagem de abertura da cortina.
     *
     * @param valor nova percentagem de abertura
     */
    public void setPercentagemAbertura(int valor) {
        this.percentagemAbertura = valor;
    }

    /**
     * Calcula o consumo atual da cortina.
     *
     * O consumo corresponde ao consumo acumulado estimado com base no tempo
     * total ligado e no consumo por hora herdado da superclasse.
     *
     * @return consumo atual da cortina
     */
    @Override
    public double getConsumo() {
        return calcularConsumoAcumulado();
    }

    /**
     * Cria uma cópia manual desta cortina inteligente.
     *
     * A nova instância é criada através do construtor de cópia da própria classe,
     * apoiado no construtor de cópia da superclasse para preservar o estado
     * herdado e o estado de abertura.
     *
     * @return nova cortina com o mesmo estado lógico
     */
    @Override
    public CortinaInteligente clone() {
        return new CortinaInteligente(this);
    }

    /**
     * Compara esta cortina com outro objeto com base no estado herdado e no
     * estado específico da classe.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem a mesma cortina lógica
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
        CortinaInteligente that = (CortinaInteligente) o;
        return this.percentagemAbertura == that.percentagemAbertura;
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão da cortina
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.percentagemAbertura);
    }

    /**
     * Produz uma representação textual legível da cortina, reutilizando a
     * informação textual da superclasse e acrescentando o estado próprio.
     *
     * @return texto descritivo da cortina
     */
    @Override
    public String toString() {
        return "CortinaInteligente{"
                + getDescricaoEstadoBase()
                + ", percentagemAbertura=" + this.percentagemAbertura
                + '}';
    }
}

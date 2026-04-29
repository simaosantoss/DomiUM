package domus.domain.devices;

import java.util.Objects;

/**
 * Representa uma fechadura inteligente no sistema DomusControl.
 *
 * Esta classe especializa um dispositivo genérico com o estado de trancamento,
 * permitindo controlar o acesso de forma programática.
 */
public class FechaduraInteligente extends Dispositivo {

    /**
     * Indica se a fechadura se encontra trancada.
     */
    private boolean trancada;

    /**
     * Cria uma nova fechadura inteligente.
     *
     * Por defeito, a fechadura começa trancada por razões de segurança.
     *
     * @param identificador identificador único da fechadura
     * @param marca marca da fechadura
     * @param modelo modelo da fechadura
     * @param consumoPorHora consumo energético por hora
     */
    public FechaduraInteligente(String identificador, String marca, String modelo,
                                double consumoPorHora) {
        super(identificador, marca, modelo, consumoPorHora);
        this.trancada = true;
    }

    /**
     * Cria uma nova fechadura inteligente por cópia de outra já existente.
     *
     * O estado comum do dispositivo é copiado diretamente pela superclasse e o
     * estado de trancamento é preservado nesta classe.
     *
     * @param outra fechadura a copiar
     */
    private FechaduraInteligente(FechaduraInteligente outra) {
        super(outra);
        this.trancada = outra.trancada;
    }

    /**
     * Coloca a fechadura no estado trancado.
     */
    public void trancar() {
        this.trancada = true;
    }

    /**
     * Coloca a fechadura no estado destrancado.
     */
    public void destrancar() {
        this.trancada = false;
    }

    /**
     * Indica se a fechadura está atualmente trancada.
     *
     * @return {@code true} se estiver trancada
     */
    public boolean isTrancada() {
        return this.trancada;
    }

    /**
     * Calcula o consumo atual da fechadura.
     *
     * Nesta fase do projeto, o consumo corresponde diretamente ao consumo base
     * por hora herdado da superclasse.
     *
     * @return consumo atual da fechadura
     */
    @Override
    public double getConsumo() {
        return getConsumoPorHora();
    }

    /**
     * Cria uma cópia manual desta fechadura inteligente.
     *
     * A nova instância é criada através do construtor de cópia da própria classe,
     * apoiado no construtor de cópia da superclasse para preservar o estado
     * herdado e o estado de trancamento.
     *
     * @return nova fechadura com o mesmo estado lógico
     */
    @Override
    public FechaduraInteligente clone() {
        return new FechaduraInteligente(this);
    }

    /**
     * Compara esta fechadura com outro objeto com base no estado herdado e no
     * estado específico da classe.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem a mesma fechadura lógica
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
        FechaduraInteligente that = (FechaduraInteligente) o;
        return this.trancada == that.trancada;
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão da fechadura
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.trancada);
    }

    /**
     * Produz uma representação textual legível da fechadura, reutilizando a
     * informação textual da superclasse e acrescentando o estado próprio.
     *
     * @return texto descritivo da fechadura
     */
    @Override
    public String toString() {
        return "FechaduraInteligente{"
                + getDescricaoEstadoBase()
                + ", trancada=" + this.trancada
                + '}';
    }
}

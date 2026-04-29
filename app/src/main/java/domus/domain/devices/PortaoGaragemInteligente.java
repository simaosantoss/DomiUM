package domus.domain.devices;

import java.util.Objects;

/**
 * Representa um portão de garagem inteligente no sistema DomusControl.
 *
 * Esta classe especializa um dispositivo genérico com o estado de abertura do
 * portão, permitindo controlar o acesso à garagem.
 */
public class PortaoGaragemInteligente extends Dispositivo {

    /**
     * Indica se o portão se encontra aberto.
     */
    private boolean aberto;

    /**
     * Cria um novo portão de garagem inteligente.
     *
     * Por defeito, o portão começa fechado.
     *
     * @param identificador identificador único do portão
     * @param marca marca do portão
     * @param modelo modelo do portão
     * @param consumoPorHora consumo energético por hora
     */
    public PortaoGaragemInteligente(String identificador, String marca, String modelo,
                                    double consumoPorHora) {
        super(identificador, marca, modelo, consumoPorHora);
        this.aberto = false;
    }

    /**
     * Cria um novo portão de garagem inteligente por cópia de outro já existente.
     *
     * O estado comum do dispositivo é copiado diretamente pela superclasse e o
     * estado de abertura é preservado nesta classe.
     *
     * @param outro portão a copiar
     */
    private PortaoGaragemInteligente(PortaoGaragemInteligente outro) {
        super(outro);
        this.aberto = outro.aberto;
    }

    /**
     * Indica se o portão está atualmente aberto.
     *
     * @return {@code true} se o portão estiver aberto
     */
    public boolean isAberto() {
        return this.aberto;
    }

    /**
     * Coloca o portão no estado aberto.
     */
    public void abrir() {
        this.aberto = true;
    }

    /**
     * Coloca o portão no estado fechado.
     */
    public void fechar() {
        this.aberto = false;
    }

    /**
     * Calcula o consumo atual do portão.
     *
     * Nesta fase do projeto, o consumo corresponde diretamente ao consumo base
     * por hora herdado da superclasse.
     *
     * @return consumo atual do portão
     */
    @Override
    public double getConsumo() {
        return getConsumoPorHora();
    }

    /**
     * Cria uma cópia manual deste portão de garagem inteligente.
     *
     * A nova instância é criada através do construtor de cópia da própria classe,
     * apoiado no construtor de cópia da superclasse para preservar o estado
     * herdado e o estado de abertura.
     *
     * @return novo portão com o mesmo estado lógico
     */
    @Override
    public PortaoGaragemInteligente clone() {
        return new PortaoGaragemInteligente(this);
    }

    /**
     * Compara este portão com outro objeto com base no estado herdado e no
     * estado específico da classe.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo portão lógico
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
        PortaoGaragemInteligente that = (PortaoGaragemInteligente) o;
        return this.aberto == that.aberto;
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão do portão
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.aberto);
    }

    /**
     * Produz uma representação textual legível do portão, reutilizando a
     * informação textual da superclasse e acrescentando o estado próprio.
     *
     * @return texto descritivo do portão
     */
    @Override
    public String toString() {
        return "PortaoGaragemInteligente{"
                + getDescricaoEstadoBase()
                + ", aberto=" + this.aberto
                + '}';
    }
}
